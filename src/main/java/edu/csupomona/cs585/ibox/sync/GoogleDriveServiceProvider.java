package edu.csupomona.cs585.ibox.sync;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;

public class GoogleDriveServiceProvider {

	private static String CLIENT_ID = "659206916847-6gcenp2fdl3vbv6i8kir1856t33uk2r1.apps.googleusercontent.com";
	private static String CLIENT_SECRET = "RkCHm8cUpaV1jIXi34ySD6YT";

	private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

	private static GoogleDriveServiceProvider INSTANCE;
	private Drive googleDriveClient;

	private GoogleDriveServiceProvider() {
		try {
			initGoogleDriveServices();
		} catch (IOException e) {
			System.out.println("Failed to create the Google drive client. Please check your Internet connection and your Google credentials.");
			e.printStackTrace();
		}
	}

	public static GoogleDriveServiceProvider get() {
		if (INSTANCE == null) {
			INSTANCE = new GoogleDriveServiceProvider();
		}
		return INSTANCE;
	}
	
	public void initGoogleDriveServices() throws IOException {
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
		.setAccessType("online")
		.setApprovalPrompt("auto").build();

		String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
		System.out.println("Please open the following URL in your browser then type the authorization code:");
		System.out.println("  " + url);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = br.readLine();

		GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
		GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);

		//Create a new authorized API client
		googleDriveClient = new Drive.Builder(httpTransport, jsonFactory, credential).setApplicationName("ibox").build();
	}

	public Drive getGoogleDriveClient() {
		return googleDriveClient;
	}
}
