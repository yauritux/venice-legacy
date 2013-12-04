package com.gdn.venice.logistics.dataexport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Log4jLoggerFactory;

/**
 * Email class for pickup reports.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class EmailSender {
	
	protected static Logger _log = null;
	private String notif="";
	
	public EmailSender(){
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.logistics.dataexport.EmailSender");
	}
	
	public EmailSender(String notification){
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.logistics.dataexport.EmailSender");
		notif = notification;
	}
	
	/**
	 * Sends files to the respective logistics providers based on the configuration in venice-logistics-mail-config.xml
	 * @throws IOException 
	 */
	@SuppressWarnings("rawtypes")
	public Boolean sendFiles() throws IOException {
		try {			
			// Read XML configuration file
			XMLConfiguration config = new XMLConfiguration(System.getenv("VENICE_HOME") + "/conf/venice-logistics-mail-config.xml");

			Object providers = config.getProperty("providers.provider.name");
			
			//Get the SMTP server configuration
			String smtpHost = config.getString("smtp.host");
			int smtpPort = config.getInt("smtp.port");
			String smtpUser = config.getString("smtp.user");
			String smtpPassword = config.getString("smtp.password");

			int totalProviders = ((List) providers).size();
			for (int i = 0; i < totalProviders; i++) {
				HierarchicalConfiguration subConfig = config.configurationAt("providers.provider(" + i + ")");

				// Find files with given pattern
				String filePath = subConfig.getString("mail-attachment-pattern");				
				String patternFileName = filePath.substring(filePath.lastIndexOf("/") + 1);
				String folderPath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
				File folder = new File(folderPath);
				File[] listOfFiles = folder.listFiles();
				
				//If there is only one entry it is the directory so exit
				if(!(listOfFiles.length > 1)){
					System.out.println("No more files to send...");
					return true;
				}
				
				for (int j = 0; j < listOfFiles.length; j++) {
					if (listOfFiles[j].isFile()) {
						String fileName = listOfFiles[j].getName();
						Pattern pattern = Pattern.compile("^" + patternFileName.replaceAll("\\*", "[\\\\s\\\\S]*") + "$");
						Matcher matcher = pattern.matcher(fileName);

						if (matcher.matches()) {
							String from = subConfig.getString("mail-from");
							String to = subConfig.getString("mail-to");
							
							@SuppressWarnings("unchecked")
							List<String> ccList = (List<String>)subConfig.getList("mail-cc-list.mail-cc");
							String subject = subConfig.getString("mail-subject") + ": " + fileName;
							subject = subject.replace("]", notif+"]");
							System.out.println("Mail subject:" + subject);
							String attachmentPath = folderPath + fileName;
							
							String contentFile =subConfig.getString("mail-content");							
							File contentFileInput = new File(System.getenv("VENICE_HOME") + "/conf/" + contentFile);							
							BufferedReader reader = new BufferedReader(new FileReader(contentFileInput));
							
							String htmlContent = "";
							String text = null;
							while((text = reader.readLine()) != null){
								htmlContent += text;
							}
							

							if (send(smtpHost, smtpPort, smtpUser, smtpPassword, from, to, ccList, subject, attachmentPath, htmlContent)) {
								System.out.println("Sending of file succeeded:" + fileName);
								File attachment = new File(attachmentPath);
								// Cek apakah folder untuk menaruh file yang sudah di email sudah ada atau belum
								// Kalau belum create
								File folderSentMail = new File(subConfig.getString("mail-attachment-sent-move-to-folder"));
								if (!folderSentMail.isFile()) {
									folderSentMail.mkdir();
								}

								attachment.renameTo(new File(subConfig.getString("mail-attachment-sent-move-to-folder") + fileName));
							} else {
								_log.error("Sending of file failed:" + fileName);
								return false;
							}
						}
					}
				}
			}
		} catch (ConfigurationException cex) {
			_log.error("A configuration exception occured:" + cex.getMessage());
			cex.printStackTrace();
		}
		return Boolean.TRUE;
	}

	/**
	 * Sends the email to the SMTP server
	 * @param smtpHost
	 * @param smtpPort
	 * @param smtpUser
	 * @param smtpPassword
	 * @param from
	 * @param to
	 * @param cc
	 * @param subject
	 * @param attachmentPath
	 * @param htmlContent
	 * @return
	 * @throws IOException 
	 */
	private boolean send(String smtpHost, int smtpPort, String smtpUser, String smtpPassword, String from, String to, List<String> ccList,
			String subject, String attachmentPath, String htmlContent) throws IOException {
		try {
			// Check whether file attachment exist or not
			File file = new File(attachmentPath);
			if (!file.exists())
				return false;

			/*
			 * Setup the properties
			 * 	o Must have authentication set on with the supported mechanisms
			 *  o Must provide an SMTP user and mail password
			 */
			Properties properties = new Properties();
			properties.setProperty("mail.smtp.auth", "true"); 
			properties.setProperty("mail.smtp.auth.mechanisms", "LOGIN PLAIN"); 
			properties.setProperty("mail.smtp.user", smtpUser); 
			properties.setProperty("mail.password", smtpPassword);
			Session session = Session.getDefaultInstance(properties);

			/*
			 *  Set mail header
			 *  	o Must have a TO and a CC so that GDN staff know what is sent and when
			 */
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			for(String cc:ccList){
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
			}
			message.setSubject(subject);

			// Text message
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(htmlContent, "text/html");

			// Attachment part
			BodyPart attachmentBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(attachmentPath){
				public String getContentType(){
					return "application/vnd.ms-excel";
				}
			};
			attachmentBodyPart.setDataHandler(new DataHandler(source));
			attachmentBodyPart.setFileName(source.getName());
			attachmentBodyPart.setDisposition(Part.ATTACHMENT);
			
//			InputStream is = new FileInputStream(file);
//			DataSource source = new ByteArrayDataSource(is, "application/vnd.ms-excel");
//			attachmentBodyPart.setDataHandler(new DataHandler(source));
//			attachmentBodyPart.setFileName(attachmentPath.substring(attachmentPath.lastIndexOf('/')+1, attachmentPath.length()));
//			attachmentBodyPart.setDisposition(Part.ATTACHMENT);

			Multipart multipart = new MimeMultipart();
			attachmentBodyPart.removeHeader("Content-Type");
			attachmentBodyPart.addHeader("Content-Type", "application/vnd.ms-excel; name=\"" + attachmentPath.substring(attachmentPath.lastIndexOf('/')+1, attachmentPath.length()) + "\"");
			multipart.addBodyPart(messageBodyPart);
			multipart.addBodyPart(attachmentBodyPart);

			message.setContent(multipart);
			
			/*
			 * Get a transport object and then connect it
			 */
			Transport transport = session.getTransport("smtp");
			transport.connect(smtpHost, smtpPort, smtpUser, smtpPassword);
			
			if(!transport.isConnected()){
				System.out.println("Transport cannot connect to email server!");
				return false;
			}
			
			transport.sendMessage(message, message.getAllRecipients());
			
		} catch (MessagingException mex) {
			mex.printStackTrace();
			return false;
		}

		return true;
	}
	
	/**
	 * Static main for testing
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		EmailSender es = new EmailSender();
		es.sendFiles();
	}
}
