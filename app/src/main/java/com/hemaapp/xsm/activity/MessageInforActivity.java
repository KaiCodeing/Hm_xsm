package com.hemaapp.xsm.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.model.Notice;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by lenovo on 2017/3/6.
 * 消息详情
 */
public class MessageInforActivity extends JhActivity {
    private TextView title_text;
    private ImageButton back_button;
    private Button next_button;
    private ImageView user_img;
    private TextView user_name;
    private TextView message_content;
    private View view_show;
    private TextView time_text;
    private Notice notice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_message_infor);
        super.onCreate(savedInstanceState);
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
        user_img = (ImageView) findViewById(R.id.user_img);
        user_name = (TextView) findViewById(R.id.user_name);
        message_content = (TextView) findViewById(R.id.message_content);
        view_show = findViewById(R.id.view_show);
        time_text = (TextView) findViewById(R.id.time_text);
    }

    @Override
    protected void getExras() {
        notice = (Notice) mIntent.getSerializableExtra("notice");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("消息详情");
        next_button.setVisibility(View.INVISIBLE);
        //
        String path1 = notice.getAvatar();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.system_message_img)
                .showImageForEmptyUri(R.mipmap.system_message_img)
                .showImageOnFail(R.mipmap.system_message_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path1, user_img, options);
      //  user_name.setText(notice.getNickname());
        message_content.setText(notice.getContent());
        time_text.setText(notice.getRegdate());
        if ("2".equals(notice.getKeytype()))
        {
            user_img.setImageResource(R.mipmap.media_low_data_img);
            user_name.setText("媒体档期到期提醒");
        }
    }
}
