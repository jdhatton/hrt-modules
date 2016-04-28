package com.hrt.modules.sms;

public interface SmsService {
	
	public void sendMessage(String toNumber, String body) throws Exception;

}
