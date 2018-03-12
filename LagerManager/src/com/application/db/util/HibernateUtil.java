package com.application.db.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static SessionFactory sessionFactory;

	private static SessionFactory buildSessionFactory() {

		try {
			StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
					.configure("hibernate.cfg.xml").build();

			// Configuration configuration = new Configuration();
			// configuration = configuration.configure("hibernate.cfg.xml");
			// System.out.println(configuration.getProperty("hibernate.connection.url"));
			// configuration.setProperty("hibernate.connection.url",
			// "jdbc:mysql://localhost:3306/lagermanager");
			// <property
			// name="hibernate.connection.url">jdbc:mysql://localhost:33306/lagermanager</property>
			// System.out.println(configuration.getProperty("hibernate.connection.url"));

			// StandardServiceRegistryBuilder builder = new
			// StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
			// sessionFactory = configuration.buildSessionFactory(builder.build());

			Metadata metaData = new MetadataSources(standardRegistry).getMetadataBuilder().build();
			sessionFactory = metaData.getSessionFactoryBuilder().build();

		} catch (Throwable th) {
			System.err.println("Enitial SessionFactory creation failed" + th);
			throw new ExceptionInInitializerError(th);
		}

		return sessionFactory;
	}

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null)
			sessionFactory = buildSessionFactory();
		return sessionFactory;
	}

}
