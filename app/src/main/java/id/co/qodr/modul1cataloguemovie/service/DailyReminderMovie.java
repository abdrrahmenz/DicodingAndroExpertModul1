package id.co.qodr.modul1cataloguemovie.service;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import id.co.qodr.modul1cataloguemovie.MainActivity;
import id.co.qodr.modul1cataloguemovie.R;

/**
 * Created by adul on 09/09/17.
 */

public class DailyReminderMovie extends BroadcastReceiver {

    public static final String TYPE_DAILY_REMINDER = "DailyReminderMovie";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";
    private final int NOTIF_ID_REPEATING = 50;

    public DailyReminderMovie() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG", "onReceive: Running");
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        String title = type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? "Daily Reminder Movie" : " Not Set";
        int notifId = type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? NOTIF_ID_REPEATING : 0;
        showDailyNotification(context, title, message, notifId);
    }

    private void showDailyNotification(Context context, String title, String message, int notifId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        Uri notifSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                .setSound(notifSound);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);
        notificationManager.notify(notifId,builder.build());
    }

    public void setDailyReminderMovieAlarm(Context context, String type, String time, String message){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderMovie.class);
        intent.putExtra(EXTRA_TYPE, type);
        intent.putExtra(EXTRA_MESSAGE, message);

        String timeArray[] = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND,0);

        int requestCode = NOTIF_ID_REPEATING;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,requestCode, intent,0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(context, "DailyReminder set up", Toast.LENGTH_SHORT).show();

    }
}
