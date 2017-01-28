package com.chrisali.easylogbook.model.utilities;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Used to convert LocalDate attributes in Hibernate entities to a Date format that MySQL can understand 
 * 
 * @author Christopher Ali
 *
 */
@Converter(autoApply = true)
public class LocalDatePersistenceConverter implements AttributeConverter<LocalDate, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDate entityValue) {
		return Date.valueOf(entityValue);
	}

	@Override
	public LocalDate convertToEntityAttribute(Date databaseValue) {
		return databaseValue.toLocalDate();
	}
}
