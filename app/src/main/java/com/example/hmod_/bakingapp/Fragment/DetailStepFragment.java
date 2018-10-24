package com.example.hmod_.bakingapp.Fragment;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hmod_.bakingapp.NetWork.NetworkUtils;
import com.example.hmod_.bakingapp.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailStepFragment extends Fragment {

    private SimpleExoPlayer playerExo;
    private SimpleExoPlayerView playerViewExo;
    private ImageView imageView;
    private TextView textViewInstructions;
    private String UriMedia;
    private Uri mediaUri;
    private boolean mobiletablet;
    private static final String POSITION = "position";
    private static final String PLAYER_URI = "player_uri";
    private static final String BAKING = "BakingApp";
    private static final String ERROR_DATA = "Error data";
    private static final String SPACE = "";
    private int current_Position;
    private int idRecipe = 1;
    private int step_Id = 0;
    private static final String TAG = "DetailStepFragment";

    public DetailStepFragment() {

    }

    public int getStepId() {
        return step_Id;
    }

    public int getCurrentPosition() {
        return current_Position;
    }

    public void setCurrentPosition(int currentPosition) {
        current_Position = currentPosition;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mobiletablet = getResources().getBoolean(R.bool.MOBILE_TABLET);
        View view = inflater.inflate(R.layout.step_frgment, container, false);
        playerViewExo = view.findViewById(R.id.exo_player);
        textViewInstructions = view.findViewById(R.id.details);
        imageView = view.findViewById(R.id.image_details);
        if (!mobiletablet) {
            if (savedInstanceState != null)
                current_Position = savedInstanceState.getInt(POSITION);
            else
                current_Position = 0;
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, current_Position);
        outState.putString(PLAYER_URI, UriMedia);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: " + current_Position);

        if (playerExo == null && mediaUri != null) {
            current_Position = (int) playerExo.getCurrentPosition();
        }

        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (playerExo == null && mediaUri != null)
            initializePlayer(mediaUri, current_Position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    private void loadData() {
        new InstructionsAsyncTask().execute();
        new PlayerVideoUriAsyncTask().execute();
        new ImageUriAsyncTask().execute();
    }

    private void initializePlayer(Uri mediaUri, long currentPosition) {


        try {
            Context context = getContext();
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            playerExo = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
            playerViewExo.setPlayer(playerExo);

            String userAgent = Util.getUserAgent(context, BAKING);
            MediaSource mediaSource = new ExtractorMediaSource
                    (mediaUri, new DefaultDataSourceFactory(context, userAgent),
                            new DefaultExtractorsFactory(), null, null);
            playerExo.prepare(mediaSource);
            playerExo.setPlayWhenReady(false);
            playerExo.seekTo(current_Position);

            UriMedia = mediaUri.toString();
        } catch (Exception ex) {
            playerViewExo.setVisibility(View.GONE);
        }
    }

    public void releasePlayer() {

        if (playerExo != null) {
            current_Position = (int) playerExo.getCurrentPosition();
            playerExo.stop();
            playerExo.release();
            playerExo = null;
        }

    }

    public void setRecipeAndStep(int recipeId, int stepId) {

        idRecipe = recipeId;
        step_Id = stepId;
        Log.d(TAG, "setRecipeAndStepIdsAndLoad: " + recipeId + " jhhhhghbdf " + stepId);
        loadData();

    }

    private class InstructionsAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return NetworkUtils.getDescription(idRecipe, step_Id);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null && !s.equals(SPACE)) {
                textViewInstructions.setText(s);
            } else {
                textViewInstructions.setText(ERROR_DATA);
            }
        }
    }

    private class PlayerVideoUriAsyncTask extends AsyncTask<Void, Void, Uri> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Uri doInBackground(Void... voids) {

            return NetworkUtils.getViedo(idRecipe, step_Id);
        }

        @Override
        protected void onPostExecute(Uri uri) {
            super.onPostExecute(uri);
            if (uri != null && !uri.toString().equals(SPACE)) {
                initializePlayer(uri, current_Position);
                mediaUri = Uri.parse(uri.toString());
                playerViewExo.setVisibility(View.VISIBLE);
            } else
                playerViewExo.setVisibility(View.GONE);
        }
    }

    private class ImageUriAsyncTask extends AsyncTask<Void, Void, Uri> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Uri doInBackground(Void... voids) {
            return NetworkUtils.getThumbnail(idRecipe, step_Id);
        }

        @Override
        protected void onPostExecute(Uri uri) {
            super.onPostExecute(uri);

            if (uri != null && !uri.toString().equals(SPACE)) {
                try {
                    Picasso.with(getContext())
                            .load(uri)
                            .error(R.drawable.card)
                            .fit()
                            .into(imageView);
                    imageView.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    imageView.setVisibility(View.GONE);
                }
            } else {
                imageView.setVisibility(View.GONE);
            }
        }
    }
}
