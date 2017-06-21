package com.hemaapp.xsm.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhFragment;
import com.hemaapp.xsm.R;

import java.util.List;

/**
 * Created by lenovo on 2017/3/2.
 * 客服的
 * ServiceLeftExpandAdapter  媒介电话
 * ModelAdapter 合同模板
 */
public class ServiceFragment extends JhFragment {
    private RadioGroup radiogroup;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_service);
        super.onCreate(savedInstanceState);
        toogleFragment(ServiceLeftFragment.class);
    }
    @Override
    public void onPause() {
        super.onPause();

    }
    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
    }

    @Override
    protected void setListener() {
        radiogroup.setOnCheckedChangeListener(new OnTabListener());
    }
    private class OnTabListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.radio1:// 策略库
                    toogleFragment(ServiceLeftFragment.class);
                    break;
                case R.id.radio2:// 名师榜
                    toogleFragment(ServiceRightFragment.class);
                    break;
            }
        }
    }
    /**
     * 显示或更换Fragment
     *
     * @param c
     */
    public void toogleFragment(Class<? extends Fragment> c) {
        FragmentManager manager = getChildFragmentManager();
        String tag = c.getName();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag(tag);

        if (fragment == null) {
            try {
                fragment = c.newInstance();
                log_i("第一次+++++++++++++++++++++");
                // 替换时保留Fragment,以便复用
                transaction.add(R.id.content_frame_service, fragment, tag);
            } catch (Exception e) {
                // ignore
            }
        } else {
            // nothing
        }
        // 遍历存在的Fragment,隐藏其他Fragment
        List<Fragment> fragments = manager.getFragments();
        if (fragments != null)
            for (Fragment fm : fragments)
                if (!fm.equals(fragment))
                    transaction.hide(fm);

        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
    }
}
