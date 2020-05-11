package com.justifiers.foodchef.Recipe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.justifiers.foodchef.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.recipeHolder> {
    private static final String TAG = "RecipeAdapter";
    ArrayList<Recipe> recipeList;
    private Context ctx;
    OutputStream outputStream;
    private OnItemClickListener rlistener;
    String language;
    DatabaseReference ref;
    DatabaseReference ref_user_favourites;
    FirebaseAuth mAuth;

    public RecipeAdapter(ArrayList<Recipe> recipeList, Context ctx) {
        this.recipeList = recipeList;
        this.ctx = ctx;
        SharedPreferences preferences = ctx.getSharedPreferences("SettingsActivity", Activity.MODE_PRIVATE);
        language = preferences.getString("Language", "");
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        rlistener=listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onLikeClick(int position);
        void onUnLikeClick(int position);
    }

    @NonNull
    @Override
    public recipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder, parent, false);
        return new recipeHolder(view, rlistener);
    }

    // binds the recipe items into the view
    @Override
    public void onBindViewHolder(final recipeHolder holder, final int position) {
        // loads the image into imageview by getting image url from firebase
        Picasso.get().load(recipeList.get(position).getrImage()).into(holder.recipeImage);
        if(language.equals("fr")){
            holder.recipeName.setText(recipeList.get(position).getrNameFr());
        } else if(language.equals("uk")){
            holder.recipeName.setText(recipeList.get(position).getrNameUk());
        } else if(language.equals("hi")){
            holder.recipeName.setText(recipeList.get(position).getrNameHi());
        } else {
            holder.recipeName.setText(recipeList.get(position).getrName());
        }
        // if the user is logged in, enable the option to add recipe to favourites otherwise disable it
        if(mAuth.getCurrentUser() != null){
            ref_user_favourites = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid()).child("favourites").child(recipeList.get(position).getrName());
            ref_user_favourites.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        holder.recipe_favourite_button.setChecked(true);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        holder.recipeTime.setText(recipeList.get(position).getrTime());
        // listens to recipe pop-up button that includes share and download clickables
        holder.recipe_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(ctx, holder.recipe_popup);
                popupMenu.getMenuInflater().inflate(R.menu.recipe_more_verc_items, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.recipe_menu_share:
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("*/*");
                                intent.putExtra(Intent.EXTRA_TEXT,"Recipe Name " + ": " + recipeList.get(position).getrName());
                                intent.putExtra(Intent.EXTRA_TEXT,"Recipe Image " + ": " +recipeList.get(position).getrImage());
                                intent.putExtra(Intent.EXTRA_TEXT,"Recipe Video " + ": " + recipeList.get(position).getrVideo());
                                ctx.startActivity(intent.createChooser(intent, "Share Via"));
                                break;
                            case R.id.recipe_menu_download:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        // returns the length of the recipelist arraylist
        return recipeList.size();
    }


    public class recipeHolder extends RecyclerView.ViewHolder {
        // declare recipe_items variable here
        ImageView recipeImage;
        TextView recipeName;
        TextView recipeTime;
        ImageButton recipe_popup;
        ToggleButton recipe_favourite_button;

        public recipeHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            // initializing the variables here
            recipeImage = itemView.findViewById(R.id.recycler_popular_today_image_view);
            recipeName = itemView.findViewById(R.id.recycler_popular_recipe_desc);
            recipeTime = itemView.findViewById(R.id.recycler_popular_today_time);
            recipe_popup = itemView.findViewById(R.id.recipe_popup);
            recipe_favourite_button = itemView.findViewById(R.id.recipe_favorite_icon);
            mAuth = FirebaseAuth.getInstance();
            if (mAuth.getCurrentUser() == null) {
                recipe_favourite_button.setVisibility(View.INVISIBLE);
            } else {
                recipe_favourite_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (listener != null) {
                            final int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                if (recipe_favourite_button.isChecked()) {
                                    Log.d(TAG, "onClick: Like_pressed");
                                    listener.onLikeClick(position);
                                } else {
                                    Log.d(TAG, "onClick: UnLike_pressed");
                                    listener.onUnLikeClick(position);
                                }
                            }
                        }
                    }
                });
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            Log.d(TAG, "onClick: pressed");
                            listener.onItemClick(position);

                        }
                    }
                }
            });
        }
    }

    private void setLocale(String language){
        Resources resources = ctx.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>= 21){
            config.setLocale(new Locale(language));
        } else {
            config.locale = new Locale(language);
        }
        resources.updateConfiguration(config, dm);
        // save the settings
        SharedPreferences.Editor lang_editor = ctx.getSharedPreferences("SettingsActivity", MODE_PRIVATE).edit();
        lang_editor.putString("Language", language);
        lang_editor.apply();
    }

    public void loadLocale(){
        SharedPreferences preferences = ctx.getSharedPreferences("SettingsActivity", MODE_PRIVATE);
        String language = preferences.getString("Language", "");
        setLocale(language);
    }
}
