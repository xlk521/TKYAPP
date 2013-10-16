package com.example.content;

import org.json.JSONException;
import org.json.JSONObject;
import com.example.content.ParamUser;

public class JSONDataUtil {

	public static String handleResult(JSONObject jsonObject)
			throws JSONException {
		String result = jsonObject.getString(JSONKey.FLAG_RESULT);

		if (result.equals(JSONKey.FLAG_RESULT_OK)) {
			return null;
		} else if (result.equals(JSONKey.FLAG_RESULT_ERROR)) {
			return jsonObject.getString(JSONKey.ERROR_INFO);
		} else {
			throw new RuntimeException("未知联网错误");
		}
	}

	// ===========================================================
	// login
	// ===========================================================
	/** 登陆--用户名及密码 */
	public static JSONObject login(String name, String pw) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JSONKey.FLAG, JSONKey.FLAG_LOGIN);
		jsonObject.put(JSONKey.USER_LOGIN, name);
		jsonObject.put(JSONKey.USER_PASSWORD, pw);
		return jsonObject;
	}

	/** 登陆成功，生成用户信息-user */
	public static ParamUser handleLogin(JSONObject jsonObject)
			throws JSONException {
		ParamUser user = new ParamUser();

		user.setId(jsonObject.getInt(JSONKey.USER_ID));
		user.setName(jsonObject.getString(JSONKey.USER_NAME));
		user.setPassword(jsonObject.getString(JSONKey.USER_PASSWORD));
		user.setEid(jsonObject.getString(JSONKey.USER_EID));
		user.setType(jsonObject.getString(JSONKey.USER_TYPE));
		user.setSex(jsonObject.getString(JSONKey.USER_SEX));
		user.setPosition(jsonObject.getString(JSONKey.USER_POSITION));
		user.setGroupName(jsonObject.getString(JSONKey.USER_GROUPNAME));
		user.setTeamName(jsonObject.getString(JSONKey.USER_TEAMNAME));
		user.setBureauName(jsonObject.getString(JSONKey.USER_BUREAUNAME));
		user.setDeptName(jsonObject.getString(JSONKey.USER_DEPTNAME));

		return user;
	}
}
