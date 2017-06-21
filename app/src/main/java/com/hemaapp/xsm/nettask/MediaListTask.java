package com.hemaapp.xsm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhNetTask;
import com.hemaapp.xsm.model.Media;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/3/14.
 * 媒体列表
 */
public class MediaListTask extends JhNetTask{
    public MediaListTask(JhHttpInformation information,
                            HashMap<String, String> params) {
        super(information, params);
        // TODO Auto-generated constructor stub
    }
    public MediaListTask(JhHttpInformation information,
                            HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }
    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        // TODO Auto-generated method stub
        return new Result(jsonObject);
    }
    private class Result extends HemaPageArrayResult<Media> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }
        @Override
        public Media parse(JSONObject jsonObject)
                throws DataParseException {
            return new Media(jsonObject);
        }
    }
}
