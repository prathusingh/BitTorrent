package bittorrent.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Home {
	protected static Shell shlMaverickBitTorrent;
	public Text txtText;
	public final static String HOME = "25.24.147.71";
	public final static String CLIENT1 = "25.53.65.86";
	public final static int PORT = 3333;
	
	private static Home instance;
	   
	private Home(){
		File downloadsDir = new File("C:\\BitTorrent\\Downloads\\");
	    if (!downloadsDir.exists()) {
	    	downloadsDir.mkdirs();
	    }
	    File torrentDir = new File("C:\\BitTorrent\\Torrents\\");
	    if (!torrentDir.exists()) {
	    	torrentDir.mkdirs();
	    }
	    File LogDir = new File("C:\\BitTorrent\\Logs\\");
	    if (!LogDir.exists()) {
	    	LogDir.mkdirs();
	    }
	    File WebServerDir = new File("C:\\Apache\\htdocs\\bittorrent\\files\\");
	    if (!WebServerDir.exists()) {
	    	WebServerDir.mkdirs();
	    }
		shlMaverickBitTorrent = new Shell();
		shlMaverickBitTorrent.setModified(true);
		shlMaverickBitTorrent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		shlMaverickBitTorrent.setMinimumSize(new Point(1024, 568));
		shlMaverickBitTorrent.setSize(931, 453);
		
		shlMaverickBitTorrent.setText("Maverick Bit Torrent");
		
		Menu menu = new Menu(shlMaverickBitTorrent, SWT.BAR);
		shlMaverickBitTorrent.setMenuBar(menu);
		
		MenuItem menuFile = new MenuItem(menu, SWT.CASCADE);
		menuFile.setText("Menu");
		
		Menu popupMenu_File = new Menu(menuFile);
		menuFile.setMenu(popupMenu_File);
		
		MenuItem mntmExit = new MenuItem(popupMenu_File, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.exit(0);
			}
		});
		mntmExit.setText("&Exit");
		
		MenuItem menuSettings = new MenuItem(menu, SWT.NONE);
		menuSettings.setText("Settings");
		
		MenuItem menuHelp = new MenuItem(menu, SWT.NONE);
		menuHelp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		menuHelp.setText("Help");
		
		Composite composite = new Composite(shlMaverickBitTorrent, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		composite.setBounds(4, 4, 1000, 75);
		
		Button btnCreateNew = new Button(composite, SWT.FLAT | SWT.CENTER);
		btnCreateNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int pieces = 0;
				String IP = null;
//				String text = txtText.getText();
//				text += "Testing";
//				txtText.setText(text);
				JFileChooser chooser = new JFileChooser();
			    //FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        //"JPG & GIF Images", "jpg", "gif");
			    //chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(null);
			    File inputFile = chooser.getSelectedFile();
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	if(inputFile.exists())
				    {
				    	File outputFile = new File("C:\\Apache\\htdocs\\bittorrent\\files\\"+stripExtension(inputFile.getName()));
				    	if (!outputFile.exists()) {
							outputFile.mkdir();
						}
				    	try {
							pieces = FileSplitter.split(inputFile.getParent()+"\\"+inputFile.getName(), "C:\\Apache\\htdocs\\bittorrent\\files\\"+stripExtension(inputFile.getName()+"\\"));
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						PrintWriter writer = null;
						try {
							writer = new PrintWriter("C:\\BitTorrent\\Torrents\\"+stripExtension(inputFile.getName())+".tor", "UTF-8");
						} catch (FileNotFoundException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						} catch (UnsupportedEncodingException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
					    writer.println("Name\t"+inputFile.getName());
					    Enumeration<?> e1 = null;
						try {
							e1 = NetworkInterface.getNetworkInterfaces();
						} catch (SocketException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
					    while(e1.hasMoreElements())
					    {
					        NetworkInterface n = (NetworkInterface) e1.nextElement();
					        Enumeration<?> ee = n.getInetAddresses();
					        while (ee.hasMoreElements())
					        {
					            InetAddress i = (InetAddress) ee.nextElement();
					            String ip = i.getHostAddress();
					            if(ip.substring(0,3).equals("25."))
					            {
					            	IP=ip;
					            }
					        }
					    }
					    writer.println("Tracker\t"+IP);
					    writer.println("File Size\t"+inputFile.length());
					    writer.println("Pieces\t"+pieces);
					   
					    writer.close();
					    @SuppressWarnings("unused")
						String text = txtText.getText();
						Home.getInstance().txtText.append("\n\nCreating Torrent for file: " + inputFile.getParent()+"\\"+inputFile.getName()+"\t\nFile Size: "+inputFile.length()+"\nPieces: "+pieces+"\nTracker: http://"+IP+"/");
				    }
			    }
			}
			
		});
		btnCreateNew.setText("Create New Torrent");
		btnCreateNew.setBounds(10, 0, 320, 75);
		
		Button btnOpenTorrent = new Button(composite, SWT.FLAT);
		btnOpenTorrent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String Name = null,Size = null,Pieces = null,Tracker = null;
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter("Advanced SE 6324 Bit Torrent File", "tor");
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(null);
			    File inputFile = chooser.getSelectedFile();
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	if(inputFile.exists())
				    {
			    		Home.getInstance().txtText.append("\n\nOpening Torrent: "+inputFile.getParent()+"\\"+inputFile.getName());
			    		Home.getInstance().txtText.append("\nTorrent Details: ");
			    		FileReader torrentFile = null;
						try {
							torrentFile = new FileReader(inputFile.getParent()+"\\"+inputFile.getName());
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			    		try {

						    BufferedReader reader = new BufferedReader(torrentFile);
						    String line = null;
						    String[] temp;
						    line=reader.readLine();
						    temp = line.split("\t");
						    if(temp[0].equals("Name"))
					        {
					        	Name = temp[1];
					        	Home.getInstance().txtText.append("\nName: "+Name);
					        }
						    while ((line = reader.readLine()) != null) {
						        temp = line.split("\t");
						        if(temp[0].equals("Name"))
						        {
						        	Name = temp[1];
						        	Home.getInstance().txtText.append("\nName: "+Name);
						        }
						        else if(temp[0].equals("Tracker"))
						        {
						        	Tracker = temp[1];
						        	Home.getInstance().txtText.append("\nTracker: "+Tracker);
						        }
						        else if(temp[0].equals("File Size"))
						        {
						        	Size = temp[1];
						        	Home.getInstance().txtText.append("\nFile Size: "+Size);
						        }
						        else if(temp[0].equals("Pieces"))
						        {
						        	Pieces = temp[1];
						        	Home.getInstance().txtText.append("\nNumber of Pieces: "+Pieces);
						        }
						        	
						        
						        
						    }
						} catch (IOException x) {
						    System.err.println(x);
						}		
			    		Thread client = new ClientService(Name,Tracker,Size,Pieces);
						client.start();	
				    }
				}
				
			}
		});
		btnOpenTorrent.setText("Open Torrent");
		btnOpenTorrent.setBounds(340, 0, 320, 75);
		
		Button btnExit = new Button(composite, SWT.FLAT);
		btnExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setText("Exit");
		btnExit.setBounds(670, 0, 320, 75);
		
		txtText = new Text(shlMaverickBitTorrent, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		txtText.setBounds(15, 85, 978, 414);

		Thread server = new ServerService();
		server.start();
		
	}
	   
	public static Home getInstance(){
        if(instance == null){
            instance = new Home();
        }
        return instance;
    }
	
	public static void main(String[] args) {
		try {
			
		    Display display = Display.getDefault();
			@SuppressWarnings("unused")
			Home window = Home.getInstance();
			
			shlMaverickBitTorrent.open();
			shlMaverickBitTorrent.layout();
			while (!shlMaverickBitTorrent.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	      
	}

	static String stripExtension (String str) {
        // Handle null case specially.

        if (str == null) return null;

        // Get position of last '.'.

        int pos = str.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.

        if (pos == -1) return str;

        // Otherwise return the string, up to the dot.

        return str.substring(0, pos);
    }

}
