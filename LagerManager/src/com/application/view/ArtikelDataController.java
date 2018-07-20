package com.application.view;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.mail.Session;

import org.apache.log4j.Logger;

import com.application.Main;
import com.application.db.util.Service;
import com.application.model.Anhang;
import com.application.model.Artikel;
import com.application.model.Lagerort;
import com.application.util.Constants;
import com.application.util.EmailUtil;
import com.application.util.GenerateUID;
import com.application.view.alert.DeleteYesNoAlert;
import com.application.view.alert.InputValidatorAlert;
import com.application.view.alert.NotImplementedAlert;
import com.application.view.alert.YesNoAlert;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class ArtikelDataController {

	private static final Logger logger = Logger.getLogger(ArtikelDataController.class);

	@FXML
	public AnchorPane dataPane;
	@FXML
	public TextField bezeichnungField;
	@FXML
	public TextField komponenteNrField;
	@FXML
	public TextField equipmentField;
	@FXML
	public TextField ausgebautVonField;
	@FXML
	public TextField lieferNummerField;
	@FXML
	public TextField stueckField;
	@FXML
	public HTMLEditor infoField;
	@FXML
	public TextField editorField;
	@FXML
	public Button anhaengeButton;
	@FXML
	public ComboBox<Lagerort> lagerortComboBox;
	@FXML
	public Button sendMailButton;
	@FXML
	public Button printButton;
	@FXML
	public Button savePngButton;

	@FXML
	public ImageView pictureImage;
	@FXML
	public Hyperlink pictureFileLink;
	@FXML
	public ListView<Anhang> anhangListView;

	private Stage dialogStage;

	private Artikel data;
	public File pictureFile;

	private ObservableList<Anhang> anhangList;

	@FXML
	private void initialize() {

		clearFields();

		pictureImage.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (event.getClickCount() == 2) {

					File file;
					file = data.getPicture();

					if (file.exists()) {
						try {
							Desktop.getDesktop().open(file);
						} catch (IOException e1) {
							e1.printStackTrace();
						}

					}
				}

			}
		});

		anhangListView.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (event.getCode() == KeyCode.DELETE) {

					DeleteYesNoAlert alert = new DeleteYesNoAlert(dialogStage);

					if (alert.isOKButton()) {
						Anhang a = anhangListView.getSelectionModel().getSelectedItem();
						Service.getInstance().deleteAnhang(anhangListView.getSelectionModel().getSelectedItem());
						if (!Service.getInstance().isErrorStatus()) {
							anhangListView.getItems().remove(a);

						}
					}

				}
			}
		});

		anhangListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (event.getClickCount() == 2) {

					Anhang anhang = anhangListView.getSelectionModel().getSelectedItem();

					File file;
					file = anhang.getFile();
					if (file.exists()) {
						try {
							Desktop.getDesktop().open(file);
						} catch (IOException e1) {
							e1.printStackTrace();
						}

					}
				}
			}
		});

		anhangListView.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				if (db.hasFiles()) {
					event.acceptTransferModes(TransferMode.COPY);
				} else {
					event.consume();
				}
			}
		});

		anhangListView.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {

				Dragboard db = event.getDragboard();
				boolean success = false;

				if (db.hasFiles()) {
					success = true;
					String filePath = null;
					for (File file : db.getFiles()) {

						// Get length of file in bytes
						long fileSizeInBytes = file.length();
						// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
						long fileSizeInKB = fileSizeInBytes / 1024;
						// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
						long fileSizeInMB = fileSizeInKB / 1024;

						if (fileSizeInKB > 512) {
							Alert alert = new Alert(AlertType.ERROR);

							alert.setTitle("Fehler");
							alert.setHeaderText("Datei zu gross, bitte verkleinern");
							alert.setContentText("Die Datei darf maximal 512KB haben");
							alert.initOwner(dialogStage);
							DialogPane dialogPane = alert.getDialogPane();
							dialogPane.getStylesheets()
									.addAll(getClass().getResource(Constants.STYLESHEET).toExternalForm());
							alert.showAndWait();
							break;
						}
						filePath = file.getAbsolutePath();
						System.out.println(filePath);

						Anhang anhang = new Anhang();
						anhang.setName(file.getName());
						anhang.setFile(file);

						anhang.setArtikelId(data.getId());

						if (data.getId() == 0) {
							Service.getInstance().insertArtikel(data);
							anhang.setArtikelId(data.getId());
						}

						if (data.getId() != 0) {
							Service.getInstance().insertAnhang(anhang);
							if (!Service.getInstance().isErrorStatus())
								anhangListView.getItems().add(anhang);

						}

					}
				}
				event.setDropCompleted(success);
				event.consume();
			}
		});

		pictureFileLink.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(final ActionEvent e) {

				FileChooser fileChooser = new FileChooser();
				FileChooser.ExtensionFilter extFilterPng = new FileChooser.ExtensionFilter("JPG files (*.jpg)",
						"*.jpg");
				fileChooser.getExtensionFilters().addAll(extFilterPng);
				File file = fileChooser.showOpenDialog(dialogStage);

				if (file != null) {
					if (checkFileSize(1024, file)) {

						try {
							FileInputStream fis = new FileInputStream(file);
							Image img = new Image(fis);

							pictureImage.setImage(img);
							pictureFile = file;

						} catch (FileNotFoundException ex) {

							ex.printStackTrace();
						}
					}
				}

			}
		});

		pictureImage.setOnDragOver(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				if (db.hasFiles()) {
					event.acceptTransferModes(TransferMode.COPY);
				} else {
					event.consume();
				}
			}
		});

		pictureImage.setOnDragDropped(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {

				Dragboard db = event.getDragboard();
				boolean success = false;

				if (db.hasFiles()) {
					success = true;
					String filePath = null;

					for (File file : db.getFiles()) {

						if (file.getName().endsWith(".jpg")) {
							if (checkFileSize(1024, file)) {
								filePath = file.getAbsolutePath();
								pictureFile = file;

								try {
									File f = pictureFile;
									FileInputStream fis = new FileInputStream(f);

									Image img = new Image(fis, 220, 220, false, false);
									pictureImage.setImage(img);

								} catch (FileNotFoundException e) { // TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} else {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Fehler");
							alert.setHeaderText("Falscher Dateityp");
							alert.setContentText("Bitte wählen Sie ein Bild vom Dateityp .jpg");
							alert.initOwner(dialogStage);

							DialogPane dialogPane = alert.getDialogPane();
							dialogPane.getStylesheets()
									.addAll(getClass().getResource(Constants.STYLESHEET).toExternalForm());
							alert.showAndWait();
						}

					}

				}
				event.setDropCompleted(success);
				event.consume();
			}
		});

	}

	private boolean checkFileSize(int maxSizeKB, File file) {

		long fileSizeInBytes = file.length();
		// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
		long fileSizeInKB = fileSizeInBytes / 1024;
		// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
		long fileSizeInMB = fileSizeInKB / 1024;

		if (fileSizeInKB < maxSizeKB) {
			return true;

		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Fehler");
			alert.setHeaderText("Datei zu gross, bitte verkleinern");
			alert.setContentText("Die Datei darf maximal 1024kB haben");
			alert.initOwner(dialogStage);

			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().addAll(getClass().getResource(Constants.STYLESHEET).toExternalForm());
			alert.showAndWait();

			return false;
		}

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;

	}

	public void setData(Artikel data) {

		this.data = data;

		if (data != null) {

			bezeichnungField.setText(data.getName());
			equipmentField.setText(data.getHersteller());
			komponenteNrField.setText(data.getKomponenteNr());
			ausgebautVonField.setText(data.getMaschine());

			String uid;
			if (data.getId() == 0)
				uid = GenerateUID.getUniqueId(Service.getInstance().getLiefernummern());
			else
				uid = data.getBox();
			lieferNummerField.setText(uid);

			stueckField.setText(String.valueOf(data.getStueck()));
			infoField.setHtmlText(data.getInfo());
			editorField.setText(data.getAuthor());
			anhangListView.setItems(null);

			Image image1 = new Image(Main.class.getResourceAsStream("/com/application/resource/icons/ladeBild.png"));
			pictureImage.setImage(image1);

			ObservableList<Lagerort> lagerorte = FXCollections
					.observableArrayList(Service.getInstance().getLagerortList());
			lagerortComboBox.setItems(lagerorte);
			lagerortComboBox.setConverter(new StringConverter<Lagerort>() {

				@Override
				public Lagerort fromString(String string) {
					return null;
				}

				@Override
				public String toString(Lagerort object) {
					return object.getName();
				}
			});
			for (Lagerort lo : lagerorte) {
				if (lo.getId() == data.getLagerortId())
					lagerortComboBox.getSelectionModel().select(lo);
			}

			File dir = new File(System.getProperty("user.home") + File.separator + "LagerManager", "files");
			if (!dir.exists())
				dir.mkdir();
			File dir1 = new File(System.getProperty("user.home") + File.separator + "LagerManager", "pictures");
			if (!dir1.exists())
				dir1.mkdir();

			Thread th = new Thread(new Runnable() {

				@Override
				public void run() {

					Service.getInstance().getPicture(data).getPicture();

					Platform.runLater(new Runnable() {

						@Override
						public void run() {

							if (data.getPicture() == null) {
								Image image = new Image(
										Main.class.getResourceAsStream("/com/application/resource/icons/keinBild.png"),
										220, 220, false, false);
								pictureImage.setImage(image);

							} else {

								try {
									InputStream is = new FileInputStream(data.getPicture());
									Image img = new Image(is, 220, 220, false, false);
									pictureImage.setImage(img);

									pictureFile = data.getPicture();

								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}

					});

				}

			});

			th.start();

			Thread th1 = new Thread(new Runnable() {

				@Override
				public void run() {

					anhangList = FXCollections.observableArrayList(Service.getInstance().getAnhangList(data));

					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							if (anhangList != null)
								anhangListView.setItems(anhangList);

						}

					});

				}
			});
			th1.start();

		} else {

			clearFields();

		}
	}

	private void clearFields() {

		bezeichnungField.setText("");
		equipmentField.setText("");
		komponenteNrField.setText("");
		ausgebautVonField.setText("");

		lieferNummerField.setText("");
		stueckField.setText("");
		infoField.setHtmlText("");
		editorField.setText("");
		anhangListView.setItems(null);

		lagerortComboBox.setItems(null);
		lagerortComboBox.getSelectionModel().select(null);

		Image img = new Image(Main.class.getResourceAsStream("/com/application/resource/icons/keinBild.png"));
		pictureImage.setImage(img);

	}

	public void setEditable(boolean editable) {

		bezeichnungField.setDisable(!editable);
		equipmentField.setDisable(!editable);
		komponenteNrField.setDisable(!editable);
		ausgebautVonField.setDisable(!editable);
		lieferNummerField.setDisable(!editable);
		stueckField.setDisable(!editable);
		infoField.setDisable(!editable);
		editorField.setDisable(!editable);
		lagerortComboBox.setDisable(!editable);
		// anhangListView.setDisable(!editable);
		anhaengeButton.setDisable(!editable);
		// pictureImage.setDisable(!editable);

		savePngButton.setDisable(!editable);
		printButton.setDisable(!editable);
		sendMailButton.setDisable(!editable);

	}

	public boolean isInputValid() {

		String text = "";

		List<String> nummern = new ArrayList<>();
		nummern = Service.getInstance().getLiefernummern();

		if (data.getBox() != null)
			nummern.remove(data.getBox());

		if (nummern.contains(lieferNummerField.getText())) {

			YesNoAlert alert = new YesNoAlert(dialogStage, "LagerManager", "Liefernummer bereits vorhanden",
					"Soll trotzdem gespeichert werden?");
			if (!alert.isOKButton()) {
				// text += "Die Liefernummer " + lieferNummerField.getText() + " ist bereits
				// vorhanden!\n";

			}
		}

		if (bezeichnungField.getText() == null || bezeichnungField.getText().length() == 0)
			text += "Keine gültige Bezeichnung!\n";
		if (lagerortComboBox.getSelectionModel().getSelectedItem() == null)
			text += "Kein gültiger Lagerort!\n";
		if (lieferNummerField.getText() == null || lieferNummerField.getText().length() == 0)
			text += "Keine gültige Liefernummer!\n";
		if (editorField.getText() == null || editorField.getText().length() == 0)
			text += "Kein gültiger Bearbeiter!\n";

		if (text.length() == 0) {
			return true;

		} else {
			new InputValidatorAlert(dialogStage, text).showAndWait();
			return false;
		}

	}

	@FXML
	public void handleSendMail() {

		// new NotImplementedAlert(dialogStage).showAndWait();
		// return;

		if (isInputValid()) {
			setData();
			showMailSendDialog(data);
		}

	}

	private void setData() {

		data.setName(bezeichnungField.getText());
		data.setHersteller(equipmentField.getText());
		data.setKomponenteNr(komponenteNrField.getText());
		data.setMaschine(ausgebautVonField.getText());
		data.setBox(lieferNummerField.getText());
		data.setStueck(Integer.parseInt(stueckField.getText()));
		data.setInfo(infoField.getHtmlText());
		data.setAuthor(editorField.getText());
		data.setPicture(pictureFile);
		data.setLagerortId(lagerortComboBox.getSelectionModel().getSelectedItem().getId());
		data.setLagerort(lagerortComboBox.getSelectionModel().getSelectedItem());

	}

	@FXML
	public void handlePrint() {

		if (isInputValid()) {
			setData();
			showPrintDialog(data);
		}

	}

	@FXML
	private void handleAnhaengeButton() {

		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilterPng = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
		fileChooser.getExtensionFilters().addAll(extFilterPng);
		File file = fileChooser.showOpenDialog(dialogStage);

		if (file != null) {
			if (checkFileSize(1024, file)) {

				Anhang anhang = new Anhang();
				anhang.setName(file.getName());
				anhang.setFile(file);

				anhang.setArtikelId(data.getId());

				if (data.getId() == 0) {
					Service.getInstance().insertArtikel(data);
					anhang.setArtikelId(data.getId());
				}

				if (data.getId() != 0) {
					Service.getInstance().insertAnhang(anhang);
					if (!Service.getInstance().isErrorStatus())
						anhangListView.getItems().add(anhang);

				}

			}
		}

	}

	@FXML
	public void handleExportScreenshot() throws Exception {

		File file = null;

		FileChooser chooser = new FileChooser();
		chooser.setTitle("Speichern unter");
		FileChooser.ExtensionFilter extFilterPng = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
		chooser.getExtensionFilters().addAll(extFilterPng);

		chooser.setInitialFileName(bezeichnungField.getText() + ".png");

		file = chooser.showSaveDialog(dialogStage);

		if (file != null) {

			if (chooser.getSelectedExtensionFilter() == extFilterPng) {
				saveAsPng(file);
			}
		} else {
			logger.info("Speichern unter abgebrochen");
		}

	}

	public void saveAsPng(File file) {

		WritableImage image = dataPane.snapshot(new SnapshotParameters(), null);

		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void showMailSendDialog(Artikel artikel) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/ArtikelPrint.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("EMail Versand: Lieferschein");
			dialogStage.getIcons().addAll(this.dialogStage.getIcons());

			dialogStage.initOwner(this.dialogStage);
			Scene scene = new Scene(page);
			scene.getStylesheets().add(getClass().getResource(Constants.STYLESHEET).toExternalForm());

			dialogStage.setScene(scene);

			ArtikelPrintController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			dialogStage.show();

			controller.setData(artikel, false, true);

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	public void showPrintDialog(Artikel artikel) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/ArtikelPrint.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Drucken: Lieferschein");
			dialogStage.getIcons().addAll(this.dialogStage.getIcons());

			dialogStage.initOwner(this.dialogStage);
			Scene scene = new Scene(page);
			scene.getStylesheets().add(getClass().getResource(Constants.STYLESHEET).toExternalForm());

			dialogStage.setScene(scene);

			ArtikelPrintController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			dialogStage.show();

			controller.setData(artikel, true, false);

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

}