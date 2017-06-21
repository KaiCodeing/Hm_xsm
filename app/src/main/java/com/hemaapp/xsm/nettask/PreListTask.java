package com.hemaapp.xsm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhNetTask;
import com.hemaapp.xsm.model.PreGet;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/6/7.
 */
public class PreListTask extends JhNetTask {
    public PreListTask(JhHttpInformation information,
                         HashMap<String, String> params) {
        super(information, params);
        // TODO Auto-generated constructor stub
    }
    public PreListTask(JhHttpInformation information,
                         HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }
    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        // TODO Auto-generated method stub
        return new Result(jsonObject);
    }
    private class Result extends HemaPageArrayResult<PreGet> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }
        @Override
        public PreGet parse(JSONObject jsonObject)
                throws DataParseException {
            return new PreGet(jsonObject);
        }
    }
}
