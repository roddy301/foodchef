package com.justifiers.foodchef.Instructions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.justifiers.foodchef.R;
import com.justifiers.foodchef.Recipe.Recipe;
import com.justifiers.foodchef.RecipeView;

import java.util.ArrayList;
import java.util.List;

enum PositionState {TO_LAST, FROM_LAST, TO_FIRST, FROM_FIRST, NONE}

public class InstructionsActivity extends AppCompatActivity {

    private static final String STEP_INSTRUCTIONS = "Step instructions";
    private static final String TAG = "InstructionsActivity";

    private Button btnNextStep;
    private Button btnPreviousStep;
    private ImageButton btnClose;

    private TextView txtStepNumber;
    private ViewPager stepPager;
    private InstructionsViewPagerAdapter instructionsViewPagerAdapter;

    private List<InstructionItem> steps;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableFullScreenMode();
        setContentView(R.layout.activity_instructions);

        configureViewPager();
        configureNavigationButtons();
        configureTextViews();
    }

    protected void configureTextViews() {

        txtStepNumber = findViewById(R.id.txt_instr_step);
        updateStepNumber(position);
    }

    protected void updateStepNumber(int newPosition) {

        this.position = newPosition;
        String stepNumber = String.format("%d/%d", position + 1, steps.size());
        txtStepNumber.setText(stepNumber);
    }

    protected void configureNavigationButtons() {

        btnClose = findViewById(R.id.btn_instr_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });

        btnNextStep = findViewById(R.id.btn_instr_next);
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = stepPager.getCurrentItem() + 1;
                Log.d(TAG, "onClick: position = " + position + " size = " + steps.size());
                if (position < steps.size()) {
                    stepPager.setCurrentItem(position);
                }

                if (position == steps.size()) {
                    closeActivity();
                }
            }
        });

        btnPreviousStep = findViewById(R.id.btn_instr_back);
        btnPreviousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = stepPager.getCurrentItem();
                if (position > 0) {
                    stepPager.setCurrentItem(position - 1);
                }
            }
        });

        // configure initial state
        btnPreviousStep.setVisibility(View.GONE);
    }

    protected void configureViewPager() {

        Intent intent = getIntent();
        steps = (List<InstructionItem>) intent.getSerializableExtra(InstructionsActivity.STEP_INSTRUCTIONS);

        stepPager = findViewById(R.id.steps_view_pager);
        instructionsViewPagerAdapter = new InstructionsViewPagerAdapter(this, steps);
        stepPager.setAdapter(instructionsViewPagerAdapter);

        stepPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: " + position);

                // additional configuration for different moving states
                switch (getMovingState(position)) {
                    case TO_LAST:
                        btnNextStep.setText(R.string.next_btn_title_finish);
                        break;
                    case FROM_LAST:
                        btnNextStep.setText(R.string.next_btn_title_instruct);
                        break;
                    case TO_FIRST:
                        btnPreviousStep.setVisibility(View.GONE);
                        break;
                    case FROM_FIRST:
                        btnPreviousStep.setVisibility(View.VISIBLE);
                        break;
                    default:
                        // do nothing
                }

                updateStepNumber(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    protected void enableFullScreenMode() {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // uncomment this line if there will be navigation bar at the top
        // getSupportActionBar().hide();
    }

    protected void closeActivity() {
        Log.d(TAG, "closeActivity: close activity");
        finish();
    }

    protected PositionState getMovingState(int newPosition) {

        int lastPosition = position;
        int maxPosition = steps.size() - 1;
        Log.d(TAG, "getMovingState: lastPosition = " + lastPosition + " maxPosition " + maxPosition + " new position " + newPosition);
        if (lastPosition == 1 && newPosition == 0) {
            return PositionState.TO_FIRST;
        }

        if (lastPosition == 0 && newPosition == 1) {
            return PositionState.FROM_FIRST;
        }

        if (lastPosition == maxPosition - 1 && newPosition == maxPosition) {
            return PositionState.TO_LAST;
        }

        if (lastPosition == maxPosition && newPosition == maxPosition - 1) {
            return PositionState.FROM_LAST;
        }

        return PositionState.NONE;
    }
}
