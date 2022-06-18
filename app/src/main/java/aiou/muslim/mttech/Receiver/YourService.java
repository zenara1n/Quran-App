package aiou.muslim.mttech.Receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import aiou.muslim.mttech.Helper.NotifyActivityHandlerStop;
import aiou.muslim.mttech.MainActivity;
import aiou.muslim.mttech.R;
import aiou.muslim.mttech.SharedData.SharedClass;

import static aiou.muslim.mttech.Receiver.Notification_receiver.TASK_ID_TO_DISPLAY;

public class YourService extends Service {

    SharedPreferences preferences;
    int flag = 0;
    public static NotificationManager notificationManager;
    public static MediaPlayer mp;
    public int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else {
            startForeground(1, new Notification());
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setAutoCancel(true)
                .build();
        startForeground(2, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        startTimer();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stoptimertask();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }


    private Timer timer;
    private TimerTask timerTask;

    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {

                Log.d("timeis", preferences.getString("fajr", "01:00")+"\n"+preferences.getString("duhur", "01:00"));


                String[] time1 = preferences.getString("fajr", "04:40").split(":");
                int fajrhh = Integer.parseInt(time1[0].trim());
                int fajrmm = Integer.parseInt(time1[1].trim());
                String[] time2 = preferences.getString("duhur", "12:06").split(":");
                int zohrhh = Integer.parseInt(time2[0].trim());
                int zohrmm = Integer.parseInt(time2[1].trim());
                String[] time3 = preferences.getString("asr", "15:27").split(":");
                int asarhh = Integer.parseInt(time3[0].trim());
                int asarmm = Integer.parseInt(time3[1].trim());
                String[] time4 = preferences.getString("maghrib", "18:19").split(":");
                int maghribhh = Integer.parseInt(time4[0].trim());
                int maghribmm = Integer.parseInt(time4[1].trim());
                String[] time5 = preferences.getString("isha", "19:28").split(":");
                int ishahh = Integer.parseInt(time5[0].trim());
                int ishamm = Integer.parseInt(time5[1].trim());

                if (DateFormat.is24HourFormat(getApplicationContext())){
                    if (fajrhh<12) {
                        fajrhh = fajrhh+12;
                    }
                    if (zohrhh<12) {
                        zohrhh = zohrhh+12;
                    }
                    if (asarhh<12) {
                        asarhh = asarhh+12;
                    }
                    if (maghribhh<12) {
                        maghribhh = maghribhh+12;
                    }
                    if (ishahh<12) {
                        ishahh = ishahh+12;
                    }
                }else {
                    if (fajrhh>12) {
                        fajrhh = fajrhh-12;
                    }
                    if (zohrhh>12) {
                        zohrhh = zohrhh-12;
                    }
                    if (asarhh>12) {
                        asarhh = asarhh-12;
                    }
                    if (maghribhh>12) {
                        maghribhh = maghribhh-12;
                    }
                    if (ishahh>12) {
                        ishahh = ishahh-12;
                    }
                }

                Calendar today = Calendar.getInstance();
                int hh = today.get(Calendar.HOUR_OF_DAY);
                int mm = today.get(Calendar.MINUTE);
                int ss = today.get(Calendar.SECOND);

                if (ss == 5) {
                    flag = 0;
                }

                if (hh == fajrhh && mm == fajrmm && ss == 0 && SharedClass.getAlarmStatus(getApplicationContext(), 1) == 1) {
                    Intent openTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
                    openTaskIntent.putExtra(TASK_ID_TO_DISPLAY, -1);
                    if (flag == 0) {
                        showNotification(getApplicationContext(), "fajr", "Its fajr time", openTaskIntent, 1, "الورد اليومي");
                        Log.i("Countfajar", "=========  " + (counter++));
                        flag = 1;
                    }
                } else if (hh == zohrhh && mm == zohrmm && ss == 0 && SharedClass.getAlarmStatus(getApplicationContext(), 2) == 1) {
                    Intent openTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
                    openTaskIntent.putExtra(TASK_ID_TO_DISPLAY, -1);
                    if (flag == 0) {
                        showNotification(getApplicationContext(), "zohr", "Its zohr time", openTaskIntent, 2, "الورد اليومي");
                        Log.i("Countzohr", "=========  " + (counter++));
                        flag = 1;
                    }
                } else if (hh == asarhh && mm == asarmm && ss == 0 && SharedClass.getAlarmStatus(getApplicationContext(), 3) == 1) {
                    Intent openTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
                    openTaskIntent.putExtra(TASK_ID_TO_DISPLAY, -1);
                    if (flag == 0) {
                        showNotification(getApplicationContext(), "asar", "Its asar time", openTaskIntent, 3, "الورد اليومي");
                        Log.i("Countasar", "=========  " + (counter++));
                        flag = 1;
                    }
                } else if (hh == maghribhh && mm == maghribmm && ss == 0 && SharedClass.getAlarmStatus(getApplicationContext(), 4) == 1) {
                    Intent openTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
                    openTaskIntent.putExtra(TASK_ID_TO_DISPLAY, -1);
                    if (flag == 0) {
                        showNotification(getApplicationContext(), "maghrib", "Its maghrib time", openTaskIntent, 4, "الورد اليومي");
                        Log.i("Countmaghrib", "=========  " + (counter++));
                        flag = 1;
                    }
                } else if (hh == ishahh && mm == ishamm && ss == 0 && SharedClass.getAlarmStatus(getApplicationContext(), 5) == 1) {
                    Intent openTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
                    openTaskIntent.putExtra(TASK_ID_TO_DISPLAY, -1);
                    if (flag == 0) {
                        showNotification(getApplicationContext(), "isha", "Its isha time", openTaskIntent, 5, "الورد اليومي");
                        Log.i("Countisha", "=========  " + (counter++));
                        flag = 1;
                    }
                }
            }
        };
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void showNotification(Context context, String title, String body, Intent intent, int id, String summary) {
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.my_notification_layout);
        Intent stopButton = new Intent(context, NotifyActivityHandlerStop.class);
        stopButton.putExtra("do_actionstop", "stop");

        Log.d("statushai", "andyhain");
        long[] pattern = {0, 100, 200, 300};
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        mp = MediaPlayer.create(context, R.raw.fajr);
        mp.start();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(pattern)
                .setContent(contentView);
        contentView.setOnClickPendingIntent(R.id.flashButton, PendingIntent.getActivity(context, 0, stopButton, 0));
        contentView.setTextViewText(R.id.title, title);
        contentView.setTextViewText(R.id.description, body);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}