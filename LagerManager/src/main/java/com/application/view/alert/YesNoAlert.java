package com.application.view.alert;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class YesNoAlert extends Alert {

	private boolean isOKButton;

	public YesNoAlert(Stage dialogStage, String title, String header, String text) {

		super(AlertType.INFORMATION);

		initOwner(dialogStage);

		setTitle(title);
		setHeaderText(header);
		setContentText(text);

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
