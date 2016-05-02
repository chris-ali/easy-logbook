package com.chrisali.easylogbook.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.chrisali.easylogbook.beans.LogbookEntry;


@Transactional
@Repository
@Component("logbookEntryDao")
public class LogbookEntryDao extends AbstractDao {

	@SuppressWarnings("unchecked")
	public List<LogbookEntry> getLogbookEntries(int id) {
		Criteria criteria = getSession().createCriteria(LogbookEntry.class);
		criteria.createAlias("logbook", "l")
				.add(Restrictions.eq("l.id", id));
		
		List<LogbookEntry> logbookEntries = criteria.list();
		closeSession();
		
		return logbookEntries;
	}
	
	public LogbookEntry getLogbookEntry(int logbookId, int entryId) {
		Criteria criteria = getSession().createCriteria(LogbookEntry.class);
		criteria.createAlias("logbook", "l")
				.add(Restrictions.eq("l.id", logbookId))
				.add(Restrictions.eq("id", entryId));
		
		LogbookEntry LogbookEntry = (LogbookEntry) criteria.uniqueResult();
		closeSession();
		
		return LogbookEntry;
	}
	
	public void createOrUpdate(LogbookEntry LogbookEntry) {
		Transaction tx = null;
		session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			session.save(LogbookEntry);
			session.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}
	
	public boolean delete(int id) {
		Query query = getSession().createQuery("delete from LogbookEntry where id=:id");
		query.setLong("id", id);
		
		boolean isDeleted = (query.executeUpdate() == 1);
		closeSession();
		
		return isDeleted;
	}
	
	public boolean exists(int logbookId, int entryId) {
		return getLogbookEntry(logbookId, entryId) != null;
	}
}
