package com.banker.framework.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;

import com.banker.R;
import com.banker.framework.application.BaseApplication;
import com.banker.framework.utils.share_sdk.onekeyshare.OnekeyShare;
import com.banker.framework.webService.WebServiceThread;

public abstract class BaseActivity extends Activity {

	private static final int TOAST_DURATION = Toast.LENGTH_SHORT;

	private ProgressDialog mProgressDialog;

	protected Toast mToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/** 震动 **/
	protected void startVibrator() {
		Vibrator mVibrator01 = (Vibrator) getApplication().getSystemService(
				Service.VIBRATOR_SERVICE);
		mVibrator01.vibrate(new long[] { 100, 400, 100, 400 }, -1);
	}

	@Override
	protected void onResume() {
		super.onResume();

		BaseApplication _baseApplication = getBaseApplication();
		// if(_baseApplication.isProgramExit){
		// finish();
		// }
	}

	/**
	 * 保持Dialog不自动关闭
	 * 
	 * @param dialog
	 */
	protected void setAlertDialogClosable(AlertDialog dialog, boolean closable) {
		try {
			Field field = dialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, closable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 使用String资源文件 **/
	protected String useStringTypeResource(int stringResId) {
		return getResources().getString(stringResId).toString();
	}

	/** 从底部弹出PopupWindow菜单框 **/
	protected void showPopupWindow(View view, View CurrentParent,
			int layoutResId, int stylePopupAnimationId) {
		view = inflateView(layoutResId);
		PopupWindow popup = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				250, true);
		popup.setFocusable(true);
		popup.setBackgroundDrawable(new BitmapDrawable());
		popup.setAnimationStyle(stylePopupAnimationId);
		popup.showAtLocation(CurrentParent, Gravity.BOTTOM, 0, 0);
		popup.update();
	}

	protected void showProgressDialog(String dialogTitle, String dialogMessage) {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle(dialogTitle);
		mProgressDialog.setMessage(dialogMessage);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(true);

		mProgressDialog.show();
	}

	protected void showLoadingProgressDialog() {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setCancelable(true);

		mProgressDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						WebServiceThread _webServiceThread = getWebServiceThread();
						if (null != _webServiceThread) {
							_webServiceThread.cancel();
							_webServiceThread = null;
						}

						finish();
					}
				});

		mProgressDialog.show();
	}

	protected void dismissProgressDialog() {
		if (null != mProgressDialog) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

	protected void cancelProgressDialog() {
		if (null != mProgressDialog) {
			mProgressDialog.cancel();
			mProgressDialog = null;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		// BaseApplication _baseApplication = getBaseApplication();
		// if(_baseApplication.isProgramExit){
		// finish();
		// }
	}

	protected BaseApplication getBaseApplication() {
		return (BaseApplication) getApplication();
	}

	/** 获得上下文 **/
	protected Context getContext() {
		return getApplicationContext();
	}

	/** 启动一个Activity **/
	protected void startActivity(Class<?> activityClass) {
		Intent intent = new Intent(this, activityClass);
		super.startActivity(intent);
	}

	/** 加载布局 **/
	protected View inflateView(int layoutResId) {
		return LayoutInflater.from(getApplicationContext()).inflate(
				layoutResId, null);
	}

	/** 显示Toast **/
	protected void showToast(int resId) {
		mToast = Toast.makeText(getApplicationContext(), resId, TOAST_DURATION);
		mToast.show();
	}

	/** 显示Toast **/
	protected void showToast(String msg) {
		mToast = Toast.makeText(getApplicationContext(), msg, TOAST_DURATION);
		mToast.show();
	}

	/** 取消Toast **/
	protected void cancelToast() {
		mToast.cancel();
	}

	/** 启动AlertDialog **/
	protected AlertDialog showConfirmDialog(int titleResId, int messageResId,
			DialogInterface.OnClickListener neutralButtonOnClickListener) {
		AlertDialog.Builder _alertDialogBuilder = new AlertDialog.Builder(this);
		return _alertDialogBuilder
				.setTitle(getApplicationContext().getString(titleResId))
				.setMessage(messageResId)
				.setNeutralButton(R.string.ButtonTextYes,
						neutralButtonOnClickListener)
				.setNegativeButton(R.string.ButtonTextNo, null).show();
	}

	protected AlertDialog showConfirmDialog(int titleResId, String message,
			DialogInterface.OnClickListener neutralButtonOnClickListener) {
		AlertDialog.Builder _alertDialogBuilder = new AlertDialog.Builder(this);
		return _alertDialogBuilder
				.setTitle(getApplicationContext().getString(titleResId))
				.setMessage(message)
				.setNeutralButton(R.string.ButtonTextYes,
						neutralButtonOnClickListener)
				.setNegativeButton(R.string.ButtonTextNo, null).show();
	}

	protected AlertDialog showAlertDialog(int titleResId, String message,
			DialogInterface.OnClickListener neutralButtonOnClickListener) {
		AlertDialog.Builder _alertDialogBuilder = new AlertDialog.Builder(this);
		return _alertDialogBuilder
				.setTitle(getApplicationContext().getString(titleResId))
				.setMessage(message)
				.setNeutralButton(R.string.ButtonTextOk,
						neutralButtonOnClickListener).show();
	}

	protected void showOutAlertDialog(
			DialogInterface.OnClickListener dialogButtonOnClickListener) {
		new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.isBack))
				.setMessage(getResources().getString(R.string.backMessage))
				.setPositiveButton(getResources().getString(R.string.confirm),
						dialogButtonOnClickListener).show();
	}

	protected abstract WebServiceThread getWebServiceThread();

	// 分享
	protected void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字
		oks.setNotification(R.drawable.icon,
				getString(R.string.MidAutumnBoBing));

		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));

		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://zhushou.360.cn/detail/index/soft_id/1936477");

		// text是分享文本，所有平台都需要这个字段
		oks.setText("中秋节博饼");

		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");
		oks.setImagePath("/sdcard/bobingIcon.png");

		// url仅在微信（包括好友和朋友圈）中使用
		// oks.setUrl("http://zhushou.360.cn/detail/index/soft_id/1936477");

		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("中秋节博饼");

		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.MidAutumnBoBing));

		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://zhushou.360.cn/detail/index/soft_id/1936477");

		// 启动分享GUI
		oks.show(this);

		saveIcon();
	}

	// 把图标存在SDCard
	private void saveIcon() {
		String savePath = Environment.getExternalStorageDirectory().toString();
		// String filePath = savePath + "bobingIcon" + ".png";

		Bitmap bm = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon);
		File file = new File(savePath, "bobingIcon" + ".png");

		try {
			OutputStream outputStream = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
