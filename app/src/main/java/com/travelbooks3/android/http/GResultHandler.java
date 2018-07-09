package com.travelbooks3.android.http;

import org.json.JSONObject;

import ra.genius.net.GBean;
import ra.genius.net.http.handler.IHttpHandler;

public abstract class GResultHandler implements IHttpHandler
{
	@Override
	public GBean handle(GBean bean)
	{
		int returnCode = (Integer) bean.get(GHttpConstants.RETURN_CODE);

		String returnMessage = (String) bean.get(GHttpConstants.RETURN_MESSAGE);

		return result(bean, bean.get(GHttpConstants.RESULT_ENTITY), returnCode, returnMessage, (JSONObject) bean.get(GHttpConstants.RESPONSE_DATA));
	}

	public abstract GBean result(GBean bean, Object result, final int returnCode, final String returnMessage, final JSONObject responseData);

}
