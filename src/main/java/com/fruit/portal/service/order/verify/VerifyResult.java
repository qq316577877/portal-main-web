package com.fruit.portal.service.order.verify;

public class VerifyResult {

	/**
	 * 指执行结果，true为成功，false为失败
	 */
	private Boolean excuteResult = true;

	/**
	 * 错误码
	 */
	private int errorCode;

	/**
	 * 备注信息：失败原因
	 */
	private String msg;

	public Boolean getExcuteResult() {
		return excuteResult;
	}

	public void setExcuteResult(Boolean excuteResult) {
		this.excuteResult = excuteResult;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
