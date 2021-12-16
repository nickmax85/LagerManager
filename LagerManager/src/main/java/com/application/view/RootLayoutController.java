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
	public void handleHomeScreen() {

		main.showArtikelOverview();

	}
	
	@FXML
	public void handleLagerorte() {

		main.showLagerortOverviewDialog();

	}

	@FXML
	public void handleSettings() {

		main.showSettingsDialog();

	}
	
	@FXML
	public void handleAbout() {

		StringBuilder sb = new StringBuilder();

		sb.append("Version: " + Main.VERSION_HAUPT + "." + Main.VERSION_NEBEN + "." + Main.REVISION + "\n\n");
		sb.append("Entwicklung mit JDK: " + Main.JDK + "\n");
		sb.append("Java: " + System.getProperty("java.version") + "\n");
		sb.append("JavaFX: " + System.getProperty("javafx.version"));

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(resources.getString("about"));
		alert.setHeaderText(resources.getString("appname") + "\n" + sb.toString().replace("$", ""));
		alert.initOwner(main.getPrimaryStage());

		alert.setContentText(resources.getString("programer"));

		alert.showAndWait();

	}

	@FXML
	public void handleExit() {
		System.exit(0);
	}

}