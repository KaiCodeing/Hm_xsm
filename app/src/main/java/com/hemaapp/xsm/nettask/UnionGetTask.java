package com.hemaapp.xsm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhNetTask;
import com.hemaapp.xsm.model.Union;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/3/22.
 * 城市详情
 */
public class UnionGetTask extends JhNetTask {

    public UnionGetTask(JhHttpInformation information,
                         HashMap<String, String> params) {
        super(information, params);
        // TODO Auto-generated constructor stub
    }
    public UnionGetTask(JhHttpInformation information,
                         HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }
    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        // TODO Auto-generated method stub
        return new Result(jsonObject);
    }
    private class Result extends HemaArrayResult<Union> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public Union parse(JSONObject jsonObject)
                throws DataParseException {
            return new Union(jsonObject);
        }

    }
}
