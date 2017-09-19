package id.co.qodr.modul1cataloguemovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import id.co.qodr.modul1cataloguemovie.BuildConfig;
import id.co.qodr.modul1cataloguemovie.MainActivity;
import id.co.qodr.modul1cataloguemovie.R;
import id.co.qodr.modul1cataloguemovie.model.MovieItems;

/**
 * Created by adul on 09/09/17.
 */

public class MovieAdapter extends BaseAdapter{
    private ArrayList<MovieItems> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;

    public MovieAdapter(Context context) {
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<MovieItems> items){
        mData = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public MovieItems getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null){
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.list_movie_items,null);
            holder.img_poster = view.findViewById(R.id.img_poster);
            holder.title = view.findViewById(R.id.tv_title);
            holder.overview = view.findViewById(R.id.tv_overview);
            holder.release_date = view.findViewById(R.id.tv_release_date);

            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        Glide.with(context).load(BuildConfig.BASE_URL_POSTER+mData.get(i).getPoster_path()).into(holder.img_poster);
        holder.title.setText(mData.get(i).getTitle());
        holder.overview.setText(mData.get(i).getOverview());
        holder.release_date.setText(mData.get(i).getRelease_date());

        return view;
    }

    public static class ViewHolder{
        public ImageView img_poster;
        public TextView title;
        public TextView overview;
        public TextView release_date;
    }
}
