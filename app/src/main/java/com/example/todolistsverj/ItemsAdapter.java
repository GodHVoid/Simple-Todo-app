package com.example.todolistsverj;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter <ItemsAdapter.ViewHolder>{
    public interface OnClickListener{
        void onItemClicked(int position);
    }

    public interface OnLongClickListener{
        void onItemLongClicked(int position);
    }

    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;
    public ItemsAdapter(List<String> items, OnLongClickListener longClickListener,OnClickListener clickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Use layout inflator to inflat view
        View todoView =LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,parent, false);
        //wrap it inside a view holder and return

        return new ViewHolder(todoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //grab the item at the position
        String item = items.get(position);
        //bind the item into the specified view holder
        holder.bind(item);
    }

    //Tells the rv how many items are in the list
    @Override
    public int getItemCount() {
        return items.size();
    }
    // container to provide access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvItem;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        //update view inside of the vew holder with data
        public void bind(String item){
            tvItem.setText(item);
            tvItem.setOnClickListener(v -> clickListener.onItemClicked(getBindingAdapterPosition()));

            tvItem.setOnLongClickListener(v -> {
                // remove item from rv by notify listener which position was long
                longClickListener.onItemLongClicked(getBindingAdapterPosition());
                return true;
            });
        }
    }
}
