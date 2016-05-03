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
		
		// String certificate = "-----BEGIN CERTIFICATE-----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-----END CERTIFICATE-----"; // This should be in pem format with \n at the
		// String privateKey = "-----BEGIN RSA PRIVATE KEY-----MIIEpAIBAAKCAQEAyJHHtxcL9rHlfyVZoh/aZ2Imz5Fw5ZItTdzBAoXnaeM3xXCXS1GEomlMqlhfk3V+A5cHqoaBkEj7rKUmxWgyewQsbf1BLplXQ0t8yeNX6B59bsHTU1ZGk72xRh9aB+SAA2B/cO6v+b6bK6X6zoFJ2dWO4rkDRQv82S1Yxc6G5+jkieSnnJuRrhwQYXLOLSbYBXb0824fPBKKr62w37ofofHz+e7S3mvO9Aq/7/xQ3wyUFgrdnSs2/MtNKHUAJMw7UdqO+iLGhV48wvXRen0p5GvHjxJEMrltOuPgvyPStAuyak9zsrENyLzmsbnIRi6mpU71OiDYa6fH6JjA8zBOaQIDAQABAoIBAQCNCqbMw7IwCq1+Gb/MWGX3aXgtYvNnHMIrDN/VbGBtf9EKUMCzwGFq7G1zJK4hPfAdiilj6y5VTl1c7M40568H/slXl04s/Lv2oLDiL7Lzke1W44o+Al+jKTPuQcHH5xF2TneP7Olm0HjB2vhiG1zZiGnwWXZJKcLm3hKdFzya+1XyaeA2LAXhrmKFsfpVRymWbfQHaJGCv2XXrAOSZU735PPV/Gvks1BYNppxnEUbzWrejfVkRVRHTwpmKm/R/tI6+/sf+s+zY17zfIWtcVJzz5JaNtGQ11xiQXvvLMfCvrd3lBOqz6fyj//OSs6vjsyMJHolYHqFJ5cAFviqqgcBAoGBAPAHtE3Wje/37B1x0X42gnhHLbI+bpcqbSmH/M/3b6jMw+K7Rc9jQlOX/o9hNQTxwnijvkGUf3yIHKSJ1v5aNsZGFs0nUUTk76YVnYiZwkPLKPIBDvwbIOyHtTBgTyXRUQiR6+VBxsCzH/JTWtPLoWYPPXLHhCX2WJLkk03TwNyxAoGBANXp9xle4znN+5OubLrrmhTmVsz7QJtZFlQEeV4k9LN8HTzbKR6r3C+mWtqDHtkNdcw8FzHNTtCuxHJDsOps4Z+ycRnBuJEiecvdjXimTH3W28TY1HSh+Pjlw/I8aLev2CAHOtJLQdonWwRD05cFrlM+ximG2bZCgRmIPNDpoJs5AoGBANvHsiizlDNt5xzu4suWTdCkHAthbxGrH1kv0yT5g5wkIhixMTK7yDeSrNvMPifQ1ihwhfy2ZCU63D8sl1c+c3kJJEelAJz8oxKIKD6zy1UX6N3uajIdBQCPIjCxfsc56eDW/BBnMf2wBkh0Wm4jvh0dRz6ZwT6hWmjmMprIkYeBAoGAKP/T1XADWyPosRCAVH8tpA3V2ynY4y17AxVscafhorkKHYzf37oO1K8swK7a7b0Ps9pck7YHAiZNDp18viGqSOJ5nPctOdwCvhkzsUkmZgQVUTBB4CzfPE1Fdhsv8uzoHpNkKcQ01ewvHK0nU83J8LiaYIExHya1+b/L8DqnBrkCgYAGA+t4JOp1JktINhrPtwsLq88Dza18kKooRe0kapCZGJRhF4nTKvVgDaLN/DRZKbkBV8oKtvv0v/iWJjYIuzMW+92Zj3z7/SOQGVxA90aLBiFhtDI+8hlfgSDGQeXtSTvKjhPbWNqS+WpRP4dGnQucawpMAb4W2TL1s3lwKjixLQ==-----END RSA PRIVATE KEY-----"; // This should be in pem format with \n at the
		//  arn:aws:sns:us-east-1:043482212558:app/APNS/Homeroom
		
		// https://console.aws.amazon.com/sns/v2/home?region=us-east-1#/applications/arn:aws:sns:us-east-1:043482212558:app/APNS/Homeroom
		
		/**
		 * Server Certificate
		 * 
		 -----BEGIN CERTIFICATE-----
		MIIFMzCCBBugAwIBAgIETCMmsDANBgkqhkiG9w0BAQUFADCBsTELMAkGA1UEBhMC
		VVMxFjAUBgNVBAoTDUVudHJ1c3QsIEluYy4xOTA3BgNVBAsTMHd3dy5lbnRydXN0
		Lm5ldC9ycGEgaXMgaW5jb3Jwb3JhdGVkIGJ5IHJlZmVyZW5jZTEfMB0GA1UECxMW
		KGMpIDIwMDkgRW50cnVzdCwgSW5jLjEuMCwGA1UEAxMlRW50cnVzdCBDZXJ0aWZp
		Y2F0aW9uIEF1dGhvcml0eSAtIEwxQzAeFw0xNDA1MjMxNzQyNDJaFw0xNjA1MjQw
		NzA1MTNaMHQxCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpDYWxpZm9ybmlhMRIwEAYD
		VQQHEwlDdXBlcnRpbm8xEzARBgNVBAoTCkFwcGxlIEluYy4xJzAlBgNVBAMTHmdh
		dGV3YXkuc2FuZGJveC5wdXNoLmFwcGxlLmNvbTCCASIwDQYJKoZIhvcNAQEBBQAD
		ggEPADCCAQoCggEBAOQpUlXpU3+LJ2XR01QdVooN7S9OFOINp3/tomPaenQAwFGo
		qIakKFcN7AotWLFXFcR0QXKJkn4PL/zPKDBucyRFkc79S5+ZraGRISWfi7G8XeaG
		G3GzgeVQ977Qrn0IdCswnbwLsJoErnmq4AveQajUbYueR9SxhkWBwMimSxXzXoOS
		XUOPzRvzObCxVZrvBBDSRJCeNVnVxtCmb17DM3+z5GZatBwWnvw0jgvSQsgof+uC
		idXgqcN4msv3tVH54ipmuD9kbbwvtnDCHBZRXMMmhUfFXZRuE8GBEbPfVkqB16ad
		JV4TVrVxwFENwdnsX9CXavHCgFJhtHRWKOoCH48CAwEAAaOCAY0wggGJMAsGA1Ud
		DwQEAwIFoDAdBgNVHSUEFjAUBggrBgEFBQcDAQYIKwYBBQUHAwIwMwYDVR0fBCww
		KjAooCagJIYiaHR0cDovL2NybC5lbnRydXN0Lm5ldC9sZXZlbDFjLmNybDBkBggr
		BgEFBQcBAQRYMFYwIwYIKwYBBQUHMAGGF2h0dHA6Ly9vY3NwLmVudHJ1c3QubmV0
		MC8GCCsGAQUFBzAChiNodHRwOi8vYWlhLmVudHJ1c3QubmV0LzIwNDgtbDFjLmNl
		cjBKBgNVHSAEQzBBMDUGCSqGSIb2fQdLAjAoMCYGCCsGAQUFBwIBFhpodHRwOi8v
		d3d3LmVudHJ1c3QubmV0L3JwYTAIBgZngQwBAgIwKQYDVR0RBCIwIIIeZ2F0ZXdh
		eS5zYW5kYm94LnB1c2guYXBwbGUuY29tMB8GA1UdIwQYMBaAFB7xq4kG+EkPATN3
		7hR67hl8kyhNMB0GA1UdDgQWBBSSGfpGPmr9+FPcqRiStH0iKRBL7DAJBgNVHRME
		AjAAMA0GCSqGSIb3DQEBBQUAA4IBAQAkj6+okMFVl7NHqQoii4e4iPDFiia+LmHX
		BCc+2UEOOjilYWYoZ61oeqRXQ2b4Um3dT/LPmzMkKmgEt9epKNBLA6lSkL+IzEnF
		wLQCHkL3BgvV20n5D8syzREV+8RKmSqiYmrF8dFq8cDcstu2joEKd173EfrymWW1
		fMeaYTbjrn+vNkgM94+M4c/JnIDOhiPPbeAx9TESQZH+/6S98hrbuPIIlmaOJsOT
		GMOUWeOTHXTCfGb1EM4SPVcyCW28TlWUBl8miqnsEO8g95jZZ25wFANlVxhfxBnP
		fwUYU5NTM3h0xi3rIlXwAKD6zLKipcQ/YXRx7oMYnAm53tfU2MxV
		-----END CERTIFICATE-----
		 */
		
		String certificate = "-----BEGIN CERTIFICATE-----"+
		"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"+
		"-----END CERTIFICATE-----";
		
		String privateKey = "-----BEGIN PRIVATE KEY-----"+
		"MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDIkce3Fwv2seV/JVmiH9pnYibPkXDlki1N3MEChedp4zfFcJdLUYSiaUyqWF+TdX4DlweqhoGQSPuspSbFaDJ7BCxt/UEumVdDS3zJ41foHn1uwdNTVkaTvbFGH1oH5IADYH9w7q/5vpsrpfrOgUnZ1Y7iuQNFC/zZLVjFzobn6OSJ5Kecm5GuHBBhcs4tJtgFdvTzbh88EoqvrbDfuh+h8fP57tLea870Cr/v/FDfDJQWCt2dKzb8y00odQAkzDtR2o76IsaFXjzC9dF6fSnka8ePEkQyuW064+C/I9K0C7JqT3OysQ3IvOaxuchGLqalTvU6INhrp8fomMDzME5pAgMBAAECggEBAI0KpszDsjAKrX4Zv8xYZfdpeC1i82ccwisM39VsYG1/0QpQwLPAYWrsbXMkriE98B2KKWPrLlVOXVzszjTnrwf+yVeXTiz8u/agsOIvsvOR7Vbjij4CX6MpM+5BwcfnEXZOd4/s6WbQeMHa+GIbXNmIafBZdkkpwubeEp0XPJr7VfJp4DYsBeGuYoWx+lVHKZZt9AdokYK/ZdesA5JlTvfk89X8a+SzUFg2mnGcRRvNat6N9WRFVEdPCmYqb9H+0jr7+x/6z7NjXvN8ha1xUnPPklo20ZDXXGJBe+8sx8K+t3eUE6rPp/KP/85Kzq+OzIwkeiVgeoUnlwAW+KqqBwECgYEA8Ae0TdaN7/fsHXHRfjaCeEctsj5ulyptKYf8z/dvqMzD4rtFz2NCU5f+j2E1BPHCeKO+QZR/fIgcpInW/lo2xkYWzSdRROTvphWdiJnCQ8so8gEO/Bsg7Ie1MGBPJdFRCJHr5UHGwLMf8lNa08uhZg89cseEJfZYkuSTTdPA3LECgYEA1en3GV7jOc37k65suuuaFOZWzPtAm1kWVAR5XiT0s3wdPNspHqvcL6Za2oMe2Q11zDwXMc1O0K7EckOw6mzhn7JxGcG4kSJ5y92NeKZMfdbbxNjUdKH4+OXD8jxot6/YIAc60ktB2idbBEPTlwWuUz7GKYbZtkKBGYg80OmgmzkCgYEA28eyKLOUM23nHO7iy5ZN0KQcC2FvEasfWS/TJPmDnCQiGLExMrvIN5Ks28w+J9DWKHCF/LZkJTrcPyyXVz5zeQkkR6UAnPyjEogoPrPLVRfo3e5qMh0FAI8iMLF+xznp4Nb8EGcx/bAGSHRabiO+HR1HPpnBPqFaaOYymsiRh4ECgYAo/9PVcANbI+ixEIBUfy2kDdXbKdjjLXsDFWxxp+GiuQodjN/fug7UryzArtrtvQ+z2lyTtgcCJk0OnXy+IapI4nmc9y053AK+GTOxSSZmBBVRMEHgLN88TUV2Gy/y7Ogek2QpxDTV7C8crSdTzcnwuJpggTEfJrX5v8vwOqcGuQKBgAYD63gk6nUmS0g2Gs+3CwurzwPNrXyQqihF7SRqkJkYlGEXidMq9WANos38NFkpuQFXygq2+/S/+JYmNgi7Mxb73ZmPfPv9I5AZXED3RosGIWG0Mj7yGV+BIMZB5e1JO8qOE9tY2pL5alE/h0adC5xrCkwBvhbZMvWzeXAqOLEt"+
		"-----END PRIVATE KEY-----";

		
		
		String certificate1 = 
		"-----BEGIN CERTIFICATE-----"+
		"\nMIIF8jCCBNqgAwIBAgIIfgpXtFgwSRgwDQYJKoZIhvcNAQELBQAwgZYxCzAJBgNV"+
		"\nBAYTAlVTMRMwEQYDVQQKDApBcHBsZSBJbmMuMSwwKgYDVQQLDCNBcHBsZSBXb3Js"+
		"\nZHdpZGUgRGV2ZWxvcGVyIFJlbGF0aW9uczFEMEIGA1UEAww7QXBwbGUgV29ybGR3"+
		"\naWRlIERldmVsb3BlciBSZWxhdGlvbnMgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkw"+
		"\nHhcNMTYwNTAyMTYwNjEyWhcNMTcwNjAxMTYwNjEyWjCBiDEYMBYGCgmSJomT8ixk"+
		"\nAQEMCEhvbWVyb29tMSYwJAYDVQQDDB1BcHBsZSBQdXNoIFNlcnZpY2VzOiBIb21l"+
		"\ncm9vbTETMBEGA1UECwwKSlo5SjY0NkVRNTEiMCAGA1UECgwZSG9tZXJvb20gVGVj"+
		"\naG5vbG9naWVzIExMQzELMAkGA1UEBhMCVVMwggEiMA0GCSqGSIb3DQEBAQUAA4IB"+
		"\nDwAwggEKAoIBAQDIkce3Fwv2seV/JVmiH9pnYibPkXDlki1N3MEChedp4zfFcJdL"+
		"\nUYSiaUyqWF+TdX4DlweqhoGQSPuspSbFaDJ7BCxt/UEumVdDS3zJ41foHn1uwdNT"+
		"\nVkaTvbFGH1oH5IADYH9w7q/5vpsrpfrOgUnZ1Y7iuQNFC/zZLVjFzobn6OSJ5Kec"+
		"\nm5GuHBBhcs4tJtgFdvTzbh88EoqvrbDfuh+h8fP57tLea870Cr/v/FDfDJQWCt2d"+
		"\nKzb8y00odQAkzDtR2o76IsaFXjzC9dF6fSnka8ePEkQyuW064+C/I9K0C7JqT3Oy"+
		"\nsQ3IvOaxuchGLqalTvU6INhrp8fomMDzME5pAgMBAAGjggJOMIICSjAdBgNVHQ4E"+
		"\nFgQU47xvSQmutYgwMTTST2rhZjfG2UswDAYDVR0TAQH/BAIwADAfBgNVHSMEGDAW"+
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
		"\nb24wDQYJKoZIhvcNAQELBQADggEBAFHvkOZfAjAscZ/PZmv8RXFwfSCHuvPPUhNo"+
		"\nvhc4K1op2V7P0eU8ffpHI200c3Wp5ybkJEEicoYVu5ua7mK0c58YdFhbIXSG6sRI"+
		"\nOTr4V0YTVFPqImk7ksPzNEIRGQ1i5eL4ql/EpiC5K/nnnKwMGIAUnu4tUbaX+7mS"+
		"\nmT5+z2Quu2y2r90AysmXLod/nWr1eFwRLw1CletEo5RlFOMPnATVJuO60REhoAcT"+
		"\nXGwfoSv8iQlHjPsBjdHg5mu6oMxvGP48iEUUOAK0IUHWydb33GIoQBEJW5AC19am"+
		"\nbEVBLt1sWWeQa5uwJyk7UP+3YZq5URd4itA0Ry203PHv5dj/t08="+
		"\n-----END CERTIFICATE-----";
		
		String privateKey1 = 
				"-----BEGIN RSA PRIVATE KEY-----"+
				"\nMIIEpAIBAAKCAQEAyJHHtxcL9rHlfyVZoh/aZ2Imz5Fw5ZItTdzBAoXnaeM3xXCX"+
				"\nS1GEomlMqlhfk3V+A5cHqoaBkEj7rKUmxWgyewQsbf1BLplXQ0t8yeNX6B59bsHT"+
				"\nU1ZGk72xRh9aB+SAA2B/cO6v+b6bK6X6zoFJ2dWO4rkDRQv82S1Yxc6G5+jkieSn"+
				"\nnJuRrhwQYXLOLSbYBXb0824fPBKKr62w37ofofHz+e7S3mvO9Aq/7/xQ3wyUFgrd"+
				"\nnSs2/MtNKHUAJMw7UdqO+iLGhV48wvXRen0p5GvHjxJEMrltOuPgvyPStAuyak9z"+
				"\nsrENyLzmsbnIRi6mpU71OiDYa6fH6JjA8zBOaQIDAQABAoIBAQCNCqbMw7IwCq1+"+
				"\nGb/MWGX3aXgtYvNnHMIrDN/VbGBtf9EKUMCzwGFq7G1zJK4hPfAdiilj6y5VTl1c"+
				"\n7M40568H/slXl04s/Lv2oLDiL7Lzke1W44o+Al+jKTPuQcHH5xF2TneP7Olm0HjB"+
				"\n2vhiG1zZiGnwWXZJKcLm3hKdFzya+1XyaeA2LAXhrmKFsfpVRymWbfQHaJGCv2XX"+
				"\nrAOSZU735PPV/Gvks1BYNppxnEUbzWrejfVkRVRHTwpmKm/R/tI6+/sf+s+zY17z"+
				"\nfIWtcVJzz5JaNtGQ11xiQXvvLMfCvrd3lBOqz6fyj//OSs6vjsyMJHolYHqFJ5cA"+
				"\nFviqqgcBAoGBAPAHtE3Wje/37B1x0X42gnhHLbI+bpcqbSmH/M/3b6jMw+K7Rc9j"+
				"\nQlOX/o9hNQTxwnijvkGUf3yIHKSJ1v5aNsZGFs0nUUTk76YVnYiZwkPLKPIBDvwb"+
				"\nIOyHtTBgTyXRUQiR6+VBxsCzH/JTWtPLoWYPPXLHhCX2WJLkk03TwNyxAoGBANXp"+
				"\n9xle4znN+5OubLrrmhTmVsz7QJtZFlQEeV4k9LN8HTzbKR6r3C+mWtqDHtkNdcw8"+
				"\nFzHNTtCuxHJDsOps4Z+ycRnBuJEiecvdjXimTH3W28TY1HSh+Pjlw/I8aLev2CAH"+
				"\nOtJLQdonWwRD05cFrlM+ximG2bZCgRmIPNDpoJs5AoGBANvHsiizlDNt5xzu4suW"+
				"\nTdCkHAthbxGrH1kv0yT5g5wkIhixMTK7yDeSrNvMPifQ1ihwhfy2ZCU63D8sl1c+"+
				"\nc3kJJEelAJz8oxKIKD6zy1UX6N3uajIdBQCPIjCxfsc56eDW/BBnMf2wBkh0Wm4j"+
				"\nvh0dRz6ZwT6hWmjmMprIkYeBAoGAKP/T1XADWyPosRCAVH8tpA3V2ynY4y17AxVs"+
				"\ncafhorkKHYzf37oO1K8swK7a7b0Ps9pck7YHAiZNDp18viGqSOJ5nPctOdwCvhkz"+
				"\nsUkmZgQVUTBB4CzfPE1Fdhsv8uzoHpNkKcQ01ewvHK0nU83J8LiaYIExHya1+b/L"+
				"\n8DqnBrkCgYAGA+t4JOp1JktINhrPtwsLq88Dza18kKooRe0kapCZGJRhF4nTKvVg"+
				"\nDaLN/DRZKbkBV8oKtvv0v/iWJjYIuzMW+92Zj3z7/SOQGVxA90aLBiFhtDI+8hlf"+
				"\ngSDGQeXtSTvKjhPbWNqS+WpRP4dGnQucawpMAb4W2TL1s3lwKjixLQ=="+
				"\n-----END RSA PRIVATE KEY-----";
			
		String applicationName = "Homeroom2";
		String deviceToken = "b59742a864ad330d86a6cf174509d6e01cdd6f4232a374f9f05100c2ba90c7b3"; // This is 64 hex characters.
		snsClientWrapper.demoNotification(Platform.APNS, certificate1,
				privateKey1, deviceToken, applicationName, attributesMap);
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
