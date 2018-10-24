package com.example.hmod_.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.hmod_.bakingapp.Fragment.DetailStepFragment;
import com.example.hmod_.bakingapp.Fragment.DetailsFragment;

public class DetailsActivity extends AppCompatActivity implements DetailsFragment.OnStepClickListener {

    private static final String NAME = "name";
    private boolean mobileTabelt;
    private DetailsFragment detailsFragment;
    private DetailStepFragment detailStepFragment;
    FragmentManager fragmentManager ;
    private static final String SCROLL_POSITION = "scroll_position";
    private static final String POSITION = "position";
    private static final String ID = "id";
    private static final String ID_RECIPE = "recipe_id";
    private static final String ID_STEP = "step_id";
    private int idRecipe;
    private int idStep;
    private String nameRecipe;
    private static final String TAG = "DetailsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);

        mobileTabelt = getResources().getBoolean(R.bool.MOBILE_TABLET);
        fragmentManager = getSupportFragmentManager();
        detailsFragment = (DetailsFragment) fragmentManager.findFragmentById(R.id.recipe_detail_fragment);

        if (mobileTabelt)
            detailStepFragment = (DetailStepFragment) fragmentManager.findFragmentById(R.id.detail_step_fragment);
        if (savedInstanceState != null) {
            idRecipe = savedInstanceState.getInt(ID_RECIPE);
            idStep = savedInstanceState.getInt(ID_STEP);
            nameRecipe = savedInstanceState.getString(NAME);
            Log.d(TAG, "onCreate: " + nameRecipe);
            if (mobileTabelt) {
                detailsFragment.setRecyclerViewState(savedInstanceState.getParcelable(SCROLL_POSITION));
                detailStepFragment.setCurrentPosition(savedInstanceState.getInt(POSITION));
            }
        } else {

            Intent i = getIntent();
            idRecipe = i.getIntExtra(ID, 1);
            nameRecipe = i.getStringExtra(NAME);
            Log.d(TAG, "onCreate: " + nameRecipe +" "+ idRecipe);
            idStep = 0;
            if (mobileTabelt)
                detailStepFragment.setCurrentPosition(0);
        }

        detailsFragment.setRecipeDataAndLoad(idRecipe, nameRecipe);
        if (mobileTabelt)
            detailStepFragment.setRecipeAndStep(idRecipe, idStep);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (mobileTabelt)
            detailStepFragment.releasePlayer();

        nameRecipe = detailsFragment.getRecipeName();
        if (intent.getStringExtra(NAME) != null && !intent.getStringExtra(NAME).equals(""))
            nameRecipe = intent.getStringExtra(NAME);
        idRecipe = intent.getIntExtra(ID, detailsFragment.getRecipeId());
        detailsFragment.setRecipeDataAndLoad(idRecipe, nameRecipe);

        if (mobileTabelt) {
            idStep = detailStepFragment.getStepId();
            detailStepFragment.setRecipeAndStep(idRecipe, idStep);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(NAME, nameRecipe);
        outState.putInt(ID_STEP, idStep);
        outState.putInt(ID_RECIPE, idRecipe);
        if (mobileTabelt) {
            outState.putLong(POSITION, detailStepFragment.getCurrentPosition());
            outState.putParcelable(SCROLL_POSITION, detailsFragment.recyclerViewState());
        }
    }


    @Override
    public void onStepClick(int step_Id) {
        Toast.makeText(this, "Position clicked" + step_Id, Toast.LENGTH_SHORT).show();
        if (mobileTabelt) {
            idRecipe = detailsFragment.getRecipeId();

            idStep = step_Id;
            detailStepFragment.setRecipeAndStep(idRecipe, step_Id);
            detailStepFragment.setCurrentPosition(step_Id);
        }
    }
}
