package com.application.view.alert;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class NotImplementedAlert extends Alert {

	public NotImplementedAlert(Stage dialogStage) {

		super(AlertType.INFORMATION);

		initOwner(dialogStage);

		setTitle("Funktion nicht implementiert");
		setHeaderText("Diese Funktion wurde noch nicht implementiert");
		setContentText("Bei Fragen kontaktieren Sie den Entwickler.");
	}

}
