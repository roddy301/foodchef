package com.justifiers.foodchef.Shopping;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.justifiers.foodchef.R;

import java.util.List;

public class itemsAdapter extends RecyclerView.Adapter<itemsAdapter.ViewHolder> {

    private List<items> itList;
    private OnItemCheckListener onItemCheckListener;
    public interface OnItemCheckListener {
        void onItemCheck(items item);
        void onItemUncheck(items item);
    }


    public  itemsAdapter(List<items> item, @NonNull OnItemCheckListener onItemCheckListener) {
        this.itList = item;
        this.onItemCheckListener = onItemCheckListener;
    }

    public itemsAdapter(List<items> items) {
        this.itList = items;

    }

    @Override
    public itemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.shopping_item, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        if(holder instanceof ViewHolder) {

            final items currentItem = itList.get(position);
            final int pos = position;
            holder.name.setText(itList.get(position).getSname());
            holder.size.setText(itList.get(position).getSsize());
            holder.price.setText(itList.get(position).getPrice());
            // holder.chkSelected.setTag(itList.get(position));


            holder.chkSelected.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if (((ViewHolder) holder).chkSelected.isChecked()){
                        Log.d("value", "onBindViewHolder: "+"hello");
                        onItemCheckListener.onItemCheck(currentItem);
                    }else{
                        onItemCheckListener.onItemUncheck(currentItem);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return itList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView size;
        public TextView price;
        public CheckBox chkSelected;
        private TextView total;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.item_name);
            size = (TextView) itemView.findViewById((R.id.quantity));
            price= (TextView) itemView.findViewById(R.id.price);
            chkSelected = (CheckBox) itemView.findViewById(R.id.checkbox);
            total=(TextView) itemView.findViewById(R.id.textView);

        }
        public void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
        }
    }

    public List<items> getItList() {
        return itList;
    }

}