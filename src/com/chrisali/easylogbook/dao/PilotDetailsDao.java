package com.chrisali.easylogbook.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chrisali.easylogbook.beans.PilotDetail;
import com.chrisali.easylogbook.beans.User;

/**
 * DAO that communicates with MySQL using Hibernate to perform CRUD operations on {@link PilotDetail} objects
 * 
 * @author Christopher Ali
 *
 */
@Transactional
@Repository
@Component("pilotDetailsDao")
public class PilotDetailsDao extends AbstractDao {

	/**
	 * @param username
	 * @return List of all {@link PilotDetail} objects belonging to {@link User} using Hibernate Criteria
	 */
	@SuppressWarnings("unchecked")
	public List<PilotDetail> getPilotDetails(String username) {
		Criteria criteria = getSession().createCriteria(PilotDetail.class);
		criteria.createAlias("user", "u")
				.add(Restrictions.eq("u.username", username));
		
		List<PilotDetail> pilotDetails = criteria.list();
		closeSession();
		
		return pilotDetails;
	}
	
	/**
	 * @param username
	 * @return List of {@link PilotDetail} objects belonging to {@link User} using HibernateHQL containing pilotMedical
	 */
	@SuppressWarnings("unchecked")
	public List<PilotDetail> getPilotMedicalDetails(String username) {
		Criteria criteria = getSession().createCriteria(PilotDetail.class);
		criteria.createAlias("user", "u")
				.add(Restrictions.eq("u.username", username))
				.add(Restrictions.isNotNull("pilotMedical"));
		
		List<PilotDetail> pilotMedicalDetails = criteria.list();
		closeSession();
		
		return pilotMedicalDetails;
	}
	
	/**
	 * @param username
	 * @return List of {@link PilotDetail} objects belonging to {@link User} using HibernateHQL containing pilotExamination
	 */
	@SuppressWarnings("unchecked")
	public List<PilotDetail> getPilotExamDetails(String username) {
		Criteria criteria = getSession().createCriteria(PilotDetail.class);
		criteria.createAlias("user", "u")
				.add(Restrictions.eq("u.username", username))
				.add(Restrictions.isNotNull("pilotExamination"));
		
		List<PilotDetail> pilotExamDetails = criteria.list();
		closeSession();
		
		return pilotExamDetails;
	}
	
	/**
	 * @param username
	 * @return List of {@link PilotDetail} objects belonging to {@link User} using HibernateHQL containing pilotLicense
	 */
	@SuppressWarnings("unchecked")
	public List<PilotDetail> getPilotLicenseDetails(String username) {
		Criteria criteria = getSession().createCriteria(PilotDetail.class);
		criteria.createAlias("user", "u")
				.add(Restrictions.eq("u.username", username))
				.add(Restrictions.isNull("typeRating"))
				.add(Restrictions.isNull("endorsement"))
				.add(Restrictions.isNull("pilotExamination"))
				.add(Restrictions.isNull("pilotMedical"));
		
		List<PilotDetail> pilotLicenseDetails = criteria.list();
		closeSession();
		
		return pilotLicenseDetails;
	}
	
	/**
	 * @param username
	 * @return List of {@link PilotDetail} objects belonging to {@link User} using HibernateHQL containing typeRating
	 */
	@SuppressWarnings("unchecked")
	public List<PilotDetail> getPilotTypeRatingDetails(String username) {
		Criteria criteria = getSession().createCriteria(PilotDetail.class);
		criteria.createAlias("user", "u")
				.add(Restrictions.eq("u.username", username))
				.add(Restrictions.isNotNull("typeRating"))
				.add(Restrictions.isNull("endorsement"))
				.add(Restrictions.isNull("pilotMedical"))
				.add(Restrictions.isNull("pilotLicense"))
				.add(Restrictions.isNull("categoryRating"))
				.add(Restrictions.isNull("classRating"))
				.add(Restrictions.isNull("pilotMedical"));
		
		List<PilotDetail> pilotTypeRatingDetails = criteria.list();
		closeSession();
		
		return pilotTypeRatingDetails;
	}
	
	/**
	 * @param username
	 * @return List of {@link PilotDetail} objects belonging to {@link User} using HibernateHQL containing endorsement
	 */
	@SuppressWarnings("unchecked")
	public List<PilotDetail> getPilotEndorsementDetails(String username) {
		Criteria criteria = getSession().createCriteria(PilotDetail.class);
		criteria.createAlias("user", "u")
				.add(Restrictions.eq("u.username", username))
				.add(Restrictions.isNotNull("endorsement"))
				.add(Restrictions.isNull("typeRating"))
				.add(Restrictions.isNull("pilotMedical"))
				.add(Restrictions.isNull("pilotLicense"))
				.add(Restrictions.isNull("categoryRating"))
				.add(Restrictions.isNull("classRating"))
				.add(Restrictions.isNull("pilotMedical"));
		
		List<PilotDetail> pilotTypeRatingDetails = criteria.list();
		closeSession();
		
		return pilotTypeRatingDetails;
	}
	
	/**
	 * @param username
	 * @param id of {@link PilotDetail}
	 * @return specific pilot detail object belonging to {@link User} 
	 */
	public PilotDetail getPilotDetail(String username, int id) {
		Criteria criteria = getSession().createCriteria(PilotDetail.class);
		criteria.createAlias("user", "u")
				.add(Restrictions.eq("u.username", username))
				.add(Restrictions.eq("id", id));
		
		PilotDetail pilotDetail = (PilotDetail)criteria.uniqueResult();
		closeSession();
		
		return pilotDetail;
	}
	
	/**
	 * Creates or updates {@link PilotDetail} in database using saveOrUpdate() from Session object. 
	 * beginTransaction() starts the process, flush() is called after saveOrUpdate(), then the Transaction
	 * is committed as long as no exception is thrown, in which case the transaction is rolled back, ensuring
	 * ACID behavior of the database
	 * 
	 * @param pilotDetail
	 */
	public void createOrUpdate(PilotDetail pilotDetail) {
		Transaction tx = null;
		session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(pilotDetail);
			session.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}
	
	/**
	 * @param username
	 * @param id of {@link PilotDetail}
	 * @return if pilot detail object was successfully deleted from database using HQL
	 */
	public boolean delete(String username, int id) {
		Query query = getSession().createQuery("delete from PilotDetail where id=:id and username=:username");
		query.setInteger("id", id);
		query.setString("username", username);
		
		boolean isDeleted = (query.executeUpdate() == 1);
		closeSession();
		
		return isDeleted;
	}
}
