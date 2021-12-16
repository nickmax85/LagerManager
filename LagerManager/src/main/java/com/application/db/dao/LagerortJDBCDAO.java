package com.application.db.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.application.db.util.ConnectionManager;
import com.application.db.util.DAOException;
import com.application.model.Lagerort;

public class LagerortJDBCDAO implements LagerortDAO {

	private static final Logger logger = Logger.getLogger(LagerortJDBCDAO.class);

	private final static String SELECT_ALL = "SELECT * FROM lagerort";
	private final static String INSERT = "INSERT INTO lagerort(name) VALUES (?)";
	private final static String UPDATE = "UPDATE lagerort SET name = ?  WHERE id = ?";
	private final static String SELECT = "SELECT * FROM lagerort where id = ?";
	private final static String DELETE = "DELETE FROM lagerort where id = ?";

	// MySQL
	private final static String SELECT_LAST_ID_MYSQL = "select last_insert_id()";

	public Integer selectLastID() throws DAOException {

		Integer lastId = null;
		String stmt = null;

		try {

			stmt = SELECT_LAST_ID_MYSQL;

			PreparedStatement ps = ConnectionManager.getInstance().getConnection().prepareStatement(stmt);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				lastId = rs.getInt(1);
			}

			if (logger.isInfoEnabled()) {
				// logger.info(lastId);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}

		return lastId;
	}

	@Override
	public Lagerort get(int id) throws DAOException {

		ResultSet rs = null;
		PreparedStatement ps = null;

		Lagerort lagerort = null;

		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(SELECT);
			ps.setInt(1, id);

			rs = ps.executeQuery();
			while (rs.next()) {
				lagerort = new Lagerort();

				lagerort.setId(rs.getInt("id"));
				lagerort.setName(rs.getString("name"));

			}
			if (logger.isInfoEnabled()) {
				logger.info(lagerort);
			}

		} catch (SQLException e) {

			throw new DAOException(e);
		}

		return lagerort;

	}

	@Override
	public boolean insert(Lagerort lagerort) throws DAOException {
		PreparedStatement ps;
		int res = 0;

		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(INSERT);

			ps.setString(1, lagerort.getName());

			res = ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info(lagerort);
			}

		}

		catch (Exception e) {
			e.printStackTrace();
			logger.error(e);

		}

		if (res > 0) {
			lagerort.setId(selectLastID());
			return true;

		} else
			return false;

	}

	@Override
	public boolean update(Lagerort lagerort) throws DAOException {

		int res = 0;
		try {

			PreparedStatement ps = ConnectionManager.getInstance().getConnection().prepareStatement(UPDATE);

			ps.setString(1, lagerort.getName());

			res = ps.executeUpdate();

			if (logger.isInfoEnabled()) {

			}

		}

		catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		if (res > 0) {
			return true;

		} else
			return false;
	}

	@Override
	public boolean delete(Lagerort lagerort) throws DAOException {

		int res = 0;

		try {
			PreparedStatement ps = ConnectionManager.getInstance().getConnection().prepareStatement(DELETE);

			ps.setInt(1, lagerort.getId());
			res = ps.executeUpdate();
		}

		catch (SQLException e) {
			e.printStackTrace();

			if (e.getErrorCode() == 547 || e.getErrorCode() == 1451)
				throw new DAOException("Entfernen nicht erlaubt, da die Daten verwendet werden.");
			else
				throw new DAOException(e);
		}

		if (res > 0)
			return true;
		else
			return false;
	}

	@Override
	public List<Lagerort> getAll() throws DAOException {
		ResultSet rs = null;
		Statement statement = null;
		Connection con = null;

		List<Lagerort> lagerortList = new ArrayList<>();

		try {

			con = ConnectionManager.getInstance().getConnection();

			statement = con.createStatement();
			rs = statement.executeQuery(SELECT_ALL);

			while (rs.next()) {
				Lagerort lagerort = new Lagerort();
				lagerort.setId(rs.getInt("id"));
				lagerort.setName(rs.getString("name"));

				lagerortList.add(lagerort);
				if (logger.isInfoEnabled()) {
				//	logger.info(lagerort);
				}
			}

		} catch (SQLException e) {

			throw new DAOException(e);
		}

		return lagerortList;
	}

}