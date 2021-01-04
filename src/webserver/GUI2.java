package webserver;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileSystemView;


import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class GUI2 extends JFrame{


    public JFrame frame = new JFrame();
    public static String status = "Stopped";
    public JButton startButton = new JButton("Start server");
    public JCheckBox checkbox = new JCheckBox("Switch to maintenance mode");
    public static JTextField textPort;
    public static JTextField textrootDir;
    public static JTextField textmaintenanceDir;
    public JLabel address_value;
    public JLabel statusvalue;
    public JLabel portvalue;


    public GUI2(){

        JPanel webinfo = new JPanel();
        JPanel webcontrol = new JPanel();
        JPanel webconfig = new JPanel();

        
        
        
    	
		TitledBorder t1 =BorderFactory.createTitledBorder("Webserver info");
		TitledBorder t2 =BorderFactory.createTitledBorder("Webserver control");
		TitledBorder t3 =BorderFactory.createTitledBorder("Webserver config");
    

        webinfo.setBorder(t1);
        webcontrol.setBorder(t2);
        webconfig.setBorder(t3);

        webinfo.setLayout(new GridLayout(3, 2));
        webcontrol.setLayout(new GridLayout(2, 0));
        webconfig.setLayout(new GridLayout(3, 3));

        
        
        
      //CONTROL PANEL
        final JCheckBox checkbox = new JCheckBox("Switch to maintenance mode");
        checkbox.setEnabled(false);
        checkbox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				if(checkbox.isSelected()) {				
						WebServer.setState(2);
				} else {
					WebServer.setState(1);
				}
			}
			
		});
        
        
        
        final JButton startButton = new JButton("Start server");
        startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(startButton.getText().equals("Start server")) {
					{
					    startButton.setText("Stop Server");
					    WebServer.setState(1);
					    statusvalue.setText("running");
					    try {
							address_value.setText(GUI2.getAddress());
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					    portvalue.setText(Integer.toString((getPort())));
					    checkbox.setEnabled(true);
					 }
				}
				else {
					WebServer.setState(3);
					startButton.setText("Start server");
					statusvalue.setText("not running");
					portvalue.setText("");
				    checkbox.setSelected(false);
					checkbox.setEnabled(false);
				}
			}
		});
        
       
        
        
        webcontrol.add(startButton, BorderLayout.CENTER);
        webcontrol.add(checkbox, BorderLayout.CENTER);
        
        
        
        
        //INFO PANEL
        JLabel status = new JLabel("Server status:");
        JLabel address = new JLabel("Server address:");
        JLabel port = new JLabel("Server listening port:");
    

        address_value = new JLabel("not running");
        statusvalue = new JLabel("not running");
        portvalue = new JLabel("not running");
        
        webinfo.add(status);
        webinfo.add(statusvalue);
        webinfo.add(address);
        webinfo.add(address_value);
        webinfo.add(port);
        webinfo.add(portvalue);




        //CONFIG PANEL
        JLabel listenPort = new JLabel("Server listening on port:");
        JLabel rootDir = new JLabel("Web root directory:");
        JLabel maintenanceDir = new JLabel("Maintenance directory");
        JLabel nothing = new JLabel("");
        
        textPort = new JTextField("10008");
        textrootDir = new JTextField(20);
        textmaintenanceDir = new JTextField(20);
        
    	JButton buttonrootDir = new JButton("...");
    	buttonrootDir.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int r = fileChooser.showSaveDialog(null);
				
				if(r == JFileChooser.APPROVE_OPTION) {
					textrootDir.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
			
		});
    	
		JButton buttonmaintenanceDir = new JButton("...");
		buttonmaintenanceDir.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int r = fileChooser.showSaveDialog(null);
				
				if(r == JFileChooser.APPROVE_OPTION) {
					textmaintenanceDir.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
			
		});

        webconfig.add(listenPort);
        webconfig.add(textPort);
        webconfig.add(nothing);
        webconfig.add(rootDir);
        webconfig.add(textrootDir);
        webconfig.add(buttonrootDir);
        webconfig.add(maintenanceDir);
        webconfig.add(textmaintenanceDir);
        webconfig.add(buttonmaintenanceDir);


        frame.add(webinfo, BorderLayout.CENTER);
        frame.add(webcontrol, BorderLayout.NORTH);
        frame.add(webconfig, BorderLayout.SOUTH);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Web Server:" + status);
        frame.pack();
        frame.setVisible(true);


    }

    
    
	public static String getRootDirectory() {
		if(textrootDir.getText().trim().isEmpty()) {
			return "";
	}
		return textrootDir.getText().trim();
	}
	
	public static String getMaintenanceDirectory() {
		if(textmaintenanceDir.getText().trim().isEmpty()) {
			return "";
	}
		return textmaintenanceDir.getText().trim();
	}
	
	public static int getPort() {
		if(textPort.getText().trim().isEmpty()) {
			return 0;
		}
		return Integer.parseInt(textPort.getText().trim());
	}

    
	public static String getAddress() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}
	
	
	
	
	//public void 
    
    
    
    
    
    
    
    
    
    
  /*  public static void main(String[] args) {

        new GUI2();

    }
*/
	

}