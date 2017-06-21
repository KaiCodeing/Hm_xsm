package com.hemaapp.xsm.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class JhViewPager extends ViewPager {

	public JhViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public JhViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	private Runnable nextRunnable = new Runnable() {

		@Override
		public void run() {
			PagerAdapter adapter = getAdapter();
			if (adapter != null) {
				int count = adapter.getCount();
				if (count > 0) {
					int next = getCurrentItem() + 1;
					if (next == count)
						next = 0;
					setCurrentItem(next, true);
				}
				startNext();
			}
		}
	};

	public void setAdapter(PagerAdapter adapter) {
		super.setAdapter(adapter);
		stopNext();
		startNext();
	};

	public void startNext() {
		stopNext();
		postDelayed(nextRunnable, 3000);
	}

	public void stopNext() {
		removeCallbacks(nextRunnable);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		stopNext();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			startNext();
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
}
