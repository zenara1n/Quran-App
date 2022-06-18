package aiou.muslim.mttech.Receiver;

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
import aiou.muslim.mttech.Helper.NotifyActivityHandlerStop;
import aiou.muslim.mttech.MainActivity;
import aiou.muslim.mttech.R;

public class Notification_receiver extends BroadcastReceiver {

    public static MediaPlayer mp;
    public static NotificationManager notificationManager;
    public static final String TASK_ID_TO_DISPLAY = "TASK_ID_TO_DISPLAY";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent repeating_intent = new Intent(context, MainActivity.class);
        repeating_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Log.d("msgis", "razi");

        Intent openTaskIntent = new Intent(context, MainActivity.class);
        openTaskIntent.putExtra(TASK_ID_TO_DISPLAY, -1);
        showNotification(context, "Today's Quote", "razi", openTaskIntent, 0, "raza");

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
        androidx.core.app.TaskStackBuilder stackBuilder = androidx.core.app.TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }

}
