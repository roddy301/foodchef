package com.justifiers.foodchef.Downloads;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.justifiers.foodchef.R;

import java.util.ArrayList;

public class RecipieAdapter extends RecyclerView.Adapter<RecipieAdapter.RecipieViewHolder> {

    private ArrayList<recipie> rList;
    private OnItemClickListener rlistener;


    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);

    }


    public  void setOnItemClickListener(OnItemClickListener listener){
        rlistener=listener;
    }

    public static class RecipieViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView name;
        public TextView size;
        public ImageView deleteImage;


        public RecipieViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            name=itemView.findViewById(R.id.recipie_name);
            size=itemView.findViewById(R.id.recipie_size);
            deleteImage=itemView.findViewById(R.id.deleteImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position =getAdapterPosition();
                        listener.onItemClick(position);

                    }
                }
            });

            deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position =getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }
    public RecipieAdapter(ArrayList<recipie> recipieList){
        rList= recipieList;
    }


    @NonNull
    @Override
    public RecipieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recipie_item,parent,false);
        RecipieViewHolder holder= new RecipieViewHolder(view,rlistener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipieViewHolder holder, int position) {
        recipie recipieItem= rList.get(position);

        holder.imageView.setImageResource(recipieItem.getImageSource());
        holder.name.setText(recipieItem.getName());
        holder.size.setText(recipieItem.getSize());
    }

    @Override
    public int getItemCount() {
        return rList.size();
    }


}