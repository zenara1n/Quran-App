package aiou.muslim.mttech.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import aiou.muslim.mttech.util.AndroidUtils;

public class AccelerometerView extends View {

	private float roll = 0f;
	private float pitch = 0f;

	private PointF point;

	private int MAX_ACCELERATION;

	private int MAX_MOVE = (int) AndroidUtils.dpToPx(50); //dip
	private float k = (float) (MAX_MOVE / (Math.PI/2));

	private AccelerometerDrawer drawer;
	private boolean isSimple = false;

	public AccelerometerView(Context context) {
		super(context);
		init(context);
	}

	public AccelerometerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public AccelerometerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		point = new PointF(0, 0);
		drawer = new AccelerometerDrawer(context, isSimple);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int w = MeasureSpec.getSize(widthMeasureSpec);
		int h = MeasureSpec.getSize(heightMeasureSpec);

		if (w > h) {
			w = h;
		} else {
			h = w;
		}

		setMeasuredDimension(
				resolveSize(w, widthMeasureSpec),
				resolveSize(h, heightMeasureSpec));
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		int width = getWidth();
		MAX_MOVE = width/2;
		k = (float) (MAX_MOVE / (Math.PI/2));
		MAX_ACCELERATION = width/10;

		drawer.layout(getWidth(), getHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawer.draw(canvas);
	}

	public void updateOrientation(float pitch, float roll) {
		if ((int)this.pitch != (int)pitch || (int)this.roll != (int)roll) {
			this.pitch = pitch;
			this.roll = roll;

			point.set(
					getWidth() * 0.37f * (float) Math.cos(Math.toRadians(90 - roll)),
					getWidth() * 0.37f * (float) Math.cos(Math.toRadians(90 - pitch))
				);

			drawer.update(point);
			invalidate();
		}
	}

	public void updateLinearAcceleration(float x, float y) {
		if ((int)pitch != (int)x*1000 || (int)roll != (int)y*1000) {
			this.pitch = x*1000;
			this.roll = y*1000;

			point.set(
					(float) (k * Math.atan(x*MAX_ACCELERATION/k)),
					(float) (k * Math.atan(y*MAX_ACCELERATION/k))
				);

			drawer.update(point);
			invalidate();
		}
	}

	public void setSimpleMode(boolean mode) {
		if (isSimple != mode) {
			isSimple = mode;
			drawer = new AccelerometerDrawer(getContext(), isSimple);
			drawer.update(point);
			requestLayout();
		}
	}
}
