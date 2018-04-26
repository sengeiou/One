package com.ubt.en.alpha1e.action.util;

import android.graphics.Bitmap;


public interface IFileListener {
	public void onReadImageFinish(Bitmap img, long request_code);

	public void onReadFileStrFinish(String erroe_str, String result,
                                    boolean result_state, long request_code);

	public void onWriteFileStrFinish(String erroe_str, boolean result,
                                     long request_code);

	public void onWriteDataFinish(long requestCode, FileTools.State state);

	public void onReadCacheSize(int size);

	public void onClearCache();

}
