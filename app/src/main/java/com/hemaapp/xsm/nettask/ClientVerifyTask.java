package com.hemaapp.xsm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhNetTask;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/10.
 * 验证用户是否注册过
 */
public class ClientVerifyTask extends JhNetTask {


    public ClientVerifyTask(JhHttpInformation information,
                            HashMap<String, String> params) {
        super(information, params);
    }

    public ClientVerifyTask(JhHttpInformation information,
                            HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new HemaBaseResult(jsonObject);
    }


}
