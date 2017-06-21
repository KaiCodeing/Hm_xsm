package com.hemaapp.xsm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhNetTask;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 密码重设
 * @author lenovo
 *
 */
public class PasswordResetTask extends JhNetTask {

	public PasswordResetTask(JhHttpInformation information,
			HashMap<String, String> params) {
		super(information, params);
	}

	public PasswordResetTask(JhHttpInformation information,
			HashMap<String, String> params, HashMap<String, String> files) {
		super(information, params, files);
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		return new HemaBaseResult(jsonObject);
	}

}
