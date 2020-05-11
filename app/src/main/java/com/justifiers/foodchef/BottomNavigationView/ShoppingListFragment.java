package com.justifiers.foodchef.BottomNavigationView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.justifiers.foodchef.R;
import com.justifiers.foodchef.Shopping.items;
import com.justifiers.foodchef.Shopping.itemsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListFragment extends Fragment {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private  RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button button;
    private TextView total;
    int count=0;
    androidx.appcompat.widget.Toolbar sToolbar;

    private List<items> itemsList= new ArrayList<items>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View shoppingview= inflater.inflate(R.layout.shopping_list_fragment, container, false);
        sToolbar = shoppingview.findViewById(R.id.sToolbar);
        sToolbar.setTitle("ShoppingList");
        ((AppCompatActivity) getActivity()).setSupportActionBar(sToolbar);

        button= shoppingview.findViewById(R.id.button);
        total=shoppingview.findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browseIntent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=grocery+stores"));
                startActivity(browseIntent);
            }
        });

        adapter= new itemsAdapter(itemsList, new itemsAdapter.OnItemCheckListener() {
            @Override

            public void onItemCheck(items item) {
                itemsList.add(item);
                count= count+ Integer.parseInt((item.getPrice()));
                total.setText(Integer.toString(count));

            }

            @Override
            public void onItemUncheck(items item) {
                itemsList.add(item);
                count= count- Integer.parseInt((item.getPrice()));
                total.setText(Integer.toString(count));


            }
        });
        recyclerView = shoppingview.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        get_json();
        return  shoppingview;
    }


    public void get_json()  {
        try{

            JSONObject object = new JSONObject(readJSON());
            JSONArray jsonArray = object.getJSONArray("Recipe");
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String name = obj.getString("title");
                String price =obj.getString("Price");
                String size = obj.getString("Quantity");
                items item=new items(name,size,price);
                itemsList.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public String readJSON() {
        String json = null;
        try {
            // Opening data.json file
            InputStream inputStream = getActivity().getAssets().open("shopping.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            // read values in the byte array
            inputStream.read(buffer);
            inputStream.close();
            // convert byte to string
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return json;
        }
        return json;
    }
}



