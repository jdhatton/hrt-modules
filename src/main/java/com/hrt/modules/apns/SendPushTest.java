package com.hrt.modules.apns;


public class SendPushTest {

	public SendPushTest() { }

	public static void main(String[] args) {
		
		try{
			System.out.println("\n >>>  Started...");
			ApnsPushyClient client = new ApnsPushyClient();
			client.sendNotification();
			client.close();
			System.out.println("\n >>>  Finished...");
        } catch (Exception e){
            System.out.println(""+e);
            e.printStackTrace();
        }

	}

}
