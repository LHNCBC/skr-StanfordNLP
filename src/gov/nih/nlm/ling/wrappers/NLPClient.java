package gov.nih.nlm.ling.wrappers;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import gov.nih.nlm.ling.util.FileUtils;

public class NLPClient {
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
    private static Logger log = Logger.getLogger(NLPClient.class.getName());

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
	    NLPClient client = new NLPClient();
	    String serverName = args[0];
	    int port = Integer.parseInt(args[1]);
	    // Socket socket = new Socket(serverName, port);
	    String in = args[2];
	    String out = args[3];
	    String repr = args[4];
	    File inFile = new File(in);
	    File outFile = new File(args[3]);
	    boolean reprocess = Boolean.parseBoolean(repr);
	    // String line = null;
	    // String inFile = new String("C:\\Factuality_data\\testTxtDir\\17288645.txt");
	    // File inFile = new File("C:\\Factuality_data\\textTxtDir\\17288645.txt");
	    // log.log(Level.INFO, "Processing file with CoreNLP: {0}.", new Object[] { file.getAbsolutePath() });
	    // String text = FileUtils.stringFromFileWithBytes(inFile, "UTF-8");
	    // text = text + "\nEOPF";

	    /*
	     * String input = "PMID- 16950600\n" +
	     * "TI  - Fundamental frequency change during offset and onset of voicing in individuals with Parkinson disease.\n"
	     * +
	     * "SUMMARY: After years of treatment with the medication levodopa, most individuals with Parkinson disease (PD) experience fluctuations in response to their medications. Although relatively consistent perceptual voice improvements have been documented to correspond with these fluctuations, consistent quantitative data to support this finding are lacking. This mismatch may have occurred because most of this phonation research has centered on long-term phonatory measures (ie, across speaking samples and prolonged vowel tasks). The current study examined short-term phonatory behavior in individuals with PD, specifically examining fundamental frequency (F0) at the offset and onset of phonation, before and after a voiceless consonant. The F0 analysis at phonatory offset supported the conclusion that individuals with PD have difficulty with the rapid offset of voicing, and that they are stopping vocal fold vibration primarily through vocal fold abduction (without adding tension). The F0 analysis at phonatory onset revealed that all groups use some laryngeal tension at the initiation of voicing. The tension was lowest for the PD participants who were in their OFF medication state, and it was highest for the age-matched control participants and the PD participants in their ON medication states.\n"
	     * + "EOPF";
	     */
	    //  String answer = client.queryServer(s, text);
	    // System.out.println(answer);
	    long startTime = System.currentTimeMillis();
	    int fileCnt = 0;

	    if (inFile.isDirectory()) {
		List<String> files = FileUtils.listFiles(in, false, "txt");
		int fileNum = 0;
		for (String f : files) {
		    Socket socket = new Socket(serverName, port);
		    String id = f.substring(f.lastIndexOf(File.separator) + 1).replace(".txt", "");
		    log.log(Level.INFO, "Processing {0}: {1}.", new Object[] { id, ++fileNum });
		    String outfile = outFile.getAbsolutePath() + File.separator + id + ".xml";
		    if (!reprocess && new File(outfile).exists()) {
			log.log(Level.INFO, "Output XML file exists: {0}. Skipping..", outfile);
		    }
		    // CoreNLPWrapper.getInstance().coreNLP(f, outfile);
		    String text = FileUtils.stringFromFileWithBytes(f, "UTF-8");
		    text = text + "\nEOPF";
		    String answer = client.queryServer(socket, text);

		    PrintWriter pw = new PrintWriter(new File(outfile));
		    pw.println(answer);
		    pw.close();
		    fileCnt++;

		}
	    }
	    long estimatedTime = System.currentTimeMillis() - startTime;
	    System.out.println("Elapsed time for " + fileCnt + " files: " + estimatedTime + " milisec.");
	    // }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
