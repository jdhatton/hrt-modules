package com.hrt.modules.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceHelper {

	
	@Autowired
	SmsService smsService;
	
	public void sendMessage(String toPhoneNumber, String body) {
		
		try{
			smsService.sendMessage(toPhoneNumber, body);
		} catch( Exception ex){
			ex.printStackTrace();
			//
			// TODO log error and write an error to a DB table for manual intervention?
			//
		}
	}


}
