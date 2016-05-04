package com.hrt.modules.aws.sns;

import com.amazonaws.auth.AWSCredentials;

public class AccessCredentials implements AWSCredentials {

    private  String accessKey;
    private  String secretAccessKey;

	public AccessCredentials(String accessKeyVal, String secretKeyVal) {
		 this.accessKey = accessKeyVal;
		 this.secretAccessKey = secretKeyVal;
	}

	@Override
	public String getAWSAccessKeyId() {
		return accessKey;
	}

	@Override
	public String getAWSSecretKey() {
		return secretAccessKey;
	}

}
