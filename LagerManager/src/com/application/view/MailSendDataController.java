package com.application.view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.mail.Session;

import com.application.model.Artikel;
import com.application.model.Mailconfig;
import com.application.util.EmailUtil;
import com.application.view.alert.InputValidatorAlert;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class MailSendDataController {

	@FXML
	private ResourceBundle resources;

	private Stage dialogStage;

	@FXML
	public TextField anField;
	@FXML
	public TextField ccField;
	@FXML
	public TextField betreffField;
	@FXML
	public TextArea messageField;

	@FXML
	public AnchorPane sendMailPane;

	private Mailconfig mailconfig;
	private Artikel artikel;
	private boolean isOkclicked;

	@FXML
	private void initialize() {

		clearFields();

	}

	public void setData(Mailconfig data, Artikel artikel) {

		this.mailconfig = data;

		if (data != null) {
			this.artikel = artikel;
			anField.setText(data.getEmpfaenger());
			ccField.setText("");
			betreffField.setText("LagerManager: Lieferauftrag");
			messageField.setText("Bitte um Abholung der Lieferung: " + artikel.getBox());

		} else
			clearFields();

	}

	private void clearFields() {
		anField.setText("");
		ccField.setText("");
		betreffField.setText("");

	}

	public void setEditable(boolean editable) {

		anField.setDisable(!editable);
		ccField.setDisable(!editable);
		betreffField.setDisable(!editable);

	}

	public boolean isInputValid() {

		String text = "";

		if (anField.getText() == null || anField.getText().length() == 0)
			text += "Kein gültiger Server!\n";
		if (ccField.getText() == null || ccField.getText().length() == 0)
			text += "Kein gültiger Absender!\n";
		if (betreffField.getText() == null || betreffField.getText().length() == 0)
			text += "Kein gültiger Empfänger!\n";

		if (text.length() == 0) {
			return true;

		} else {
			new InputValidatorAlert(dialogStage, text).showAndWait();
			return false;
		}

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	@FXML
	private void handleSend() {

		sendMailOLE(betreffField.getText().replaceAll(" ", "%20"), messageField.getText().replaceAll(" ", "%20"),
				mailconfig.getEmpfaenger(), ccField.getText());

		isOkclicked = true;

		this.dialogStage.close();

	}

	private void sendMailOLE(String subject, String body, String toEmail, String ccEmail) {

		try {
			try {
				if (!ccEmail.isEmpty())
					Desktop.getDesktop().mail(
							new URI("mailto:" + toEmail + "?subject=" + subject + "&cc=" + ccEmail + "&body=" + body));
				else
					Desktop.getDesktop().mail(new URI("mailto:" + toEmail + "?subject=" + subject + "&body=" + body));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException ex) {
			showExceptionAlertDialog(ex);
			ex.printStackTrace();
		}
	}

	private void sendMail() {

		File dir = new File(System.getProperty("user.home") + File.separator + "LagerManager", "files");
		if (!dir.exists())
			dir.mkdir();

		File mailFile = new File(dir + File.separator + artikel.getBox() + ".png");

		saveAsPng(mailFile);

		// String smtpHostServer = mailconfig.getMailserver();
		// String smtpHostServer = "mail";
		// String smtpHostServer = "mpt-rbs.magna.global";
		// String emailID = mailconfig.getEmpfaenger();
		//
		// Properties props = System.getProperties();
		// props.put("mail.smtp.host", smtpHostServer);
		// props.put("mail.smtp.auth", "false");
		//
		// Session session = Session.getInstance(props, null);

		// EmailUtil.sendEmail(session, mailconfig.getAbsender(), emailID,
		// betreffField.getText(), messageField.getText(),
		// mailFile, ccField.getText(), this.dialogStage);

	}

	@FXML
	private void handleCancel() {

		isOkclicked = false;

		this.dialogStage.close();
	}

	public boolean isOkclicked() {
		return isOkclicked;
	}

	public void saveAsPng(File file) {

		WritableImage image = sendMailPane.snapshot(new SnapshotParameters(), null);

		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void setSendMailPane(AnchorPane sendMailPane) {
		this.sendMailPane = sendMailPane;
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
