package com.justifiers.foodchef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.FirebaseDatabase;
import com.justifiers.foodchef.BottomNavigationView.SearchFragment;
import com.justifiers.foodchef.Instructions.InstructionsActivity;
import com.justifiers.foodchef.Recipe.Recipe;

import java.io.Serializable;

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


    String url = "https://foodchef-d5481.firebaseio.com/";
    String videoURL;
    Recipe recipe;

    FirebaseDatabase database = FirebaseDatabase.getInstance(url);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        Intent intent = getIntent();
        recipe = (Recipe) intent.getSerializableExtra(RecipeView.RECIPE);
        Log.d(TAG, "onCreate: recipe " + recipe.toString());

        recipe_name = findViewById(R.id.recipe_name);
        recipe_name.setText(recipe.getrName());

        setupVideo();



        TextView duration = findViewById(R.id.durationValue);
        duration.setText(recipe.getrTime());


        TextView description = findViewById(R.id.textDescription);
        description.setText(recipe.getrDescription());

        TextView utensils = findViewById(R.id.utensils);
        utensils.setText(recipe.getUtensils());

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

        StepperTouch stepperTouch = findViewById(R.id.stepperTouch);
        stepperTouch.setMinValue(0);
        stepperTouch.setMaxValue(3);
        stepperTouch.setSideTapEnabled(true);
        stepperTouch.addStepCallback(new OnStepCallback() {
            @Override
            public void onStep(int value, boolean positive) {
                Toast.makeText(getApplicationContext(), value + "", Toast.LENGTH_SHORT).show();
            }
        });

        setupPreparationButton();
    }

    protected void setupVideo() {

        videoURL = recipe.getrVideo();
        Uri uri;
        uri = Uri.parse("https://drive.google.com/file/d/1CeIy3h2jTtGN72aVhtTTK0N4-V0NKxRq/view?usp=sharing");

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

}
