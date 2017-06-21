package com.hemaapp.xsm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhNetTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/3/20.
 * 消息未读数
 */
public class NoticeUnreadTask extends JhNetTask {

    public NoticeUnreadTask(JhHttpInformation information,
                         HashMap<String, String> params) {
        super(information, params);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<String> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
            // TODO Auto-generated constructor stub
        }

        @Override
        public String parse(JSONObject jsonObject) throws DataParseException {
            try {
                return get(jsonObject, "count");
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }

    }
}
