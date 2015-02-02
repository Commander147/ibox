package edu.csupomona.cs585.ibox;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.*;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.*;
import com.google.api.services.drive.model.FileList;

import edu.csupomona.cs585.ibox.sync.FileSyncManager;
import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;
import edu.csupomona.cs585.ibox.sync.GoogleDriveServiceProvider;
import static org.mockito.Matchers.*;

public class GoogleDriveFileSyncManagerTest {

	GoogleDriveFileSyncManager fileSyncManager;
	Drive mockService;
	String path;
	
	@Before
	public void setup()
	{
		this.mockService = mock(Drive.class);
		this.fileSyncManager = new  GoogleDriveFileSyncManager(this.mockService);
		String dir = "/Users/Joseph/Documents/workspace/sync/";
		String filename = "f1.txt";
		this.path = dir + filename;
	}
	
	@After
	public void teardown()
	{
		this.fileSyncManager = null;
		this.path = null;
	}
	
	@Test(expected=Exception.class)
	public void testAddFile() throws IOException
	{	
		// Test 1: Test Add With Good Path
		try
		{		
			// Setup Mock Objects
			Files files = mock(Files.class);
			when(this.mockService.files()).thenReturn(files);
			
			Insert insert = mock(Insert.class);
			
			when(files.insert(
					any(com.google.api.services.drive.model.File.class), 
					any(AbstractInputStreamContent.class)
					)
			).thenReturn(insert);
			
			com.google.api.services.drive.model.File googleFile = new com.google.api.services.drive.model.File();
			when(insert.execute()).thenReturn(googleFile);
			
			// Call Method With Good FileName
			this.fileSyncManager.addFile(new File(this.path));
			
			// Verify Path
			verify(this.mockService, atLeastOnce()).files();
			verify(files, atLeastOnce()).insert(any(com.google.api.services.drive.model.File.class), 
					any(AbstractInputStreamContent.class));
			verify(insert, atLeastOnce()).execute();
		}
		catch (Exception e)
		{
			assertEquals("Add File Test 1: Failed", null,  new IOException());
		}
		
		// Test 2: Add With Null Path
		this.fileSyncManager.addFile(null);
		assertEquals("Add File Test 2: Failed", true, false);
	}
	
	@Test(expected=Exception.class)
	public void testUpdateFile() throws IOException
	{	
		// Test 1: Test Add With Good Path
		try
		{		
			// Setup Mock Objects			
			Files files = mock(Files.class);
			when(this.mockService.files()).thenReturn(files);
			
			List request = mock (List.class);
			when(this.mockService.files().list()).thenReturn(request);
			
			com.google.api.services.drive.model.File file = new com.google.api.services.drive.model.File();
			file.setTitle("File");
			file.setId("ID");
			
			FileList filelist = new FileList();
			when(request.execute()).thenReturn(filelist);
			
			ArrayList list = new ArrayList();
			list.add(file);
			filelist.setItems(list);
						
			Update update = mock(Update.class);
			
			when(files.update(
					anyString(),
					any(com.google.api.services.drive.model.File.class), 
					any(AbstractInputStreamContent.class)
					)
			).thenReturn(update);
			
			com.google.api.services.drive.model.File googleFile = new com.google.api.services.drive.model.File();
			when(update.execute()).thenReturn(googleFile);
			
			// Call Method With Good FileName
			
			File f = mock(java.io.File.class);
			when(f.getName()).thenReturn("File");
			
			this.fileSyncManager.updateFile(f);
			
			// Verify Path
						verify(
								this.mockService, 
								atLeastOnce()
						).files();
						
						verify(files, atLeastOnce()).update(
								anyString(),
								any(com.google.api.services.drive.model.File.class), 
								any(AbstractInputStreamContent.class)
						);
						
						verify(
								update, 
								atLeastOnce()
						).execute();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			assertEquals("Update File Test 1: Failed", null,  new IOException());
		}
		
		// Test 2: Add With Null Path
		this.fileSyncManager.updateFile(null);
		assertEquals("Update File Test 2: Failed", true, false);
	}
	
	@Test(expected=Exception.class)
	public void testDelete() throws Exception
	{
		// Setup Mock Objects
		List request = mock (List.class);
		Files files = mock(Files.class);
		when(this.mockService.files()).thenReturn(files);
			
		when(this.mockService.files().list()).thenReturn(request);
		
		com.google.api.services.drive.model.File file = new com.google.api.services.drive.model.File();
		file.setTitle("FileTitle");
		file.setId("FileID");
		
		FileList filelist = new FileList();
		ArrayList list = new ArrayList();
		list.add(file);
		filelist.setItems(list);
		when(request.execute()).thenReturn(filelist);
		
		// Test 1: Valid File
		File f = mock(java.io.File.class);
		when(f.getName()).thenReturn("FileTitle");
		this.fileSyncManager.deleteFile(f);
		
		// Test 2: Invalid File/null
		when(f.getName()).thenReturn("WrongTitle");
		this.fileSyncManager.deleteFile(new File(this.path));
		assertEquals("Delete File Test 2: Failed", true, false);
	}
	
	@Test(expected=Exception.class)
	public void testGetID() throws Exception
	{
		// Setup Mock Objects
		List request = mock (List.class);
		Files files = mock(Files.class);
		when(this.mockService.files()).thenReturn(files);
			
		when(this.mockService.files().list()).thenReturn(request);
		
		com.google.api.services.drive.model.File file = new com.google.api.services.drive.model.File();
		file.setTitle("FileTitle");
		file.setId("FileID");
		
		FileList filelist = new FileList();
		ArrayList list = new ArrayList();
		list.add(file);
		filelist.setItems(list);
		when(request.execute()).thenReturn(filelist);
		try
		{
			// Test 1: Valid FileID
			String fileID = this.fileSyncManager.getFileId("FileTitle");
			assertEquals("GetID Test 1: Failed ", true, "FileID".equals(fileID));
			
			// Test 2: Mismatch FileID
			fileID = this.fileSyncManager.getFileId("WrongTitle");
			assertEquals("GetID Test 2: Failed ", true, fileID == null);
			
		}
		catch(Exception e)
		{
			assertEquals("Update File Test 1 or 2: Failed", true, false);
		}

		// Test 3: IOException
		when(request.execute()).thenThrow(Exception.class);
		String fileID = this.fileSyncManager.getFileId("FileTitle");
		assertEquals("GetID Test 3: Failed ", true, false);	
	}
}
