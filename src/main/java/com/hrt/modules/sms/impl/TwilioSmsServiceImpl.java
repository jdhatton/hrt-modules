package com.hrt.modules.sms.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

import com.hrt.modules.sms.SmsService;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

@Component
public class TwilioSmsServiceImpl implements SmsService {

	// Find your Account Sid and Token at twilio.com/user/account
	public static final String ACCOUNT_SID = "AC9a9ef4735f6a8e37708a3a1f6e95917f";
	public static final String AUTH_TOKEN = "7d19f9942e48e2d89a4f555b80e7116d";

	public void sendMessage(String toNumber, String body) throws Exception {
		try {
			TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

			// Build the parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("To", toNumber));
			params.add(new BasicNameValuePair("From", "+19136030012"));
			params.add(new BasicNameValuePair("Body", "Testing: Download the app here:   https://itunes.apple.com/us/app/homeroomclassroom-free/id1018616543?mt=8       ."));  

			MessageFactory messageFactory = client.getAccount().getMessageFactory();
			Message message = messageFactory.create(params);
			System.out.println(message.getSid());
		} catch (TwilioRestException tex) {
			throw new Exception(tex);
		}
	}
}
