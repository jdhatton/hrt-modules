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
import com.amazonaws.auth.PropertiesCredentials;
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
		AmazonSNS sns = new AmazonSNSClient(new PropertiesCredentials(
				SNSMobilePush.class
						.getResourceAsStream("AwsCredentials.properties")));

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
		
		// - String applicationARN = "arn:aws:sns:us-west-2:043482212558:app/APNS/Homeroom-APNS";
		
		String certificate = "-----BEGIN CERTIFICATE-----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-----END CERTIFICATE-----"; // This should be in pem format with \n at the
									// end of each line.
		String privateKey = "-----BEGIN RSA PRIVATE KEY-----MIIEpAIBAAKCAQEAyJHHtxcL9rHlfyVZoh/aZ2Imz5Fw5ZItTdzBAoXnaeM3xXCXS1GEomlMqlhfk3V+A5cHqoaBkEj7rKUmxWgyewQsbf1BLplXQ0t8yeNX6B59bsHTU1ZGk72xRh9aB+SAA2B/cO6v+b6bK6X6zoFJ2dWO4rkDRQv82S1Yxc6G5+jkieSnnJuRrhwQYXLOLSbYBXb0824fPBKKr62w37ofofHz+e7S3mvO9Aq/7/xQ3wyUFgrdnSs2/MtNKHUAJMw7UdqO+iLGhV48wvXRen0p5GvHjxJEMrltOuPgvyPStAuyak9zsrENyLzmsbnIRi6mpU71OiDYa6fH6JjA8zBOaQIDAQABAoIBAQCNCqbMw7IwCq1+Gb/MWGX3aXgtYvNnHMIrDN/VbGBtf9EKUMCzwGFq7G1zJK4hPfAdiilj6y5VTl1c7M40568H/slXl04s/Lv2oLDiL7Lzke1W44o+Al+jKTPuQcHH5xF2TneP7Olm0HjB2vhiG1zZiGnwWXZJKcLm3hKdFzya+1XyaeA2LAXhrmKFsfpVRymWbfQHaJGCv2XXrAOSZU735PPV/Gvks1BYNppxnEUbzWrejfVkRVRHTwpmKm/R/tI6+/sf+s+zY17zfIWtcVJzz5JaNtGQ11xiQXvvLMfCvrd3lBOqz6fyj//OSs6vjsyMJHolYHqFJ5cAFviqqgcBAoGBAPAHtE3Wje/37B1x0X42gnhHLbI+bpcqbSmH/M/3b6jMw+K7Rc9jQlOX/o9hNQTxwnijvkGUf3yIHKSJ1v5aNsZGFs0nUUTk76YVnYiZwkPLKPIBDvwbIOyHtTBgTyXRUQiR6+VBxsCzH/JTWtPLoWYPPXLHhCX2WJLkk03TwNyxAoGBANXp9xle4znN+5OubLrrmhTmVsz7QJtZFlQEeV4k9LN8HTzbKR6r3C+mWtqDHtkNdcw8FzHNTtCuxHJDsOps4Z+ycRnBuJEiecvdjXimTH3W28TY1HSh+Pjlw/I8aLev2CAHOtJLQdonWwRD05cFrlM+ximG2bZCgRmIPNDpoJs5AoGBANvHsiizlDNt5xzu4suWTdCkHAthbxGrH1kv0yT5g5wkIhixMTK7yDeSrNvMPifQ1ihwhfy2ZCU63D8sl1c+c3kJJEelAJz8oxKIKD6zy1UX6N3uajIdBQCPIjCxfsc56eDW/BBnMf2wBkh0Wm4jvh0dRz6ZwT6hWmjmMprIkYeBAoGAKP/T1XADWyPosRCAVH8tpA3V2ynY4y17AxVscafhorkKHYzf37oO1K8swK7a7b0Ps9pck7YHAiZNDp18viGqSOJ5nPctOdwCvhkzsUkmZgQVUTBB4CzfPE1Fdhsv8uzoHpNkKcQ01ewvHK0nU83J8LiaYIExHya1+b/L8DqnBrkCgYAGA+t4JOp1JktINhrPtwsLq88Dza18kKooRe0kapCZGJRhF4nTKvVgDaLN/DRZKbkBV8oKtvv0v/iWJjYIuzMW+92Zj3z7/SOQGVxA90aLBiFhtDI+8hlfgSDGQeXtSTvKjhPbWNqS+WpRP4dGnQucawpMAb4W2TL1s3lwKjixLQ==-----END RSA PRIVATE KEY-----"; // This should be in pem format with \n at the
								// end of each line.
		String applicationName = "HomeRoom";
		String deviceToken = ""; // This is 64 hex characters.
		snsClientWrapper.demoNotification(Platform.APNS, certificate,
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
