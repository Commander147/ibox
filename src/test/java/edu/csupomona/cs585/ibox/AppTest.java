package edu.csupomona.cs585.ibox;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import edu.csupomona.cs585.ibox.sync.FileSyncManager;
import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;
import edu.csupomona.cs585.ibox.sync.GoogleDriveServiceProvider;
import edu.csupomona.cs585.ibox.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Placeholder for unit test
 */
public class AppTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /** 
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class, GoogleDriveFileSyncManagerTest.class );
    }

    public void testApp() throws IOException
    {        
    	String testDir = "/Users/Joseph/Documents/workspace/sync";
    	Path path = Paths.get(testDir);
    	// Verify FileSync Manager Gets Created
    	//FileSyncManager fileSyncManager = mock(GoogleDriveFileSyncManager.class);
    	//FileSyncManager.when(fileSyncManager.addFile(path.toFile())).thenReturn(43);
    	
    	
    	// Verify Watcher Gets Started
    	//WatchDir dir = new WatchDir(, fileSyncManager);
    	
    	// Verify WatchDir is running
        //dir.processEvents();
    	
    	assertTrue( true );
    }
}
