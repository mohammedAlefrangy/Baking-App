/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.hmod_.bakingapp.NetWork;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public final class NetworkUtils {
    private static final String TAG = "NetworkUtils";


    private final static String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public NetworkUtils() {
    }


    public static JSONArray getIngredients(int id) {
        try {

            URL url = new URL(BASE_URL);
            String http_Url = getResponseFromHttpUrl(url);

            JSONArray arrayForIngredients = new JSONArray(http_Url);

            if (arrayForIngredients == null)
                return null;

            for (int i = 0; i < arrayForIngredients.length(); i++)
                if (arrayForIngredients.getJSONObject(i).getInt("id") == id)
                    return arrayForIngredients.getJSONObject(i).getJSONArray("ingredients");

            return null;

        } catch (IOException | JSONException e) {
            return null;
        }

    }

    public static String getDescription(int recipeId, int stepId) {

        try {

            JSONArray json_Array = getStepsJSONArray(recipeId);

            if (json_Array == null)
                return null;

            for (int i = 0; i < json_Array.length(); i++)
                if (json_Array.getJSONObject(i).getInt("id") == stepId)
                    return json_Array.getJSONObject(i).getString("description");

            return null;

        } catch (JSONException e) {
            return null;
        }

    }

    public static Uri getViedo(int recipeId, int stepId) {

        try {

            JSONArray json_Array = getStepsJSONArray(recipeId);

            if (json_Array == null)
                return null;

            for (int i = 0; i < json_Array.length(); i++)
                if (json_Array.getJSONObject(i).getInt("id") == stepId)
                    return Uri.parse(json_Array.getJSONObject(i).getString("videoURL"));
            return null;

        } catch (JSONException e) {
            return null;
        }

    }

    public static Uri getThumbnail(int recipeId, int stepId) {

        try {

            JSONArray json_Array = getStepsJSONArray(recipeId);

            if (json_Array == null)
                return null;

            for (int i = 0; i < json_Array.length(); i++)
                if (json_Array.getJSONObject(i).getInt("id") == stepId)
                    return Uri.parse(json_Array.getJSONObject(i).getString("thumbnailURL"));


            return null;

        } catch (JSONException e) {
            return null;
        }

    }


    public static JSONArray getRecipeJSONArray() {

        try {
            URL url = new URL(BASE_URL);
            String http_Url = getResponseFromHttpUrl(url);
            return (http_Url == null) ? null : new JSONArray(http_Url);
        } catch (IOException | JSONException e) {
            return null;
        }

    }

    public static JSONArray getStepsJSONArray(int recipeId) {

        try {

            URL url = new URL(BASE_URL);
            String http_Url = getResponseFromHttpUrl(url);

            JSONArray json_Array = new JSONArray(http_Url);

            if (json_Array == null)
                return null;

            for (int i = 0; i < json_Array.length(); i++)
                if (json_Array.getJSONObject(i).getLong("id") == recipeId)
                    return json_Array.getJSONObject(i).getJSONArray("steps");


            return null;

        } catch (IOException | JSONException e) {
            return null;
        }

    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}