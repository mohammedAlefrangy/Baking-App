package com.example.hmod_.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.hmod_.bakingapp.Adapter.AdapterForRecipe;
import com.example.hmod_.bakingapp.NetWork.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AdapterForRecipe.RecipeClickListener {

    private static final String TAG = "MainActivity";
    private AdapterForRecipe adapterForRecipe;
    private static final String ID = "id";
    private static final String NAME = "name";
    LinearLayoutManager layoutManager;
    GridLayoutManager gridLayoutManager ;
    private Context mContext;
    private boolean mobileTapelt;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mContext = this;
        mobileTapelt = getResources().getBoolean(R.bool.MOBILE_TABLET);
        adapterForRecipe = new AdapterForRecipe(this, this);


        if (!mobileTapelt) {
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapterForRecipe);
        }
        else {
            gridLayoutManager = new GridLayoutManager(this, 3);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(adapterForRecipe);
        }



        new AsyncTaskForRecipe().execute();

    }

    @Override
    public void onRecipeClick(int id, String name) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(ID, id);
        intent.putExtra(NAME, name);
        startActivity(intent);

    }


    private class AsyncTaskForRecipe extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adapterForRecipe.add(null, null);
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: " + "mmmmmmmmm");
            return NetworkUtils.getRecipeJSONArray();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            super.onPostExecute(jsonArray);

            if (jsonArray != null && jsonArray.length() != 0) {

                int jsonArrayLength = jsonArray.length();
                JSONObject jsonObject;

                try {
                    int[] id = new int[jsonArrayLength];
                    String[] name = new String[jsonArrayLength];
                    for (int i = 0; i < jsonArrayLength; i++) {

                        jsonObject = jsonArray.getJSONObject(i);
                        id[i] = jsonObject.getInt(ID);
                        name[i] = jsonObject.getString(NAME);
                    }
                    adapterForRecipe.add(id, name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
