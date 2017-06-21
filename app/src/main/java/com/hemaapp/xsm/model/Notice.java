package com.hemaapp.xsm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

public class Notice extends XtomObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String keytype;

	private String keyid;
	private String content;
	private String nickname;
	private String avatar;
	private String client_id;
	private String from_id;
	private String looktype;
	private String regdate;

	public Notice(JSONObject jsonObject) throws DataParseException {
		if (jsonObject != null) {
			try {
				id = get(jsonObject, "id");
				keytype = get(jsonObject, "keytype");
				keyid = get(jsonObject, "keyid");
				content = get(jsonObject, "content");
				nickname = get(jsonObject, "nickname");
				avatar = get(jsonObject, "avatar");

				client_id = get(jsonObject, "client_id");
				from_id = get(jsonObject, "from_id");
				looktype = get(jsonObject, "looktype");
				regdate = get(jsonObject, "regdate");

				log_i(toString());
			} catch (JSONException e) {
				throw new DataParseException(e);
			}
		}
	}

	@Override
	public String toString() {
		return "Notice [id=" + id + ", keytype=" + keytype + ", keyid=" + keyid
				+ ", content=" + content + ", nickname=" + nickname
				+ ", avatar=" + avatar + ", client_id=" + client_id
				+ ", from_id=" + from_id + ", looktype=" + looktype
				+ ", regdate=" + regdate + "]";
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the keytype
	 */
	public String getKeytype() {
		return keytype;
	}

	/**
	 * @return the keyid
	 */
	public String getKeyid() {
		return keyid;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @return the avatar
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * @return the client_id
	 */
	public String getClient_id() {
		return client_id;
	}

	/**
	 * @return the from_id
	 */
	public String getFrom_id() {
		return from_id;
	}

	/**
	 * @return the looktype
	 */
	public String getLooktype() {
		return looktype;
	}

	/**
	 * @return the regdate
	 */
	public String getRegdate() {
		return regdate;
	}

	/**
	 * @param looktype
	 *            the looktype to set
	 */
	public void setLooktype(String looktype) {
		this.looktype = looktype;
	}

}
