package aiou.muslim.mttech;

import android.content.Context;

import aiou.muslim.mttech.main.MainContract;
import aiou.muslim.mttech.main.MainPresenter;
import aiou.muslim.mttech.data.Prefs;
import aiou.muslim.mttech.data.PrefsImpl;
import aiou.muslim.mttech.sensor.SensorsContract;
import aiou.muslim.mttech.sensor.SensorsImpl;

public class Injector {

	private Context context;

	private MainContract.UserActionsListener mainPresenter;

	private SensorsContract.Sensors sensors;

	public Injector(Context context) {
		this.context = context;
	}

	public Prefs providePrefs() {
		return PrefsImpl.getInstance(context);
	}

	public MainContract.UserActionsListener provideMainPresenter() {
		if (mainPresenter == null) {
			mainPresenter = new MainPresenter(providePrefs(), provideSensors());
		}
		return mainPresenter;
	}

	public SensorsContract.Sensors provideSensors() {
		if (sensors == null) {
			sensors = new SensorsImpl(context);
		}
		return sensors;
	}

	public void releaseMainPresenter() {
		if (mainPresenter != null) {
			mainPresenter.unbindView();
			mainPresenter = null;
		}
	}

	public void closeTasks() {
	}
}
