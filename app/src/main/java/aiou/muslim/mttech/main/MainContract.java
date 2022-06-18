package aiou.muslim.mttech.main;

import aiou.muslim.mttech.Contract;

public interface MainContract {

	interface View extends Contract.View {

		void keepScreenOn(boolean on);

		void showAccelerationView(boolean on);
		void showOrientationView(boolean on);
		void showAccuracyView(boolean on);
		void showMagneticView(boolean on);
		void showAccuracyViewSimple(boolean on);
		void showMagneticViewSimple(boolean on);

		void updateRotation(float azimuth);

		void updateOrientation(float pitch, float roll);

		void updateMagneticField(float magneticVal);

		void updateMagneticFieldSimple(float magneticVal);

		void updateLinearAcceleration(float x, float y);

		void updateAccuracy(int accuracy);

		void updateAccuracySimple(int accuracy);

		void alertPoorAccuracy();

		void hideAlertPoorAccuracy();

		void showSensorsNotFound();

		void showSimpleMode(boolean isSimple);
	}

	interface UserActionsListener extends Contract.UserActionsListener<MainContract.View> {
	}
}
