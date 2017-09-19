package id.co.qodr.modul1cataloguemovie.network;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import id.co.qodr.modul1cataloguemovie.BuildConfig;
import id.co.qodr.modul1cataloguemovie.model.MovieItems;

/**
 * Created by adul on 12/09/17.
 */

public class DetailMovieLoader extends AsyncTaskLoader<ArrayList<MovieItems>> {

    private ArrayList<MovieItems> mDataMovie;
    private boolean hasResult = false;
    private int ID_MOVIE;

    public DetailMovieLoader(Context context, int ID_MOVIE) {
        super(context);
        this.ID_MOVIE = ID_MOVIE;
        onContentChanged();
        Log.d("INIT", "SearchMovieLoader: 1");
    }

    @Override
    protected void onStartLoading() {
        Log.d("Content Changed", "onStartLoading: 1");
        if (takeContentChanged()){
            forceLoad();
        } else if (hasResult){
            deliverResult(mDataMovie);
        }
    }

    @Override
    public void deliverResult(ArrayList<MovieItems> data) {
        mDataMovie = data;
        hasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (hasResult){
            onReleaseResources(mDataMovie);
            hasResult = false;
        }
    }

    private void onReleaseResources(ArrayList<MovieItems> mDataMovie) {

    }

    @Override
    public ArrayList<MovieItems> loadInBackground() {
        Log.d("LoaderDetail", "loadInBackground: 1");
        final ArrayList<MovieItems> movieItemses = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/movie/"+ID_MOVIE+"?api_key="+ BuildConfig.API_KEY_MOVIE+"&language=en-US";
        SyncHttpClient client = new SyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d("TAGLoaderDetail", "onSuccess: "+result);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    MovieItems movieItems = new MovieItems(responseObject);
                    movieItems.setPoster_path(responseObject.getString("poster_path"));
                    movieItems.setTitle(responseObject.getString("title"));
                    movieItems.setOverview(responseObject.getString("overview"));
                    movieItems.setRelease_date(responseObject.getString("release_date"));
                    movieItemses.add(movieItems);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("TAGLoaderDetail", "REQUEST FAIL: 1");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("TAGLoaderDetail", "onFailure: FAIL");
            }
        });
        return movieItemses;
    }
}
