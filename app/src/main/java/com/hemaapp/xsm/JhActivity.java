package com.hemaapp.xsm;

import com.hemaapp.hm_FrameWork.HemaActivity;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.view.JhProgressDoalog;
import com.hemaapp.xsm.view.JhTextDialog;

import xtom.frame.net.XtomNetWorker;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2016/9/6.
 */
public abstract class JhActivity extends HemaActivity {

    JhTextDialog textDialog;
    JhProgressDoalog progressDoalog;
    @Override
    protected HemaNetWorker initNetWorker() {
        return new JhNetWorker(mContext);
    }
    /**
     * ��ʾ��������(Ĭ�ϲ����Ե���������ȡ��)
     *
     * @param text
     *            ������ʾ��
     */
    public void showProgressDialog(String text) {
        if (progressDoalog == null)
            progressDoalog = new JhProgressDoalog(this);
        progressDoalog.setText(text);
        progressDoalog.setCancelable(false);
        progressDoalog.show();
    }
    /**
     * ��ʾ��������
     *
     * @param text
     *            ������ʾ��id
     * @param cancelable
     *            �Ƿ���Ե���������ȡ��
     */
    public void showProgressDialog(String text, boolean cancelable) {
        if (progressDoalog == null)
            progressDoalog = new JhProgressDoalog(this);
        progressDoalog.setText(text);
        progressDoalog.setCancelable(cancelable);
        progressDoalog.show();
    }
    /**
     * ��ʾ��������(Ĭ�ϲ����Ե���������ȡ��)
     *
     * @param text
     *            ������ʾ��
     */
    public void showProgressDialog(int text) {
        if (progressDoalog == null)
            progressDoalog = new JhProgressDoalog(this);
        progressDoalog.setText(text);
        progressDoalog.setCancelable(false);
        progressDoalog.show();
    }
    /**
     * ��ʾ��������
     *
     * @param text
     *            ������ʾ��
     * @param cancelable
     *            �Ƿ���Ե���������ȡ��
     */
    public void showProgressDialog(int text, boolean cancelable) {
        if (progressDoalog == null)
            progressDoalog = new JhProgressDoalog(this);
        progressDoalog.setText(text);
        progressDoalog.setCancelable(cancelable);
        progressDoalog.show();
    }

    /**
     * ȡ������(ͬʱsetCancelable(false))
     */
    public void cancelProgressDialog() {
        if (progressDoalog != null) {
            progressDoalog.setCancelable(false);
            progressDoalog.cancel();
        }
    }

    /**
     * ��ʾ��ʾ����
     *
     * @param text
     *            ������ʾ��
     */
    public void showTextDialog(String text) {
        if (textDialog == null)
            textDialog = new JhTextDialog(this);
        textDialog.setText(text);
        textDialog.show();
    }

    /**
     * ��ʾ��ʾ����
     *
     * @param text
     *            ������ʾ��id
     */
    public void showTextDialog(int text) {
        if (textDialog == null)
            textDialog = new JhTextDialog(this);
        textDialog.setText(text);
        textDialog.show();
    }

    /**
     * ȡ����ʾ����
     */
    public void cancelTextDialog() {
        if (textDialog != null)
            textDialog.cancel();
    }

    @Override
    public void finish() {
        cancelTextDialog();
        if (progressDoalog != null)
            progressDoalog.cancelImmediately();
        super.finish();
    }
    @Override
    public JhNetWorker getNetWorker() {
        return (JhNetWorker) super.getNetWorker();
    }

    @Override
    public JhctmApplication getApplicationContext() {
        return (JhctmApplication) super.getApplicationContext();
    }
    /**
     * ����������
     *
     * @param cityName
     */
    public void saveCityName(String cityName) {
        XtomSharedPreferencesUtil.save(this, "city_name", cityName);
    }

    /**
     * @return ��ȡ�������
     */
    public String getCityName() {
        if (null== XtomSharedPreferencesUtil.get(this, "city_name")) {
            return "";
        }
        else {
            return XtomSharedPreferencesUtil.get(this, "city_name");
        }
    }

    /**
     * �������id
     *
     * @param cityId
     */
    public void saveCityId(String cityId) {
        XtomSharedPreferencesUtil.save(this, "city_id", cityId);
    }

    /**
     * @return ��ȡ����id
     */
    public String getCityId() {
        return XtomSharedPreferencesUtil.get(this, "city_id");
    }
    @Override
    public boolean onAutoLoginFailed(HemaNetWorker netWorker,
                                     HemaNetTask netTask, int failedType, HemaBaseResult baseResult) {
        switch (failedType) {
            case 0:// ����������ʧ��
                int error_code = baseResult.getError_code();
                switch (error_code) {
                    case 102:// �������
                        // XtomActivityManager.finishAll();
                        // Intent it = new Intent(mContext, LoginActivity.class);
                        // startActivity(it);
                        return true;
                    default:
                        break;
                }
            case XtomNetWorker.FAILED_HTTP:// �����쳣
            case XtomNetWorker.FAILED_DATAPARSE:// ����쳣
            case XtomNetWorker.FAILED_NONETWORK:// ������
                break;
        }
        return false;
    }



}
