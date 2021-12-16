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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.application.db.util.ConnectionManager;
import com.application.db.util.DAOException;
import com.application.model.Anhang;
import com.application.model.Artikel;

public class AnhangJDBCDAO implements AnhangDAO {

	private static final Logger logger = Logger.getLogger(AnhangJDBCDAO.class);

	private final static String SELECT_ALL = "SELECT * FROM anhang where artikelId = ? ORDER BY name ASC";
	private final static String INSERT = "INSERT INTO anhang(name, file, artikelId) VALUES (?, ?, ?)";
	private final static String DELETE = "DELETE FROM anhang where id = ?";

	private final static String ANZAHL = "SELECT id from anhang where artikelId = ?";

	@Override
	public void deleteAnhang(Anhang anhang) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(DELETE);
			ps.setInt(1, anhang.getId());
			ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info(anhang);
			}

		} catch (SQLException e) {
			throw new DAOException(e);

		}

	}

	@Override
	public List<Anhang> getAnhangList(Artikel artikel) throws DAOException {

		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;

		List<Anhang> attachmentList = new ArrayList<Anhang>();

		try {

			File dir = new File(System.getProperty("user.home") + File.separator + "LagerManager", "files");
			dir.mkdirs();

			dir.deleteOnExit();
			con = ConnectionManager.getInstance().getConnection();
			ps = con.prepareStatement(SELECT_ALL);

			ps.setInt(1, artikel.getId());
			rs = ps.executeQuery();

			while (rs.next()) {

				Anhang anhang = new Anhang();
				anhang.setId(rs.getInt("id"));
				anhang.setName(rs.getString("name"));
				// ===================================================================================
				File file = new File(dir.getAbsolutePath() + File.separator + anhang.getName());
				file.createNewFile();

				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1];

				InputStream is = rs.getBinaryStream("file");
				while (is.read(buffer) > 0) {
					fos.write(buffer);
				}
				file.deleteOnExit();
				fos.close();
				anhang.setFile(file);
				// ===================================================================================

				anhang.setArtikelId(artikel.getId());
				attachmentList.add(anhang);
			}

			if (logger.isInfoEnabled()) {
				logger.info(attachmentList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		} catch (IOException e) {

			e.printStackTrace();
		}

		return attachmentList;
	}

	@Override
	public void insertAnhang(Anhang anhang) throws DAOException {
		PreparedStatement ps;

		try {
			FileInputStream fin = new FileInputStream(anhang.getFile());
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(INSERT);

			ps.setString(1, anhang.getName());
			ps.setBinaryStream(2, fin, (int) anhang.getFile().length());
			ps.setInt(3, anhang.getArtikelId());

			ps.executeUpdate();

			anhang.setId(selectLastID());

			if (logger.isInfoEnabled()) {
				logger.info(anhang);
			}

			fin.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Integer selectLastID() throws DAOException {

		Integer lastId = null;

		try {
			PreparedStatement ps = ConnectionManager.getInstance().getConnection()
					.prepareStatement("select last_insert_id()");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				lastId = rs.getInt(1);
			}

			if (logger.isInfoEnabled()) {
				logger.info(lastId);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}

		return lastId;
	}

	@Override
	public boolean getAnhangAnzahl(Artikel artikel) throws DAOException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(ANZAHL);
			ps.setInt(1, artikel.getId());
			rs = ps.executeQuery();

			while (rs.next()) {

				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return false;
	}

}