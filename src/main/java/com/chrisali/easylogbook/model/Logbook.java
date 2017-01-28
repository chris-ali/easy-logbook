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

/**
 * Logbook bean tied to {@link User}, which {@link LogbookEntry} beans are tied to
 * 
 * @author Christopher Ali
 *
 */
@Entity
@Table(name="logbooks")
public class Logbook implements Serializable {

	private static final long serialVersionUID = 137834015011735687L;

	@Id
	@GeneratedValue
	private int id;
	
	@ManyToOne
	@JoinColumn(name="username")
	private User user;

	@Size(min=5, max= 45) 
	private String name;
		
	@OneToMany(mappedBy="logbook", cascade=CascadeType.ALL)
	private List<LogbookEntry> logbookEntries;
	
	public Logbook() {
		this.user = new User();
	}

	public Logbook(User user, String name) {
		this.user = user;
		this.name = name;
	}

	public Logbook(int id, User user, String name) {
		this.id = id;
		this.user = user;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Logbook [id=" + id + ", user=" + user + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Logbook other = (Logbook) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
}
