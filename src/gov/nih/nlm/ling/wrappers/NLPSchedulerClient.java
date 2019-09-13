package gov.nih.nlm.ling.wrappers;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Logger;

public class NLPSchedulerClient {
    /*- private String queryServer(Socket socket,String input) {
    	 StringBuilder sb = new StringBuilder();  
    	try {
    		// write text to the socket
    		DataInputStream bis = new DataInputStream(socket.getInputStream());
    	BufferedReader br = new BufferedReader(new InputStreamReader(bis));
     	PrintWriter bw = new PrintWriter(socket.getOutputStream(), true);
    	bw.println(input);
    	bw.flush();
    	String output= br.readLine();
    	bis.close();
    	br.close();
    	return output;
    
    	} catch (IOException ioe) {
    		System.err.println("Socket error");
    	}
          return sb.toString();
    } */
    private static Logger log = Logger.getLogger(NLPSchedulerClient.class.getName());

    public static String queryServer(Socket socket, String input) {
	StringBuilder sb = new StringBuilder();
	try {
	    // write text to the socket
	    DataInputStream bis = new DataInputStream(socket.getInputStream());
	    BufferedReader br = new BufferedReader(new InputStreamReader(bis));
	    PrintWriter bw = new PrintWriter(socket.getOutputStream(), true);
	    bw.println(input);
	    bw.flush();
	    String line = null;
	    do {
		line = br.readLine();
		sb.append(line);
	    } while (line != null && !line.trim().endsWith("</document>"));
	    bis.close();
	    br.close();
	} catch (IOException ioe) {
	    // log.warning("Socket I/O error: " + socket.getInetAddress().getHostName() + ":" + socket.getPort());
	    ioe.printStackTrace();
	}
	return sb.toString();
    }

    public static void main(String args[]) throws IOException {
	try {
	    NLPSchedulerClient client = new NLPSchedulerClient();
	    NLPSchedulerClient ssc = new NLPSchedulerClient();
	    InputStream is = ssc.getClass().getClassLoader().getResourceAsStream("factuality_semrep.properties");
	    Properties prop = new Properties();
	    if (is != null)
		prop.load(is);
	    else
		System.out.println("Input Stream is null!");
	    String serverName = prop.getProperty("NLP_SERVER_HOSTS");
	    int port = Integer.parseInt(prop.getProperty("NLP_SERVER_PORT"));
	    System.out.println("calling - " + serverName + ":" + port);
	    System.out.println("args[0] = " + args[0]);
	    System.out.println("args[1] = " + args[1]);
	    System.out.println("args[2] = " + args[2]);
	    // String serverName = "indsrv2";
	    // int port = Integer.parseInt(args[1]);
	    // int port = 22222;
	    Socket socket = new Socket(serverName, port);

	    /*- Scanner scanner = new Scanner(System.in);
	    StringBuffer sb = new StringBuffer();
	    String myString = null;
	    while (scanner.hasNextLine()) {
	    myString = scanner.nextLine();
	    sb.append(myString + "\n");
	    } */
	    String line = null;
	    StringBuffer sb = new StringBuffer();
	    BufferedReader bf = new BufferedReader(new FileReader(args[1]));
	    while ((line = bf.readLine()) != null) {
		sb = sb.append(line + "\n");
	    }

	    // String text = FileUtils.stringFromFileWithBytes(args[0], "ASCII");
	    // String text = new String("Aspirin helps to kill cancer tumors.\n");
	    String text = sb.toString() + "EOPF";
	    // System.out.println(text);

	    //  Socket socket = new Socket(serverName, port);

	    // CoreNLPWrapper.getInstance().coreNLP(f, outfile);
	    // String text = sb.toString();
	    // text = text + "\nEOPF";
	    String answer = client.queryServer(socket, text);

	    // PrintWriter pw = new PrintWriter(System.out);
	    PrintWriter pw = new PrintWriter(args[2]);
	    pw.println(answer);
	    pw.println("<<< EOT >>>");
	    pw.close();
	    // }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
