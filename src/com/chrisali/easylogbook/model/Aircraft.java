package com.chrisali.easylogbook.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.chrisali.easylogbook.validation.FormValidationGroup;
import com.chrisali.easylogbook.validation.PersistenceValidationGroup;

/**
 * Aircraft bean tied to {@link LogbookEntry} objects to simplify flight logging process
 * 
 * @author Christopher Ali
 *
 */
@Entity
@Table(name="aircraft")
public class Aircraft implements Serializable {

	private static final long serialVersionUID = 5061658878035904314L;
	
	@Id
	@GeneratedValue
	private int id;
	
	@ManyToOne
	@JoinColumn(name="username")
	private User user;
	
	@NotBlank
	@Size(min=2, max= 45, groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private String make;
	
	@NotBlank
	@Size(min=2, max= 45, groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private String model;

	@NotBlank
	@Size(min=2, max= 45, groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private String tailNumber;
	
	@OneToMany(mappedBy="aircraft", cascade=CascadeType.ALL)
	private List<LogbookEntry> logbookEntries;
	
	public Aircraft() {
		this.user = new User();
	}
	
	public Aircraft(int id, User user, String make, String model, String tailNumber) {
		this(user, make, model, tailNumber);
		this.id = id;
	}

	public Aircraft(User user, String make, String model, String tailNumber) {
		this.user = user;
		this.make = make;
		this.model = model;
		this.tailNumber = tailNumber;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getTailNumber() {
		return tailNumber;
	}

	public void setTailNumber(String tailNumber) {
		this.tailNumber = tailNumber;
	}
		
	public List<LogbookEntry> getLogbookEntries() {
		return logbookEntries;
	}

	public void setLogbookEntries(List<LogbookEntry> logbookEntries) {
		this.logbookEntries = logbookEntries;
	}

	@Override
	public String toString() {
		return "Aircraft [user=" + user + ", make=" + make + ", model=" + model + ", tailNumber="
				+ tailNumber + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((make == null) ? 0 : make.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((tailNumber == null) ? 0 : tailNumber.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aircraft other = (Aircraft) obj;
		if (make == null) {
			if (other.make != null)
				return false;
		} else if (!make.equals(other.make))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (tailNumber == null) {
			if (other.tailNumber != null)
				return false;
		} else if (!tailNumber.equals(other.tailNumber))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
}
