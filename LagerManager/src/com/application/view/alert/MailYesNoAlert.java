package com.application.view.alert;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class MailYesNoAlert extends Alert {

	private boolean isOKButton;

	public MailYesNoAlert(Stage dialogStage) {

		super(AlertType.INFORMATION);

		initOwner(dialogStage);

		setTitle("EMail versenden");
		setHeaderText("EMail");
		setContentText("Soll das EMail versandt werden?");

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
