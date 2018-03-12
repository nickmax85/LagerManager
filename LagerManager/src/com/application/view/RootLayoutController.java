package com.application.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.application.Main;
import com.application.util.Constants;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;

public class RootLayoutController implements Initializable {

	private ResourceBundle resources;

	private Main main;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.resources = resources;
	}

	public void setMain(Main main) {
		this.main = main;
	}

	@FXML
	private void handleHomeScreen() {

		main.showArtikelOverview();

	}
	
	@FXML
	private void handleLagerorte() {

		main.showLagerortOverviewDialog();

	}

	@FXML
	private void handleSettings() {

		main.showSettingsDialog();

	}
	
	@FXML
	private void handleMailSettings() {

		main.showMailSettingsDialog();

	}

	@FXML
	private void handleAbout() {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(resources.getString("about"));
		alert.setHeaderText(resources.getString("appname"));
		alert.setContentText(resources.getString("programer"));

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().addAll(getClass().getResource(Constants.STYLESHEET).toExternalForm());
		alert.show();

	}

	@FXML
	private void handleExit() {
		System.exit(0);
	}

}