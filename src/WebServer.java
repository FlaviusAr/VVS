import java.net.*;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.*;


class Main{
    public static void main(String[] args) throws IOException
    {
    	
    	int PORT=10008;
    	int SERVER_STATUS=1;  //SERVER_STATUS=1->RUNNING SERVER_STATUS=2->MAINTENANCE  SERVER_STATUS=3->STOPPED
    	String ROOT_DIRECTORY="C:\\Users\\user\\Desktop\\TestSite";
    	String MAINTENANCE_DIRECTORY=ROOT_DIRECTORY+"\\maintenance";		
        
    	
    	WebServer webserver = new WebServer(PORT,SERVER_STATUS,ROOT_DIRECTORY,MAINTENANCE_DIRECTORY);      
        webserver.startServer(); 
        
    }
}

public class WebServer extends Thread {
	
	protected Socket clientSocket;
	public int port;
	public int status;
	public String root_dir;
	public String maint_dir;
	
	public WebServer(int port,int status,String root_dir,String maint_dir) {
		this.port=port;
		this.status=status;
		this.root_dir=root_dir;
		this.maint_dir=maint_dir;
	}
	
	public void setPort(int new_port) {
		port=new_port;
		
	}
	
	public int getPort() {
		return port;
	}
	
	public void setStatus(int new_status) {
		status=new_status;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setRoot_dir(String new_root_dir) {
		File auxd1=new File(new_root_dir);
		if(auxd1.isDirectory()) {
		root_dir=new_root_dir;
		}
	}
	
	public String getRoot_dir() {
		return root_dir;
	}
	
	public void setMaint_dir(String new_maint_dir) {
		File auxd2=new File(new_maint_dir);
		if(auxd2.isDirectory()) {
		maint_dir=new_maint_dir;
		}
	}
	
	public String getMaint_dir() {
		return maint_dir;
	}
	
	public String getContentType(String fileRequested) {
		if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html")||fileRequested.endsWith("/"))
			return "text/html";
		else if(fileRequested.endsWith(".css"))
			return "text/css";
		else return "NOTFOUND ContentType";
	}
	
	public void startServer() throws IOException {	
		ServerSocket serverSocket=null;
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Connection Socket Created");
			try {
				//while (true) {
					System.out.println("Waiting for Connection");
				   clientSocket=serverSocket.accept();
					clientHandler(clientSocket);
				//}
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}
		} catch (IOException e) {
			System.err.println("Could not listen on port:"+port);
			System.exit(1);
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.err.println("Could not close port:"+port);
				System.exit(1);
			}
		}
	}

	public String getPath(String filepath) {
		if(filepath.equals("/")) {
			filepath=root_dir+"\\index.html";
			return filepath;
		}
		else if(filepath.equals("\\index.html")||filepath.equals("\\index.css"))
		{
			filepath=root_dir+filepath;
			return filepath;
		}
		else return filepath;
	}

	public void runningResponse(String filepath,String contentType,OutputStream clientOutput ) throws IOException {
	
		System.out.println("HTTP/1.1 200 OK");
		System.out.println("ContentType: " + contentType);
		clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes(Charset.forName("UTF-8")));
        clientOutput.write(("ContentType: " + contentType + "\r\n").getBytes());
        clientOutput.write("\r\n".getBytes());
        
        Scanner scanner = new Scanner(new File(filepath));
        String htmlString = scanner.useDelimiter("\\Z").next();
        scanner.close();
        
        clientOutput.write(htmlString.getBytes("UTF-8"));

        clientOutput.write("\r\n\r\n".getBytes());
        clientOutput.flush();

	}
	
	public void maintenanceResponse(String filepath,String contentType,OutputStream clientOutput ) throws IOException {
		
		
		System.out.println("HTTP/1.1 503 Service Unavailable");
		System.out.println("ContentType: " + contentType);
		clientOutput.write("HTTP/1.1 503 Service Unavailable\r\n".getBytes(Charset.forName("UTF-8")));
		clientOutput.write(("ContentType: " + contentType + "\r\n").getBytes());
		clientOutput.write("\r\n".getBytes());
	
	    Scanner scanner = new Scanner(new File(filepath));
	    String htmlString = scanner.useDelimiter("\\Z").next();
	    scanner.close();
	        
	    clientOutput.write(htmlString.getBytes("UTF-8"));

	    clientOutput.write("\r\n\r\n".getBytes());
	    clientOutput.flush();

	
	}

	/*public void stoppedResponse(String filepath,String contentType,PrintWriter out,OutputStream clientOutput ) throws IOException {
	
	out.println("HTTP/1.1 503 Service Unavailable");
	out.println("ContentType: " + contentType);
	
	
	clientOutput .write(Files.readAllBytes(Paths.get(filepath)));
	
	}*/
	
	public void notfoundResponse(String contentType,OutputStream clientOutput ) throws IOException {
		
		
		System.out.println("HTTP/1.1 404 File Not Found");
		System.out.println("ContentType: " + contentType);
		clientOutput.write("HTTP/1.1 404 File Not Found\r\n".getBytes(Charset.forName("UTF-8")));
		clientOutput.write(("ContentType: " + contentType + "\r\n").getBytes());
		//clientOutput.write("\r\n".getBytes());
		
	
	    //Scanner scanner = new Scanner(new File(filepath));
	    //String htmlString = scanner.useDelimiter("\\Z").next();
	    //scanner.close();
	        
	    //clientOutput.write(htmlString.getBytes("UTF-8"));

	    clientOutput.write("\r\n".getBytes());
	    clientOutput.flush();
	
		}
	
	
	public void clientHandler(Socket clientSocket) {
		System.out.println("New Communication Thread Started");

		try {
			//PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			OutputStream clientOutput  =  clientSocket.getOutputStream();

		/*	String inputLine;
			
			while ((inputLine = in.readLine()) != null) {
				System.out.println("Server: " + inputLine);
				out.println(inputLine);
				if (inputLine.trim().equals(""))
					break;
			}*/
			
			String firstline = null;
			try {
		        firstline = in.readLine();
		        if(firstline == null) {return ;}
	                           }
		    catch (IOException e) {e.printStackTrace();}

			
			StringTokenizer parse = new StringTokenizer(firstline);
			parse.nextToken();
			String path=parse.nextToken();
			String contentType=getContentType(path);
			String filepath=getPath(path);
		
			
			if(status==1) {
				File auxf1=new File(filepath);
				if(auxf1.isFile()) {
				runningResponse(filepath,contentType,clientOutput );
				}
				else {
					
					notfoundResponse(contentType,clientOutput );
				}
			}
			else if(status==2) {
				File auxf2=new File(maint_dir+"\\maintenance.html");
				if(auxf2.isFile()) {
				maintenanceResponse(maint_dir+"\\maintenance.html",contentType,clientOutput );
				}
				else {
					notfoundResponse(contentType,clientOutput );
				}
			}
			else if(status==3){
				in.close();
				clientOutput.close();
				clientSocket.close();
			}
			//out.close();
			in.close();
			clientOutput .close();
			clientSocket.close();
			//serverSocket.close();
		} catch (IOException e) {
			System.err.println("Problem with Communication Server");
			System.exit(1);
		}
	}
		
}


