package com.hemaapp.xsm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhNetTask;
import com.hemaapp.xsm.model.DemoGet;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/3/20.
 * 案例详情
 */
public class DemoGetTask extends JhNetTask {

    public DemoGetTask(JhHttpInformation information,
                        HashMap<String, String> params) {
        super(information, params);
        // TODO Auto-generated constructor stub
    }
    public DemoGetTask(JhHttpInformation information,
                        HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }
    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        // TODO Auto-generated method stub
        return new Result(jsonObject);
    }
    private class Result extends HemaArrayResult<DemoGet> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public DemoGet parse(JSONObject jsonObject)
                throws DataParseException {
            return new DemoGet(jsonObject);
        }

    }
}
