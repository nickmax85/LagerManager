package com.application.view.alert;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class DeleteYesNoAlert extends Alert {

	private boolean isOKButton;

	public DeleteYesNoAlert(Stage dialogStage) {

		super(AlertType.INFORMATION);

		initOwner(dialogStage);

		setTitle("Daten entfernen");
		setHeaderText("Entfernen");
		setContentText("Sollen die Daten wirklich entfernt werden?");

		ButtonType buttonTypeOk = new ButtonType("Ja");
		ButtonType buttonTypeCancel = new ButtonType("Nein");

		getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);
		Optional<ButtonType> result = showAndWait();

		if (result.get() == buttonTypeOk)
			isOKButton = true;
		else
			isOKButton = false;

	}

	public boolean isOKButton() {
		return isOKButton;
	}

}
