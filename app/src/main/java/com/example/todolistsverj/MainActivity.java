package com.example.todolistsverj;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static final String Key_Item_Text = "item text";
    public static final String Key_Item_Position = "item position";
    public static final int Edit_Text_Code = 20;
    List<String> items;
    Button btnAdd;
    EditText etText;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etText = findViewById(R.id.etText);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = position -> {
            // delete the item
            items.remove(position);
            itemsAdapter.notifyItemRemoved(position);
            Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
            saveItems();
        };

        ItemsAdapter.OnClickListener onClickListener = position -> {
           Log.d("mainActivity", "Single click at position " + position);
           // create the new activity, pass data edited, display edited activity
            Intent i = new Intent(MainActivity.this,EditActivity.class);
            i.putExtra(Key_Item_Text,items.get(position));
            i.putExtra(Key_Item_Position,position);
            startActivityIfNeeded(i,Edit_Text_Code);
        };

        itemsAdapter = new ItemsAdapter(items,onLongClickListener,onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(v -> {
            String todoItem = etText.getText().toString();
            // add item
            items.add(todoItem);
            // notify adapter
            itemsAdapter.notifyItemInserted(items.size()-1);
            etText.setText("");
            Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
            saveItems();
        });
    }
    // handle results from editAct
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Edit_Text_Code) {
            // retreive updated text value
            String intenText = Objects.requireNonNull(data).getStringExtra(Key_Item_Text);
            // extract original position of edited item from the key position
            int position = data.getExtras().getInt(Key_Item_Position);
            //update the model at the right position with new item text
            items.set(position,intenText);
            itemsAdapter.notifyItemChanged(position);
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknow call to onActivityResult");
        }
    }
    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    // function will load items by reading line of data file
    private void loadItems(){
        try{
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
    // function saves items by writing into data file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
        }
    }
}