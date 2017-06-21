package com.hemaapp.xsm.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhNetTask;
import com.hemaapp.xsm.model.FileUploadResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 上传文件
 * @author lenovo
 *
 */
public class FileUploadTask extends JhNetTask {

	public FileUploadTask(JhHttpInformation information,
						  HashMap<String, String> params, HashMap<String, String> files) {
		super(information, params, files);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		return new Result(jsonObject);
	}

	private class Result extends HemaArrayResult<FileUploadResult> {

		public Result(JSONObject jsonObject) throws DataParseException {
			super(jsonObject);
			// TODO Auto-generated constructor stub
		}

		@Override
		public FileUploadResult parse(JSONObject jsonObject)
				throws DataParseException {
			return new FileUploadResult(jsonObject);
		}
		
	}
}
