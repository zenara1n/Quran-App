package aiou.muslim.mttech.Helper;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import aiou.muslim.mttech.BuildConfig;
import aiou.muslim.mttech.Injector;
import aiou.muslim.mttech.R;
import timber.log.Timber;

public class NotifsChannels extends Application {

    public static Injector injector;

    public static Injector getInjector() {
        return injector;
    }

    @Override
    public void onCreate() {
        if (BuildConfig.DEBUG) {
            //Timber initialization
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return "LC-LC " + super.createStackElementTag(element) + ":" + element.getLineNumber();
                }
            });
        }
        super.onCreate();
        createNotificationChannels();

        injector = new Injector(getApplicationContext());
    }

    public static final String CHANNEL_1_ID = "DailyAdkar";

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription(getString(R.string.notifications));
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        injector.releaseMainPresenter();
        injector.closeTasks();
    }
}
