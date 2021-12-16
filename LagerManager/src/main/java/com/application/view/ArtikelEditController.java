package com.application.view;

import org.apache.log4j.Logger;

import com.application.db.util.Service;
import com.application.model.Anhang;
import com.application.model.Artikel;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class ArtikelEditController {

	private static final Logger logger = Logger.getLogger(ArtikelEditController.class);

	private Stage dialogStage;
	private ObservableList<Anhang> anhangList;

	private boolean okClicked = false;

	@FXML
	private ArtikelDataController artikelDataController;

	private Artikel artikel;

	@FXML
	private void initialize() {

	}

	public void setDialogStage(Stage dialogStage) {

		this.dialogStage = dialogStage;
		artikelDataController.setDialogStage(dialogStage);

	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() {

		if (!artikelDataController.isInputValid())
			return;

		if (artikel == null)
			artikel = new Artikel();

		if (artikel != null) {
			artikel.setName(artikelDataController.bezeichnungField.getText());
			artikel.setHersteller(artikelDataController.equipmentField.getText());
			artikel.setKomponenteNr(artikelDataController.komponenteNrField.getText());
			artikel.setMaschine(artikelDataController.ausgebautVonField.getText());
			artikel.setBox(artikelDataController.lieferNummerField.getText());
			artikel.setStueck(Integer.parseInt(artikelDataController.stueckField.getText()));
			artikel.setInfo(artikelDataController.infoField.getText());
			artikel.setAuthor(artikelDataController.editorField.getText());
			artikel.setPicture(artikelDataController.pictureFile);
			artikel.setLagerortId(artikelDataController.lagerortComboBox.getSelectionModel().getSelectedItem().getId());
			artikel.setLagerort(artikelDataController.lagerortComboBox.getSelectionModel().getSelectedItem());

			if (Service.getInstance().getAnhangAnzahlFromWartung(artikel))
				artikel.setAnhang(true);
			else
				artikel.setAnhang(false);

		}

		if (artikel.getId() == 0)
			insert();
		else
			update();

		if (!Service.getInstance().isErrorStatus()) {
			okClicked = true;
			dialogStage.close();
		}

	}

	private void insert() {

		Service.getInstance().insertArtikel(artikel);

	}

	private void update() {

		Service.getInstance().updateArtikel(artikel);

	}

	@FXML
	private void handleAnhaenge() {

	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	public void setData(Artikel data) {

		this.artikel = data;

		artikelDataController.setData(this.artikel);
	}

}