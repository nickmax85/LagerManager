package com.application.view.mailsettings;

import java.util.ResourceBundle;

import com.application.model.Mailconfig;
import com.application.view.alert.InputValidatorAlert;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MailSettingsDataController {

	@FXML
	private ResourceBundle resources;

	private Stage dialogStage;

	@FXML
	public TextField empfaengerField;

	private Mailconfig mailconfig;

	@FXML
	private void initialize() {

		clearFields();

	}

	public void setData(Mailconfig data) {

		this.mailconfig = data;

		if (data != null) {
			empfaengerField.setText(data.getEmpfaenger());

		} else
			clearFields();

	}

	private void clearFields() {

		empfaengerField.setText("");

	}

	public void setEditable(boolean editable) {


		empfaengerField.setDisable(!editable);

	}

	public boolean isInputValid() {

		String text = "";
		if (empfaengerField.getText() == null || empfaengerField.getText().length() == 0)
			text += "Kein gültiger Empfänger!\n";

		if (text.length() == 0) {
			return true;

		} else {
			new InputValidatorAlert(dialogStage, text).showAndWait();
			return false;
		}

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

}
