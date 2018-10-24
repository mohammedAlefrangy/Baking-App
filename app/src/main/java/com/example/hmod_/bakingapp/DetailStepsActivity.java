package com.example.hmod_.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.hmod_.bakingapp.Fragment.DetailStepFragment;


public class DetailStepsActivity extends AppCompatActivity {
    private static final String TAG = "DetailStepsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);


        FragmentManager fragmentManager = getSupportFragmentManager();
        DetailStepFragment detailStepsActivity = (DetailStepFragment) fragmentManager.findFragmentById(R.id.detail_step_fragment);

        Intent intent = getIntent();
        Log.d(TAG, "onCreate: " + getIntent());
        Log.d(TAG, "onCreate:mmmmmmmmmm " + intent.getIntExtra("idRecipe" , 1));
        detailStepsActivity.setRecipeAndStep(intent.getIntExtra("recipe_id", 1), intent.getIntExtra("step_id", 0));

    }
}
