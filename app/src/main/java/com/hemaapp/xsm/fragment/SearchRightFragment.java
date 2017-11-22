package com.hemaapp.xsm.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhFragment;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.activity.RqLoactionResultActivity;
import com.hemaapp.xsm.activity.RqSearchResultActivity;
import com.hemaapp.xsm.adapter.RQSearchAdapter;
import com.hemaapp.xsm.db.SearchDBClient;
import com.hemaapp.xsm.model.Media;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * Created by lenovo on 2017/5/23.
 * 搜索左
 */
public class SearchRightFragment extends JhFragment {
    private EditText EdSearch;
    private TextView tvright;
    private XtomListView listview;
    private ImageView imgnone;
    private SearchDBClient client;
    private ArrayList<String> search_strs;
    private RQSearchAdapter adapter;
    private String keytype;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_right_search);
        super.onCreate(savedInstanceState);

        client = SearchDBClient.get(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        getHistoryList();
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
            adapter = new RQSearchAdapter(SearchRightFragment.this, search_strs, keytype);
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
        EdSearch = (EditText) findViewById(R.id.EdSearch);
        tvright = (TextView) findViewById(R.id.tvright);
        listview = (XtomListView) findViewById(R.id.listview);
        imgnone = (ImageView) findViewById(R.id.imgnone);
    }

    @Override
    protected void setListener() {
        //获取类型， 非空为点位 1 没有选择 2 有选择
        keytype = getActivity().getIntent().getStringExtra("keytype");
        if (isNull(keytype))
            EdSearch.setHint("请输入楼盘名称或客户名称关键词");
        else
            EdSearch.setHint("请输入楼盘名称关键词");
        //确定搜索
        tvright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(EdSearch.getText().toString())) {
                    showTextDialog("请输入搜索内容");
                    return;
                }
                String search = EdSearch.getText().toString().trim();
                select_search_str(search);
                Intent intent;
                if (isNull(keytype))
                    intent = new Intent(getActivity(), RqSearchResultActivity.class);
                else {
                    intent = new Intent(getActivity(), RqLoactionResultActivity.class);
                }
                intent.putExtra("keyword", search);
                intent.putExtra("keytype", keytype);
                intent.putExtra("typekey", getActivity().getIntent().getStringExtra("typekey"));
                startActivityForResult(intent, 1);
            }
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_OK)
            return;
        switch (requestCode) {
            case 1:
                ArrayList<Media> medias = (ArrayList<Media>) data.getSerializableExtra("medias");
                Intent intent = new Intent();
                intent.putExtra("medias", medias);
                getActivity().setResult(getActivity().RESULT_OK, intent);
                getActivity().finish();
                break;
        }
    }
}
