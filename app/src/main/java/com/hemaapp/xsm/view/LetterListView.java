package com.hemaapp.xsm.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hemaapp.xsm.R;


public class LetterListView extends View {

	OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	String[] b = {  "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
			"L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
			"Y", "Z"};
	int choose = -1;
	Paint paint = new Paint();
	
	boolean showBkg = false;

	public LetterListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LetterListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LetterListView(Context context) {
		super(context);
	}

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (showBkg) {
			canvas.drawColor(Color.parseColor("#00000000"));
		}

		int height = getHeight();
		int width = getWidth();
		int singleHeight = height / b.length;
		for (int i = 0; i < b.length; i++) {
			paint.setColor(getResources().getColor(R.color.white));// R.color.new_b
			paint.setTextSize(getResources().getDimensionPixelSize(
					R.dimen.textsize_4));
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			if (i == choose) {
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
			}
			float xPos = width / 2 - paint.measureText(b[i]);
//			float xPos = width / 2 ;
			float yPos = singleHeight * i + singleHeight;
			paint.setTextSize(30);
			canvas.drawText(b[i], xPos, yPos, paint);
			paint.reset();
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * b.length);

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			showBkg = true;
			if (oldChoose != c && listener != null) {
				if (c >= 0 && c < b.length) {
					listener.onTouchingLetterChanged(b[c]);
					choose = c;
					invalidate();
				}
			}

			break;
		case MotionEvent.ACTION_MOVE:
			if (oldChoose != c && listener != null) {
				if (c >= 0 && c < b.length) {
					listener.onTouchingLetterChanged(b[c]);
					choose = c;
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			showBkg = false;
			choose = -1;
			invalidate();
			break;
		}
		return true;
	}

	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}

	public void setShowBkg(boolean showBkg) {
		this.showBkg = showBkg;
		if(!showBkg){
			choose = -1;
			invalidate();
		}
	}
}