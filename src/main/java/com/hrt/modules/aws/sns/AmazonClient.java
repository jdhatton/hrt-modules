package com.hrt.modules.aws.sns;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;

public class AmazonClient {

	public AmazonClient(){ }
	
	private final void init(){
		
		//create a new SNS client and set endpoint
		AmazonSNSClient snsClient = new AmazonSNSClient(new ClasspathPropertiesFileCredentialsProvider());		                           
		snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));

		//create a new SNS topic
		CreateTopicRequest createTopicRequest = new CreateTopicRequest("MyNewTopic");
		CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
		//print TopicArn
		System.out.println(createTopicResult);
		//get request id for CreateTopicRequest from SNS metadata		
		System.out.println("CreateTopicRequest - " + snsClient.getCachedResponseMetadata(createTopicRequest));

		String topicArn = "arn:aws:sns:us-east-1:043482212558:homeroomclassroom_alldevices_MOBILEHUB_1264636960";
	}
	
	public void subscribeToTopic(){
		
	}
	
	public void publishToTopic(){
		
		// arn:aws:sns:us-east-1:043482212558:homeroomclassroom_alldevices_MOBILEHUB_1264636960
	}
	
}
