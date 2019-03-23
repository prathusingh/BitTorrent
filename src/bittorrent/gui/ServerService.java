package bittorrent.gui;

import java.net.*;
import java.io.*;
class ServerService extends Thread
{
	@SuppressWarnings("resource")
	public void run()
	{
		ServerSocket server = null;
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter("C:\\BitTorrent\\Logs\\"+"server.log", true)));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			server = new ServerSocket(1239);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true)
		{
			writer.println(new java.util.Date()+": Server Initialized");
			writer.close();
			
			
			Socket client = null;
			try {
				client = server.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				writer = new PrintWriter(new BufferedWriter(new FileWriter("C:\\BitTorrent\\Logs\\"+"server.log", true)));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			writer.println(new java.util.Date()+": Connected with: "+client.getInetAddress());
			writer.close();
	
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			String st = null;
			try {
				st = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PrintStream ps = null;
			try {
				ps = new PrintStream(client.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				writer = new PrintWriter(new BufferedWriter(new FileWriter("C:\\BitTorrent\\Logs\\"+"server.log", true)));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			writer.println("Received Message from: "+client.getInetAddress()+" : '"+st+"'");
			writer.close();
			String[] temp = st.split("\t");
			String pieceInfo = null;
			if(temp[0].equals("Requesting"));
			{
				File f = new File("C:\\Apache\\htdocs\\bittorrent\\files\\"+stripExtension(temp[1]));
				if(f.isDirectory())
				{
					FileReader pieceInfoFile = null;
					try {
						pieceInfoFile = new FileReader("C:\\Apache\\htdocs\\bittorrent\\files\\"+stripExtension(temp[1])+"\\info.ase");
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    		try {

					    BufferedReader reader = new BufferedReader(pieceInfoFile);
					    pieceInfo=reader.readLine();
		    		}
		    		catch (IOException x) {
					    System.err.println(x);
					}		
		    		ps.println("FileRequestResponse\t" + temp[1] + "\tYes\t"+pieceInfo);
		    		try {
						pieceInfoFile.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
					ps.println("FileRequestResponse\t" + temp[1] + "\tNo");
			}
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
