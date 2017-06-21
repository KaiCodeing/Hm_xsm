package com.hemaapp.xsm.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhFragmentActivity;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.fragment.SearchLiftFragment;
import com.hemaapp.xsm.fragment.SearchRightFragment;

import java.util.List;

/**
 * Created by lenovo on 2017/5/22.
 * 二期媒体搜索
 */
public class RQSearchActivity extends JhFragmentActivity {
    private ImageButton back_button;
    private RadioGroup radiogroup;
    private FrameLayout content_frame_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_rq_search);
        super.onCreate(savedInstanceState);
        toogleFragment(SearchRightFragment.class);
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
        back_button = (ImageButton) findViewById(R.id.back_button);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        content_frame_service = (FrameLayout) findViewById(R.id.content_frame_service);
    }

    @Override
    protected void getExras() {

    }

    /**
     * 显示或更换Fragment
     *
     * @param c
     */
    public void toogleFragment(Class<? extends Fragment> c) {
        FragmentManager manager = getSupportFragmentManager();
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

    @Override
    protected void setListener() {
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio1:
                        toogleFragment(SearchRightFragment.class);
                        break;
                    case R.id.radio2:
                        toogleFragment(SearchLiftFragment.class);
                        break;
                }
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
