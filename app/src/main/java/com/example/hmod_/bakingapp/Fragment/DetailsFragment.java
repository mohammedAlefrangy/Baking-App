package com.example.hmod_.bakingapp.Fragment;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hmod_.bakingapp.Adapter.AdapterForStep;
import com.example.hmod_.bakingapp.DetailStepsActivity;
import com.example.hmod_.bakingapp.NetWork.NetworkUtils;
import com.example.hmod_.bakingapp.R;


public class DetailsFragment extends Fragment implements AdapterForStep.StepClickListener {

    private int idRecipe = 1;
    private String recipe_Name;
    private AdapterForStep adapterForStep;
    TextView titel_Name, ingridents_detilas;
    LinearLayoutManager LayoutManager;
    private RecyclerView recyclerView;
    private Parcelable listState;
    private boolean mobileTapelt ;
    private OnStepClickListener onStepClickListener;

    private static final String SCROLL_POSITION = "scroll_position";
    private static final String DESCRIPTION = "shortDescription";
    private static final String INGREDIENT = "ingredient";
    private static final String QUENTITY = "quantity";
    private static final String MEASURE = "measure";
    private static final String ID_RECIPE = "recipe_id";
    private static final String ID_STEP = "step_id";
    private static final String TAG = "DetailsFragment";


    public DetailsFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mobileTapelt = getResources().getBoolean(R.bool.MOBILE_TABLET);
        View view = inflater.inflate(R.layout.details_recipe, container, false);
        titel_Name = view.findViewById(R.id.title_details);
        ingridents_detilas = view.findViewById(R.id.ingridents_detilas);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            adapterForStep = new AdapterForStep(getContext(), this);
            LayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }
        recyclerView = view.findViewById(R.id.steps_list);
        recyclerView.setLayoutManager(LayoutManager);
        recyclerView.setAdapter(adapterForStep);

        if (!mobileTapelt) {
            if (savedInstanceState != null) {
                listState = savedInstanceState.getParcelable(SCROLL_POSITION);
            }
        }

        new StepsAsyncTask().execute();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listState != null)
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SCROLL_POSITION, recyclerView.getLayoutManager().onSaveInstanceState());
    }


    public void setRecipeDataAndLoad(int recipeId, String recipeName) {
        idRecipe = recipeId;
        recipe_Name = recipeName;
        titel_Name.setText(recipe_Name);
        new StepsAsyncTask().execute();
    }

    public Parcelable recyclerViewState() {
        return recyclerView.getLayoutManager().onSaveInstanceState();
    }

    public void setRecyclerViewState(Parcelable state) {
        listState = state;
    }

    public int getRecipeId() {
        return idRecipe;
    }

    public String getRecipeName() {
        return recipe_Name;
    }


    public interface OnStepClickListener {
        void onStepClick(int stepId);
    }

    @Override
    public void onStepsClick(int id) {
        Log.d(TAG, "onStepsClick: " + mobileTapelt);
        if (!mobileTapelt) {
            Log.d(TAG, "onStepsClick: " + mobileTapelt);
            Intent intent = new Intent(getContext(), DetailStepsActivity.class);
            intent.putExtra(ID_RECIPE, idRecipe);
            intent.putExtra(ID_STEP, id);
            Log.d(TAG, "onStepsClick: " + idRecipe + " " + id);
            startActivity(intent);

        } else
            Toast.makeText(getContext(), "Position clicked" + id, Toast.LENGTH_SHORT).show();
        onStepClickListener.onStepClick(id);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onStepClickListener = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }


    private class StepsAsyncTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adapterForStep.add(null);
        }

        @Override
        protected String[] doInBackground(Void... voids) {

            String[] returnResult = null;
            JSONArray ingredients_array = NetworkUtils.getIngredients(idRecipe);
            JSONArray steps_Array = NetworkUtils.getStepsJSONArray(idRecipe);
            try {
                if (steps_Array != null && steps_Array.length() != 0) {
                    int stepsLength = steps_Array.length();
                    returnResult = new String[stepsLength + 1];

                    JSONObject jsonObject;
                    for (int i = 0; i < stepsLength; i++) {
                        jsonObject = steps_Array.getJSONObject(i);
                        returnResult[i] = jsonObject.getString(DESCRIPTION);
                    }
                }

                if (ingredients_array != null && ingredients_array.length() != 0) {

                    if (returnResult == null)
                        returnResult = new String[1];

                    int ingsLength = ingredients_array.length();

                    StringBuilder builder = new StringBuilder(100);

                    JSONObject jsonObject = ingredients_array.getJSONObject(0);

                    String quantity = jsonObject.getString(QUENTITY);
                    String measure = jsonObject.getString(MEASURE);
                    String ingredient = jsonObject.getString(INGREDIENT);


                    builder.append(" " + quantity + " " + measure + " " + ingredient);
                    for (int i = 1; i < ingsLength; i++) {
                        jsonObject = ingredients_array.getJSONObject(i);
                        ingredient = jsonObject.getString(INGREDIENT);
                        quantity = jsonObject.getString(QUENTITY);
                        measure = jsonObject.getString(MEASURE);
                        builder.append("\n " + quantity + "-" + measure + "-" + ingredient);
                    }
                    builder.trimToSize();
                    returnResult[returnResult.length - 1] = builder.toString();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnResult;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);

            if (strings != null && strings.length != 0) {
                ingridents_detilas.setText(strings[strings.length - 1]);
                String[] description = new String[strings.length - 1];
                for (int i = 0; i < description.length; i++)
                    description[i] = strings[i];
                adapterForStep.add(description);
                recyclerView.getLayoutManager().onRestoreInstanceState(listState);
            }
        }
    }

}
