package com.hemaapp.xsm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhNetTask;
import com.hemaapp.xsm.model.SysInitInfo;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/9/7.
 */
public class InitTask extends JhNetTask {


    public InitTask(JhHttpInformation information,
                    HashMap<String, String> params) {
        super(information, params);
        // TODO Auto-generated constructor stub
    }
    public InitTask(JhHttpInformation information,
                    HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }
    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        // TODO Auto-generated method stub
        return new Result(jsonObject);
    }
    private class Result extends HemaArrayResult<SysInitInfo> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public SysInitInfo parse(JSONObject jsonObject)
                throws DataParseException {
            return new SysInitInfo(jsonObject);
        }

    }

}
