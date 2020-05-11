package com.justifiers.foodchef.BottomNavigationView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.justifiers.foodchef.Downloads.RecipieAdapter;
import com.justifiers.foodchef.Downloads.recipie;
import com.justifiers.foodchef.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DownloadsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecipieAdapter rAdapter;
    private  RecyclerView.LayoutManager layoutManager;
    private ArrayList<recipie> recipielist  = new ArrayList<>();
    private  int deletedposition;
    private recipie item;
    private Context context;
    Toolbar dToolbar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View downloadview= inflater.inflate(R.layout.downloads_fragment, container, false);
        setHasOptionsMenu(true);
        dToolbar = downloadview.findViewById(R.id.dToolbar);
        dToolbar.setTitle("Downloads");
        ((AppCompatActivity) getActivity()).setSupportActionBar(dToolbar);


        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = downloadview.findViewById(R.id.recycleView);
        rAdapter = new RecipieAdapter(recipielist);
        get_json();
        rAdapter.setOnItemClickListener(new RecipieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                recipielist.get(position);
                rAdapter.notifyItemChanged(position);
            }

            @Override
            public void onDeleteClick(int position)
            {
                deletedposition=position;
                item=recipielist.get(position);
                recipielist.remove(position);
                rAdapter.notifyItemRemoved(position);
                snackbar();
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(rAdapter);
        return downloadview;
    }

    public void snackbar() {
        Snackbar snackbar = Snackbar.make(recyclerView,"Didn't want to delete that recipe?",Snackbar.LENGTH_INDEFINITE)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recipielist.add(deletedposition,item);
                        rAdapter.notifyItemInserted(deletedposition);
                        rAdapter.notifyItemChanged(deletedposition);
                    }
                });
        snackbar.show();
    }
    public void get_json()  {

        try{
            JSONObject object = new JSONObject(readJSON());
            JSONArray jsonArray = object.getJSONArray("Recipe");
            Log.d("val", "get_json: "+jsonArray);
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String name = obj.getString("rName");
                String size = obj.getString("rTime");
                recipie recipies = new recipie(name,size,(getResources().getIdentifier(name, "drawable", getActivity().getPackageName())));
                recipielist.add(recipies);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public String readJSON() {
        String json = null;
        try {
            // Opening data.json file
            InputStream inputStream = getActivity().getAssets().open("check.json");
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

