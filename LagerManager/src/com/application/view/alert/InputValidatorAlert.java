package com.application.view.alert;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class InputValidatorAlert extends Alert {

	public InputValidatorAlert(Stage dialogStage, String text) {

		super(AlertType.INFORMATION);

		initOwner(dialogStage);

		setTitle("Daten ungültig");
		setHeaderText("Es wurden nicht alle benötigten Daten eingegeben.");
		setContentText(text);
	}

}
