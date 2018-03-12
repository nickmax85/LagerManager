package com.application.db.dao;

public class DAOFactory {

	private ArtikelDAO artikelDAO;
	private AnhangDAO anhangDAO;
	private LagerortDAO lagerortDAO;
	private MailSettingsDAO mailSettingsDAO;

	public DAOFactory(EDAOType eDAOType) {

		if (eDAOType == EDAOType.JDBC) {
			artikelDAO = new ArtikelJDBCDAO();
			anhangDAO = new AnhangJDBCDAO();
			lagerortDAO = new LagerortJDBCDAO();
			mailSettingsDAO = new MailSettingsJDBCDAO();
		}

		if (eDAOType == EDAOType.MEMORY) {

		}

	}

	public ArtikelDAO getArtikelDAO() {
		return artikelDAO;
	}

	public AnhangDAO getAnhangDAO() {
		return anhangDAO;
	}

	public LagerortDAO getLagerortDAO() {
		return lagerortDAO;
	}

	public MailSettingsDAO getMailSettingsDAO() {
		return mailSettingsDAO;
	}

}
