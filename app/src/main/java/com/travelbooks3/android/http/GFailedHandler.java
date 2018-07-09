package com.travelbooks3.android.http;

import com.travelbooks3.android.util.LogUtil;

import org.apache.commons.httpclient.HttpStatus;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import ra.genius.net.GBean;
import ra.genius.net.http.handler.IHttpHandler;

public abstract class GFailedHandler implements IHttpHandler
{
	public abstract void failedExecute(GBean bean);

	// private Dialog mDialog;

	// public GFailedExecute(Dialog dialog)
	// {
	// super();
	// mDialog = dialog;
	// }

	@Override
	public GBean handle(GBean bean)
	{
		// ProgressDialogUtil.dismiss(mDialog);
		Exception e = bean.getException();
		if(e != null)
		{

			LogUtil.e("통신 에러");
			LogUtil.e("bean.getException().getMessage() : " + e.getMessage());
			e.printStackTrace();
			String message = "";

			if(e instanceof ConnectException)
			{
				message = "네트워크가 활성화 되지 않았습니다.\n3G/4G 혹은 Wifi를 켜주시기 바랍니다.";
			}
			else if(e instanceof UnknownHostException)
			{
				message = "네트워크가 활성화 되지 않았습니다.\n3G/4G 혹은 Wifi를 켜주시기 바랍니다.";
			}

			else if(e instanceof SocketException)
			{
				if(e.getMessage().contains("unreachable"))
				{
					message = "네트워크가 활성화 되지 않았습니다.\n3G/4G 혹은 Wifi를 켜주시기 바랍니다.";
				}
				else
				{
					message = "네트워크가 불안정합니다.";
				}
			}
			else if(e instanceof SocketTimeoutException)
			{
				message = "네트워크가 불안정합니다. 다시 시도해 주세요.";
			}
			else
			{
				message = "관리자에게 문의 바랍니다.";
				// message = (String) bean.get("error");
				// if(FormatUtil.isNullorEmpty(message))
				// {
				// message = e.getMessage();
				// }
			}
			bean.put(GHttpConstants.ERROR_MESSAGE, message);

			failedExecute(bean);
			return null;
		}
		else if(bean.getHttpStatus() != HttpStatus.SC_OK)
		{
			// message = "관리자에게 문의 바랍니다.";
			String message = "관리자에게 문의 바랍니다.";
			bean.put(GHttpConstants.ERROR_MESSAGE, message);
			LogUtil.e("HttpStatus : " + bean.getHttpStatus());
			failedExecute(bean);
			return null;
		}
		else
		{
			return bean;
		}
	}
}
