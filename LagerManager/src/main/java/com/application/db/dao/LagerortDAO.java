package com.application.db.dao;

import java.util.List;

import com.application.db.util.DAOException;
import com.application.model.Lagerort;

public interface LagerortDAO {

	public boolean delete(Lagerort data) throws DAOException;

	public Lagerort get(int dataId) throws DAOException;

	public List<Lagerort> getAll() throws DAOException;

	public boolean insert(Lagerort data) throws DAOException;

	public boolean update(Lagerort data) throws DAOException;

}