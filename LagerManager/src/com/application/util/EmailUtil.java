package com.application.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

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

import org.apache.log4j.Logger;

import com.application.Main;
import com.application.db.util.DAOException;
import com.application.view.ArtikelDataController;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class EmailUtil {

	private static final Logger logger = Logger.getLogger(EmailUtil.class);

	/**
	 * Utility method to send simple HTML email
	 * 
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	public static void sendEmail(Session session, String from, String toEmail, String subject, String body, File file,
			String ccEmail, Stage dialogStage) {
		try {
			MimeMessage msg = new MimeMessage(session);
			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress(from, from));

			// msg.setReplyTo(InternetAddress.parse("no_reply@journaldev.com", false));

			msg.setSubject(subject, "UTF-8");

			msg.setText(body, "UTF-8");

			msg.setSentDate(new Date());

			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			if (!ccEmail.isEmpty())
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(ccEmail));

			// msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail,
			// false));
			// msg.addRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail,
			// false));

			// msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail,
			// false));
			// msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail,
			// false));

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setText(body);

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			String filename = file.getAbsolutePath();
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(file.getName());
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			msg.setContent(multipart);

			logger.info("Nachricht ist bereit");
			Transport.send(msg);

			Alert alert = new Alert(AlertType.INFORMATION, "Mail wurde erfolgreich gesendet");
			alert.initOwner(dialogStage);
			alert.showAndWait();
			logger.info("EMail wurde erfolgreich versendet");
		} catch (Exception e) {
			showExceptionAlertDialog(e);

			e.printStackTrace();
		}
	}

	

	private static void showExceptionAlertDialog(Exception e) {

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception Dialog");
		alert.setContentText(e.getClass().getName());

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was: ");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();

	}

}
