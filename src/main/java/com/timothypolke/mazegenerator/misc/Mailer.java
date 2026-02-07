package com.timothypolke.mazegenerator.misc;

import java.util.Properties;
import java.util.ArrayList;
import java.util.Base64;
import java.io.IOException;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class Mailer{
    private String username;
	private String password;
	private String host;
	private String port;
	private ArrayList<String> to;
	private String subject;
	private String content;
	private byte[] puzzleAttachments;
	private byte[] solutionAttachments;
	
	public Mailer(String username, String password, String host, String port, ArrayList<String> to, String subject, String content, byte[] puzzleAttachments, byte[] solutionAttachments){
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
		this.to = to;
		this.subject = subject;
		this.content = content;
		this.puzzleAttachments = puzzleAttachments;
		this.solutionAttachments = solutionAttachments;
	}
	
	public void send(){
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.ssl.enable", "true");
		props.setProperty("mail.debug", "true");
		props.setProperty("mail.smtp.host", host);
		props.setProperty("mail.smtp.port", port);

		Session session = Session.getInstance(props, new jakarta.mail.Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication(){
				return new PasswordAuthentication(username, password);
			}
		});
		
		Message message;
		Multipart multipart = new MimeMultipart();
		BodyPart mimebodypart = new MimeBodyPart();
		MimeBodyPart [] mimebodyparts = new MimeBodyPart[2];
		DataSource [] datasource = new ByteArrayDataSource[mimebodyparts.length];
		String [] filenames = new String[datasource.length];
		filenames[0] = "Puzzles.zip";
		filenames[1] = "Solutions.zip";
		datasource[0] = new ByteArrayDataSource(puzzleAttachments, "application/x-7z-compressed");
		datasource[1] = new ByteArrayDataSource(solutionAttachments, "application/x-7z-compressed");

		try {
            for (String s : to) {
                message = new MimeMessage(session);

                message.setFrom(new InternetAddress(username));

                message.setSubject(subject);
                mimebodypart.setText(content);
                multipart.addBodyPart(mimebodypart);

                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(s));

                for (int i = 0; i < 2; i++) {
                    mimebodyparts[i] = new MimeBodyPart();
                    mimebodyparts[i].setDataHandler(new DataHandler(datasource[i]));
                    mimebodyparts[i].setFileName(filenames[i]);
                    multipart.addBodyPart(mimebodyparts[i]);
                }

                message.setContent(multipart);

                Transport.send(message);
            }
		} 
		catch (MessagingException e){
			throw new RuntimeException(e);
		}
	}
}