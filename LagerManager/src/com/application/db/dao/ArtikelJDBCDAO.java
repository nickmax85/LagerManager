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
import com.application.model.Artikel;
import com.application.model.Lagerort;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class ArtikelJDBCDAO implements ArtikelDAO {

	private static final Logger logger = Logger.getLogger(ArtikelJDBCDAO.class);

	private final static String SELECT_ALL = "SELECT * FROM artikel";
	private final static String INSERT = "INSERT INTO artikel(name, maschine, box, komponenteNr, hersteller, stueck, info, author, picture, lagerort_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final static String UPDATE = "UPDATE artikel SET name = ?,  hersteller = ?, maschine = ?, box = ?, komponenteNr = ?, stueck = ?, info = ?, author = ?, picture = ?, lagerort_id = ? WHERE id = ?";
	private final static String SELECT = "SELECT * FROM artikel where id = ?";
	private final static String SELECT_PICTURE = "SELECT picture FROM artikel where id = ?";
	private final static String DELETE = "DELETE FROM artikel where id = ?";

	private final static String ANZAHL = "SELECT id from anhang where artikelId = ?";

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

	public Artikel getPicture(Artikel artikel) throws DAOException {

		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			File dir = new File(System.getProperty("user.home") + File.separator + "LagerManager", "pictures");
			dir.mkdirs();

			dir.deleteOnExit();
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(SELECT_PICTURE);
			ps.setInt(1, artikel.getId());

			rs = ps.executeQuery();
			while (rs.next()) {

				// ===================================================================================
				InputStream is = rs.getBinaryStream("picture");
				if (is != null) {
					File file = new File(dir.getAbsolutePath() + File.separator + artikel.getName() + ".jpg");
					file.createNewFile();

					FileOutputStream fos = new FileOutputStream(file);
					byte[] buffer = new byte[1];
					while (is.read(buffer) > 0) {
						fos.write(buffer);
					}
					file.deleteOnExit();
					fos.close();
					artikel.setPicture(file);
				}
				// ===================================================================================

			}
			if (logger.isInfoEnabled()) {
				logger.info(artikel);
			}

		} catch (SQLException e) {

			throw new DAOException(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return artikel;

	}

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

	@Override
	public Artikel getArtikel(int id) throws DAOException {

		ResultSet rs = null;
		PreparedStatement ps = null;

		Artikel artikel = null;

		try {

			File dir = new File(System.getProperty("user.home") + File.separator + "LagerManager", "pictures");
			dir.mkdirs();

			dir.deleteOnExit();
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(SELECT);
			ps.setInt(1, id);

			rs = ps.executeQuery();
			while (rs.next()) {
				artikel = new Artikel();

				artikel.setId(rs.getInt("id"));
				artikel.setName(rs.getString("name"));
				artikel.setMaschine(rs.getString("maschine"));
				artikel.setBox(rs.getString("box"));
				artikel.setHersteller(rs.getString("hersteller"));
				artikel.setStueck(rs.getInt("stueck"));
				artikel.setInfo(rs.getString("info"));
				artikel.setAuthor(rs.getString("author"));
				artikel.setLagerortId(rs.getInt("lagerort_id"));

				// ===================================================================================
				// InputStream is = rs.getBinaryStream("picture");
				// if (is != null) {
				// File file = new File(dir.getAbsolutePath() + File.separator +
				// artikel.getName() + ".jpg");
				// file.createNewFile();
				//
				// FileOutputStream fos = new FileOutputStream(file);
				// byte[] buffer = new byte[1];
				// while (is.read(buffer) > 0) {
				// fos.write(buffer);
				// }
				// file.deleteOnExit();
				// fos.close();
				// artikel.setPicture(file);
				// }
				// ===================================================================================

				artikel.setAnhang(getAnhangAnzahl(artikel));

			}
			if (logger.isInfoEnabled()) {
				logger.info(artikel);
			}

		} catch (SQLException e) {

			throw new DAOException(e);
		}

		return artikel;

	}

	@Override
	public boolean insertArtikel(Artikel artikel) throws DAOException {
		PreparedStatement ps;
		int res = 0;

		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(INSERT);

			ps.setString(1, artikel.getName());
			ps.setString(2, artikel.getHersteller());
			ps.setString(3, artikel.getMaschine());
			ps.setString(4, artikel.getBox());
			ps.setString(5, artikel.getKomponenteNr());
			ps.setInt(6, artikel.getStueck());
			ps.setString(7, artikel.getInfo());
			ps.setString(8, artikel.getAuthor());

			if (artikel.getPicture() != null) {
				FileInputStream fin = new FileInputStream(artikel.getPicture());
				ps.setBinaryStream(9, fin, (int) artikel.getPicture().length());
			} else
				ps.setBinaryStream(9, null);

			ps.setInt(10, artikel.getLagerort().getId());

			res = ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info(artikel);
			}

		}

		catch (Exception e) {
			e.printStackTrace();
			logger.error(e);

		}

		if (res > 0) {
			artikel.setId(selectLastID());
			return true;

		} else
			return false;

	}

	@Override
	public boolean updateArtikel(Artikel artikel) throws DAOException {

		int res = 0;
		try {

			PreparedStatement ps = ConnectionManager.getInstance().getConnection().prepareStatement(UPDATE);

			ps.setString(1, artikel.getName());
			ps.setString(2, artikel.getHersteller());
			ps.setString(3, artikel.getMaschine());
			ps.setString(4, artikel.getBox());
			ps.setString(5, artikel.getKomponenteNr());
			ps.setInt(6, artikel.getStueck());
			ps.setString(7, artikel.getInfo());
			ps.setString(8, artikel.getAuthor());

			if (artikel.getPicture() != null) {
				File file = new File(artikel.getPicture().getAbsolutePath());
				InputStream fin = new FileInputStream(file);
				ps.setBinaryStream(9, fin, (int) artikel.getPicture().length());
			} else
				ps.setBinaryStream(9, null);

			ps.setInt(10, artikel.getLagerort().getId());
			ps.setInt(11, artikel.getId());

			res = ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info(artikel.getBox());
			}

		}

		catch (SQLException e) {

			if (e.getErrorCode() == 1062)
				throw new DAOException("Diese Liefernummer ist schon vorhanden.");

			e.printStackTrace();
			throw new DAOException(e);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (res > 0) {
			return true;

		} else
			return false;
	}

	@Override
	public boolean deleteArtikel(Artikel artikel) throws DAOException {

		int res = 0;

		try {
			PreparedStatement ps = ConnectionManager.getInstance().getConnection().prepareStatement(DELETE);

			ps.setInt(1, artikel.getId());
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
	public List<Artikel> getArtikelList() throws DAOException {
		ResultSet rs = null;
		Statement statement = null;
		Connection con = null;

		List<Artikel> artikelList = new ArrayList<>();

		try {
			File dir = new File(System.getProperty("user.home") + File.separator + "LagerManager", "pictures");
			dir.mkdirs();

			dir.deleteOnExit();
			con = ConnectionManager.getInstance().getConnection();

			statement = con.createStatement();
			rs = statement.executeQuery(SELECT_ALL);

			while (rs.next()) {
				Artikel artikel = new Artikel();
				artikel.setId(rs.getInt("id"));
				artikel.setName(rs.getString("name"));
				artikel.setMaschine(rs.getString("maschine"));
				artikel.setBox(rs.getString("box"));
				artikel.setKomponenteNr(rs.getString("komponenteNr"));
				artikel.setHersteller(rs.getString("hersteller"));
				artikel.setStueck(rs.getInt("stueck"));
				artikel.setInfo(rs.getString("info"));
				artikel.setAuthor(rs.getString("author"));
				artikel.setLagerortId(rs.getInt("lagerort_id"));
				// ===================================================================================
				// InputStream is = rs.getBinaryStream("picture");
				// if (is != null) {
				// File file = new File(dir.getAbsolutePath() + File.separator +
				// artikel.getName() + ".jpg");
				// file.createNewFile();
				//
				// FileOutputStream fos = new FileOutputStream(file);
				// byte[] buffer = new byte[1];
				// while (is.read(buffer) > 0) {
				// fos.write(buffer);
				// }
				// file.deleteOnExit();
				// fos.close();
				// artikel.setPicture(file);
				// }
				// ===================================================================================

				artikel.setAnhang(getAnhangAnzahl(artikel));

				artikelList.add(artikel);
				if (logger.isInfoEnabled()) {
					// logger.info(artikel);
				}
			}

		} catch (SQLException e) {

			throw new DAOException(e);
		}

		return artikelList;
	}

	@Override
	public List<String> getLiefernummern() throws DAOException {
		ResultSet rs = null;
		Statement statement = null;
		Connection con = null;

		List<String> data = new ArrayList<>();

		try {

			con = ConnectionManager.getInstance().getConnection();

			statement = con.createStatement();
			rs = statement.executeQuery(SELECT_ALL);

			while (rs.next()) {
				data.add(rs.getString("box"));

			}

		} catch (SQLException e) {

			throw new DAOException(e);
		}

		return data;
	}

}