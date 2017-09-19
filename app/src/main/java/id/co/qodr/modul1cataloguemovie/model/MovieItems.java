package id.co.qodr.modul1cataloguemovie.model;

import org.json.JSONObject;

/**
 * Created by adul on 09/09/17.
 */

public class MovieItems {
    private int id;
    private String title;
    private String poster_path;
    private String overview;
    private String release_date;

    public MovieItems(JSONObject object) {
        try {
            int id = object.getInt("id");
            String title = object.getString("title");
            String poster = object.getString("poster_path");
            String overview = object.getString("overview");
            String release_date = object.getString("release_date");
            this.id = id;
            this.title = title;
            this.poster_path = poster;
            this.overview = overview;
            this.release_date = release_date;

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
