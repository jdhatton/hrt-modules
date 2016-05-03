package com.hrt.modules.apns;

import java.io.File;
import java.util.concurrent.ExecutionException;

import com.relayrides.pushy.apns.ApnsClient;
import com.relayrides.pushy.apns.ClientNotConnectedException;
import com.relayrides.pushy.apns.PushNotificationResponse;
import com.relayrides.pushy.apns.util.ApnsPayloadBuilder;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import com.relayrides.pushy.apns.util.TokenUtil;

import io.netty.util.concurrent.Future;

public class ApnsPushyClient {

	ApnsClient<SimpleApnsPushNotification> apnsClient = null;
	Future<Void> connectFuture = null;
	SimpleApnsPushNotification pushNotification;
	Future<PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = null;

	public ApnsPushyClient() {
		try {
			apnsClient = new ApnsClient<>(new File("/Users/jhatton/Documents/devenv/devserv/workspaces-ang/hrt-modules/HomeroomCertificates.p12"), "");

			connectFuture = apnsClient.connect(ApnsClient.DEVELOPMENT_APNS_HOST);
			connectFuture.await();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void sendNotification() {
		final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
		payloadBuilder.setAlertBody("Example!");

		final String payload = payloadBuilder.buildWithDefaultMaximumLength();
		final String token = TokenUtil.sanitizeTokenString("<efc7492 bdbd8209>");

		pushNotification = new SimpleApnsPushNotification(token, "com.example.myApp", payload);

		sendNotificationFuture = apnsClient.sendNotification(pushNotification);

		try {
			try {
				final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse = sendNotificationFuture
						.get();

				if (pushNotificationResponse.isAccepted()) {
					System.out.println("Push notitification accepted by APNs gateway.");
				} else {
					System.out.println("Notification rejected by the APNs gateway: "
							+ pushNotificationResponse.getRejectionReason());

					if (pushNotificationResponse.getTokenInvalidationTimestamp() != null) {
						System.out.println("\t…and the token is invalid as of "
								+ pushNotificationResponse.getTokenInvalidationTimestamp());
					}
				}
			} catch (final ExecutionException e) {
				System.err.println("Failed to send push notification.");
				e.printStackTrace();

				if (e.getCause() instanceof ClientNotConnectedException) {
					System.out.println("Waiting for client to reconnect…");
					apnsClient.getReconnectionFuture().await();
					System.out.println("Reconnected.");
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void close() {
		final Future<Void> disconnectFuture = apnsClient.disconnect();
		try {
			disconnectFuture.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
