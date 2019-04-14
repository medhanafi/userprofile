package com.comoressoft.profile.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Notifier {


	public static final String API_KEY = "AAAAemdKFi8:APA91bFiGUf-lWUcpJkM7OAAgna3M2ZYPYkTnQXjdiMPf3bIQtgTr60EbFrR3WqK7t4bQ1RBeHd1sVE9V5BRBsHL4Uxtr2m-6z9QU9uqkwOmEu-vDHih_Z3OnFnxEzNKaLsfwwb29OXY";

	

	//@Scheduled(cron = "0/2 * * * * ?")
	public void verifyAndSend() {
		
			if (this.sendNotification()) {
				System.out.println("SENDING NOTIFICATION");
			}
		
	}

	@SuppressWarnings("unchecked")
	public boolean sendNotification() {
			try {

			
					// Prepare JSON containing the GCM message content. What to send and where to
					// send.
					JSONObject jGcmData = new JSONObject();
					JSONObject jData = new JSONObject();
					jData.put("message", "La « Nuit du doute » aura lieu le samedi 4 mai 2019, nous vous tiendrons informer en temps réel");
					jData.put("title", "Quand débute le mois de Ramadan 2019 ?");  
					jData.put("idp", "12");
					jGcmData.put("to", "/topics/global");
					jGcmData.put("data", jData);

					// Create connection to send GCM Message request.
					URL url = new URL("https://android.googleapis.com/gcm/send");
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestProperty("Authorization", "key=" + API_KEY);
					conn.setRequestProperty("Content-Type", "application/json");
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);

					// Send GCM message content.
					OutputStream outputStream = conn.getOutputStream();
					outputStream.write(jGcmData.toString().getBytes(StandardCharsets.UTF_8));

					// Read GCM response.
					InputStream inputStream = conn.getInputStream();
					String resp = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
					System.out.println(resp);
					System.out.println("Check your device/emulator for notification or logcat for "
							+ "confirmation of the receipt of the GCM message.");
					return true;
				
			} catch (IOException e) {
				System.out.println("Unable to send GCM message.");
				System.out.println("Please ensure that API_KEY has been replaced by the server "
						+ "API key, and that the device's registration token is correct (if specified).");
						System.out.println("Error  "+e);
				return false;
			}

		
	}
}
