package com.puzzle.app.puzzlegame.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.puzzle.app.puzzlegame.R;
import com.puzzle.app.puzzlegame.view.DialogView;
import com.puzzle.app.puzzlegame.view.wheel.WheelListAdapter;
import com.puzzle.app.puzzlegame.view.wheel.WheelView;


/**
 * 封装了滚轮操作的类
 *
 * */
@SuppressWarnings({"UnusedDeclaration"})
public class WheelDialogShowUtil {


	private Context mContext;

	private String title;
	private String[] data;

	private WheelView wheelView;
	private Dialog dialog;
	public DialogView dialogView;

	private int visibleItems=5;

	public int getVisibleItems() {
		return visibleItems;
	}


	public void setVisibleItems(int visibleItems) {
		this.visibleItems = visibleItems;
	}


	public WheelView getWheelView() {
		return wheelView;
	}


	public void setTitle(String title) {
		this.title = title;
	}



	public WheelDialogShowUtil(Context mContext, Display mDisplay, String[] data, String title) {

		this.mContext = mContext;
		this.data=data;
		this.title=title;
		DisplayMetrics dm = new DisplayMetrics();
		mDisplay.getMetrics(dm);
		dialogView=new DialogView(mContext);
		dialogView.setWidth((int)(dm.widthPixels*0.65));
		dialogView.setHeight(dm.heightPixels/100*40);

		//默认的点击事件
		dialogView.setBtnNegClick(new DialogView.onWheelBtnNegClick() {

			@Override
			public void onClick(String text, int position) {
				// TODO Auto-generated method stub
				dissmissWheel();
			}
		});

		//默认的点击事件
		dialogView.setBtnPosClick(new DialogView.onWheelBtnPosClick() {

			@Override
			public void onClick(String text, int position) {
				// TODO Auto-generated method stub
				dissmissWheel();
			}
		});

		initDialog( dialogView);

	}


	private Dialog initDialog(DialogView dialogWeelUtil)	{
		dialog =dialogWeelUtil.initDialog(title, "内容");
		initWheel(dialogWeelUtil.getWheelView(),data);
		return dialog;
	}


	public void showWheel()	{
		if(dialog !=null)	{
			dialog.show();
		}

	}


	public void dissmissWheel()	{
		if(dialog !=null && dialog.isShowing())	{
			dialog.dismiss();
		}

	}

	public boolean isShowing()	{
		return dialog != null && dialog.isShowing();
	}


	public void setWheelHint(int index)	{
		if(wheelView!=null)	{
			wheelView.setCurrentItem(index);
			dialogView.setHint(index);
		}

	}

	public void setWindowAlpha(Activity mActivity)	{
		WindowManager.LayoutParams lp =mActivity.getWindow().getAttributes();
		lp.alpha =0.1f;
		mActivity.getWindow().setAttributes(lp);

	}

	WheelListAdapter mAdapter;
	// Scrolling flag
	@SuppressLint("NewApi")
	private void initWheel(WheelView wheel,final String[] data )	{

		//为dialog的确定和取消按钮设置数据
		dialogView.setWheel(wheel, data);

		wheelView=wheel;
		wheel.setVisibleItems(visibleItems);

		mAdapter =new WheelListAdapter(mContext,data, R.layout.wheel_layout, wheel);
		wheel.setViewAdapter(mAdapter);

	}

	/**
	 * 在选择完以后要执行的事件
	 */
	public  void setTextToView(View view,String text)	{

		if(view instanceof TextView)	{
			TextView mTextView=(TextView)view;
			mTextView.setText(text);
		}

//		else if(view instanceof EditText)	{
//			EditText mEditText=(EditText)view;
//			mEditText.setText(text);
//		}
	}

}
