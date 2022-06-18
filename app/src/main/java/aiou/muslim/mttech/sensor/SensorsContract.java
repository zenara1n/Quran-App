package aiou.muslim.mttech.sensor;

public interface SensorsContract {

	interface SensorsCallback {
		void onRotationChange(float azimuth, float pitch, float roll);
		void onMagneticFieldChange(float value);
		void onLinearAccelerationChange(float x, float y, float z);
		void onAccuracyChanged(int accuracy);
		void onSensorsNotFound();
	}

	interface Sensors {
		void setSensorsCallback(SensorsContract.SensorsCallback callback);
		void setEnergySavingMode(boolean b);
		void start();
		void stop();
	}
}
