package com.application.db.dao;

import java.util.List;

import com.application.db.util.DAOException;
import com.application.model.Artikel;

public interface ArtikelDAO {

	public Artikel getArtikel(int id) throws DAOException;

	public List<Artikel> getArtikelList() throws DAOException;

	public boolean insertArtikel(Artikel artikel) throws DAOException;

	public boolean updateArtikel(Artikel artikel) throws DAOException;

	public boolean deleteArtikel(Artikel artikel) throws DAOException;

	public Artikel getPicture(Artikel artikel) throws DAOException;

	public List<String> getLiefernummern() throws DAOException;

}