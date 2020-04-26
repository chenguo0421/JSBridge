package com.cg.jsbridge.core.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.jsbridge.core.R;

import java.util.Timer;
import java.util.TimerTask;


public class AlertDialogWithoutAnything extends Dialog {

	private long mTimeOut = 0;// 默认timeOut为0即无限大
	private OnTimeOutListener mTimeOutListener = null;// timeOut后的处理器
	private Timer mTimer = null;// 定时器
	private Handler mHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (isShowing()) {
				if (mTimeOutListener != null) {
					mTimeOutListener.onTimeOut(AlertDialogWithoutAnything.this);
					dismiss();
				} else {
					dismiss();
				}
			}
		}
	};
	private Animation animation;
	
	
	/**
	 * 设置timeOut长度，和处理器
	 * 
	 * @param t
	 *            timeout时间长度
	 * @param timeOutListener
	 *            超时后的处理器
	 */
	public void setTimeOut(long t, OnTimeOutListener timeOutListener) {
		mTimeOut = t;
		if (timeOutListener != null) {
			this.mTimeOutListener = timeOutListener;
		}
	}

	private Context context;
	private String mTitleStr;
	private TextView title;
	private ImageView load;
	private RelativeLayout load_icon;

	public AlertDialogWithoutAnything(Context context, String titleStr) {
		super(context, R.style.dialog_style);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mTitleStr = titleStr;
	}

	public AlertDialogWithoutAnything(Context context, int theme, String titleStr) {
		super(context, theme);
		this.context = context;
		this.mTitleStr = titleStr;
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		if (isShowing()) {
			super.dismiss();
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_alert);
		
		title = (TextView) findViewById(R.id.title);
//		if (mTitleStr != null) {
//			title.setText(mTitleStr);
//		}
		load = (ImageView) findViewById(R.id.load);
		load_icon=(RelativeLayout)findViewById(R.id.load_icon);
		title.setVisibility(View.INVISIBLE);
		load.setVisibility(View.INVISIBLE);
		load_icon.setVisibility(View.INVISIBLE);
//		animation = AnimationUtils.loadAnimation(context,
//				R.anim.loading);
//		load.startAnimation(animation);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		if (!this.isShowing()) {
			super.show();
			if(load!=null && animation!=null) {
				load.startAnimation(animation);
			}
		}
	}
	
	public void showDialogWithoutAnything(){
		if (!this.isShowing()) {
			super.show();
			if(load!=null && animation!=null) {
				load.startAnimation(animation);
			}
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (mTimeOut != 0) {
			mTimer = new Timer();
			TimerTask timerTast = new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Message msg = mHandler.obtainMessage();
					mHandler.sendMessage(msg);
				}
			};
			mTimer.schedule(timerTast, mTimeOut);
		}
	}

	/**
	 * 通过静态Create的方式创建一个实例对象
	 * 
	 * @param context
	 * @param time
	 *            timeout时间长度
	 * @param listener
	 *            timeOutListener 超时后的处理器
	 * @return MyProgressDialog 对象
	 */
	public static AlertDialogWithoutAnything createAlertDialog(Context context, String title,
                                                               long time, OnTimeOutListener listener) {
		AlertDialogWithoutAnything Dialog = new AlertDialogWithoutAnything(context, title);
		if (time != 0) {
			Dialog.setTimeOut(time, listener);
		}
		Dialog.setCancelable(false);
		return Dialog;
	}

	/**
	 * 
	 * 处理超时的的接口
	 * 
	 */
	public interface OnTimeOutListener {

		/**
		 * 当progressDialog超时时调用此方法
		 */
		abstract public void onTimeOut(Dialog dialog);
	}



}
