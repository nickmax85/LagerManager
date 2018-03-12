package com.application.view.mailsettings;

import java.util.ResourceBundle;

import com.application.db.util.Service;
import com.application.model.Mailconfig;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class MailSettingsEditController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private MailSettingsDataController mailSettingsDataController;

	private Stage dialogStage;

	private Mailconfig mailsettings;

	private boolean okClicked = false;

	@FXML
	private void initialize() {
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setData(Mailconfig lagerort) {

		this.mailsettings = lagerort;

		mailSettingsDataController.setData(lagerort);
		mailSettingsDataController.setDialogStage(dialogStage);

	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() {

		if (!mailSettingsDataController.isInputValid())
			return;

		if (mailsettings == null)
			mailsettings = new Mailconfig();

		if (mailsettings != null) {

			mailsettings.setEmpfaenger(mailSettingsDataController.empfaengerField.getText());

		}

		if (mailsettings.getId() == 0)
			insert();
		else
			update();

		if (!Service.getInstance().isErrorStatus()) {
			okClicked = true;
			dialogStage.close();
		}

	}

	private void insert() {

		Service.getInstance().insertMailSettings(mailsettings);

	}

	private void update() {

		Service.getInstance().updateMailSettings(mailsettings);

	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

}
