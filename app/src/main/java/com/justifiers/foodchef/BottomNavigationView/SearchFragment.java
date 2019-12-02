package com.justifiers.foodchef.BottomNavigationView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.justifiers.foodchef.R;
import com.justifiers.foodchef.Recipe.Recipe;
import com.justifiers.foodchef.Recipe.RecipeAdapter;
import com.wanderingcan.persistentsearch.PersistentSearchView;
import com.wanderingcan.persistentsearch.SearchMenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class SearchFragment extends Fragment {

    // Declare Variables here
    DatabaseReference ref;
    DatabaseReference ref_user_favourites;
    DatabaseReference ref_user_favourites_unlike;
    TextView recipe_dinner;
    ArrayList<Recipe> recipeList;
    ArrayList<Recipe> recipeList_dinner;
    RecyclerView recyclerView;
    RecyclerView recyclerView_dinner;
    RecipeAdapter recipeAdapter;
    RecipeAdapter recipeAdapter_dinner;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager layoutManager_dinner;
    PersistentSearchView persistentSearchView;
    SwipeRefreshLayout pullToRefresh;
    FirebaseAuth mAuth;
    ChipGroup chip_selection;
    FrameLayout frameLayout;
    String language;
    private boolean micEnabled;
    private static final int VOICE_RECOGNITION_CODE = 9999;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View searchView =  inflater.inflate(R.layout.search_fragment, container, false);
        SharedPreferences preferences = getActivity().getSharedPreferences("SettingsActivity", Activity.MODE_PRIVATE);
        language = preferences.getString("Language", "");
        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference().child("Recipe");
        System.out.println(ref);
        recipe_dinner = searchView.findViewById(R.id.recipe_dinner);
        recyclerView = searchView.findViewById(R.id.recycler_view);
        recyclerView_dinner = searchView.findViewById(R.id.recycler_view_dinner);
        persistentSearchView = searchView.findViewById(R.id.search_bar);
        frameLayout = searchView.findViewById(R.id.search_bar_frame);

        pullToRefresh = searchView.findViewById(R.id.swipe_container);

        micEnabled = isIntentAvailable(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
        chip_selection = searchView.findViewById(R.id.search_chips);
        chip_selection.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip = group.findViewById(checkedId);
                if (chip != null) {
                    ArrayList<Recipe> rlist = new ArrayList<>();
                    for (Recipe object : recipeList) {
                        if (object.getRType().contains((chip.getText().toString()))) {
                            rlist.add(object);
                        } else if(object.getrTypeHi().contains((chip.getText().toString()))){
                            rlist.add(object);
                        } else if(object.getrTypeFr().contains((chip.getText().toString()))){
                            rlist.add(object);
                        } else if(object.getrTypeUk().contains((chip.getText().toString()))){
                            rlist.add(object);
                        }
                    }
                    layoutManager = new GridLayoutManager(getActivity(), 2);
                    recyclerView.setLayoutManager(layoutManager);
                    if(getActivity() != null) {
                        RecipeAdapter adapter = new RecipeAdapter(rlist, getActivity());
                        recyclerView.setAdapter(adapter);
                        recyclerView_dinner.setAlpha(0);
                        recipe_dinner.setAlpha(0);
                    }
                }
            }
        });
        persistentSearchView.clearFocus();
        persistentSearchView.setOnSearchListener(new PersistentSearchView.OnSearchListener() {
            @Override
            public void onSearchOpened() {
//                persistentSearchView.getSearchMenu().addSearchMenuItem(1, "Biryani");
//                persistentSearchView.getSearchMenu().addSearchMenuItem(2, "Paneer");
            }

            @Override
            public void onSearchClosed() {
//                persistentSearchView.setNavigationDrawable(
            }

            @Override
            public void onSearchCleared() {
                persistentSearchView.getSearchMenu().removeSearchMenuItem(persistentSearchView.getSearchMenu().getSearchMenuItem(1));
            }

            @Override
            public void onSearchTermChanged(CharSequence term) {
                search(term.toString());
            }

            @Override
            public void onSearch(final CharSequence text) {
                persistentSearchView.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        if (i == KeyEvent.KEYCODE_ENTER) {
                            search(text.toString());
                            persistentSearchView.setShowSearchMenu(false);
                        }
                        return true;
                    }
                });
            }
        });


        persistentSearchView.setOnMenuItemClickListener(new PersistentSearchView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(SearchMenuItem item) {
                search(item.toString());
                persistentSearchView.setShowSearchMenu(false);
            }
        });

        persistentSearchView.setOnIconClickListener(new PersistentSearchView.OnIconClickListener() {
            @Override
            public void OnNavigationIconClick() {
                if (persistentSearchView.isSearchOpen()) {
                    persistentSearchView.closeSearch();
                }
            }

            @Override
            public void OnEndIconClick() {
                startVoiceRecognition();
            }
        });
        return searchView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ref != null) {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        recipeList = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren())
                            recipeList.add(ds.getValue(Recipe.class));

                    }
                    layoutManager = new GridLayoutManager(getActivity(), 2);
                    recyclerView.setLayoutManager(layoutManager);
                    recipeAdapter = new RecipeAdapter(recipeList, getActivity());
                    Log.e("WDWD", "FIRST_ADAPTER");
                    recyclerView.setAdapter(recipeAdapter);
                    recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {

                        }

                        @Override
                        public void onLikeClick(final int position) {
                            Map<String, Object> favorites_recipe = new HashMap<>();
                            if (language.equals("en")) {
                                favorites_recipe.put("rName", recipeList.get(position).getrName());
                            } else if (language.equals("fr")) {
                                favorites_recipe.put("rName", recipeList.get(position).getrNameFr());
                            } else if (language.equals("hi")) {
                                favorites_recipe.put("rName", recipeList.get(position).getrNameHi());
                            } else if (language.equals("uk")) {
                                favorites_recipe.put("rName", recipeList.get(position).getrNameUk());
                            }
                            favorites_recipe.put("rImage", recipeList.get(position).getrImage());
                            favorites_recipe.put("rTime", recipeList.get(position).getrTime());
                            favorites_recipe.put("rLikes", recipeList.get(position).getLikes());

                            try {
                                final String userId = mAuth.getCurrentUser().getUid();
                                ref_user_favourites = FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("favourites").child(recipeList.get(position).getrName());
                                ref_user_favourites.updateChildren(favorites_recipe);
                            } catch (NullPointerException e){
                                Toast.makeText(getActivity(), "Login or SignUp is required to add the recipe to favourites", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onUnLikeClick(int position) {
//                            if (mAuth.getCurrentUser().getUid() != null) {
//                                String userId = mAuth.getCurrentUser().getUid();
//                                ref_user_favourites_unlike = FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("favourites").child(recipeList.get(position).getrName());
//                                ref_user_favourites_unlike.removeValue();
//                            } else {
//                                Toast.makeText(getActivity(), "Login/SignUp is required", Toast.LENGTH_SHORT).show();
//                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            ref.orderByChild("rType").equalTo("Dinner").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        recipeList_dinner = new ArrayList<>();
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            recipeList_dinner.add(ds.getValue(Recipe.class));
                        }
                        layoutManager_dinner = new GridLayoutManager(getActivity(), 2);
                        recyclerView_dinner.setLayoutManager(layoutManager_dinner);
                        if(getActivity() != null) {
                            recipeAdapter_dinner = new RecipeAdapter(recipeList_dinner, getActivity());
                        }
                        recyclerView_dinner.setAdapter(recipeAdapter_dinner);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onStart();
                chip_selection.clearCheck();
                persistentSearchView.clearFocus();
                persistentSearchView.openSearch();
                persistentSearchView.setNavigationDrawable(getResources().getDrawable(R.drawable.ic_search));
                persistentSearchView.setShowSearchMenu(true);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pullToRefresh.isRefreshing()) {
                            pullToRefresh.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });
    }


    private void search(String str) {
        ArrayList<Recipe> rlist = new ArrayList<>();
        for (Recipe object : recipeList) {
            if (object.getrName().toLowerCase().contains(str.toLowerCase())) {
                rlist.add(object);
            }
        }
        layoutManager = new GridLayoutManager(getActivity(), 2);
        if(getActivity() != null){
            RecipeAdapter adapter = new RecipeAdapter(rlist, getActivity());
            Log.e("WDWDWA", "SECOND_ADAPTER");
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            Toast.makeText(getActivity(), "Recipes Fetched Successfully!", Toast.LENGTH_LONG).show();
            persistentSearchView.setNavigationDrawable(getResources().getDrawable(R.drawable.ic_search));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(getActivity(), "Pull Down to load all Recipies back", Toast.LENGTH_LONG);
                    toast.show();
                }
            }, 4000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if(matches.get(0) != null) {
                persistentSearchView.populateSearchText(matches.get(0));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isIntentAvailable(Intent intent) {
        if(getContext().getPackageManager() != null) {
            PackageManager mgr = getContext().getPackageManager();
            if (mgr != null) {
                List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                return list.size() > 0;
            }
        }
        return false;
    }

    private void startVoiceRecognition() {
        if (micEnabled) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    this.getString(R.string.speak_now));
            startActivityForResult(intent, VOICE_RECOGNITION_CODE);
        }
    }
}
