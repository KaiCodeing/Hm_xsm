package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.SearchAdapter;
import com.hemaapp.xsm.db.SearchDBClient;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * Created by lenovo on 2017/3/20.
 * 搜索
 */
public class SearchActivity extends JhActivity {
    private SearchDBClient client;
    private ArrayList<String> search_strs;
    private ImageButton back_button;
    private EditText search_input;
    private Button next_button;
    private XtomListView listview;
    private SearchAdapter adapter;
    private String keytype; //1 媒体 2 全国单 3 案例

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_saarch);
        super.onCreate(savedInstanceState);
        client = SearchDBClient.get(mContext);
    }

    @Override
    protected void onResume() {
        getHistoryList();
        super.onResume();
    }

    private void getHistoryList() {
        new LoadDBTask().execute();
    }

    private class LoadDBTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // XtomProcessDialog.show(mContext, R.string.loading);
        }

        @Override
        protected Void doInBackground(Void... params) {
            search_strs = client.select();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            //   getHistoryList_done();
//            refreshLoadmoreLayout.setVisibility(View.VISIBLE);
//            progressbar.setVisibility(View.GONE);
            adapter = new SearchAdapter(mContext, search_strs, SearchActivity.this, keytype);
            listview.setAdapter(adapter);
        }
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
        search_input = (EditText) findViewById(R.id.search_input);
        next_button = (Button) findViewById(R.id.next_button);
        listview = (XtomListView) findViewById(R.id.listview);
    }

    @Override
    protected void getExras() {
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
/**
 * 搜索框的改变
 */
        search_input.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (search_input.getText().toString().trim().equals("")
                        || search_input.getText().toString() == null) {
                    next_button.setText("取消");
                } else {
                    next_button.setText("搜索");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        //取消
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (next_button.getText().toString().equals("取消")) {
                    mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    finish();
                } else {
                    String search = search_input.getText().toString().trim();
                    select_search_str(search);
                    Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                    intent.putExtra("keyword", search);
                    intent.putExtra("keytype", keytype);
                    startActivity(intent);
                }
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //默认字
        if ("1".equals(keytype))
            search_input.setHint("请输入媒体关键词或客户名称关键词");
        else if ("2".equals(keytype))
            search_input.setHint("请输入客户公司名称或联盟公司联系人关键词");
        else
            search_input.setHint("请输入客户公司名称或小区名称关键词");
    }

    /**
     * 删除
     */
    public void Clear_HistoryList() {
        client.clear();
        search_strs.clear();
        adapter.notifyDataSetChanged();
    }

    /**
     * 添加
     */

    public void select_search_str(String str) {

        boolean found = false;
        if (search_strs != null && search_strs.size() > 0) {
            for (String hstr : search_strs) {
                if (hstr.equals(str)) {
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            if (search_strs == null)
                search_strs = new ArrayList<String>();
            search_strs.add(0, str);
            client.insert(str);
        }
    }
}
