package com.hemaapp.xsm;

import android.content.Context;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaNetTaskExecuteListener;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import xtom.frame.XtomActivityManager;
import xtom.frame.net.XtomNetWorker;

/**
 * Created by lenovo on 2016/9/6.
 */
public abstract class JhNetTaskExecuteListener extends HemaNetTaskExecuteListener {


    public JhNetTaskExecuteListener(Context context) {
        super(context);
    }

    @Override
    public boolean onAutoLoginFailed(HemaNetWorker netWorker,
                                     HemaNetTask netTask, int failedType, HemaBaseResult baseResult) {
        switch (failedType) {
            case 0:// 服务器处理失�?
                int error_code = baseResult.getError_code();
                switch (error_code) {
                    case 102:// 密码错误
                        XtomActivityManager.finishAll();
                        //Intent it = new Intent(mContext, LoginActivity.class);
                        //mContext.startActivity(it);
                        return true;
                    default:
                        break;
                }
            case XtomNetWorker.FAILED_HTTP:// 网络异常
            case XtomNetWorker.FAILED_DATAPARSE:// 数据异常
            case XtomNetWorker.FAILED_NONETWORK:// 无网�?
                break;
        }
        return false;
    }


}
