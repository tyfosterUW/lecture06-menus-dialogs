package edu.uw.uicomponentsdemo;

/**
 * Created by joelross on 4/12/17.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
* A simple {@link Fragment} subclass representing a list of Movie search results.
*/
public class MoviesFragment extends Fragment {

    private static final String TAG = "MoviesFragment";
    private static final String SEARCH_PARAM_KEY = "search_term";

    private ArrayAdapter<Movie> adapter;

    public MoviesFragment() {
        // Required empty public constructor
    }

    //factory method for creating the Fragment
    public static MoviesFragment newInstance(String searchTerm) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putString(SEARCH_PARAM_KEY, searchTerm);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        //controller
        adapter = new ArrayAdapter<Movie>(this.getActivity(),
                R.layout.movie_item_layout, R.id.txtItem, new ArrayList<Movie>());

        ListView listView = (ListView)rootView.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie)parent.getItemAtPosition(position);
                Log.v(TAG, "You clicked on: "+movie);
            }
        });

        if(getArguments() != null) {
            String searchTerm = getArguments().getString(SEARCH_PARAM_KEY);
            if(searchTerm != null)
                downloadMovieData(searchTerm);
        }

        return rootView;
    }

    //helper method for downloading the data via the MovieDowloadTask
    public void downloadMovieData(String searchTerm){
        Log.v(TAG, "You searched for: "+searchTerm);
        MovieDownloadTask task = new MovieDownloadTask();
        task.execute(searchTerm);
    }

    //A task to download movie data from the internet on a background thread
    public class MovieDownloadTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            ArrayList<Movie> data = MovieDownloader.downloadMovieData(params[0]);
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);

            adapter.clear();
            for(Movie movie : movies){
                adapter.add(movie);
            }
        }
    }

    /**
     * A simple container for Movie info
     */
    public static class Movie {
        public String title;
        public int year;
        public String imdbId;
        public String posterUrl;

        public Movie(String title, int year, String imdbId, String posterUrl){
            this.title = title;
            this.year = year;
            this.imdbId = imdbId;
            this.posterUrl = posterUrl;
        }

        //default constructor; empty movie
        public Movie(){}

        public String toString(){
            return this.title + " ("+this.year+")";
        }
    }

    /**
     * A class for downloading movie data from the internet.
     * Code adapted from Google.
     *
     * @author Joel Ross
     * @version 2 (ArrayList-based)
     */
    public static class MovieDownloader {

        private static final String TAG = "MovieDownloader";

        //Returns ArrayList of Movies
        public static ArrayList<Movie> downloadMovieData(String movie) {

            //construct the url for the omdbapi API
            String urlString = "";
            try {
                urlString = "http://www.omdbapi.com/?s=" + URLEncoder.encode(movie, "UTF-8") + "&type=movie";
            }catch(UnsupportedEncodingException uee){
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            ArrayList<Movie> movies = null;

            try {

                URL url = new URL(urlString);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line = reader.readLine();
                while (line != null) {
                    buffer.append(line + "\n");
                    line = reader.readLine();
                }

                if (buffer.length() == 0) {
                    return null;
                }
                String results = buffer.toString();

                movies = parseMovieJSONData(results);
                if(movies == null)
                    movies = new ArrayList<Movie>();

                Log.v(TAG, movies.toString()); //for debugging purposes
            }
            catch (IOException e) {
                return null;
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    }
                    catch (IOException e) {
                    }
                }
            }

            return movies;
        }

        /**
         * Parses a JSON-format String (from OMDB search results) into a list of Movie objects
         */
        public static ArrayList<Movie> parseMovieJSONData(String json){
            ArrayList<Movie> movies = new ArrayList<Movie>();

            try {
                JSONArray moviesJsonArray = new JSONObject(json).getJSONArray("Search"); //get array from "search" key
                for(int i=0; i<moviesJsonArray.length(); i++){
                    JSONObject movieJsonObject = moviesJsonArray.getJSONObject(i); //get ith object from array
                    Movie movie = new Movie();
                    movie.title = movieJsonObject.getString("Title"); //get title from object
                    movie.year = Integer.parseInt(movieJsonObject.getString("Year")); //get year from object
                    movie.imdbId = movieJsonObject.getString("imdbID"); //get imdb from object
                    movie.posterUrl = movieJsonObject.getString("Poster"); //get poster from object

                    movies.add(movie);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing json", e);
                return null;
            }

            return movies;
        }
    }
}
