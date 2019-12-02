package com.justifiers.foodchef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.justifiers.foodchef.Instructions.InstructionsActivity;

import android.view.SurfaceHolder;

public class RecipeView extends AppCompatActivity {

    private static final String STEP_INSTRUCTIONS = "Step instructions";

    private Button btnBeginPreparation;
    private TextView recipe_name;
    private Button download;
    private Button favorite;
    private TextView favoriteText;
    private Button minus;
    private Button add;
    private TextView serving;
    boolean isFav = false;
    private Button goBack;
    private VideoView videoView;
    private MediaController mediaController;

    String url = "https://foodchef-d5481.firebaseio.com/";
    String videoURL;
    String recipeID;

    FirebaseDatabase database = FirebaseDatabase.getInstance(url);
    private DatabaseReference recipeName;
    private DatabaseReference recipeURL;
    private DatabaseReference recipeDuration;
    private DatabaseReference likeCount;
    private DatabaseReference recipeDescription;
    private DatabaseReference recipeUtensils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);
        Intent intent =getIntent();
        recipeID = intent.getStringExtra("recipeID");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recipeName = database.getReferenceFromUrl(url+"Recipe/"+recipeID+"/rName");
        recipeURL = database.getReferenceFromUrl(url+"Recipe/1/rVideo");
        recipeDuration = database.getReferenceFromUrl(url+"Recipe/"+recipeID+"/rTime");
        likeCount = database.getReferenceFromUrl(url+"Recipe/"+recipeID+"/likes");
        recipeDescription = database.getReferenceFromUrl(url+"Recipe/"+recipeID+"/Description");
        recipeUtensils = database.getReferenceFromUrl(url+"Recipe/"+recipeID+"/Utensils");

        recipe_name = findViewById(R.id.recipe_name);

        recipeName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                recipe_name.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recipeURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                videoURL = dataSnapshot.getValue(String.class);
                Uri uri;
                uri = Uri.parse(videoURL);

                videoView = findViewById(R.id.video_view);
                mediaController = new MediaController(RecipeView.this);
                mediaController.setMediaPlayer(videoView);
                mediaController.setAnchorView(videoView);

                videoView.setMediaController(mediaController);

                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setLooping(true);
                    }
                });

                videoView.setVideoURI(uri);
                videoView.requestFocus();
                videoView.start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                mediaController.hide();
            }
        });

        recipeDuration.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                TextView duration = findViewById(R.id.durationValue);
                duration.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        likeCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                TextView likes = findViewById(R.id.favCountText);
                likes.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recipeDescription.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                TextView description = findViewById(R.id.textDescription);
                description.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recipeUtensils.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                TextView utensils = findViewById(R.id.utensils);

                utensils.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        goBack = findViewById(R.id.buttonBack);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        download = findViewById(R.id.buttonDownload);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Button Pressed",Toast.LENGTH_SHORT);
                downloadTask download = new downloadTask(getApplicationContext(),url);
                int downloadRequest = download.download();
            }
        });

        favorite = findViewById(R.id.favoriteButton);
        favoriteText = findViewById(R.id.favCountText);

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFav == false) {
                    int count = Integer.parseInt(favoriteText.getText().toString());
                    count = count + 1;
                    String value = String.valueOf(count);
                    favoriteText.setText(value);
                    favorite.setBackgroundResource(R.drawable.ic_favorite_filled);
                    isFav = true;
                    likeCount.setValue(value);
                } else {
                    int count = Integer.parseInt(favoriteText.getText().toString());
                    count = count - 1;
                    String value = String.valueOf(count);
                    favoriteText.setText(value);
                    favorite.setBackgroundResource(R.drawable.ic_favorite_unfilled);
                    isFav = false;
                    likeCount.setValue(value);
                }
            }
        });

        minus = findViewById(R.id.minusRecipe);
        add = findViewById(R.id.addRecipe);
        serving = findViewById(R.id.sevingCount);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(serving.getText().toString());
                if (count > 1) {
                    count--;
                    String value = String.valueOf(count);
                    serving.setText(value);
                } else {
                    Toast.makeText(getApplicationContext(), "Serving cannot be less than 1", Toast.LENGTH_SHORT);
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(serving.getText().toString());
                count++;
                String value = String.valueOf(count);
                serving.setText(value);
            }
        });

    }

    protected void setupPreparationButton() {

        btnBeginPreparation = findViewById(R.id.startPreparation);
        btnBeginPreparation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RecipeView.this, InstructionsActivity.class);
                // intent.putExtra(RecipeView.STEP_INSTRUCTIONS, );
                RecipeView.this.startActivity(intent);
            }
        });
    }

}
