package com.puzzle.app.puzzlegame.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.GridView;

public class FlexibleGridView extends GridView{
	private int mMaxOverDistance = 10;

	public FlexibleGridView(Context context) {
		super(context);
		initView(context);
	}
	public FlexibleGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public FlexibleGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float density = metrics.density;
		mMaxOverDistance = (int)(density * mMaxOverDistance);
	}


	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxOverDistance, isTouchEvent);
	}
}
