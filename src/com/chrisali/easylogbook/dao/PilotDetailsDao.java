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

@Transactional
@Repository
@Component("pilotDetailsDao")
public class PilotDetailsDao extends AbstractDao {

	@SuppressWarnings("unchecked")
	public List<PilotDetail> getPilotDetails(String username) {
		Criteria criteria = getSession().createCriteria(PilotDetail.class);
		criteria.createAlias("user", "u")
				.add(Restrictions.eq("u.username", username));
		
		List<PilotDetail> pilotDetails = criteria.list();
		closeSession();
		
		return pilotDetails;
	}
	
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
	
	@SuppressWarnings("unchecked")
	public List<PilotDetail> getPilotTypeRatingDetails(String username) {
		Criteria criteria = getSession().createCriteria(PilotDetail.class);
		criteria.createAlias("user", "u")
				.add(Restrictions.eq("u.username", username))
				.add(Restrictions.isNotNull("typeRating"))
				.add(Restrictions.isNull("endorsement"))
				.add(Restrictions.isNull("pilotMedical"))
				.add(Restrictions.isNull("pilotLicenses"))
				.add(Restrictions.isNull("categoryRatings"))
				.add(Restrictions.isNull("classRatings"))
				.add(Restrictions.isNull("pilotMedical"));
		
		List<PilotDetail> pilotTypeRatingDetails = criteria.list();
		closeSession();
		
		return pilotTypeRatingDetails;
	}
	
	@SuppressWarnings("unchecked")
	public List<PilotDetail> getPilotEndorsementDetails(String username) {
		Criteria criteria = getSession().createCriteria(PilotDetail.class);
		criteria.createAlias("user", "u")
				.add(Restrictions.eq("u.username", username))
				.add(Restrictions.isNotNull("endorsement"))
				.add(Restrictions.isNull("typeRating"))
				.add(Restrictions.isNull("pilotMedical"))
				.add(Restrictions.isNull("pilotLicenses"))
				.add(Restrictions.isNull("categoryRatings"))
				.add(Restrictions.isNull("classRatings"))
				.add(Restrictions.isNull("pilotMedical"));
		
		List<PilotDetail> pilotTypeRatingDetails = criteria.list();
		closeSession();
		
		return pilotTypeRatingDetails;
	}
	
	public PilotDetail getPilotDetail(String username, int id) {
		Criteria criteria = getSession().createCriteria(PilotDetail.class);
		criteria.createAlias("user", "u")
				.add(Restrictions.eq("u.username", username))
				.add(Restrictions.eq("id", id));
		
		PilotDetail pilotDetail = (PilotDetail)criteria.uniqueResult();
		closeSession();
		
		return pilotDetail;
	}
	
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
	
	public boolean delete(String username, int id) {
		Query query = getSession().createQuery("delete from PilotDetail where id=:id and username=:username");
		query.setInteger("id", id);
		query.setString("username", username);
		
		boolean isDeleted = (query.executeUpdate() == 1);
		closeSession();
		
		return isDeleted;
	}
}
