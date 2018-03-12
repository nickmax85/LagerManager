package com.application.db.dao;

import java.util.List;

import com.application.db.util.DAOException;
import com.application.model.Anhang;
import com.application.model.Artikel;

public interface AnhangDAO {

	public void deleteAnhang(Anhang anhang) throws DAOException;

	public List<Anhang> getAnhangList(Artikel artikel) throws DAOException;

	public void insertAnhang(Anhang anhang) throws DAOException;

	public boolean getAnhangAnzahl(Artikel artikel) throws DAOException;

}