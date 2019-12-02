package com.justifiers.foodchef.BottomNavigationView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.justifiers.foodchef.EditProfile.EditProfile;
import com.justifiers.foodchef.LoginAndSignUp.Favourites;
import com.justifiers.foodchef.LoginAndSignUp.FavouritesAdapter;
import com.justifiers.foodchef.LoginAndSignUp.LoginFragment;
import com.justifiers.foodchef.R;
import com.justifiers.foodchef.Recipe.Recipe;
import com.justifiers.foodchef.Recipe.RecipeAdapter;
import com.justifiers.foodchef.SettingsActivity;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    Button Edit_Profile_button;
    TextView profile_name;
    ImageView profile_image;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference ref_user_favourites;
    ArrayList<Favourites> recipe_list;
    Toolbar sToolbar;
    RecyclerView recyclerView;
    FavouritesAdapter favouritesAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View profileView = inflater.inflate(R.layout.profile_fragment, container, false);
        setHasOptionsMenu(true);
        sToolbar = profileView.findViewById(R.id.pToolbar);
        recyclerView = profileView.findViewById(R.id.profile_favorites_recycler_view);
        sToolbar.setTitle("Profile");
        ((AppCompatActivity) getActivity()).setSupportActionBar(sToolbar);
        profile_name = profileView.findViewById(R.id.profile_name);
        profile_image = profileView.findViewById(R.id.profile_image);
        Edit_Profile_button = profileView.findViewById(R.id.edit_profile_button);
        Edit_Profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseAuth != null) {
                    Intent i = new Intent(getActivity(), EditProfile.class);
                    startActivity(i);
                }
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("User");
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("name").getValue() != null) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        profile_name.setText(name);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            ref_user_favourites = FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("favourites");
            ref_user_favourites.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    recipe_list = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                        recipe_list.add(ds.getValue(Favourites.class));

                    layoutManager = new GridLayoutManager(getActivity(), 2);
                    recyclerView.setLayoutManager(layoutManager);
                    favouritesAdapter = new FavouritesAdapter(recipe_list);
                    recyclerView.setAdapter(favouritesAdapter);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        return profileView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.settings, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.tool_settings) {
            if(getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                Intent i = new Intent(getActivity(), SettingsActivity.class);
                startActivity(i);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (firebaseAuth == null) {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
            }
        }
    }
}