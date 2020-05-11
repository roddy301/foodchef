package com.justifiers.foodchef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.justifiers.foodchef.Recipe.RecipeAdapter;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ingredrientHolder> {
    List<Ingredient> ingredientsList;
    private Context ctx;
    OutputStream outputStream;
    //private RecipeAdapter.OnItemClickListener iListner;
    String language;

    @NonNull
    @Override
    public IngredientsAdapter.ingredrientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_card_holder, parent, false);
        return new ingredrientHolder(view);
    }

    public IngredientsAdapter(List<Ingredient> ingredientsList, Context ctx) {

        while (ingredientsList.remove(null)) {
        }

        this.ingredientsList = ingredientsList;
        this.ctx = ctx;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapter.ingredrientHolder holder, int position) {

        holder.ingredients_name.setText(ingredientsList.get(position).getrIng());
        holder.ingredients_quantity.setText(ingredientsList.get(position).getrQuantity());
    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }

    public class ingredrientHolder extends RecyclerView.ViewHolder {
        TextView ingredients_name;
        TextView ingredients_quantity;

        public ingredrientHolder(@NonNull View itemView) {
            super(itemView);
            ingredients_name = itemView.findViewById(R.id.ingredients_name);
            ingredients_quantity = itemView.findViewById(R.id.ingredients_quantity);
        }
    }
}
