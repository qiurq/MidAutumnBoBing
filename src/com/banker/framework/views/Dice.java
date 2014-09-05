package com.banker.framework.views;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class Dice extends View{	
	private static final String TAG = "Dice";	
	private int screenW;
	private int screenH;
	public int picW;
	public int picH;
	private Bitmap bitmap;	
	private Paint paint;
	private Random random;

	public Dice(Context context, int screenW, int screenH, Bitmap bitmap) {
		super(context);
		this.screenH = screenH;
		this.screenW = screenW;
		this.bitmap = bitmap;
		picW = bitmap.getWidth();
		picH = bitmap.getHeight();
		paint = new Paint();
		random = new Random();		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		picW = bitmap.getWidth();
		picH = bitmap.getHeight();
		int x = random.nextInt(screenW - picW);
		int y = random.nextInt(screenH - picH);
		canvas.drawBitmap(bitmap, x, y, paint);
	}

}
