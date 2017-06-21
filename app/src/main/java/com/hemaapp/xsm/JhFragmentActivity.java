package com.hemaapp.xsm;

import com.hemaapp.hm_FrameWork.HemaFragmentActivity;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import xtom.frame.net.XtomNetWorker;

/**
 * Created by lenovo on 2016/9/6.
 */
public abstract class JhFragmentActivity extends HemaFragmentActivity {

    @Override
    protected HemaNetWorker initNetWorker() {
        return new JhNetWorker(mContext);
    }

    @Override
    public JhNetWorker getNetWorker() {
        return (JhNetWorker) super.getNetWorker();
    }

    @Override
    public JhctmApplication getApplicationContext() {
        return (JhctmApplication) super.getApplicationContext();
    }

    @Override
    public boolean onAutoLoginFailed(HemaNetWorker netWorker,
                                     HemaNetTask netTask, int failedType, HemaBaseResult baseResult) {
        switch (failedType) {
            case 0:// ����������ʧ��
                int error_code = baseResult.getError_code();
                switch (error_code) {
                    case 102:// �������?
                        // XtomActivityManager.finishAll();
                        // Intent it = new Intent(mContext, LoginActivity.class);
                        // startActivity(it);
                        return true;
                    default:
                        break;
                }
            case XtomNetWorker.FAILED_HTTP:// �����쳣
            case XtomNetWorker.FAILED_DATAPARSE:// �����?
            case XtomNetWorker.FAILED_NONETWORK:// ������
                break;
        }
        return false;
    }
    // ------------------------���������Ŀ�Զ��巽��?--------------------------


}
