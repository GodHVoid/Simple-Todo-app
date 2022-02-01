package com.example.todolistsverj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

public class EditActivity extends AppCompatActivity {
    EditText editAct;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editAct = findViewById(R.id.editAct);
        btnSave = findViewById(R.id.btnSave);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit Item");
        editAct.setText(getIntent().getStringExtra(MainActivity.Key_Item_Text));
        //when user is done editing
        btnSave.setOnClickListener(((View v) -> {
            // create intent which contain results
            Intent intent = new Intent();
            // pass data
            intent.putExtra(MainActivity.Key_Item_Text,editAct.getText().toString());
            intent.putExtra(MainActivity.Key_Item_Position,getIntent().getExtras().getInt(MainActivity.Key_Item_Position));
            //set result to intent
            setResult(RESULT_OK,intent);
            // finish activity/ close screen
            finish();
        }));
    }
}