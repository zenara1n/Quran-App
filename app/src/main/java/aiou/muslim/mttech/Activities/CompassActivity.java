package aiou.muslim.mttech.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import aiou.muslim.mttech.Helper.NotifsChannels;
import aiou.muslim.mttech.main.MainContract;
import aiou.muslim.mttech.widget.AccelerometerView;
import aiou.muslim.mttech.widget.AccuracyView;
import aiou.muslim.mttech.widget.MagneticFieldView;

import aiou.muslim.mttech.R;
import aiou.muslim.mttech.widget.CompassCompoundView;

public class CompassActivity extends Activity implements MainContract.View {

	private TextView txtAccuracyAlert;
	private TextView txtAcceleration;
	private TextView txtOrientation;
	private TextView txtAccuracy;
	private TextView txtMagnetic;

	private CompassCompoundView compassCompoundView;
	private MagneticFieldView magneticFieldView;
	private AccuracyView accuracyView;

	private AccelerometerView orientationView;
	private AccelerometerView linearAccelerationView;
	private MainContract.UserActionsListener presenter;

	private String mT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compass_activity);

		mT = getResources().getString(R.string.mt_val);
		compassCompoundView = findViewById(R.id.compass_view_compound);
//		compassView = findViewById(R.id.compass_view);
		magneticFieldView = findViewById(R.id.magnetic_field_view);
		txtAccuracyAlert = findViewById(R.id.txt_accuracy_calibration);
		txtAcceleration = findViewById(R.id.txt_acceleration);
		txtOrientation = findViewById(R.id.txt_orientation);
		accuracyView = findViewById(R.id.accuracy_view);
		orientationView = findViewById(R.id.accelerometer_view);
		linearAccelerationView = findViewById(R.id.accelerometer_view2);
		txtAccuracy = findViewById(R.id.txt_accuracy);
		txtMagnetic = findViewById(R.id.txt_magnetic);

		presenter = NotifsChannels.getInjector().provideMainPresenter();

	}

	@Override
	public void onStart() {
		super.onStart();
		presenter.bindView(this);
	}

	@Override
	public void onStop() {
		if (presenter != null) {
			presenter.unbindView();
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void keepScreenOn(boolean on) {
		if (on) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		} else {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}

	@Override
	public void showAccelerationView(boolean on) {
		if (on) {
			txtAcceleration.setVisibility(View.VISIBLE);
			linearAccelerationView.setVisibility(View.VISIBLE);
		} else {
			txtAcceleration.setVisibility(View.GONE);
			linearAccelerationView.setVisibility(View.GONE);
		}
	}

	@Override
	public void showOrientationView(boolean on) {
		if (on) {
			txtOrientation.setVisibility(View.VISIBLE);
			orientationView.setVisibility(View.VISIBLE);
		} else {
			txtOrientation.setVisibility(View.GONE);
			orientationView.setVisibility(View.GONE);
		}
	}

	@Override
	public void showAccuracyView(boolean on) {
		if (on) {
			accuracyView.setVisibility(View.VISIBLE);
		} else {
			accuracyView.setVisibility(View.GONE);
		}
	}

	@Override
	public void showMagneticView(boolean on) {
		if (on) {
			magneticFieldView.setVisibility(View.VISIBLE);
		} else {
			magneticFieldView.setVisibility(View.GONE);
		}
	}

	@Override
	public void showAccuracyViewSimple(boolean on) {
		if (on) {
			txtAccuracy.setVisibility(View.VISIBLE);
		} else {
			txtAccuracy.setVisibility(View.GONE);
		}
	}

	@Override
	public void showMagneticViewSimple(boolean on) {
		if (on) {
			txtMagnetic.setVisibility(View.VISIBLE);
		} else {
			txtMagnetic.setVisibility(View.GONE);
		}
	}

	@Override
	public void updateRotation(float azimuth) {
		compassCompoundView.updateRotation((azimuth + 360) % 360);
	}

	@Override
	public void updateOrientation(float pitch, float roll) {
		orientationView.updateOrientation(pitch, roll);
	}

	@Override
	public void updateMagneticField(float magneticVal) {
		magneticFieldView.updateMagneticField(magneticVal);
	}

	@Override
	public void updateMagneticFieldSimple(float magneticVal) {
		txtMagnetic.setText(getResources().getString(R.string.magnetic_field2, (int)magneticVal, mT));
	}

	@Override
	public void updateLinearAcceleration(float x, float y) {
		linearAccelerationView.updateLinearAcceleration(x, y);
	}

	@Override
	public void updateAccuracy(int accuracy) {
		accuracyView.updateAccuracyField(accuracy);
	}

	@Override
	public void updateAccuracySimple(int accuracy) {
		String accValue;
		switch (accuracy) {
			case 1:
				accValue = getResources().getString(R.string.accuracy_low);
				break;
			case 2:
				accValue = getResources().getString(R.string.accuracy_medium);
				break;
			case 3:
				accValue = getResources().getString(R.string.accuracy_high);
				break;
			case 0:
			default:
				accValue = getResources().getString(R.string.accuracy_unreliable);
		}
		txtAccuracy.setText(getResources().getString(R.string.accuracy3, accValue));
	}

	@Override
	public void alertPoorAccuracy() {
		txtAccuracyAlert.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideAlertPoorAccuracy() {
		txtAccuracyAlert.setVisibility(View.GONE);
	}

	@Override
	public void showSensorsNotFound() {
		AlertDialog alertDialog = new AlertDialog.Builder(this)
				.setTitle(R.string.error)
				.setMessage(R.string.unable_to_init_sensor)
				.setNegativeButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				})
				.create();
		alertDialog.show();
	}

	@Override
	public void showSimpleMode(boolean isSimple) {
		linearAccelerationView.setSimpleMode(isSimple);
		orientationView.setSimpleMode(isSimple);
		compassCompoundView.setSimpleMode(isSimple);
		if (isSimple) {
			txtOrientation.setVisibility(View.GONE);
			txtAcceleration.setVisibility(View.GONE);
			txtAccuracy.setVisibility(View.VISIBLE);
			txtMagnetic.setVisibility(View.VISIBLE);
			magneticFieldView.setVisibility(View.GONE);
			accuracyView.setVisibility(View.GONE);
		} else {
			txtOrientation.setVisibility(View.VISIBLE);
			txtAcceleration.setVisibility(View.VISIBLE);
			txtAccuracy.setVisibility(View.GONE);
			txtMagnetic.setVisibility(View.GONE);
			magneticFieldView.setVisibility(View.VISIBLE);
			accuracyView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void showProgress() {
	}

	@Override
	public void hideProgress() {
	}

	@Override
	public void showError(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}

	@Override
	public void showError(int resId) {
		Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_LONG).show();
	}
}
