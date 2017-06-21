package com.hemaapp.xsm;

import com.hemaapp.hm_FrameWork.HemaFragment;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.model.CitySan;

import xtom.frame.net.XtomNetWorker;

/**
 * Created by lenovo on 2016/9/6.
 */
public abstract class JhFragment extends HemaFragment {
    private CitySan cityInfo;
    /**
     * @return
     * @方法名称: getCityInfo
     * @功能描述: TODO三级城市保存
     * @返回值: CitySan
     */
    public CitySan getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CitySan cityInfo) {
        this.cityInfo = cityInfo;
    }
    @Override
    protected HemaNetWorker initNetWorker() {
        return new JhNetWorker(getActivity());
    }

    @Override
    public JhNetWorker getNetWorker() {
        return (JhNetWorker) super.getNetWorker();
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
