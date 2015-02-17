package com.farbox.wml.withy.app;


import android.app.Application;

import com.farbox.wml.withy.db.SQLHelper;

public class MyApplication extends Application {

	private static MyApplication mApplication;
	private SQLHelper sqlHelper;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
        mApplication = this;
	}

	/** 获取Application */
	public static MyApplication getApp() {
		return mApplication;
	}
	
	/** 获取数据库Helper */
	public SQLHelper getSQLHelper() {
		if (sqlHelper == null)
			sqlHelper = new SQLHelper(mApplication);
		return sqlHelper;
	}
	
	/** 摧毁应用进程时候调用 */
	public void onTerminate() {
		if (sqlHelper != null)
			sqlHelper.close();
		super.onTerminate();
	}

	public void clearAppCache() {
	}
}
