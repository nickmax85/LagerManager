package com.application.view.lagerort;

import java.util.ResourceBundle;

import com.application.db.util.Service;
import com.application.model.Lagerort;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class LagerortEditController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private LagerortDataController lagerortDataController;

	private Stage dialogStage;

	private Lagerort lagerort;

	private boolean okClicked = false;

	@FXML
	private void initialize() {
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setData(Lagerort lagerort) {

		this.lagerort = lagerort;

		lagerortDataController.setData(lagerort);
		lagerortDataController.setDialogStage(dialogStage);

	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() {

		if (!lagerortDataController.isInputValid())
			return;

		if (lagerort == null)
			lagerort = new Lagerort();

		if (lagerort != null) {
			lagerort.setName(lagerortDataController.nameField.getText());

		}

		if (lagerort.getId() == 0)
			insert();
		else
			update();

		if (!Service.getInstance().isErrorStatus()) {
			okClicked = true;
			dialogStage.close();
		}

	}

	private void insert() {

		Service.getInstance().insertLagerort(lagerort);

	}

	private void update() {

		Service.getInstance().updateLagerort(lagerort);

	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

}
