package aiou.muslim.mttech.data;

public interface Prefs {

	boolean isFirstRun();
	void firstRunExecuted();

	void setAppThemeColor(int colorMapPosition);
	int getThemeColor();

	void setKeepScreenOn(boolean on);
	boolean isKeepScreenOn();

	void setSimpleMode(boolean on);
	boolean isSimpleMode();

	void setEnergySavingMode(boolean on);
	boolean isEnergySavingMode();

	void setShowAcceleration(boolean b);
	boolean isShowAcceleration();

	void setShowOrientation(boolean b);
	boolean isShowOrientation();

	void setShowAccuracy(boolean b);
	boolean isShowAccuracy();

	void setShowMagnetic(boolean b);
	boolean isShowMagnetic();
}
