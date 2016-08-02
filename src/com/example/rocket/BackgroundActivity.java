package com.example.rocket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class BackgroundActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smog);
		
		ImageView ivTop = (ImageView) findViewById(R.id.iv_top);
		ImageView ivBottom = (ImageView) findViewById(R.id.iv_bottom);
		
		//渐变动画
		AlphaAnimation alpha = new AlphaAnimation(0, 1);
		alpha.setDuration(1000);
		alpha.setFillAfter(true);//动画结束后保持动画画面
		
		//运行动画
		ivTop.startAnimation(alpha);
		ivBottom.startAnimation(alpha);
		//延时一秒后结束Activity
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				finish();
				stopService(new Intent(BackgroundActivity.this, RocketService.class));
			}
		}, 1000);
	}
}
