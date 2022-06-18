package aiou.muslim.mttech;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.widget.Toast;

import java.util.Calendar;

public class myBackgroundProcess extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Ringtone ringtone = RingtoneManager.getRingtone(context,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        ringtone.play();

        Toast.makeText(context, "This is my background process: \n" + Calendar.getInstance().getTime().toString(), Toast.LENGTH_LONG).show();

        SystemClock.sleep(2000);
        ringtone.stop();
    }
}