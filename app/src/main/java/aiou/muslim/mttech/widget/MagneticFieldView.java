package aiou.muslim.mttech.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class MagneticFieldView extends View {

	private float magneticField;

	private ViewDrawer<Float> drawer;

	public MagneticFieldView(Context context) {
		super(context);
		init(context);
	}

	public MagneticFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MagneticFieldView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		drawer = new MagneticFieldDrawer(context);
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
		drawer.layout(getWidth(), getHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawer.draw(canvas);
	}

	public void updateMagneticField(float field) {
		if ((int)magneticField != (int)field) {
			magneticField = field;
			drawer.update(field);
			invalidate();
		}
	}
}
