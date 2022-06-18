package aiou.muslim.mttech.util;

import android.content.res.Resources;

public class AndroidUtils {

	private AndroidUtils() {
	}

	public static float dpToPx(int dp) {
		return dpToPx((float) dp);
	}

	public static float dpToPx(float dp) {
		return (dp * Resources.getSystem().getDisplayMetrics().density);
	}

}
