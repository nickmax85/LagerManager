package com.application.db.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import com.application.db.dao.AnhangDAO;
import com.application.db.dao.ArtikelDAO;
import com.application.db.dao.DAOFactory;
import com.application.db.dao.EDAOType;
import com.application.db.dao.LagerortDAO;
import com.application.model.Anhang;
import com.application.model.Artikel;
import com.application.model.Lagerort;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class Service {

	private static Service instance;
	private final static EDAOType SOURCE = EDAOType.JDBC;

	private DAOFactory daoFactory;

	private boolean errorStatus = false;

	private ArtikelDAO artikelDAO;
	private AnhangDAO anhangDAO;
	private LagerortDAO lagerortDAO;

	private Service() {

		daoFactory = new DAOFactory(SOURCE);

		artikelDAO = daoFactory.getArtikelDAO();
		anhangDAO = daoFactory.getAnhangDAO();
		lagerortDAO = daoFactory.getLagerortDAO();
		//mailSettingsDAO = daoFactory.getMailSettingsDAO();
	}

	public synchronized static Service getInstance() {

		if (instance == null) {
			instance = new Service();
		}

		return instance;

	}

	public void insertAnhang(Anhang anhang) {

		try {
			anhangDAO.insertAnhang(anhang);

			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public boolean insertArtikel(Artikel artikel) {

		try {
			artikelDAO.insertArtikel(artikel);
			errorStatus = false;
			return true;
		} catch (DAOException e) {

			e.printStackTrace();
			showExceptionMessage(e);
		}
		return false;

	}

	public List<Anhang> getAnhangList(Artikel artikel) {

		List<Anhang> anhangList = null;

		try {
			anhangList = anhangDAO.getAnhangList(artikel);

			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

		return anhangList;

	}

	public boolean getAnhangAnzahlFromWartung(Artikel artikel) {

		boolean anhang = false;

		try {
			if (anhangDAO.getAnhangAnzahl(artikel))
				anhang = true;
			else
				anhang = false;

			errorStatus = false;

		} catch (DAOException e) {
			showExceptionAlertDialog(e);
			e.printStackTrace();
		}

		return anhang;

	}

	public void deleteAnhang(Anhang anhang) {

		try {
			anhangDAO.deleteAnhang(anhang);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public boolean updateArtikel(Artikel artikel) {

		try {
			artikelDAO.updateArtikel(artikel);
			errorStatus = false;
			return true;
		} catch (DAOException e) {

			e.printStackTrace();
			showExceptionMessage(e);
		}

		return false;

	}

	public boolean deleteArtikel(Artikel artikel) {

		try {
			artikelDAO.deleteArtikel(artikel);
			errorStatus = false;
			return true;
		} catch (DAOException e) {

			e.printStackTrace();
			showExceptionMessage(e);
		}
		return false;
	}

	public void getArtikel(int id) {

		try {
			artikelDAO.getArtikel(id);
			errorStatus = false;
		} catch (DAOException e) {

			e.printStackTrace();
			showExceptionMessage(e);
		}

	}

	public List<Artikel> getArtikelList() {

		List<Artikel> artikelList = null;

		try {
			artikelList = artikelDAO.getArtikelList();

			for (Lagerort lo : getLagerortList()) {

				for (Artikel artikel : artikelList) {
					if (lo.getId() == artikel.getLagerortId())
						artikel.setLagerort(lo);

				}

			}

			errorStatus = false;
		} catch (DAOException e) {

			e.printStackTrace();
			showExceptionMessage(e);
		}
		return artikelList;

	}

	public boolean updateLagerort(Lagerort lagerort) {

		try {
			lagerortDAO.update(lagerort);
			errorStatus = false;
			return true;
		} catch (DAOException e) {

			e.printStackTrace();
			showExceptionMessage(e);
		}

		return false;

	}

	public boolean deleteLagerort(Lagerort lagerort) {

		try {
			lagerortDAO.delete(lagerort);
			errorStatus = false;
			return true;
		} catch (DAOException e) {

			e.printStackTrace();
			showExceptionMessage(e);
		}
		return false;
	}

	public void getLagerort(int id) {

		try {
			lagerortDAO.get(id);
			errorStatus = false;
		} catch (DAOException e) {

			e.printStackTrace();
			showExceptionMessage(e);
		}

	}

	public Artikel getPicture(Artikel artikel) {

		try {

			errorStatus = false;
			return artikelDAO.getPicture(artikel);

		} catch (DAOException e) {

			e.printStackTrace();
			showExceptionMessage(e);
		}
		return artikel;

	}

	public boolean insertLagerort(Lagerort lagerort) {

		try {
			lagerortDAO.insert(lagerort);
			errorStatus = false;
			return true;
		} catch (DAOException e) {

			e.printStackTrace();
			showExceptionMessage(e);
		}
		return false;

	}

	public List<Lagerort> getLagerortList() {

		List<Lagerort> lagerortList = null;

		try {
			lagerortList = lagerortDAO.getAll();
			errorStatus = false;
		} catch (DAOException e) {

			e.printStackTrace();
			showExceptionMessage(e);
		}
		return lagerortList;

	}
	
	public List<String> getLiefernummern() {

		List<String> data = null;

		try {
			data = artikelDAO.getLiefernummern();
			errorStatus = false;
		} catch (DAOException e) {

			e.printStackTrace();
			showExceptionMessage(e);
		}
		return data;

	}

	

	public boolean isErrorStatus() {
		return errorStatus;
	}

	private void showExceptionMessage(DAOException e) {
		errorStatus = true;
		showExceptionAlertDialog(e);
	}

	private void showExceptionAlertDialog(DAOException e) {
		
		

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception Dialog");
	//	alert.setContentText(e.getClass().getName());
		alert.setContentText(e.getMessage());

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was: ");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();

	}

}
