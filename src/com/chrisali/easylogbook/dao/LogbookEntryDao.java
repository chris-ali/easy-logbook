package com.chrisali.easylogbook.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.chrisali.easylogbook.beans.Aircraft;
import com.chrisali.easylogbook.beans.Logbook;
import com.chrisali.easylogbook.beans.LogbookEntry;

/**
 * DAO that communicates with MySQL using Hibernate to perform CRUD operations on {@link LogbookEntry} objects
 * 
 * @author Christopher Ali
 *
 */
@Transactional
@Repository
@Component("logbookEntryDao")
public class LogbookEntryDao extends AbstractDao {
	
	/**
	 * Implementation of {@link AbstractDao#createOrUpdate(Object)}
	 * 
	 * @param logbookEntry
	 */
	public void createOrUpdateIntoDb(LogbookEntry logbookEntry) {
		createOrUpdate(logbookEntry);
	}
	
	/**
	 * @return List of {@link LogbookEntry} objects in database using Hibernate Criteria
	 */
	@SuppressWarnings("unchecked")
	public List<LogbookEntry> getAllLogbookEntries(int logbookId) {
		Criteria criteria = getSession().createCriteria(LogbookEntry.class)
				.createAlias("logbook", "l")
				.add(Restrictions.eq("l.id", logbookId));
		
		List<LogbookEntry> logbookEntries = criteria.list();
		closeSession();
		
		return logbookEntries;
	}
	
	/**
	 * @param logbookId
	 * @param pageNumber
	 * @param resultsSize
	 * @return paginated List of {@link LogbookEntry} objects belonging to user using Hibernate Criteria 
	 */
	@SuppressWarnings("unchecked")
	public List<LogbookEntry> getPaginatedLogbookEntries(int logbookId, 
														 int pageNumber,
														 int resultsSize) {
		Criteria criteria = getSession().createCriteria(LogbookEntry.class)
				.setMaxResults(resultsSize)
				.setFirstResult(pageNumber*resultsSize)
				.addOrder(Order.asc("date"))
				.createAlias("logbook", "l")
				.add(Restrictions.eq("l.id", logbookId));
		
		List<LogbookEntry> logbookEntries = criteria.list();
		closeSession();
		
		return logbookEntries;
	}
	
	/**
	 * @param logbookId
	 * @param aircraftId
	 * @return List of {@link LogbookEntry} objects tied to {@link Aircraft}
	 */
	@SuppressWarnings("unchecked")
	public List<LogbookEntry> getLogbookEntriesByAircraft(int logbookId, int aircraftId) {
		Criteria criteria = getSession().createCriteria(LogbookEntry.class)
				.createAlias("logbook", "l")
				.add(Restrictions.eq("l.id", logbookId))
				.createAlias("aircraft", "a")
				.add(Restrictions.eq("a.id", aircraftId));
		
		List<LogbookEntry> logbookEntries = criteria.list();
		closeSession();
		
		return logbookEntries;
	}
	
	/**
	 * @param logbookId
	 * @return total number of {@link LogbookEntry} objects existing in {@link Logbook} using HQL
	 */
	public Long getTotalNumberLogbookEntries(int logbookId) {
		Query criteria = getSession().createQuery("Select count (id) from LogbookEntry le where le.logbook.id = " + logbookId);
		
		Long count = (Long)criteria.uniqueResult();
		
		closeSession();
		
		return count;
	}
	
	/**
	 * @param logbookId
	 * @param entryId
	 * @return specific {@link LogbookEntry} object
	 */
	public LogbookEntry getLogbookEntry(int logbookId, int entryId) {
		Criteria criteria = getSession().createCriteria(LogbookEntry.class)
				.add(Restrictions.eq("id", entryId))
				.createAlias("logbook", "l")
				.add(Restrictions.eq("l.id", logbookId));
		
		LogbookEntry LogbookEntry = (LogbookEntry) criteria.uniqueResult();
		closeSession();
		
		return LogbookEntry;
	}

	/**
	 * @param logbookId
	 * @param entryId
	 * @return if {@link LogbookEntry} was successfully deleted from database using HQL
	 */
	public boolean delete(int logbookId, int entryId) {
		Query query = getSession().createQuery("delete from LogbookEntry where id=:id and logbooks_id=:logbooks_id");
		query.setLong("id", entryId);
		query.setLong("logbooks_id", logbookId);
		
		boolean isDeleted = (query.executeUpdate() == 1);
		closeSession();
		
		return isDeleted;
	}
	
	/**
	 * @param logbookId
	 * @param entryId
	 * @return if specific {@link LogbookEntry} exists in database
	 */
	public boolean exists(int logbookId, int entryId) {
		return getLogbookEntry(logbookId, entryId) != null;
	}
}
