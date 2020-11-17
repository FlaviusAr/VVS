import org.junit.*;
import java.net.*;
import java.io.*;




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
    public void getHomepath() {
    	Assert.assertEquals(webserver.root_dir+"\\index.html",webserver.getPath("/"));
    }
   
    @Test(expected=IOException.class)
    public void usesameportsametime() throws IOException{
    Thread ServerThread1 = new Thread(new Runnable() {
        @Override
      	public void run(){	
      	try {
  			webserver.startServer();
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
      	}
    });

    Thread ServerThread2 = new Thread(new Runnable() {

        @Override
    	public void run(){	
    	try {
			webserver.startServer();
		} catch (IOException e) {
			System.out.println("eroare port");
			// TODO Auto-generated catch block
			e.printStackTrace();
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

