package id.co.qodr.modul1cataloguemovie;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.qodr.modul1cataloguemovie.adapter.MovieAdapter;
import id.co.qodr.modul1cataloguemovie.model.MovieItems;
import id.co.qodr.modul1cataloguemovie.network.SearchMovieLoader;
import id.co.qodr.modul1cataloguemovie.service.DailyReminderMovie;
import id.co.qodr.modul1cataloguemovie.service.UpComingTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<ArrayList<MovieItems>>{

    private static final String TAG = "TAG";
    private MovieAdapter adapter;
    private ArrayList<MovieItems> movieItemses;
    private DailyReminderMovie dailyReminderMovie;
    private UpComingTask mUpComingTask;
    static final long ONE_MINUTE_IN_MILLIS=60000; //millisecs
    @BindView(R.id.tv_not_found) TextView tvNotFound;
    @BindView(R.id.edt_search_movie) EditText edtSearchMovie;
    @BindView(R.id.btn_search_movie) Button btnSearchMovie;
    @BindView(R.id.listview_movie) ListView listMovie;
    @BindView(R.id.progress) ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loading.setVisibility(View.GONE);
        listMovie.setVisibility(View.INVISIBLE);
        movieItemses = new ArrayList<>();

        adapter = new MovieAdapter(this);
        adapter.notifyDataSetChanged();
        listMovie.setAdapter(adapter);

        btnSearchMovie.setOnClickListener(this);
        dailyReminderMovie = new DailyReminderMovie();
        mUpComingTask = new UpComingTask(this);

        if (savedInstanceState != null) {
            getSupportLoaderManager().initLoader(0,null,this);
        }

        // Tambahkan 1 menit untuk mengetes Daily Reminder Notif jalan atau tidak, jika jalan comment lagi.
//        Calendar date = Calendar.getInstance();
//        long t= date.getTimeInMillis();
//        Date afterAddingMinutes=new Date(t + (1 * ONE_MINUTE_IN_MILLIS));

        // Mengambil waktu
//        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
//        String timeDailyReminder = dateFormat.format(afterAddingMinutes.getTime());
//        Log.d(TAG, "onCreate: Time -> "+timeDailyReminder);

        // Mengeset waktu dan message Daily Reminder Secara Manual dulu :D
        dailyReminderMovie.setDailyReminderMovieAlarm(this, DailyReminderMovie.TYPE_DAILY_REMINDER,
                "12:48", "Catalogue Movie is missing you...");

        // Mengeset Upcoming Movie / NowPlaying Movie (dengan tanggal 2017-09-14)
        mUpComingTask.createPeriodicTask();


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_search_movie){
            listMovie.setVisibility(View.INVISIBLE);
            tvNotFound.setVisibility(View.INVISIBLE);
            String searchMovie = edtSearchMovie.getText().toString();

            boolean isEmptyFields = false;

            if (TextUtils.isEmpty(searchMovie)){
                isEmptyFields = true;
                edtSearchMovie.setError("Field tidak boleh kosong");
            }
            if (!isEmptyFields){
                getSupportLoaderManager().initLoader(0,null,this);
            }
        }
    }

    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: 1");
        String searchMovie = edtSearchMovie.getText().toString();

        if (!TextUtils.isEmpty(searchMovie)){
            loading.setVisibility(View.VISIBLE);
            listMovie.setVisibility(View.INVISIBLE);
            Log.d(TAG, "onCreateLoader: "+searchMovie);
            return new SearchMovieLoader(this,searchMovie);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItems>> loader,final ArrayList<MovieItems> data) {
        Log.d(TAG, "onLoadFinished: 1");
        if (data.size()!=0) {
            loading.setVisibility(View.GONE);
            listMovie.setVisibility(View.VISIBLE);
            adapter.setData(data);
            listMovie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(MainActivity.this, ""+data.get(i).getId(), Toast.LENGTH_SHORT).show();
                    int ID_MOVIE = data.get(i).getId();
                    Intent intent = new Intent(MainActivity.this, DetailMovieActivity.class);
                    intent.putExtra(DetailMovieActivity.EXTRA_ID, ID_MOVIE);
                    startActivity(intent);
                }
            });
        }else {
            loading.setVisibility(View.GONE);
            listMovie.setVisibility(View.INVISIBLE);
            tvNotFound.setVisibility(View.VISIBLE);
            tvNotFound.setText("Result not Found !\nPastikan Internet Anda Aktif!");
            tvNotFound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportLoaderManager().restartLoader(0,null,MainActivity.this);
                    tvNotFound.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItems>> loader) {
        Log.d(TAG, "onLoaderReset: 1");
    }
}
