package com.Gmail.Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.testng.internal.ConfigurationGroupMethods;

import com.Gmail.Support.HTMLReportSupport;
import com.Gmail.reports.HTMLReporting;
import com.Gmail.accelerators.TestEngine;
import com.Gmail.Support.ConfiguratorSupport;
import com.Gmail.Support.HTMLReportSupport;

@SuppressWarnings("all")
public class SendMail extends HTMLReportSupport
{
	static ConfiguratorSupport config = new ConfiguratorSupport("config.properties");

	public static void attachReportsToEmail() throws Exception {

		String strBrowser = config.getProperty("browserType");
		String strReportsFolder = "";

		if (strBrowser.equalsIgnoreCase("ie")) 
		{
			strReportsFolder = "IE";

		} 
		else if (strBrowser.equalsIgnoreCase("Chrome")) 
		{
			strReportsFolder = "Chrome";

		}

		String str = TestEngine.filePath();
		String strZipFilePath = "Results\\Report.Zip";	
		
		String toAddress = config.getProperty("to");
		String ccAddress = config.getProperty("cc");
		String bccAddress = config.getProperty("bcc");
		
		String[] to = null, cc = null, bcc = null;
		
		if (!toAddress.isEmpty()) {
			to = toAddress.split(",");	
		}
		
		if (!ccAddress.isEmpty()) {
			cc =ccAddress.split(",");
		}
		
		if (!bccAddress.isEmpty()) {
			bcc =bccAddress.split(",");
		}
		
		
		String subject = config.getProperty("EmailSubject");
	
		String emailBodyMessage = config.getProperty("BodyMessage");
		String footer = config.getProperty("BodyFooterMessage");
		
		sendMail(config.getProperty("MailUserName"), config.getProperty("MailpassWord"),
                "outlook.standard.com",
                        "587",
                        "true", "true", true, "javax.net.ssl.SSLSocketFactory", "true", to, cc, bcc, subject , emailBodyMessage ,footer,
                        strZipFilePath ,
                        "Report.Zip");
		
		
	}

	public static boolean sendMail(final String userName, String passWord,
			String host, String port, String starttls, String auth,
			boolean debug, String socketFactoryClass, String fallback,
			String[] to, String[] cc, String[] bcc, String subject,
			String text, String footer, String attachmentPath, String attachmentName) {

		//		String strReportsFolder = "Firefox";
		/**/

		Properties props = new Properties();

		props.put("mail.smtp.user", userName);

		props.put("mail.smtp.host", host);

		if (!"".equals(port))

			props.put("mail.smtp.port", port);

		if (!"".equals(starttls))

			props.put("mail.smtp.starttls.enable", starttls);

		props.put("mail.smtp.auth", auth);


		if (debug) {

			props.put("mail.smtp.debug", "true");

		} else {

			props.put("mail.smtp.debug", "false");

		}


		if (!"".equals(port))

			props.put("mail.smtp.socketFactory.port", port);

		if (!"".equals(socketFactoryClass))

			props.put("mail.smtp.socketFactory.class", socketFactoryClass);

		if (!"".equals(fallback))

			props.put("mail.smtp.socketFactory.fallback", fallback);

		props.put("mail.smtp.ssl.trust", "outlook.standard.com");
		props.put("mail.mime.address.strict", "false");
		

		try

		{

			Session session = Session.getDefaultInstance(props, null);
			

			session.setDebug(debug);

			MimeMessage msg = new MimeMessage(session);

			msg.setSubject(subject);

			MimeBodyPart messageBodyPart1 = new MimeBodyPart();
			//StringBuffer table3 = callhtml();
			//messageBodyPart1.setContent(table3.toString(), "text/html");

			Multipart multipart = new MimeMultipart();
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			
			messageBodyPart.setContent(text, "text/html; charset=utf-8");
			messageBodyPart1.setContent(footer, "text/html; charset=utf-8");
			
			DataSource source = new FileDataSource(attachmentPath);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(attachmentName);
			multipart.addBodyPart(messageBodyPart);
			multipart.addBodyPart(messageBodyPart1);
			msg.setContent(multipart);
			
			
			msg.setFrom(new InternetAddress(config.getProperty("FromAddresses")));
			

			if (to != null) {
				try {
					for (int i = 0; i < to.length; i++) {

						msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
								to[i]));

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if (cc != null) {
				try {
					for (int i = 0; i < cc.length; i++) {

						msg.addRecipient(Message.RecipientType.CC, new InternetAddress(
								cc[i]));

					}
				} catch (Exception e) {
				e.printStackTrace();	
				}
			}
			
			if (bcc != null) {
				try {
					for (int i = 0; i < bcc.length; i++) {

						msg.addRecipient(Message.RecipientType.BCC,
								new InternetAddress(bcc[i]));

					}

				} catch (Exception e) {
					e.printStackTrace();	
				}
			}



			msg.saveChanges();
			
			Transport transport = session.getTransport("smtp");
			
			transport.connect(host, userName, passWord);
			
			transport.sendMessage(msg, msg.getAllRecipients());

			transport.close();

			return true;
			
		}

		catch (Exception mex)

		{

			mex.printStackTrace();

			return false;

		}

	}

	/*private static StringBuffer callhtml() throws UnknownHostException

	{

		StringBuffer str = new StringBuffer(
				"<TABLE border=1 align='center' cellSpacing=1 cellPadding=1 width='35%' font='arial'>"
						+ "<tr><td width=150 align='center' bgcolor='#153E7E'><FONT COLOR='#E0E0E0' FACE='Arial' SIZE=1.85><b>Build Version</b></td>"
						+ "<td width=150 align='left' color='#153E7E'><FONT FACE='Arial' SIZE=1.85><b>"
						+ config.getProperty("BuildVersion")
						+ "</b></td></tr>"
						+ "<tr><td width=150 align='left' bgcolor='#153E7E'><FONT COLOR='#E0E0E0' FACE='Arial' SIZE=1.85><b>Run ID</b></td>"
						+ "<td width=150 align='left' color='#153E7E'><FONT FACE='Arial' SIZE=1.85><b>"
						+ config.getProperty("RunID")
						+ "</b></td></tr>"
						+ "<tr><td width=150 align='left' bgcolor='#153E7E'><FONT COLOR='#E0E0E0' FACE='Arial' SIZE=1.85><b>Run Date&Time</b></td>"
						+ "<td width=150 align='left' color='#153E7E'><FONT FACE='Arial' SIZE=1.85><b>"
						+ ReportStampSupport.timeStamp()
						+ "</b></td></tr>"
						+ "</table><hr/>"
						+ "<DIV style='padding:20px;margin:5px;'>"
						+ "<DIV style='float:left'>"
						+ "<DIV>"
						+ "<TABLE border=1 cellSpacing=1 cellPadding=1 width='100%' font='arial'>"
						+ "<tr><td colspan='2' align='center'><FONT COLOR='#153E7E' FACE='Arial' SIZE=2><b>Environment</b></td></tr>"
						+ "<tr><td width=150 align='left' bgcolor='#153E7E'><FONT COLOR='#E0E0E0' FACE='Arial' SIZE=1.85><b>Host Name</b></td>"
						+ "<td width=150 align='left' color='#153E7E'><FONT FACE='Arial' SIZE=1.85><b>"
						+ ReportStampSupport.getHostName()
						+ "</b></td></tr>"
						+ "<tr><td width=150 align='left' bgcolor='#153E7E'><FONT COLOR='#E0E0E0' FACE='Arial' SIZE=1.85><b>OS Name</b></td>"
						+ "<td width=150 align='left' color='#153E7E'><FONT FACE='Arial' SIZE=1.85><b>"
						+ System.getProperty("os.name")
						+ "</b></td></tr>"
						+ "<tr><td width=150 align='left' bgcolor='#153E7E'><FONT COLOR='#E0E0E0' FACE='Arial' SIZE=1.85><b>OS Version</b></td>"
						+ "<td width=150 align='left' color='#153E7E'><FONT FACE='Arial' SIZE=1.85><b>"
						+ System.getProperty("os.version")
						+ "</b></td></tr>"
						+ "</TABLE>"
						+ "</DIV></br>"
						+ "<DIV>"
						+ "<TABLE border=1 cellSpacing=1 cellPadding=1 width='100%' font='arial'>"
						+ "<TR><TD colspan='2' align='center'><FONT COLOR='#153E7E' FACE='Arial' SIZE=2><b>Execution Status</b></TD></TR>"
						+ "<tr>"
						+ "<tr><td width=150 align='left' bgcolor='#153E7E'><FONT COLOR='#E0E0E0' FACE='Arial' SIZE=1.85><b>Suite</b></td>"
						+ "<td width=150 align='left'><FONT FACE='Arial' SIZE=1.85><b>"
						+ currentSuit
						+ "</b></td></tr>"
						+ "<tr><td width=150 align='left'bgcolor='#153E7E' ><FONT COLOR='#E0E0E0' FACE='Arial' SIZE=1.85><b>No. of  Scripts Executed</b></td>"
						+ "<td width=150 align='left' color='#153E7E'><FONT FACE='Arial' SIZE=1.85><b>"
						+ map.size()
						+ "</b></td></tr>"
						+ "<tr>"
						+ "<tr><td width=150 align='left' bgcolor='#153E7E'><FONT COLOR='#E0E0E0' FACE='Arial' SIZE=1.85><b>No. of  Scripts Passed</b></td>"
						+ "<td width=150 align='left' color='#E0E0E0'><FONT FACE='Arial' SIZE=1.85><b>"
						+ HtmlReportSupport.pCount
						+ "</b></td></tr>"
						+ "<tr>"
						+ "<tr><td width=150 align='left'bgcolor='#153E7E'><FONT COLOR='#E0E0E0' FACE='Arial' SIZE=1.85><b>No. of  Scripts Failed</b></td>"
						+ "<td width=150 align='left'color='red' ><FONT FACE='Arial' SIZE=1.85><b>"
						+ HtmlReportSupport.fCount
						+ "</b></td></tr>"
						+ "<TR><td width=150 align='left'bgcolor='#153E7E'><FONT COLOR='#E0E0E0' FACE='Arial' SIZE=1.85><b>Suite Execution Time</b></td>"
						+ "<TD width=150 align='left'color='red' ><FONT FACE='Arial' SIZE=1.85><b>"
						+ iSuiteExecutionTime
						+ " secs"
						+ "</b></TD></TR>"
						+ "</TR></TABLE></DIV></br>"
						+ "<DIV><TABLE border=1 cellSpacing=1 cellPadding=1 width='200%' font='arial'>"
						+ "<tr><td colspan='3' align='center'><FONT COLOR='#153E7E' FACE='Arial' SIZE=2><b>Summary Report</b></td></tr>"
						+ "<tr><td align='center'><FONT COLOR='#153E7E' FACE='Arial' SIZE=2.25><b>Module</b></td>"
						+ "<td align='center'><FONT COLOR='#153E7E' FACE='Arial' SIZE=2.25><b>Test Case</b></td>"
						+ "<td align='center'><FONT COLOR='#153E7E' FACE='Arial' SIZE=2.25><b>Status</b></td></tr>");

		Iterator<Entry<String, String>> iterator1 = map.entrySet().iterator();

		while (iterator1.hasNext()) {

			@SuppressWarnings("rawtypes")
			Map.Entry mapEntry1 = (Map.Entry) iterator1.next();
			key = mapEntry1.getKey().toString().split(":");
			String value = (String) mapEntry1.getValue();
			str.append("<tr>" + "<td><FONT color=#153e7e size=1 face=Arial><B>"
					+ key[0] + "</B></td>"
					+ "<td><FONT color=#153e7e size=1 face=Arial><B>" + key[1]
					+ "</B></td>");

			if (value.equals("PASS")) {
				str.append("<TD width='30%' bgcolor=green align=center><FONT color=white size=1 face=Arial>"
						+ value + "</td>");
			} else {
				str.append("<TD width='30%' bgcolor=red align=center><FONT color=white size=1 face=Arial>"
						+ value + "</td>");
			}

			str.append("</tr>");
		}
		str.append("</table></DIV></DIV>");
		return str;

	}

	public static String readEMail() {

		return currentSuit;

	}*/

	public static void copyTestFolder(File src, File dest)
	    	throws IOException{
	    	
		//if directory not exists, create it
		if(!dest.exists()){
		   dest.mkdir();
		   System.out.println("Directory copied from " 
                          + src + "  to " + dest);
		}
	    	if(!dest.isDirectory()){
	    		
	    		
	    		
	    		//list all the directory contents
	    		String files[] = src.list();
	    		
	    		for (String file : files) {
	    		   //construct the src and dest file structure
	    		   File srcFile = new File(src, file);
	    		   File destFile = new File(dest, file);
	    		   //recursive copy
	    		   copyFolder(srcFile,destFile);
	    		}
	    	   
	    	}else{
	    		//if file, then copy it
	    		//Use bytes stream to support all file types
	    		InputStream in = new FileInputStream(src);
	    	        OutputStream out = new FileOutputStream(dest); 
	    	                     
	    	        byte[] buffer = new byte[1024];
	    	    
	    	        int length;
	    	        //copy the file content in bytes 
	    	        while ((length = in.read(buffer)) > 0){
	    	    	   out.write(buffer, 0, length);
	    	        }
	 
	    	        in.close();
	    	        out.close();
	    	        System.out.println("File copied from " + src + " to " + dest);
	    	}
	    }
	public static void main(String[] args) throws Exception
	{
		String to[] = new String [1];
		to[0] = "vinayak.patil@in.ibm.com";
		String cc[] = new String [1];
		cc[0] = "vinayak.patil@in.ibm.com";
		String bcc[] = new String [1];
		bcc[0] = "vinayak.patil@in.ibm.com";
		
		sendMail(config.getProperty("MailUserName"), config.getProperty("MailpassWord"),
                "outlook.standard.com",
                        "587",
                        "true", "true", true, "javax.net.ssl.SSLSocketFactory", "true", to,cc, bcc, "Test REport" , "Test Email" ,"Test Email",
                        "C:\\Selenium\\FinalCopy\\GOLD\\Report.zip" ,
                        "Report.Zip");
		
		/*attachReportsToEmail();
		
	
		

		String desttoShare = configProps.getProperty("sharedPath").trim()+"\\ScreenShots"+".Zip";		
		//zipresultpath = new File(desttoShare).getCanonicalPath();
		
		
		File srcFolder = new File(configProps.getProperty("toCopyScreens").trim()+"\\ScreenShots"+".Zip");
    	File destFolder = new File(new File(desttoShare).getCanonicalPath());
    	
   	
    	TestEngine.copyFileUsingStream(srcFolder, destFolder);	
*/ 	
		
		  
	    
	}

}
