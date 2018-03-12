package com.application.db.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.application.db.util.DAOException;
import com.application.db.util.HibernateUtil;
import com.application.model.Mailconfig;

public class MailSettingsJDBCDAO implements MailSettingsDAO {

	private static final Logger logger = Logger.getLogger(Mailconfig.class);
	private Session session;

	@Override
	public void delete(Mailconfig abteilung) throws DAOException {

		session = HibernateUtil.getSessionFactory().openSession();
		Transaction tr = session.beginTransaction();

		session.delete(abteilung);

		tr.commit();

		session.close();

	}

	@Override
	public Mailconfig get(int wartungId) throws DAOException {
		return null;

	}

	@Override
	public List<Mailconfig> getAll() throws DAOException {
		Transaction transaction = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Mailconfig> query = builder.createQuery(Mailconfig.class);

			Root<Mailconfig> root = query.from(Mailconfig.class);
			query.select(root);

			Query<Mailconfig> q = session.createQuery(query);
			List<Mailconfig> data = q.getResultList();

			transaction.commit();

			return data;

		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			session.close();
		}
		return null;

	}

	@Override
	public void insert(Mailconfig data) throws DAOException {

		session = HibernateUtil.getSessionFactory().openSession();
		Transaction tr = session.beginTransaction();

		session.save(data);

		tr.commit();

		session.close();

	}

	@Override
	public void update(Mailconfig data) throws DAOException {

		session = HibernateUtil.getSessionFactory().openSession();
		Transaction tr = session.beginTransaction();

		session.update(data);

		tr.commit();

		session.close();

	}

}
