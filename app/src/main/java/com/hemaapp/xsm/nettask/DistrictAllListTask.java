package com.hemaapp.xsm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhNetTask;
import com.hemaapp.xsm.model.CitySan;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * ���ȫ������
 * @author lenovo
 *
 */
public class DistrictAllListTask extends JhNetTask {

	public DistrictAllListTask(JhHttpInformation information,
							   HashMap<String, String> params) {
		super(information, params);
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		return new Result(jsonObject);
	}

	private class Result extends HemaArrayResult<CitySan> {

		public Result(JSONObject jsonObject) throws DataParseException {
			super(jsonObject);
		}

		@Override
		public CitySan parse(JSONObject jsonObject) throws DataParseException {
			return new CitySan(jsonObject);
		}

	}
}
