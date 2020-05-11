package com.justifiers.foodchef.LoginAndSignUp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.justifiers.foodchef.R;
import com.justifiers.foodchef.Recipe.Recipe;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.favouritesHolder>{

    ArrayList<Favourites> favouritesList;
    private Context ctx;

    public FavouritesAdapter(ArrayList<Favourites> favouritesList) {
        this.favouritesList = favouritesList;
    }

    @NonNull
    @Override
    public favouritesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_card_view, parent, false);
        return new favouritesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull favouritesHolder holder,final int position) {
        // binds all the favourite items to view

        // loads the recipe image using picasso
        Picasso.get().load(favouritesList.get(position).getrImage()).into(holder.profile_recipe_image);
        holder.profile_recipe_time.setText(favouritesList.get(position).getrName());
        holder.profile_recipe_name.setText(favouritesList.get(position).getrName());
        holder.profile_favourite_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_TEXT,"Recipe Name " + ": " +  favouritesList.get(position).getrName());
                intent.putExtra(Intent.EXTRA_TEXT,"Recipe Image " + ": " +  favouritesList.get(position).getrImage());
                intent.putExtra(Intent.EXTRA_TEXT,"Recipe Video " + ": " + favouritesList.get(position).getrVideo());
                ctx.startActivity(intent.createChooser(intent, "Share Via"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return favouritesList.size();
    }

    public class favouritesHolder extends  RecyclerView.ViewHolder {
        // declare favourite recipe_items here
        ImageView profile_recipe_image;
        TextView profile_recipe_time;
        TextView profile_recipe_name;
        ToggleButton profile_favourite_icon;
        ImageButton profile_favourite_share;
        public favouritesHolder(@NonNull View itemView) {
            super(itemView);
            // initializing variables here
            profile_recipe_image = itemView.findViewById(R.id.profile_recipe_image_view);
            profile_recipe_time = itemView.findViewById(R.id.profile_favourites_time);
            profile_recipe_name = itemView.findViewById(R.id.profile_favourites_food_name);
            profile_favourite_icon = itemView.findViewById(R.id.profile_favorite_icon);
            profile_favourite_share = itemView.findViewById(R.id.profile_favourite_share);





        }
    }
}
