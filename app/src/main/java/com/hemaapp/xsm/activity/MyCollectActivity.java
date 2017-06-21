package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.CollecttPageAdapter;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/3/3.
 * 我的收藏
 */
public class MyCollectActivity extends JhActivity {
    private TextView title_text;
    private ImageButton back_button;
    private Button next_button;
    private RadioGroup collect_radio;
    private ViewPager viewpager;
    private CollecttPageAdapter adapter;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_collect);
        super.onCreate(savedInstanceState);
        inIt();
    }

//    @Override
//    protected void onResume() {
//        if (adapter != null)
//            adapter.freshAll();
//        super.onResume();
//    }

    /**
     * @方法名称: inIt
     * @功能描述: TODO初始化
     * @返回值: void
     */
    private void inIt() {
        viewpager.setOffscreenPageLimit(100);
        adapter = new CollecttPageAdapter(getParams(), MyCollectActivity.this, position);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(position);
    }

    private ArrayList<CollecttPageAdapter.Params> getParams() {
        ArrayList<CollecttPageAdapter.Params> ps = new ArrayList<CollecttPageAdapter.Params>();
        ps.add(new CollecttPageAdapter.Params("1", ""));
        ps.add(new CollecttPageAdapter.Params("2", ""));
        ps.add(new CollecttPageAdapter.Params("3", ""));
        ps.add(new CollecttPageAdapter.Params("4", ""));
        return ps;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        position = viewpager.getCurrentItem();
        inIt();
        super.onActivityResult(requestCode, resultCode, data);
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
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
        collect_radio = (RadioGroup) findViewById(R.id.collect_radio);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title_text.setText("我的收藏");
        next_button.setVisibility(View.INVISIBLE);

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        collect_radio.check(R.id.radio1);
                        break;
                    case 1:
                        collect_radio.check(R.id.radio2);
                        break;
                    case 2:
                        collect_radio.check(R.id.radio3);
                        break;
                    case 3:
                        collect_radio.check(R.id.radio4);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        collect_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio1:
                        viewpager.setCurrentItem(0);
                        break;
                    case R.id.radio2:
                        viewpager.setCurrentItem(1);
                        break;
                    case R.id.radio3:
                        viewpager.setCurrentItem(2);
                        break;
                    case R.id.radio4:
                        viewpager.setCurrentItem(3);
                        break;
                }
            }
        });
    }
}
