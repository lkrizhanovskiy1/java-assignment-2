package try2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ThreadedServer {
	ServerSocket myServerSocket;
	boolean ServerOn = true;

	public ThreadedServer() throws IOException {

		File file = new File("theFile.txt");
		if (!file.exists()){
			file.createNewFile();
			System.out.println("file Created");
		}

		try {
			myServerSocket = new ServerSocket(8000);
			//System.out.println( "listening for a connection..." );

		} catch (IOException ioe) 
		{ 
			System.out.println("Could not create server socket on post. Quitting."); 
		} 
		while (ServerOn) {
			try {
				//System.out.println( "listening for a connection AGAIN..." );
				Socket clientSocket = myServerSocket.accept();
				ThreadItself cliThread = new ThreadItself(clientSocket, file);
				cliThread.start();
			}catch(IOException ioe) 
			{ 
				System.out.println("Exception encountered on accept. Ignoring. Stack Trace :"); 
			}
		}
		try
		{ 
			myServerSocket.close(); 
			System.out.println("Server Stopped"); 
		} 
		catch(Exception ioe) 
		{ 
			System.out.println("Problem stopping server socket"); 
			System.exit(-1); 
		} 	

	}

	public static void main (String[] args) throws IOException {
		new ThreadedServer();
	}

	class ThreadItself extends Thread {
		Socket myClientSocket;
		boolean m_bRunThread = true;
		File file=null;
		PrintWriter writer=null;


		FileReader fr=null;
		BufferedReader br=null;
		int curLine=1;
		int wordPos=0;
		int beginI=0;
		int endI=0;
		int lineCount=0;
		String searchWord;
		String lineLC=null;
		String wordLC=null;
		String curLineR=null;
		String tosend=null;
		String subStr1=null;
		String subStr2=null;
		char[] charArr1=null;
		char[] charArr2=null;
		String [] lines;

		public ThreadItself () {
			super();
		}
		ThreadItself(Socket c, File f) {
			myClientSocket = c;
			file=f;
		}

		public void run () {
			DataInputStream in = null; 
			DataOutputStream out = null; 
			System.out.println("Accepted Client");
			try {
				in=new DataInputStream(myClientSocket.getInputStream());
				out=new DataOutputStream(myClientSocket.getOutputStream());
				while(m_bRunThread) {
					String str=in.readUTF();
					if (!ServerOn) {
						System.out.println("Server already closed");
						out.flush();
						m_bRunThread=false;
					}
					if (str.startsWith("textAr")) {

						System.out.println("text Area accepted by server");

						str=str.substring(6);
						try {
							writer = new PrintWriter(file.getAbsoluteFile());
						} catch (FileNotFoundException e) {
							System.out.println("Error with printwriter");
						}

						writer.print("");
						writer.print(str);
						writer.close();
					}
					if (str.startsWith("textFld")) {
						System.out.println(str);
						str=str.substring(7);
						System.out.println(str);
						try {
							fr= new FileReader(file);
						} catch (FileNotFoundException e){
							System.out.println("file not found");
						}
						br=new BufferedReader(fr);

						/*_____*/
						str = " "+str+" ";
						

						try {

							while((curLineR=br.readLine())!=null) {
								lineLC=curLineR.toLowerCase();
								curLine++;

								if(lineLC.contains(str)) {

									wordPos=lineLC.indexOf(str);
									subStr1=lineLC.substring(0, wordPos);
									subStr2=lineLC.substring(wordPos, lineLC.length()-1);
									charArr1=subStr1.toCharArray();
									charArr2=subStr2.toCharArray();
									int x=subStr1.length()-1;
									while (charArr1[x]!='.' && x>0) 
										x--;
									subStr1=subStr1.substring(x, wordPos-1);

									x=0;
									while (charArr2[x]!='.' && x!=subStr2.length()-1) 
										x++;
									subStr2=subStr2.substring(0, x);
									tosend=subStr1+subStr2;
									tosend=tosend.substring(2);
									/*
									wordPos = lineLC.lastIndexOf(str);
									beginI=wordPos;
									if(beginI<10)
										beginI=0;
									else 
										beginI=-10;
									endI=wordPos;
									if(endI>(lineLC.length()-str.length()-10))
										endI=lineLC.length();
									else 
										endI+=str.length()+10; 



									tosend=curLineR.substring(beginI, wordPos) +
											str.toUpperCase();*/
									//System.out.println("sending string TOSEND");
									out.writeUTF(tosend);
									//System.out.println(tosend);

								}

							}
						}catch (IOException e)  {
							System.out.println("Error searching");

						}
					}

				}


			}
			catch (Exception e) 
			{ 
				e.printStackTrace(); 
			} 
			finally
			{ 
				// Clean up 
				try
				{                    
					in.close(); 
					out.close(); 
					myClientSocket.close(); 
					System.out.println("...Stopped"); 
				} 
				catch(IOException ioe) 
				{ 
					ioe.printStackTrace(); 
				} 

			}

		}

	}
}