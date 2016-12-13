package playground.jkzhou.filetransfer.controller;

import android.app.ProgressDialog;
import android.os.Handler;

/**
 * Created by JK.Zhou on 2016/12/12.
 */

public class ProgressUIController {

	private Handler mHandler = new Handler();
	private ProgressDialog progressUI;

	public ProgressUIController(ProgressDialog progressDialog) {
		this.progressUI = progressDialog;
	}

	public void dismiss() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				progressUI.dismiss();
			}
		});
	}

	public void initAndShow(final int style, final boolean cancelable, final String message) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				progressUI.setProgressStyle(style);
				progressUI.setCancelable(cancelable);
				progressUI.setMessage(message);
				progressUI.setProgress(0);
				progressUI.show();
			}
		});
	}

	public void setMax(final int max) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				progressUI.setMax(max);
			}
		});
	}

	public void update(String message) {
		update(-1, message);
	}

	public void update(int progress) {
		update(progress, null);
	}

	private void update(final int progress, final String message) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (message != null)
					progressUI.setMessage(message);
				if (progress > 0)
					progressUI.setProgress(progress);
			}
		});
	}
}
