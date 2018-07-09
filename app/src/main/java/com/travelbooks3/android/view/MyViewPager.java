package com.travelbooks3.android.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.travelbooks3.android.util.LogUtil;

public class MyViewPager extends ViewPager
{
	private boolean	isPagingEnabled	= true;

	public MyViewPager(Context context)
	{
		super(context);
		this.isPagingEnabled = true;
	}

	public MyViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.isPagingEnabled = true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		LogUtil.d("onMeasure widthMeasureSpec : "+widthMeasureSpec+", heightMeasureSpec : "+heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(this.isPagingEnabled)
		{
			try
			{
				return super.onTouchEvent(event);
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}

		}

		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event)
	{
		if(this.isPagingEnabled)
		{
			try
			{
				return super.onInterceptTouchEvent(event);
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}
			
		}

		return false;
	}

	public void setPagingEnabled(boolean b)
	{
		this.isPagingEnabled = b;
	}

	public boolean isPagingEnabled()
	{
		return isPagingEnabled;
	}
}
