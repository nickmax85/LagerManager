package com.application.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.mail.Session;

import org.apache.log4j.Logger;

import com.application.Main;
import com.application.db.util.Service;
import com.application.model.Artikel;
import com.application.util.Constants;
import com.application.util.EmailUtil;
import com.application.view.alert.MailYesNoAlert;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ArtikelPrintController {

	private static final Logger logger = Logger.getLogger(ArtikelPrintController.class);

	@FXML
	public AnchorPane dataPane;
	@FXML
	public Label bezeichnungField;
	@FXML
	public Label equipmentField;
	@FXML
	public Label ausgebautVonField;
	@FXML
	public Label lieferNummerField;
	@FXML
	public Label stueckField;
	@FXML
	public Label komponenteNrField;
	@FXML
	public Label lagerortField;
	@FXML
	public Label editorField;
	@FXML
	public ImageView image;

	private Stage dialogStage;

	private Artikel data;

	@FXML
	private void initialize() {

	}

	public void setDialogStage(Stage dialogStage) {

		this.dialogStage = dialogStage;

	}

	public void setData(Artikel data, boolean print, boolean mail) {

		this.data = data;

		if (data != null) {

			bezeichnungField.setText(data.getName());
			equipmentField.setText(data.getHersteller());
			ausgebautVonField.setText(data.getMaschine());
			lieferNummerField.setText(data.getBox());
			stueckField.setText(String.valueOf(data.getStueck()));
			editorField.setText(data.getAuthor());
			komponenteNrField.setText(data.getKomponenteNr());
			lagerortField.setText(data.getLagerort().getName());
			try {
				if (data.getPicture() != null) {
					InputStream is = new FileInputStream(data.getPicture());
					Image img = new Image(is, 220, 220, false, false);
					image.setImage(img);

				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

			bezeichnungField.setText(null);
			equipmentField.setText(null);
			ausgebautVonField.setText(null);
			lieferNummerField.setText(null);
			stueckField.setText(null);
			editorField.setText(null);
			image.setImage(null);
		}

		if (print)
			print(dataPane);

		if (mail)
			sendMail();

	}

	private void sendMail() {

		showMailSendDialog(data);

	}

	private void print(Node node) {

		Printer printer = javafx.print.Printer.getDefaultPrinter();
		PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE,
				Printer.MarginType.DEFAULT);

		PrinterJob job = PrinterJob.createPrinterJob();
		if (job != null) {
			job.setPrinter(printer);
			job.getJobSettings().setPageLayout(pageLayout);

			boolean ok = job.showPrintDialog(dialogStage); // Window must be your main Stage

			if (ok) {
				job.printPage(node);

				logger.info(job.jobStatusProperty().asString());
				boolean printed = job.printPage(node);

				if (printed) {
					job.endJob();
				} else {
					logger.info("Printing failed.");
				}
			} else {
				logger.info("Could not create a printer job.");
			}

		}
		job.endJob();

	}

	public void saveAsPng(File file) {

		WritableImage image = dataPane.snapshot(new SnapshotParameters(), null);

		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private void showMailSendDialog(Artikel artikel) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/MailSendData.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Mailversand");
			dialogStage.getIcons().addAll(this.dialogStage.getIcons());

			dialogStage.initOwner(this.dialogStage);
			Scene scene = new Scene(page);
			scene.getStylesheets().add(getClass().getResource(Constants.STYLESHEET).toExternalForm());

			dialogStage.setScene(scene);

			MailSendDataController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			controller.setData(Service.getInstance().getMailSettings().get(0), data);
			controller.setSendMailPane(dataPane);

			dialogStage.show();

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

}