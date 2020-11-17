import org.junit.*;
import java.net.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.nio.file.*;



public class WebServerTest {

  	int PORT=10008;
	int SERVER_STATUS=2;  //SERVER_STATUS=1->RUNNING SERVER_STATUS=2->MAINTENANCE  SERVER_STATUS=3->STOPPED
	String ROOT_DIRECTORY="C:\\Users\\user\\Desktop\\TestSite";
	String MAINTENANCE_DIRECTORY=ROOT_DIRECTORY+"\\maintenance";		
    
	WebServer webserver = new WebServer(PORT,SERVER_STATUS,ROOT_DIRECTORY,MAINTENANCE_DIRECTORY);    	
	
	
	/*@Before
	public void setup() {
     	WebServer webserver = new WebServer(PORT,SERVER_STATUS,ROOT_DIRECTORY,MAINTENANCE_DIRECTORY);    
		}
	*/
	
    @Test
    public void setwrongRootdir(){
	        webserver.setRoot_dir("Wrong dir");
	        Assert.assertNotEquals("Wrong dir",  webserver.root_dir);;
	    }

    @Test
    public void setwrongMaintenancedir(){
	        webserver.setMaint_dir("Wrong dir");
	        Assert.assertNotEquals("Wrong dir", webserver.maint_dir);
	    }

	
    @Test
    public void getnotfoundContenttype(){
	        String x=webserver.getContentType("file.xcc");
	        Assert.assertEquals(x, "NOTFOUND ContentType");
	    }

    
    @Test
    public void gethomePath(){
	        String x=webserver.getPath("/");
	        Assert.assertEquals(x, webserver.root_dir+"\\index.html");
	    }
    
    

	
    @Test(expected=IOException.class)
    public void usesameportsametime() throws IOException{
    Thread ServerThread1 = new Thread(new Runnable() {
        @Override
    	public void run(){	
    		ServerSocket serverSocket=null;
    		try {
    			serverSocket = new ServerSocket(webserver.port);
    			System.out.println("Connection Socket Created");
    			try {
    				while (true) {
    					System.out.println("Waiting for Connection");
    				    Socket clientSocket=serverSocket.accept();
    					webserver.clientHandler(clientSocket);
    				}
    			} catch (IOException e) {
    				System.err.println("Accept failed.");
    				System.exit(1);
    			}
    		} catch (IOException e) {
    			//System.err.println("Could not listen on port:"+webserver.port);
    			System.exit(1);
    		} finally {
    			try {
    				serverSocket.close();
    			} catch (IOException e) {
    				System.err.println("Could not close port:"+webserver.port);
    				System.exit(1);
    			}
    		}
    	}
    });

    Thread ServerThread2 = new Thread(new Runnable() {

        @Override
    	public void run(){	
    		ServerSocket serverSocket=null;
    		try {
    			serverSocket = new ServerSocket(webserver.port);
    			System.out.println("Connection Socket Created");
    			try {
    				while (true) {
    					System.out.println("Waiting for Connection");
    				    Socket clientSocket=serverSocket.accept();
    					webserver.clientHandler(clientSocket);
    				}
    			} catch (IOException e) {
    				System.err.println("Accept failed.");
    				System.exit(1);
    			}
    		} catch (IOException e) {
    			//System.err.println("Could not listen on port:"+webserver.port);
    			System.exit(1);
    		} finally {
    			try {
    				serverSocket.close();
    			} catch (IOException e) {
    				System.err.println("Could not close port:"+webserver.port);
    				System.exit(1);
    			}
    		}
    	}
    });

    ServerThread1.start();
    ServerThread2.start();
	
    }
    

    @Test(expected=NullPointerException.class)
    public void nullclient() throws NullPointerException{
    
    	webserver.clientHandler(null);
    	
    }
    
    
    @Test(expected=IOException.class)
    public void wrongpathresponse() throws IOException{
    	ServerSocket serverSocket = new ServerSocket(10080);
        Socket clientSocket = serverSocket.accept();
        OutputStream infoout = clientSocket.getOutputStream();

       
    	webserver.runningResponse("wrongpath","text/html",infoout);
    	serverSocket.close();
    }
    
    /*
    @Test
    public void searchforhomepage() throws IOException {
    	ServerSocket serverSocket = new ServerSocket(10080);
        Socket clientSocket = serverSocket.accept();
    	webserver.clientHandler(clientSocket);
    	OutputStream infoout = clientSocket.getOutputStream();
    	
    	Assert.assertEquals(webserver.filepath,);
    	
    	serverSocket.close();
    }*/
    
    @Test(expected=IOException.class)
    public void clienthandlerwithserverclosed() throws IOException{
     	ServerSocket serverSocket = new ServerSocket(10080);
        Socket clientSocket = serverSocket.accept();
        serverSocket.close();
        webserver.clientHandler(clientSocket);
        }
    
    
    
    
 }

