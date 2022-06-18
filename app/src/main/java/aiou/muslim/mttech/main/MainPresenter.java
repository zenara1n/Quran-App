package aiou.muslim.mttech.main;

import aiou.muslim.mttech.data.Prefs;
import aiou.muslim.mttech.sensor.SensorsContract;

public class MainPresenter implements MainContract.UserActionsListener {

	private MainContract.View view;

	private SensorsContract.Sensors sensors;
	private SensorsContract.SensorsCallback sensorsCallback;

	private final Prefs prefs;
	private boolean isSimple = false;

	public MainPresenter(final Prefs prefs, final SensorsContract.Sensors sensors) {
		this.prefs = prefs;
		this.sensors = sensors;
	}

	@Override
	public void bindView(final MainContract.View v) {
		this.view = v;

		isSimple = prefs.isSimpleMode();
		view.showSimpleMode(isSimple);
		view.updateAccuracySimple(0);
		view.keepScreenOn(prefs.isKeepScreenOn());
		view.showAccelerationView(prefs.isShowAcceleration());
		view.showOrientationView(prefs.isShowOrientation());
		if (isSimple) {
			view.showAccuracyViewSimple(prefs.isShowAccuracy());
			view.showMagneticViewSimple(prefs.isShowMagnetic());
			view.updateMagneticFieldSimple(0);
		} else {
			view.showAccuracyView(prefs.isShowAccuracy());
			view.showMagneticView(prefs.isShowMagnetic());
			view.updateMagneticField(0);
		}

		if (sensorsCallback == null) {
			sensorsCallback = new SensorsContract.SensorsCallback() {
				@Override
				public void onRotationChange(float azimuth, float pitch, float roll) {
					if (view != null) {
						view.updateRotation(azimuth);
						view.updateOrientation(pitch, roll);
					}
				}

				@Override
				public void onMagneticFieldChange(float value) {
					if (view != null) {
						if (isSimple) {
							view.updateMagneticFieldSimple(value);
						} else {
							view.updateMagneticField(value);
						}
					}
				}

				@Override
				public void onLinearAccelerationChange(float x, float y, float z) {
					if (view != null) {
						view.updateLinearAcceleration(x, y);
					}
				}

				@Override
				public void onAccuracyChanged(int accuracy) {
					if (view != null) {
						if (isSimple) {
							view.updateAccuracySimple(accuracy);
						} else {
							view.updateAccuracy(accuracy);
						}
						switch (accuracy) {
							case 0:
							case 1:
							case 2:
								view.alertPoorAccuracy();
								break;
							case 3:
								view.hideAlertPoorAccuracy();
								break;
						}
					}
				}

				@Override
				public void onSensorsNotFound() {
					if (view != null) {
						view.showSensorsNotFound();
					}
				}
			};
		}
		sensors.setEnergySavingMode(prefs.isEnergySavingMode());
		sensors.setSensorsCallback(sensorsCallback);
		sensors.start();
	}

	@Override
	public void unbindView() {
		this.view = null;
		sensors.stop();
	}
}
