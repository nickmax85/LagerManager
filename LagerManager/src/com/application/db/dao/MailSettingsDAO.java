package com.application.db.dao;

import java.util.List;

import com.application.db.util.DAOException;
import com.application.model.Lagerort;
import com.application.model.Mailconfig;

public interface MailSettingsDAO {

	public void delete(Mailconfig data) throws DAOException;

	public Mailconfig get(int dataId) throws DAOException;

	public List<Mailconfig> getAll() throws DAOException;

	public void insert(Mailconfig data) throws DAOException;

	public void update(Mailconfig data) throws DAOException;

}