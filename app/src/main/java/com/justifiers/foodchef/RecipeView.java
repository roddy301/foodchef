package com.justifiers.foodchef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.FirebaseDatabase;
import com.justifiers.foodchef.Instructions.InstructionsActivity;
import com.justifiers.foodchef.Recipe.Recipe;

import java.io.Serializable;
import java.util.ArrayList;

import nl.dionsegijn.steppertouch.OnStepCallback;
import nl.dionsegijn.steppertouch.StepperTouch;


public class RecipeView extends AppCompatActivity {

    private static final String STEP_INSTRUCTIONS = "Step instructions";
    private static final String RECIPE = "RecipeInformation";
    private static final String TAG = "RecipeView";

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
    ArrayList<Ingredient> ingredientsList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Context ctx;



    String url = "https://foodchef-d5481.firebaseio.com/";
    String videoURL;
    Recipe recipe;
    IngredientsAdapter iAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance(url);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        Intent intent = getIntent();
        recipe = (Recipe) intent.getSerializableExtra(RecipeView.RECIPE);

        recyclerView = findViewById(R.id.nameOfIngredients_recycler_view);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        iAdapter = new IngredientsAdapter(recipe.getIngredients(), ctx);
        recyclerView.setAdapter(iAdapter);

        setupVideo();

        TextView recipeName = findViewById(R.id.recipe_name);
        recipeName.setText(recipe.getrName());

        TextView duration = findViewById(R.id.durationValue);
        duration.setText(recipe.getrTime());


        TextView description = findViewById(R.id.textDescription);
        description.setText(recipe.getrDescription());

        TextView utensils = findViewById(R.id.utensils);
        utensils.setText(recipe.getUtensils());

        backButton();
        StepperView();
        setupPreparationButton();
    }
    protected void backButton(){
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

                Toast.makeText(getApplicationContext(), "Button Pressed", Toast.LENGTH_SHORT);
                downloadTask download = new downloadTask(getApplicationContext(), url);

                int downloadRequest = download.download();
            }
        });
    }

    protected void StepperView(){
        StepperTouch stepperTouch = findViewById(R.id.stepperTouch);
        stepperTouch.setMinValue(1);
        stepperTouch.setMaxValue(10);
        stepperTouch.setSideTapEnabled(true);
        stepperTouch.addStepCallback(new OnStepCallback() {
            @Override
            public void onStep(int value, boolean positive) {
                Toast.makeText(getApplicationContext(), value + "", Toast.LENGTH_SHORT).show();
            }
        });
    }



    protected void setupVideo() {

        videoURL = recipe.getrVideo();
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

        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                mediaController.hide();
            }
        });
    }

    protected void setupPreparationButton() {

        btnBeginPreparation = findViewById(R.id.startPreparation);
        btnBeginPreparation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RecipeView.this, InstructionsActivity.class);
                intent.putExtra(RecipeView.STEP_INSTRUCTIONS, (Serializable) recipe.getrInstructionSteps());
                RecipeView.this.startActivity(intent);
            }
        });
    }
    /*protected void dowloadData (){
        String URL = "https://foodchef-d5481.firebaseio.com/Recipe.json";

    }*/

    protected void showIngredients () {


    }

}

