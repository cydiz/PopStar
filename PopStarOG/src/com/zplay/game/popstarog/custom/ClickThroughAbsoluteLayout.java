package com.zplay.game.popstarog.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsoluteLayout;

@SuppressWarnings("deprecation")
public class ClickThroughAbsoluteLayout extends AbsoluteLayout {
	private boolean isCLickThrough = false;

	public ClickThroughAbsoluteLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public ClickThroughAbsoluteLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ClickThroughAbsoluteLayout(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isCLickThrough) {
			return true;
		} else {
			return super.onInterceptTouchEvent(ev);
		}
	}

	public void enableClickThrough() {
		isCLickThrough = true;
	}

	public void disableClickThrough() {
		isCLickThrough = false;
	}

}
