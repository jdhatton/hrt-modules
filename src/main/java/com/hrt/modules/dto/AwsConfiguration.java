package com.hrt.modules.dto;

import java.util.List;

public class AwsConfiguration {

	String awsAccessKey;
	String awsSecretKey;
	String awsAPNSCertificate;
	String awsAPNSPrivateKey;
	String awsSNSApplicationName;
	
	public AwsConfiguration() { }
	
	public AwsConfiguration(String awsAccessKey, String awsSecretKey, String awsAPNSCertificate,
			String awsAPNSPrivateKey, String awsSNSApplicationName) {
		super();
		this.awsAccessKey = awsAccessKey;
		this.awsSecretKey = awsSecretKey;
		this.awsAPNSCertificate = awsAPNSCertificate;
		this.awsAPNSPrivateKey = awsAPNSPrivateKey;
		this.awsSNSApplicationName = awsSNSApplicationName;
	}

	public String getAwsAccessKey() {
		return awsAccessKey;
	}

	public void setAwsAccessKey(String awsAccessKey) {
		this.awsAccessKey = awsAccessKey;
	}

	public String getAwsSecretKey() {
		return awsSecretKey;
	}

	public void setAwsSecretKey(String awsSecretKey) {
		this.awsSecretKey = awsSecretKey;
	}

	public String getAwsAPNSCertificate() {
		return awsAPNSCertificate;
	}

	public void setAwsAPNSCertificate(String awsAPNSCertificate) {
		this.awsAPNSCertificate = awsAPNSCertificate;
	}

	public String getAwsAPNSPrivateKey() {
		return awsAPNSPrivateKey;
	}

	public void setAwsAPNSPrivateKey(String awsAPNSPrivateKey) {
		this.awsAPNSPrivateKey = awsAPNSPrivateKey;
	}

	public String getAwsSNSApplicationName() {
		return awsSNSApplicationName;
	}

	public void setAwsSNSApplicationName(String awsSNSApplicationName) {
		this.awsSNSApplicationName = awsSNSApplicationName;
	}
	
	

}
