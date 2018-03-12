package com.application.db.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.application.util.ApplicationProperties;

public class ConnectionManager {

	private static final Logger logger = Logger.getLogger(ConnectionManager.class);

	public static ConnectionManager getInstance() {
		if (instance == null)
			instance = new ConnectionManager();
		return instance;
	}

	private static ConnectionManager instance;

	private Connection connection;

	public Connection getConnection() throws SQLException {

		String url;
		String user;
		String password;

		url = "jdbc:mysql://" + ApplicationProperties.getInstance().getProperty("db_host") + ":"
				+ ApplicationProperties.getInstance().getProperty("db_port") + "/"
				+ ApplicationProperties.getInstance().getProperty("db_model");

		user = ApplicationProperties.getInstance().getProperty("db_user");
		password = ApplicationProperties.getInstance().getProperty("db_password");

		if (connection == null || connection.isClosed())
			connection = DriverManager.getConnection(url, user, password);

		return connection;
	}
}
