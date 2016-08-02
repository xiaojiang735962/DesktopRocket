package com.example.rocket;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class RocketService extends Service {

	private WindowManager.LayoutParams params;
	private int winWidth;
	private int winHeight;
	private WindowManager mWM;
	private View view;
	
	private int startX;
	private int startY;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		//获取屏幕的宽度和高度
		winWidth = mWM.getDefaultDisplay().getWidth();
		winHeight = mWM.getDefaultDisplay().getHeight();
		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;//电话窗口，用于电话交互,置于所以应用之上，标题栏之下
		params.gravity = Gravity.LEFT + Gravity.TOP;//设置偏移的中心点为左上角
		params.setTitle("Toast");
		
		view = View.inflate(this, R.layout.activity_rocket , null);
		ImageView ivRocket = (ImageView) view.findViewById(R.id.iv_rocket);
		ivRocket.setBackgroundResource(R.drawable.anim_rocket);
		AnimationDrawable rocketAnimation = (AnimationDrawable) ivRocket.getBackground();
		rocketAnimation.start();
		mWM.addView(view, params);
		
		//给浮窗设置触摸事件(使可以拖动浮窗)
				view.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							//初始化起点坐标
							startX = (int) event.getRawX();
							startY = (int) event.getRawY();
							break;
						case MotionEvent.ACTION_MOVE:
							int endX = (int) event.getRawX();
							int endY = (int) event.getRawY();
							
							//计算偏移量
							int dx = endX - startX;
							int dy = endY - startY;
							
							params.x += dx;
							params.y += dy;
							//防止坐标偏离屏幕
							if(params.x < 0){
								params.x = 0;
							}
							if(params.y < 0){
								params.y = 0;
							}
							if(params.x > winWidth-view.getWidth()){
								params.x = winWidth-view.getWidth();
							}
							if(params.y > winHeight-view.getHeight()){
								params.y = winHeight-view.getHeight();
							}
							//更新View的位置
							mWM.updateViewLayout(view, params);
							//重新初始化起点坐标
							startX = (int) event.getRawX();
							startY = (int) event.getRawY();
							break;
						case MotionEvent.ACTION_UP:
							if(params.x > 100 && params.x < 300 && params.y > winHeight - 200){
								launchRocket();
								//启动烟雾效果
								Intent intent = new Intent(RocketService.this, BackgroundActivity.class);
								//启动一个新的任务栈存放activity
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}
							break;
						}
						return true;
					}
				});
	}
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			int y = msg.arg1;
			params.y = y;
			//更新火箭的位置
			mWM.updateViewLayout(view, params);
		};
	};
	//发送火箭
	protected void launchRocket() {
		//设置x坐标(使火箭每次启动从中间启动)
		params.x = (winWidth - view.getWidth()) / 2;
		mWM.updateViewLayout(view, params);
		
		new Thread(){
			public void run() {
				super.run();
				int pos = 380;
				for(int i = 0; i < 10 ; i++){
					int y = pos - 38 * i ;
					try {
						//线程休眠，用来控制火箭速度
						sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Message msg = Message.obtain();
					msg.arg1 = y ;
					handler.sendMessage(msg);
				}
			}
		}.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mWM != null && view != null){
			mWM.removeView(view);
			view = null ;
		}
	}
}
