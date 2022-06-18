package aiou.muslim.mttech.Helper;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import aiou.muslim.mttech.Receiver.YourService;

public class NotifyActivityHandlerStop extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String actionp = (String) getIntent().getExtras().get("do_actionstop");
        if (actionp != null) {
            if (actionp.equals("stop")) {
                Log.d("checkhai", "stop");
                YourService.mp.reset();
                YourService.mp.stop();
                YourService.mp.release();
                YourService.notificationManager.cancelAll();
            }
        }

        finish();
    }
}