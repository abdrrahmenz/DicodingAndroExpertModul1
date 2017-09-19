package id.co.qodr.modul1cataloguemovie;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.qodr.modul1cataloguemovie.model.MovieItems;
import id.co.qodr.modul1cataloguemovie.network.DetailMovieLoader;

public class DetailMovieActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<MovieItems>>{

    private static final String TAG = "TAG";
    public static String EXTRA_ID = "id";
    public static String EXTRA_TITLE = "title";
    public static String EXTRA_POSTER_PATCH = "poster";
    public static String EXTRA_OVERVIEW = "overview";
    public static String EXTRA_RELEASE_DATE = "release";
    private int ID_MOVIE;
    @BindView(R.id.progress) ProgressBar loading;
    @BindView(R.id.img_poster) ImageView imgPoster;
    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.tv_overview) TextView tvOverView;
    @BindView(R.id.tv_release_date) TextView tvReleaseDate;
    @BindView(R.id.tv_not_found) TextView tvNotFound;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ID_MOVIE = getIntent().getIntExtra(EXTRA_ID, 0);

        if (ID_MOVIE != 0) {
            if (savedInstanceState != null) {
                loading.setVisibility(View.GONE);
                getSupportLoaderManager().initLoader(0,null,this);
            } else {
                getSupportLoaderManager().initLoader(0,null,this);
            }
        } else {
            Log.d(TAG, "onCreate: EXTRA_ID == 0");
            loading.setVisibility(View.GONE);
            String title = getIntent().getStringExtra(EXTRA_TITLE);
            String poster = getIntent().getStringExtra(EXTRA_POSTER_PATCH);
            String overview = getIntent().getStringExtra(EXTRA_OVERVIEW);
            String release = getIntent().getStringExtra(EXTRA_RELEASE_DATE);

            Glide.with(DetailMovieActivity.this)
                    .load(BuildConfig.BASE_URL_POSTER+poster)
                    .into(imgPoster);
            tvTitle.setText(title);
            tvOverView.setText("Overview : "+overview);
            tvReleaseDate.setText("release date : "+release);
        }
    }

    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, final Bundle args) {
        if (ID_MOVIE!=0){
            loading.setVisibility(View.VISIBLE);
            return new DetailMovieLoader(this,ID_MOVIE);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> data) {
        loading.setVisibility(View.GONE);
        String poster = data.get(0).getPoster_path();
        String title = data.get(0).getTitle();
        String overview = data.get(0).getOverview();
        String release_date = data.get(0).getRelease_date();
        Glide.with(DetailMovieActivity.this)
                .load(BuildConfig.BASE_URL_POSTER+poster)
                .into(imgPoster);
        tvTitle.setText(title);
        tvOverView.setText("Overview : "+overview);
        tvReleaseDate.setText("release date : "+release_date);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItems>> loader) {

    }
}
