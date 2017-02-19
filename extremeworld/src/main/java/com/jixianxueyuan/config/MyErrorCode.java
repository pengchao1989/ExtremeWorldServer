package com.jixianxueyuan.config;

public enum MyErrorCode {
	
	NAME_REPEAT(10001, "nick name repeat"), //昵称重复
	NAME_EMPTY(10002, "nick name empty"),
	PHONE_EMPTY(10003, "phone empty"),
	SMS_VERIFICATION_CODE_ERROR(10004,"send sms code error"),
	SMS_VERIFICATION_CODE_CHECK_ERROR(10005, "sms code checked failed"),
	PHONE_REGISTERED(10006,"phone registered"),
	PASSWORD_EMPTY(10007,"password empty"),
	PHONE_FAILED(10008,"phone failed"),
	VERIFICATION_CODE_ERROR(10009,"verification code failed"),
	NO_USER(10010, "user not found"),
	PASSWORD_FAILED(10011,"password failed"),
	WEI_XIN_PAGE_ALREADY_INCLUDED(10012,"the wei xin page is already included"),
	WEI_XIN_PAGE_ERROR(10013, "the wei xin page is error"),
	NO_PRIVILEGE(10014, "non-privileged"),
	UNKNOW_ERROR(1001,"unknow error");

	private int errorCode;
	private String errorInfo;
	

	private MyErrorCode(int errorCode, String errorInfo){
		this.errorCode = errorCode;
		this.errorInfo = errorInfo;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	
	
	
}
