package com.application.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.application.Main;
import com.application.db.util.Service;
import com.application.model.Anhang;
import com.application.model.Artikel;
import com.application.model.Lagerort;
import com.application.util.Constants;
import com.application.util.DeleteDirectory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ArtikelOverviewController implements Initializable {

	private static final Logger logger = Logger.getLogger(ArtikelOverviewController.class);

	@FXML
	public ArtikelDataController artikelDataController;

	@FXML
	public AnchorPane dataPane;
	@FXML
	public ListView<Anhang> anhangListView;
	@FXML
	private TableView<Artikel> dataTable;
	@FXML
	private TableColumn<Artikel, String> nameColumn;
	@FXML
	private TableColumn<Artikel, String> herstellerColumn;
	@FXML
	private TableColumn<Artikel, String> komponenteNrColumn;
	@FXML
	private TableColumn<Artikel, String> maschineColumn;
	@FXML
	private TableColumn<Artikel, String> boxColumn;
	@FXML
	private TableColumn<Artikel, Integer> stueckColumn;
	@FXML
	private TableColumn<Artikel, Boolean> anhangColumn;
	@FXML
	private TableColumn<Artikel, Lagerort> lagerortColumn;
	@FXML
	private TableColumn<Artikel, String> infoColumn;

	@FXML
	private TextField filterField;

	private Stage dialogStage;

	private ObservableList<Artikel> artikelList;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		herstellerColumn.setCellValueFactory(cellData -> cellData.getValue().herstellerProperty());
		maschineColumn.setCellValueFactory(cellData -> cellData.getValue().maschineProperty());
		komponenteNrColumn.setCellValueFactory(cellData -> cellData.getValue().komponenteNrProperty());
		boxColumn.setCellValueFactory(cellData -> cellData.getValue().boxProperty());
		stueckColumn.setCellValueFactory(new PropertyValueFactory<Artikel, Integer>("stueck"));
		stueckColumn.setCellFactory(column -> {

			return new TableCell<Artikel, Integer>() {

				@Override
				protected void updateItem(Integer item, boolean empty) {
					super.updateItem(item, empty);

					TableRow<?> currentRow = getTableRow();

					if (item == null || empty) {
						setText(null);
						setStyle("");

					} else {

						if (item == 0)
							setStyle("-fx-background-color:lightcoral");
						else
							setStyle("");

						setText(String.valueOf(item));
						setAlignment(Pos.CENTER);

					}
				}
			};
		});

		anhangColumn.setCellValueFactory(new PropertyValueFactory<Artikel, Boolean>("anhang"));
		anhangColumn.setCellFactory(CheckBoxTableCell.forTableColumn(anhangColumn));

		lagerortColumn.setCellValueFactory(new PropertyValueFactory<Artikel, Lagerort>("lagerort"));
		lagerortColumn.setCellFactory(column -> {
			return new TableCell<Artikel, Lagerort>() {

				@Override
				protected void updateItem(Lagerort item, boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty) {
						setText(null);
						setStyle("");
					} else {
						setText(item.getName());
					}
				}

			};

		});
		
		infoColumn.setCellValueFactory(cellData -> cellData.getValue().infoProperty());
		

		dataTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showDetails(newValue));

		dataTable.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY)
					if (event.getClickCount() == 2) {
						handleEdit();
					}
			}
		});

		dataTable.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER)
					handleEdit();

				if (event.getCode() == KeyCode.DELETE)
					handleDelete();
			}
		});

		showDetails(null);

	}

	public void setData() {

		ObservableList<Artikel> artikelFX = FXCollections.observableArrayList(Service.getInstance().getArtikelList());
		dataTable.setItems(artikelFX);

		artikelList = artikelFX;
		filter();

	}

	private void showDetails(Artikel data) {

		try {
			File dir = new File(System.getProperty("user.home") + File.separator + "LagerManager", "files");
			if (dir.exists())
				DeleteDirectory.delete(dir);
		} catch (IOException e) {
			logger.info("Ordner files konnte nicht entfernt werden.");
		}

		try {
			File dir = new File(System.getProperty("user.home") + File.separator + "LagerManager", "pictures");
			if (dir.exists())
				DeleteDirectory.delete(dir);
		} catch (IOException e) {
			logger.info("Ordner pictures konnte nicht entfernt werden.");
		}

		artikelDataController.setData(data);
		artikelDataController.setEditable(false);

	}

	@FXML
	public void handleDelete() {

		int selectedIndex = dataTable.getSelectionModel().getSelectedIndex();
		Artikel data = dataTable.getSelectionModel().getSelectedItem();

		if (selectedIndex >= 0) {

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Loeschen");
			alert.setHeaderText("Wollen Sie wirklich loeschen?");
			alert.setContentText("Hinweis:\nVorgang kann nicht rueckgaengig gemacht werden!\n\n");

			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().addAll(getClass().getResource(Constants.STYLESHEET).toExternalForm());
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.OK) {

				if (Service.getInstance().deleteArtikel(data))
					setData();
			}

		}
	}

	@FXML
	public void handleNew() {

		Artikel data = new Artikel();
		boolean okClicked = showEditDialog(data);
		if (okClicked) {

			if (data.getId() == 0)
				if (Service.getInstance().insertArtikel(data))
					setData();

			if (data.getId() != 0)
				if (Service.getInstance().updateArtikel(data))
					setData();
		}
	}

	@FXML
	public void handleUpdateKeyEvent(KeyEvent event) {

		if (event.getCode() == KeyCode.F5) {
			logger.info("handleUpdateKeyEvent");
			setData();

		}

	}

	@FXML
	public void handleEdit() {
		Artikel selectedData = dataTable.getSelectionModel().getSelectedItem();
		if (selectedData != null) {
			boolean okClicked = showEditDialog(selectedData);

			if (okClicked) {

				if (Service.getInstance().updateArtikel(selectedData)) {
					// setData();
					showDetails(selectedData);
				}
			}

		}
	}

	public void setDialogStage(Stage dialogStage) {

		this.dialogStage = dialogStage;

		artikelDataController.setDialogStage(dialogStage);

	}

	private void filter() {

		FilteredList<Artikel> filteredData;
		filteredData = new FilteredList<>(artikelList, p -> true);

		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(artikel -> {

				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();

				if (artikel.getName() != null)
					if (artikel.getName().toLowerCase().contains(lowerCaseFilter)) {
						return true;

					}
				
				if (artikel.getInfo() != null)
					if (artikel.getInfo().toLowerCase().contains(lowerCaseFilter)) {
						return true;

					}

				if (artikel.getHersteller() != null)
					if (artikel.getHersteller().toLowerCase().contains(lowerCaseFilter)) {
						return true;
					}

				if (artikel.getKomponenteNr() != null)
					if (artikel.getKomponenteNr().toLowerCase().contains(lowerCaseFilter)) {
						return true;
					}

				if (String.valueOf(artikel.getStueck()).contains(lowerCaseFilter)) {
					return true;
				}

				if (artikel.getMaschine() != null)
					if (artikel.getMaschine().contains(lowerCaseFilter)) {
						return true;
					}

				if (artikel.getBox() != null)
					if (artikel.getBox().contains(lowerCaseFilter)) {
						return true;
					}

				return false;
			});
		});

		SortedList<Artikel> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(dataTable.comparatorProperty());

		dataTable.setItems(sortedData);
	}

	public boolean showEditDialog(Artikel artikel) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/com/application/view/ArtikelEdit.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Bearbeiten: Artikel");
			dialogStage.getIcons().addAll(this.dialogStage.getIcons());

			dialogStage.initOwner(this.dialogStage);
			Scene scene = new Scene(page);
			scene.getStylesheets().add(getClass().getResource(Constants.STYLESHEET).toExternalForm());

			dialogStage.setScene(scene);

			ArtikelEditController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setData(artikel);

			dialogStage.showAndWait();

			return controller.isOkClicked();

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@FXML
	public void exportCSV() throws Exception {

		File file = null;

		FileChooser chooser = new FileChooser();
		chooser.setTitle("Speichern unter");
		FileChooser.ExtensionFilter extFilterPng = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		chooser.getExtensionFilters().addAll(extFilterPng);

		chooser.setInitialFileName(dataTable.getSelectionModel().getSelectedItem().getName() + ".csv");

		file = chooser.showSaveDialog(dialogStage);

		if (file != null) {

			if (chooser.getSelectedExtensionFilter() == extFilterPng) {
				saveAsCsv(file);
			}
		} else {
			logger.info("Speichern unter abgebrochen");
		}

	}

	public void saveAsCsv(File file) throws Exception {

		Writer writer = null;

		writer = new BufferedWriter(new FileWriter(file));

		writer.write(nameColumn.getText() + ";" + herstellerColumn.getText() + ";" + maschineColumn.getText() + ";"
				+ boxColumn.getText() + ";" + stueckColumn.getText() + "\n");

		for (Artikel artikel : dataTable.getItems()) {

			String text1 = artikel.getName() + ";" + artikel.getHersteller() + ";" + artikel.getMaschine() + ";"
					+ artikel.getBox() + ";" + artikel.getStueck() + "\n";

			writer.write(text1.replace("null", ""));
		}

		writer.flush();

		writer.close();
	}

}