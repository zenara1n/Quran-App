package aiou.muslim.mttech;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import aiou.muslim.mttech.Activities.PrayerTimes;
import aiou.muslim.mttech.Helper.NotifyActivityHandlerStop;

public class AlarmReceiver extends BroadcastReceiver {
    static int flag = 0;
    public static MediaPlayer mp;
    private Random rng = new Random();
    public static NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("whatstatus", "ab status hai yeh");
        Intent activityintent = new Intent(context, PrayerTimes.class);
        AlarmNotif(intent, context, activityintent);
        System.out.println("Alarm Receiver");
    }

    private void AlarmNotif(Intent intent, Context context, Intent activityintent) {

        int id10 = intent.getIntExtra("NotifId10", 0);
        int id11 = intent.getIntExtra("NotifId11", 0);
        int id1 = intent.getIntExtra("NotifId1", 0);
        int id2 = intent.getIntExtra("NotifId2", 0);
        int id3 = intent.getIntExtra("NotifId3", 0);
        int id4 = intent.getIntExtra("NotifId4", 0);
        int id5 = intent.getIntExtra("NotifId5", 0);

        Calendar today = Calendar.getInstance();
        int hh = today.get(Calendar.HOUR_OF_DAY);
        int mm = today.get(Calendar.MINUTE);

        ArrayList<String> NotifMessages = intent.getStringArrayListExtra("ArrayMessages");
        ArrayList<String> NotifSalatMessages = intent.getStringArrayListExtra("ArraySalatMessages");
        int random1 = rng.nextInt(NotifMessages.size());

        //AdkarSabah Notification
        int hh10 = intent.getIntExtra("NotifTimeHH10", 0);
        int mm10 = intent.getIntExtra("NotifTimeMM10", 0);
        Log.d("hhmm", hh + "<=" + hh10 + " and " + mm + "<=" + mm10 + 15);
        if (id10 != 0 && hh <= hh10 && mm <= mm10 + 15) {
            String title10 = intent.getStringExtra("NotifTitle10");
            String message10 = NotifMessages.get(random1);
            showNotification(context, title10, message10, intent, 10, "الورد اليومي");
        }
        //AdkarMassaa Notification
        int hh11 = intent.getIntExtra("NotifTimeHH11", 0);
        int mm11 = intent.getIntExtra("NotifTimeMM11", 0);
        if (id11 != 0 && hh <= hh11 && mm <= mm11 + 15) {
            random1 = rng.nextInt(NotifMessages.size());
            String title11 = intent.getStringExtra("NotifTitle11");
            String message11 = NotifMessages.get(random1);
            showNotification(context, title11, message11, intent, 11, "الورد اليومي");
        }
        //Prayers Notifications
        int random2 = rng.nextInt(NotifSalatMessages.size());
        int hh1 = intent.getIntExtra("NotifTimeHH1", 0);
        int mm1 = intent.getIntExtra("NotifTimeMM1", 0);
        if (id1 != 0 && hh <= hh1 && mm <= mm1 + 15) {
            String message1 = NotifSalatMessages.get(random2);
            String title1 = intent.getStringExtra("NotifTitle1");
            showNotification(context, title1, message1, intent, 1, "الورد اليومي");
        }
        int hh2 = intent.getIntExtra("NotifTimeHH2", 0);
        int mm2 = intent.getIntExtra("NotifTimeMM2", 0);
        if (id2 != 0 && hh <= hh2 && mm <= mm2 + 15) {
            random2 = rng.nextInt(NotifSalatMessages.size());
            String message2 = NotifSalatMessages.get(random2);
            String title2 = intent.getStringExtra("NotifTitle2");
            showNotification(context, title2, message2, intent, 2, "الورد اليومي");
        }
        int hh3 = intent.getIntExtra("NotifTimeHH3", 0);
        int mm3 = intent.getIntExtra("NotifTimeMM3", 0);
        if (id3 != 0 && hh <= hh3 && mm <= mm3 + 15) {
            random2 = rng.nextInt(NotifSalatMessages.size());
            String message3 = NotifSalatMessages.get(random2);
            String title3 = intent.getStringExtra("NotifTitle3");
            showNotification(context, title3, message3, intent, 3, "الورد اليومي");
        }
        int hh4 = intent.getIntExtra("NotifTimeHH4", 0);
        int mm4 = intent.getIntExtra("NotifTimeMM4", 0);
        if (id4 != 0 && hh <= hh4 && mm <= mm4 + 15) {
            random2 = rng.nextInt(NotifSalatMessages.size());
            String message4 = NotifSalatMessages.get(random2);
            String title4 = intent.getStringExtra("NotifTitle4");
            showNotification(context, title4, message4, intent, 4, "الورد اليومي");
        }
        int hh5 = intent.getIntExtra("NotifTimeHH5", 0);
        int mm5 = intent.getIntExtra("NotifTimeMM5", 0);
        if (id5 != 0 && hh <= hh5 && mm <= mm5 + 15) {
            random2 = rng.nextInt(NotifSalatMessages.size());
            String message5 = NotifSalatMessages.get(random2);
            String title5 = intent.getStringExtra("NotifTitle5");
            showNotification(context, title5, message5, intent, 5, "الورد اليومي");
        }

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

        mp = MediaPlayer.create(context, R.raw.dohr);
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
}
