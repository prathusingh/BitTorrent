package bittorrent.gui;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.io.*;
class ClientService extends Thread
{
	String Name = null,Size = null,Pieces = null,Tracker = null;
	ClientService(String fileName,String fileTracker,String fileSize,String filePieces)
	{
		Name = fileName;
		Tracker = fileTracker;
		Size = fileSize;
		Pieces = filePieces;
	}
	ClientService()
	{
	}
	public final static String[] CLIENT = {"25.24.147.71","25.53.65.86","25.15.74.80","25.24.105.194"};
	public void run()
	{
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter("C:\\BitTorrent\\Logs\\"+"client.log", true)));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] reply = {"NULL\tOffline","NULL\tOffline","NULL\tOffline","NULL\tOffline","NULL\tOffline"};
		String p = "";
		String IP="";
		for(int i=0;i<CLIENT.length;i++)
		{
			Enumeration<?> e11 = null;
			try {
				e11 = NetworkInterface.getNetworkInterfaces();
			} catch (SocketException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		    while(e11.hasMoreElements())
		    {
				NetworkInterface n = (NetworkInterface) e11.nextElement();
		        Enumeration<?> ee = n.getInetAddresses();
		        while (ee.hasMoreElements())
		        {
		            InetAddress i1 = (InetAddress) ee.nextElement();
		            String ip = i1.getHostAddress();
		            if(ip.substring(0,3).equals("25."))
		            {
		            	IP=ip;
		            }
		        }
		    }
		    if(!CLIENT[i].equals(IP))
		    {
				try {
					Process p1 = java.lang.Runtime.getRuntime().exec("ping -n 1 "+CLIENT[i]);
					int returnVal = 1;
					try {
						returnVal = p1.waitFor();
					} catch (InterruptedException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					
					boolean reachable = (returnVal==0);
					if(reachable)
					{
						Socket client = null;
						try {
							client = new Socket(CLIENT[i],1239);
						} catch(ConnectException ce)
						{
							ce.printStackTrace();
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						writer.println(new java.util.Date()+": Sending File ("+Name+") Request to: "+CLIENT[i]);
						writer.close();
						@SuppressWarnings("unused")
						BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
						PrintStream ps = null;
						try {
							ps = new PrintStream(client.getOutputStream());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						BufferedReader br = null;
						try {
							br = new BufferedReader(new InputStreamReader(client.getInputStream()));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							writer = new PrintWriter(new BufferedWriter(new FileWriter("C:\\BitTorrent\\Logs\\"+"client.log", true)));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						writer.println(new java.util.Date()+": Asking Information about File from ..."+CLIENT[i]);
						writer.close();
						String s="Requesting\t" + Name;
						ps.println(s);
						String st = null;
						try {
							st = br.readLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							writer = new PrintWriter(new BufferedWriter(new FileWriter("C:\\BitTorrent\\Logs\\"+"client.log", true)));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						writer.println(new java.util.Date()+": Response From "+CLIENT[i]+": "+st);
						try {
							client.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						reply[i]=CLIENT[i]+"\t"+st;
						writer.close();
					}
					else
						reply[i] = CLIENT[i]+"\tOffline";
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(reply[i]);
		    }
		}
		p = downloadStrategy(reply,Name,Pieces)+"\t";
		PrintWriter writer2 = null;
		try {
			writer2 = new PrintWriter(new BufferedWriter(new FileWriter("C:\\Apache\\htdocs\\bittorrent\\files\\"+stripExtension(Name)+"\\info.ase", true)));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		writer2.println(p);
		writer2.close();
	}
	
	public String downloadStrategy(String[] response, String fileName, String pieces)
	{
		HashMap<String,String> hm = new HashMap<String,String>();
		for(int i=0;i<5;i++)
		{
			String[] temp = response[i].split("\t");
			if(!temp[1].equals("Offline"))
			{
				if(temp[3].equals("Yes"))
				{
					for(int j=4;j<temp.length;j++)
					{
						if(hm.containsKey(temp[j]))
						{
							hm.put(temp[j],hm.get(temp[j])+"\t"+temp[0]);
						}
						else
							hm.put(temp[j],temp[0]);
					}
				}
			}
		}
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter("C:\\BitTorrent\\Logs\\"+"client.log", true)));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		writer.println("Downloading File: "+fileName+" with '"+pieces+"' pieces from the following seeds;");
		String baseURL = "/bittorrent/files/"+stripExtension(fileName)+"/";
		String URL = "";
		Iterator<Entry<String, String>> it = hm.entrySet().iterator();
		
		String p = "";
		ArrayList<String> temp = new ArrayList<String>();
	    while (it.hasNext()) {
	        @SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry)it.next();
	        String[] t = ((String) pairs.getValue()).split("\t");
	        Random rand = new Random();  
	        int c = rand.nextInt(t.length);
	        URL = "http://"+t[c]+baseURL+fileName+"."+pairs.getKey();
	        writer.println("\t"+URL);
	        temp.add(URL);
//	        try {
//				download(URL,"C:\\Apache\\htdocs\\bittorrent\\files\\"+stripExtension(Name)+"\\");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				
//			}
	        p += pairs.getKey()+"\t";
	    }
	    int i=0;
	    Thread[] download = new Thread[5];
    	download[0]=null;
    	download[1]=null;
    	download[2]=null;
    	download[3]=null;
    	download[4]=null;
	    while(i<temp.size())
	    {
	    	
	    	for(int l=0;l<5;l++)
	    	{
	    		if(download[l] == null || !download[l].isAlive())
	    		{
	    			download[l] = new DownloadPiece(temp.get(i),"C:\\Apache\\htdocs\\bittorrent\\files\\"+stripExtension(Name)+"\\");
	        		download[l].start();
	        		i++;
	    		}
	    	}
	    }
	    writer.close();
	    try {
			FileSplitter.join(Name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return p;
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
	
//	private void download(String fileURL, String destinationDirectory) throws IOException {
//        // File name that is being downloaded
//        String downloadedFileName = fileURL.substring(fileURL.lastIndexOf("/")+1);
//        File downloadsDir = new File(destinationDirectory);
//	    if (!downloadsDir.exists()) {
//	    	downloadsDir.mkdirs();
//	    }
//        // Open connection to the file
//        URL url = new URL(fileURL);
//        InputStream is = url.openStream();
//        // Stream to the destination file
//        FileOutputStream fos = new FileOutputStream(destinationDirectory+ downloadedFileName);
//  
//        // Read bytes from URL to the local file
//        byte[] buffer = new byte[1048576];
//        int bytesRead = 0;
//         
//        System.out.print("Downloading " + downloadedFileName);
//        while ((bytesRead = is.read(buffer)) != -1) {
//            System.out.print(".");  // Progress bar :)
//            fos.write(buffer,0,bytesRead);
//        }
//        System.out.println("done!");
//        
//        // Close destination stream
//        fos.close();
//        // Close URL stream
//        is.close();
//        
//    } 
}
