package com.hrt.modules.aws.sns;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.hrt.modules.aws.sns.SampleMessageGenerator.Platform;
import com.hrt.modules.dto.AwsConfiguration;
import com.hrt.modules.utils.StringUtils;

public class AmazonSNSPublisher {

	private static final ObjectMapper objectMapper = new ObjectMapper();
	private final AmazonSNS snsClient;

	public static final Map<Platform, Map<String, MessageAttributeValue>> attributesMap = new HashMap<Platform, Map<String, MessageAttributeValue>>();
	static {
		attributesMap.put(Platform.ADM, null);
		attributesMap.put(Platform.GCM, null);
		attributesMap.put(Platform.APNS, null);
		attributesMap.put(Platform.APNS_SANDBOX, null);
	}
	
	public AmazonSNSPublisher(AmazonSNS client) {
		this.snsClient = client;
	}
	
	public void sendNotification(String deviceToken, String message, int badge, String sound, AwsConfiguration awsConf) throws Exception {

		// AmazonSNS sns = new AmazonSNSClient(new
		// AccessCredentials("AKIAJGE6YKV7ZOB2BU2Q","VHNI9fZcMQirkslT9juVpodG4Mf94ixv7CXRuc4s"));

		AmazonSNS sns = new AmazonSNSClient(
				new AccessCredentials(awsConf.getAwsAccessKey(), awsConf.getAwsSecretKey()));

		sns.setEndpoint("https://sns.us-west-2.amazonaws.com");

		try {
			SNSMobilePush sample = new SNSMobilePush(sns);

			sample.demoAppleAppNotification();

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon SNS, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with SNS, such as not "
					+ "being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}
	
	private PublishResult publish(String endpointArn, Platform platform,
			Map<Platform, Map<String, MessageAttributeValue>> attributesMap, String message, int badge, String sound) {
		PublishRequest publishRequest = new PublishRequest();
		Map<String, MessageAttributeValue> notificationAttributes = getValidNotificationAttributes(attributesMap
				.get(platform));
		if (notificationAttributes != null && !notificationAttributes.isEmpty()) {
			publishRequest.setMessageAttributes(notificationAttributes);
		}
		publishRequest.setMessageStructure("json");
		// If the message attributes are not set in the requisite method,
		// notification is sent with default attributes
		String notification = formatMessage(platform, message, badge, sound);
		Map<String, String> messageMap = new HashMap<String, String>();
		messageMap.put(platform.name(), message);
		message = jsonify(messageMap);
		// For direct publish to mobile end points, topicArn is not relevant.
		publishRequest.setTargetArn(endpointArn);

		// Display the message that will be sent to the endpoint/
		System.out.println("{Message Body: " + notification + "}");
		StringBuilder builder = new StringBuilder();
		builder.append("{Message Attributes: ");
		for (Map.Entry<String, MessageAttributeValue> entry : notificationAttributes
				.entrySet()) {
			builder.append("(\"" + entry.getKey() + "\": \""
					+ entry.getValue().getStringValue() + "\"),");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append("}");
		System.out.println(builder.toString());

		publishRequest.setMessage(notification);
		return snsClient.publish(publishRequest);
	}

	public void demoNotification(Platform platform, String principal,
			String credential, String platformToken, String applicationName,
			Map<Platform, Map<String, MessageAttributeValue>> attrsMap, String message, int badge, String sound) {

		String ARN = "arn:aws:sns:us-west-2:043482212558:endpoint/APNS_SANDBOX/Homeroom-Notifications/6c6c1c8e-19be-3a9c-82e4-b5fab2e2a603";
		PublishResult publishResult = publish( ARN, platform, attrsMap, message,  badge, sound);

		System.out.println("Published! \n{MessageId="+ publishResult.getMessageId() + "}");
	}

	public static Map<String, MessageAttributeValue> getValidNotificationAttributes(
			Map<String, MessageAttributeValue> notificationAttributes) {
		Map<String, MessageAttributeValue> validAttributes = new HashMap<String, MessageAttributeValue>();

		if (notificationAttributes == null) return validAttributes;

		for (Map.Entry<String, MessageAttributeValue> entry : notificationAttributes
				.entrySet()) {
			if (!StringUtils.isBlank(entry.getValue().getStringValue())) {
				validAttributes.put(entry.getKey(), entry.getValue());
			}
		}
		return validAttributes;
	}
	
	private String formatMessage(Platform platform, String message, int badge, String sound) {
		switch (platform) {
		case APNS:
			return formatAppleMessage(message, badge, sound);
		case APNS_SANDBOX:
			return formatAppleMessage(message, badge, sound);
		case GCM:
			return SampleMessageGenerator.getSampleAndroidMessage();
		case ADM:
			return SampleMessageGenerator.getSampleKindleMessage();
		case BAIDU:
			return SampleMessageGenerator.getSampleBaiduMessage();
		case WNS:
			return SampleMessageGenerator.getSampleWNSMessage();
		case MPNS:
			return SampleMessageGenerator.getSampleMPNSMessage();
		default:
			throw new IllegalArgumentException("Platform not supported : "
					+ platform.name());
		}
	}
	
	public String formatAppleMessage(String message, int badge, String sound) {
		Map<String, Object> appleMessageMap = new HashMap<String, Object>();
		Map<String, Object> appMessageMap = new HashMap<String, Object>();
		appMessageMap.put("alert", message);
		appMessageMap.put("badge", badge);
		appMessageMap.put("sound", sound);
		appleMessageMap.put("aps", appMessageMap);
		return jsonify(appleMessageMap);
	}
	
	public static String jsonify(Object message) {
		try {
			return objectMapper.writeValueAsString(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw (RuntimeException) e;
		}
	}
}
