package id.co.qodr.modul1cataloguemovie.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import id.co.qodr.modul1cataloguemovie.BuildConfig;
import id.co.qodr.modul1cataloguemovie.DetailMovieActivity;
import id.co.qodr.modul1cataloguemovie.MainActivity;
import id.co.qodr.modul1cataloguemovie.R;
import id.co.qodr.modul1cataloguemovie.model.MovieItems;

/**
 * Created by adul on 10/09/17.
 */

public class UpComingReminderMovie extends GcmTaskService {

    public static final String TAG = "TAG";
    public static String TAG_TASK_UPCOMING_LOG = "UpcomingTask";
    private int notifId = 100;


    @Override
    public int onRunTask(TaskParams taskParams) {
        int result = 0;
        if (taskParams.getTag().equals(TAG_TASK_UPCOMING_LOG)){
            getUpComingMovieData();
            result = GcmNetworkManager.RESULT_SUCCESS;
        }
        return result;
    }

    private void getUpComingMovieData() {
        final ArrayList<MovieItems> movieItemses = new ArrayList<>();
        SyncHttpClient client = new SyncHttpClient();
        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key="+ BuildConfig.API_KEY_MOVIE+"&language=en-US";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movie = list.getJSONObject(i);
                        MovieItems movieItems = new MovieItems(movie);
                        movieItemses.add(movieItems);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                for (int i = 0; i < movieItemses.size(); i++) {
                    if (movieItemses.get(i).getRelease_date().equals("2017-09-14")){
                        String title = movieItemses.get(i).getTitle();
                        String message = "Hari ini "+title+" release";
                        String poster = movieItemses.get(i).getPoster_path();
                        String overview = movieItemses.get(i).getOverview();
                        String release = movieItemses.get(i).getRelease_date();
                        showNotification(getApplicationContext(), title , message , poster ,
                                overview , release, ++notifId);
                        Log.d("TITLE", movieItemses.get(i).getTitle());
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();
        UpComingTask mUpComingTask = new UpComingTask(this);
        mUpComingTask.createPeriodicTask();
    }

    private void showNotification(Context context, String title, String message, String poster,
                                  String overview, String release, int notifId) {
        Intent notifDetailIntent = new Intent(context, DetailMovieActivity.class);
        notifDetailIntent.putExtra(DetailMovieActivity.EXTRA_TITLE, title);
        notifDetailIntent.putExtra(DetailMovieActivity.EXTRA_POSTER_PATCH, poster);
        notifDetailIntent.putExtra(DetailMovieActivity.EXTRA_OVERVIEW, overview);
        notifDetailIntent.putExtra(DetailMovieActivity.EXTRA_RELEASE_DATE, release);

        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addParentStack(DetailMovieActivity.class)
                .addNextIntent(notifDetailIntent)
                .getPendingIntent(notifId, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(notifId, builder.build());
    }
}
