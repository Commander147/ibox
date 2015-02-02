package edu.csupomona.cs585.ibox;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.List;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import edu.csupomona.cs585.ibox.sync.FileSyncManager;
import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;
import edu.csupomona.cs585.ibox.sync.GoogleDriveServiceProvider;

public class AppIntegrationTest {

	GoogleDriveFileSyncManager manager;
	
	@Before
	public void Setup()
	{
		Drive drive = null;
		HttpTransport httpTransport = new NetHttpTransport();
			JsonFactory jsonFactory = new JacksonFactory();
			
			GoogleCredential credential;
			try {
				credential = new GoogleCredential.Builder()
				.setTransport(httpTransport)
				.setJsonFactory(jsonFactory)
				.setServiceAccountId(
				"659206916847-uaoohck6i44v170ta5s1ngq1229b0m5b@developer.gserviceaccount.com"
				)
				.setServiceAccountScopes(Collections.singleton(DriveScopes.DRIVE))
				.setServiceAccountPrivateKeyFromP12File(
				new java.io.File("My Project-6878a282b22d.p12"))
				.setServiceAccountUser("csjoe585@gmail.com")
				.build();
				
				drive = new Drive.Builder(httpTransport, jsonFactory, credential).setApplicationName("ibox").build();
			} 
			catch (GeneralSecurityException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		FileSyncManager fileSyncManager = new GoogleDriveFileSyncManager(drive);
		this.manager = (GoogleDriveFileSyncManager) fileSyncManager;
	}
	
	@Test
	public void testAddFile()
	{	
		boolean added = false;
		java.io.File file = new java.io.File("testfile1.txt");
		try {
			file.createNewFile();
			this.manager.addFile(file);
		
			FileList list;
			
			list = this.manager.service.files().list().execute();
			
			for(File aFile : list.getItems())
			{
				if(aFile.getTitle().equals("testfile1.txt"))
				{
					added = true;
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		file.delete();
		
		if(added)
		{
			try {
				this.manager.deleteFile(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		assertEquals("Add File Failed", true, added);
	}
	
	@Test
	public void updateFile()
	{
		boolean updated = false;
		java.io.File file = new java.io.File("testfile1.txt");
		try {
			file.createNewFile();
			this.manager.addFile(file);
			
			//Add Data To File
			FileWriter fileWritter = new FileWriter(file.getName(),true);
	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	        bufferWritter.write("data".toCharArray());
	        bufferWritter.close();
			
			this.manager.updateFile(file);
			
			FileList list;
			
			list = this.manager.service.files().list().execute();
			
			for(File aFile : list.getItems())
			{
				if(aFile.getTitle().equals("testfile1.txt") && aFile.getFileSize() > 0)
				{
					updated = true;
					break;
				}
			}
		}
		catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		
		try {
			this.manager.deleteFile(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals("Update File Failed", true, updated);
	}
	
	@Test
	public void deleteFile()
	{
		boolean deleted = true;
		java.io.File file = new java.io.File("testfile1.txt");
		try {
			file.createNewFile();
			this.manager.addFile(file);
			this.manager.deleteFile(file);
		
			FileList list;
			
			list = this.manager.service.files().list().execute();
			
			for(File aFile : list.getItems())
			{
				if(aFile.getTitle().equals("testfile1.txt"))
				{
					deleted = false;
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		file.delete();
		assertEquals("Delete File Failed", true, deleted);
	}
}
