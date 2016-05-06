package com.hrt.modules.aws.sns;

/*
 * Copyright 2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.hrt.modules.aws.sns.SampleMessageGenerator.Platform;
 

public class SNSMobilePush {

	private AmazonSnsClientWrapper snsClientWrapper;

	public SNSMobilePush(AmazonSNS snsClient) {
		this.snsClientWrapper = new AmazonSnsClientWrapper(snsClient);
	}

	public static final Map<Platform, Map<String, MessageAttributeValue>> attributesMap = new HashMap<Platform, Map<String, MessageAttributeValue>>();
	static {
		attributesMap.put(Platform.ADM, null);
		attributesMap.put(Platform.GCM, null);
		attributesMap.put(Platform.APNS, null);
		attributesMap.put(Platform.APNS_SANDBOX, null);
		attributesMap.put(Platform.BAIDU, addBaiduNotificationAttributes());
		attributesMap.put(Platform.WNS, addWNSNotificationAttributes());
		attributesMap.put(Platform.MPNS, addMPNSNotificationAttributes());
	}

	public static void main(String[] args) throws IOException {
		/*
		 * TODO: Be sure to fill in your AWS access credentials in the
		 * AwsCredentials.properties file before you try to run this sample.
		 * http://aws.amazon.com/security-credentials
		 */
//		AmazonSNS sns = new AmazonSNSClient(new PropertiesCredentials(
//				SNSMobilePush.class.getResourceAsStream("AwsCredentials.properties")));
		
		AmazonSNS sns = new AmazonSNSClient(new AccessCredentials("AKIAJGE6YKV7ZOB2BU2Q","VHNI9fZcMQirkslT9juVpodG4Mf94ixv7CXRuc4s"));

		sns.setEndpoint("https://sns.us-west-2.amazonaws.com");
		System.out.println("===========================================\n");
		System.out.println("Getting Started with Amazon SNS");
		System.out.println("===========================================\n");
		try {
			SNSMobilePush sample = new SNSMobilePush(sns);
			/* TODO: Uncomment the services you wish to use. */
			// sample.demoAndroidAppNotification();
			// sample.demoKindleAppNotification();
			 sample.demoAppleAppNotification();
			// sample.demoAppleSandboxAppNotification();
			// sample.demoBaiduAppNotification();
			// sample.demoWNSAppNotification();
			// sample.demoMPNSAppNotification();
		} catch (AmazonServiceException ase) {
			System.out
					.println("Caught an AmazonServiceException, which means your request made it "
							+ "to Amazon SNS, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out
					.println("Caught an AmazonClientException, which means the client encountered "
							+ "a serious internal problem while trying to communicate with SNS, such as not "
							+ "being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}


	public void demoAppleAppNotification() {
		// TODO: Please fill in following values for your application. You can
		// also change the notification payload as per your preferences using
		// the method
		// com.amazonaws.sns.samples.tools.SampleMessageGenerator.getSampleAppleMessage()

		String certificate = 
				"-----BEGIN CERTIFICATE-----"+
				"\nMIIF8jCCBNqgAwIBAgIIN9ELSpFrSSUwDQYJKoZIhvcNAQELBQAwgZYxCzAJBgNV"+
				"\nBAYTAlVTMRMwEQYDVQQKDApBcHBsZSBJbmMuMSwwKgYDVQQLDCNBcHBsZSBXb3Js"+
				"\nZHdpZGUgRGV2ZWxvcGVyIFJlbGF0aW9uczFEMEIGA1UEAww7QXBwbGUgV29ybGR3"+
				"\naWRlIERldmVsb3BlciBSZWxhdGlvbnMgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkw"+
				"\nHhcNMTYwNTAzMTgzNTE0WhcNMTcwNjAyMTgzNTE0WjCBiDEYMBYGCgmSJomT8ixk"+
				"\nAQEMCEhvbWVyb29tMSYwJAYDVQQDDB1BcHBsZSBQdXNoIFNlcnZpY2VzOiBIb21l"+
				"\ncm9vbTETMBEGA1UECwwKSlo5SjY0NkVRNTEiMCAGA1UECgwZSG9tZXJvb20gVGVj"+
				"\naG5vbG9naWVzIExMQzELMAkGA1UEBhMCVVMwggEiMA0GCSqGSIb3DQEBAQUAA4IB"+
				"\nDwAwggEKAoIBAQCzDYckuAATbBF66Jhm9HGnErKpByMQVCVlFYHHOt/FcJmOLwDt"+
				"\nqKLIzX437lM4xpk9e2M1rCkIW+4FJvEFg8tWMv7/tSusnx1YE9/LyON6/rtvenwm"+
				"\nbQPt2W20B0xF73qhSQPj2h+sLUQMxXRpkPh51QVMrXfvREAY0I0fQiJe+rkA6qPq"+
				"\n4kvhGXcx1DAypCZiT9ZgdqC7aKSALfyDhaeSHRpnZjq+HsUJYG67EXmjH0UTnHiJ"+
				"\n87JJ5e67ugTsw0LgZJTRxl1s4Huf+P39nWw+k9E/V2dDzxO4iK45Ab98D9GtONg6"+
				"\nunKYSoLzNn4SK/KoFjZVm8gvIleWNvZ7j0P5AgMBAAGjggJOMIICSjAdBgNVHQ4E"+
				"\nFgQU16Gday+CVqq+jsnqw5zWI0u9g5YwDAYDVR0TAQH/BAIwADAfBgNVHSMEGDAW"+
				"\ngBSIJxcJqbYYYIvs67r2R1nFUlSjtzCCARwGA1UdIASCARMwggEPMIIBCwYJKoZI"+
				"\nhvdjZAUBMIH9MIHDBggrBgEFBQcCAjCBtgyBs1JlbGlhbmNlIG9uIHRoaXMgY2Vy"+
				"\ndGlmaWNhdGUgYnkgYW55IHBhcnR5IGFzc3VtZXMgYWNjZXB0YW5jZSBvZiB0aGUg"+
				"\ndGhlbiBhcHBsaWNhYmxlIHN0YW5kYXJkIHRlcm1zIGFuZCBjb25kaXRpb25zIG9m"+
				"\nIHVzZSwgY2VydGlmaWNhdGUgcG9saWN5IGFuZCBjZXJ0aWZpY2F0aW9uIHByYWN0"+
				"\naWNlIHN0YXRlbWVudHMuMDUGCCsGAQUFBwIBFilodHRwOi8vd3d3LmFwcGxlLmNv"+
				"\nbS9jZXJ0aWZpY2F0ZWF1dGhvcml0eTAwBgNVHR8EKTAnMCWgI6Ahhh9odHRwOi8v"+
				"\nY3JsLmFwcGxlLmNvbS93d2RyY2EuY3JsMA4GA1UdDwEB/wQEAwIHgDATBgNVHSUE"+
				"\nDDAKBggrBgEFBQcDAjAQBgoqhkiG92NkBgMBBAIFADAQBgoqhkiG92NkBgMCBAIF"+
				"\nADBfBgoqhkiG92NkBgMGBFEwTwwISG9tZXJvb20wBQwDYXBwDA1Ib21lcm9vbS52"+
				"\nb2lwMAYMBHZvaXAMFUhvbWVyb29tLmNvbXBsaWNhdGlvbjAODAxjb21wbGljYXRp"+
				"\nb24wDQYJKoZIhvcNAQELBQADggEBADKJEMGjHuuDpK4EH/i+jMhXyb6TOgXVLXfv"+
				"\ngJzpkO6V4EgBVqabYr8GzMGRAG9D21N9EKBTHB5aq4GmNEfQQDFE60O//WXrzcpO"+
				"\nirKz/w35WIvAXE62DsizNOfeZf13XxAUTiCDVl9I9RszWWpyvVG3/Ups/o8rMV6c"+
				"\njx9JXeWbp1BJXbwEne2LPeh3zmRc3IjMjV7nJLdG6SkGIRnK7Yroiv4jMKmCGIKm"+
				"\nrscKccEySkAgG/o614foafH4PUHRUgZHct7N0qeH7gHPxyvUWaTDc/6jT0v5SYq2"+
				"\nmz8S2bogWsu0G4aAkp0rZSeVdE5hGZHSI6BKFX2v+xUT/SKBP/g="+
				"\n-----END CERTIFICATE-----";

		String privateKey =
				"\n-----BEGIN RSA PRIVATE KEY-----"+
						"\nMIIEogIBAAKCAQEAsw2HJLgAE2wReuiYZvRxpxKyqQcjEFQlZRWBxzrfxXCZji8A"+
						"\n7aiiyM1+N+5TOMaZPXtjNawpCFvuBSbxBYPLVjL+/7UrrJ8dWBPfy8jjev67b3p8"+
						"\nJm0D7dlttAdMRe96oUkD49ofrC1EDMV0aZD4edUFTK1370RAGNCNH0IiXvq5AOqj"+
						"\n6uJL4Rl3MdQwMqQmYk/WYHagu2ikgC38g4Wnkh0aZ2Y6vh7FCWBuuxF5ox9FE5x4"+
						"\nifOySeXuu7oE7MNC4GSU0cZdbOB7n/j9/Z1sPpPRP1dnQ88TuIiuOQG/fA/RrTjY"+
						"\nOrpymEqC8zZ+EivyqBY2VZvILyJXljb2e49D+QIDAQABAoIBAFu6JG+1K6THijTz"+
						"\nD5A+zjKPK6PYGih1Uyrz16ZfDOAzDReAEPOvnqVMUMsKduvxIfQTUAHhDqxPnCJb"+
						"\njsIKgJLKUURiZdtrMDc9Bzt40MDj2tYDY7Kzyn2BE+hUjMiPJL2XkDycM/mavu/j"+
						"\n//1M3V5cuPhNLdIPnt6XiJRo5v7Mi6SCDOBn29sh1gUpDE/KxciHH3xU3Fa4J9XP"+
						"\n6mI0sm667H+Ssoue6lJf+yb/P5Rn/wOXkYkK9E3lUK1rT16FEoVXJs2FZKvRhEOq"+
						"\nv8zzzWLAoMvwbbucbueaVJpui9sbJNRKhGqUIVADqXdpI5D0YYZxse5sUQNR0htq"+
						"\n4R3O0zkCgYEA3ATAsJ84SRZyjx7kyuw2oSw11XXdWZKSzAH8G1XXr3Vh2+JAlgoX"+
						"\nVqxuH6xlcrmZCBQpIo8tKjqL3zsNVTMZ6gGxCo7Gr0U1mQiRucreIuYX/HjbAVFJ"+
						"\nQetFsy3iJ7scMo5i5FSUeoLRYyAsvpYkDfSXM3mCM5OqoEO3vJgnke8CgYEA0FW2"+
						"\nrlzQi58R87egOOBSTXKjAi+xgUPLGwNjq0TlnHb4T4kC6x/05yXM8z7X5YR/PjSr"+
						"\nEM0oyqJwoEOo6eem3mUV/gimshhucvvQ88XGRLSXzYrlLsnGFCN5ontDKylvsfk/"+
						"\n7yH4MJGCJBYOh2zXo5fS0l078VwkyqJqBlK60JcCgYBeLNqdv0lEX4thn4OnKDyp"+
						"\n+FaCrBS3Bno3QJI80R2lSMU+bTXgAynCO8RXYl96H99VCewhks9uR+kfABPQ6DKr"+
						"\na1XwgvSgiStL+dMGh8NVhM+3jbOQeM62nJIVBArNntWO/ktuGJYFaoFdwKPmYDXG"+
						"\nk2805UnJ8ecS+mlnHGmzmQKBgGTduu68fTO8NkLbWrVu1iotIIMEeFpvajyP7YeU"+
						"\ned7BMwO/ACy6ciYfhRUYDgtPlROXCGPAEMnaz9CoMXXd9EqkgntV0O9VKNR+2vHL"+
						"\nhWzqSrz3THLUuhQyAMgMCb8x9qQiWzP6LGHHcUzraOlkZxsLfMTg4w6+YAnDjLW3"+
						"\nBD/NAoGAb/Y2SnW/ZQSpWjgYMbKRg6YDF/sKPt497Y0pWTgQczEL35UJOlGW1RWo"+
						"\n/rEv/dFz7hnNcqCKde/IuOg9Tu1U5xgjeZdG8OwbUFQxfbiD9vIcI7FmEBj5sweB"+
						"\nzGyhNUx8c5CNTA93nGJ1kyvjlP63awkXjBmyEtovSUGxuJXS+L4="+
						"\n-----END RSA PRIVATE KEY-----";
		
		String applicationName = "Homeroom-Notifications";
		
		String deviceToken = "0b4847603c3570946b218d9f3df1dc33be20f72c2079e6dbbafe6e647cbe9f4f"; // This is 64 hex characters.
		snsClientWrapper.demoNotification(Platform.APNS_SANDBOX, certificate,
				privateKey, deviceToken, applicationName, attributesMap);
	}

	public void demoAppleSandboxAppNotification() {
		// TODO: Please fill in following values for your application. You can
		// also change the notification payload as per your preferences using
		// the method
		// com.amazonaws.sns.samples.tools.SampleMessageGenerator.getSampleAppleMessage()
		String certificate = ""; // This should be in pem format with \n at the
									// end of each line.
		String privateKey = ""; // This should be in pem format with \n at the
								// end of each line.
		String applicationName = "";
		String deviceToken = ""; // This is 64 hex characters.
		snsClientWrapper.demoNotification(Platform.APNS_SANDBOX, certificate,
				privateKey, deviceToken, applicationName, attributesMap);
	}
	
	public void demoAndroidAppNotification() {
		// TODO: Please fill in following values for your application. You can
		// also change the notification payload as per your preferences using
		// the method
		// com.amazonaws.sns.samples.tools.SampleMessageGenerator.getSampleAndroidMessage()
		String serverAPIKey = "";
		String applicationName = "";
		String registrationId = "";
		snsClientWrapper.demoNotification(Platform.GCM, "", serverAPIKey,
				registrationId, applicationName, attributesMap);
	}
	
	public void demoKindleAppNotification() {
		// TODO: Please fill in following values for your application. You can
		// also change the notification payload as per your preferences using
		// the method
		// com.amazonaws.sns.samples.tools.SampleMessageGenerator.getSampleKindleMessage()
		String clientId = "";
		String clientSecret = "";
		String applicationName = "";

		String registrationId = "";
		snsClientWrapper.demoNotification(Platform.ADM, clientId, clientSecret,
				registrationId, applicationName, attributesMap);
	}

	public void demoBaiduAppNotification() {
		/*
		 * TODO: Please fill in the following values for your application. If
		 * you wish to change the properties of your Baidu notification, you can
		 * do so by modifying the attribute values in the method
		 * addBaiduNotificationAttributes() . You can also change the
		 * notification payload as per your preferences using the method
		 * com.amazonaws
		 * .sns.samples.tools.SampleMessageGenerator.getSampleBaiduMessage()
		 */
		String userId = "";
		String channelId = "";
		String apiKey = "";
		String secretKey = "";
		String applicationName = "";
		snsClientWrapper.demoNotification(Platform.BAIDU, apiKey, secretKey,
				channelId + "|" + userId, applicationName, attributesMap);
	}

	public void demoWNSAppNotification() {
		/*
		 * TODO: Please fill in the following values for your application. If
		 * you wish to change the properties of your WNS notification, you can
		 * do so by modifying the attribute values in the method
		 * addWNSNotificationAttributes() . You can also change the notification
		 * payload as per your preferences using the method
		 * com.amazonaws.sns.samples
		 * .tools.SampleMessageGenerator.getSampleWNSMessage()
		 */
		String notificationChannelURI = "";
		String packageSecurityIdentifier = "";
		String secretKey = "";
		String applicationName = "";
		snsClientWrapper.demoNotification(Platform.WNS,
				packageSecurityIdentifier, secretKey, notificationChannelURI,
				applicationName, attributesMap);
	}

	public void demoMPNSAppNotification() {
		/*
		 * TODO: Please fill in the following values for your application. If
		 * you wish to change the properties of your MPNS notification, you can
		 * do so by modifying the attribute values in the method
		 * addMPNSNotificationAttributes() . You can also change the
		 * notification payload as per your preferences using the method
		 * com.amazonaws
		 * .sns.samples.tools.SampleMessageGenerator.getSampleMPNSMessage ()
		 */
		String notificationChannelURI = "";
		String applicationName = "";
		snsClientWrapper.demoNotification(Platform.MPNS, "", "",
				notificationChannelURI, applicationName, attributesMap);
	}

	private static Map<String, MessageAttributeValue> addBaiduNotificationAttributes() {
		Map<String, MessageAttributeValue> notificationAttributes = new HashMap<String, MessageAttributeValue>();
		notificationAttributes.put("AWS.SNS.MOBILE.BAIDU.DeployStatus",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("1"));
		notificationAttributes.put("AWS.SNS.MOBILE.BAIDU.MessageKey",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("default-channel-msg-key"));
		notificationAttributes.put("AWS.SNS.MOBILE.BAIDU.MessageType",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("0"));
		return notificationAttributes;
	}

	private static Map<String, MessageAttributeValue> addWNSNotificationAttributes() {
		Map<String, MessageAttributeValue> notificationAttributes = new HashMap<String, MessageAttributeValue>();
		notificationAttributes.put("AWS.SNS.MOBILE.WNS.CachePolicy",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("cache"));
		notificationAttributes.put("AWS.SNS.MOBILE.WNS.Type",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("wns/badge"));
		return notificationAttributes;
	}

	private static Map<String, MessageAttributeValue> addMPNSNotificationAttributes() {
		Map<String, MessageAttributeValue> notificationAttributes = new HashMap<String, MessageAttributeValue>();
		notificationAttributes.put("AWS.SNS.MOBILE.MPNS.Type",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("token")); // This attribute is required.
		notificationAttributes.put("AWS.SNS.MOBILE.MPNS.NotificationClass",
				new MessageAttributeValue().withDataType("String")
						.withStringValue("realtime")); // This attribute is required.
														
		return notificationAttributes;
	}
}
