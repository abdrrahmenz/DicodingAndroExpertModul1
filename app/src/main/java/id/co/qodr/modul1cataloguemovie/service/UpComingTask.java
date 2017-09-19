package id.co.qodr.modul1cataloguemovie.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

/**
 * Created by adul on 10/09/17.
 */

public class UpComingTask {

    private GcmNetworkManager mGcmNetworkManager;

    public UpComingTask(Context context) {
        mGcmNetworkManager = GcmNetworkManager.getInstance(context);
    }

    public void createPeriodicTask() {
        Task periodicTask = new PeriodicTask.Builder()
                .setService(UpComingReminderMovie.class)
                .setPeriod(60)
                .setFlex(10)
                .setTag(UpComingReminderMovie.TAG_TASK_UPCOMING_LOG)
                .setPersisted(true)
                .build();

        mGcmNetworkManager.schedule(periodicTask);
        Log.d("TAG", "createPeriodicTask: 1");
    }
}
