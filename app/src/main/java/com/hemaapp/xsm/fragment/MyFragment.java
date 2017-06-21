package com.hemaapp.xsm.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.xsm.JhFragment;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.activity.ChangePWDActivity;
import com.hemaapp.xsm.activity.FeedBackActivity;
import com.hemaapp.xsm.activity.MyCollectActivity;
import com.hemaapp.xsm.activity.SetingActivity;
import com.hemaapp.xsm.activity.UserInformationActivity;
import com.hemaapp.xsm.model.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by lenovo on 2017/3/2.
 * 我的
 */
public class MyFragment extends JhFragment {
    private TextView position;//角色类型
    private TextView nickname;//昵称
    private TextView position_user;//用户职业
    private TextView user_tel;//用户电话
    private TextView user_address;//公司地址
    private RoundedImageView user_img;//头像
    private LinearLayout layout_password;//密码管理
    private LinearLayout layout_collect;//我的收藏
    private LinearLayout layout_opinion;//意见反馈
    private LinearLayout layout_set;//设置
    private User user;
    private TextView tel_go;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_my);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        inIt();
    }

    /**
     * 初始化
     */
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        String userId = JhctmApplication.getInstance().getUser().getId();
        getNetWorker().clientGet(token, userId);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        HemaArrayResult<User> result = (HemaArrayResult<User>) hemaBaseResult;
        user = result.getObjects().get(0);
        setData();
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        showTextDialog("获取用户信息失败，请稍后重试");
    }

    //填写数据
    private void setData() {
        String path = user.getAvatar();
        user_img.setCornerRadius(100);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.my_fr_user_img)
                .showImageForEmptyUri(R.mipmap.my_fr_user_img)
                .showImageOnFail(R.mipmap.my_fr_user_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, user_img, options);
        nickname.setText(user.getNickname());
        user_tel.setText(user.getMobile());
        user_address.setText(user.getCompany());
        position_user.setText(user.getSelfsign());
        //公司地址。角色名称。公司职位、
        if ("1".equals(user.getFeeaccount()))
            position.setText("普通用户");
        else if ("2".equals(user.getFeeaccount()))
            position.setText("联盟公司CEO");
        else if ("3".equals(user.getFeeaccount()))
            position.setText("联盟公司媒介人员");
        else
            position.setText("联盟公司其他人员");
        //电话
        final String tel = JhctmApplication.getInstance().getSysInitInfo().getSys_service_phone();
        if (isNull(tel))
            tel_go.setVisibility(View.GONE);
        else
            tel_go.setVisibility(View.VISIBLE);
        tel_go.setText(Html.fromHtml("<u>" + "投诉电话: " + tel + "</u>"));
        tel_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
            }
        });

    }

    @Override
    protected void findView() {
        position = (TextView) findViewById(R.id.position);
        nickname = (TextView) findViewById(R.id.nickname);
        position_user = (TextView) findViewById(R.id.position_user);
        user_tel = (TextView) findViewById(R.id.user_tel);
        user_address = (TextView) findViewById(R.id.user_address);
        user_img = (RoundedImageView) findViewById(R.id.user_img);
        layout_password = (LinearLayout) findViewById(R.id.layout_password);
        layout_collect = (LinearLayout) findViewById(R.id.layout_collect);
        layout_opinion = (LinearLayout) findViewById(R.id.layout_opinion);
        layout_set = (LinearLayout) findViewById(R.id.layout_set);
        tel_go = (TextView) findViewById(R.id.tel_go);
    }

    @Override
    protected void setListener() {
        //查看个人中心 编辑
        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserInformationActivity.class);
                intent.putExtra("keytype", "1");
                getActivity().startActivity(intent);
            }
        });
        //密码管理
        layout_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePWDActivity.class);
                getActivity().startActivity(intent);
            }
        });
        //收藏
        layout_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyCollectActivity.class);
                getActivity().startActivity(intent);
            }
        });
        //意见反馈
        layout_opinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FeedBackActivity.class);
                getActivity().startActivity(intent);
            }
        });
        //设置
        layout_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SetingActivity.class);
                getActivity().startActivity(intent);
            }
        });

    }
}
