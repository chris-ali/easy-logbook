package com.chrisali.easylogbook.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.chrisali.easylogbook.validation.FormValidationGroup;
import com.chrisali.easylogbook.validation.PersistenceValidationGroup;
import com.chrisali.easylogbook.validation.ValidEmail;

/**
 * Easy Logbook account bean used by Spring Security for user management
 * 
 * @author Christopher Ali
 *
 */
@Entity
@Table(name="users")
public class User implements Serializable {

	private static final long serialVersionUID = 3185463234356067448L;
	
	@Id
	@NotBlank
	@Size(min=5, max= 45, groups={PersistenceValidationGroup.class, FormValidationGroup.class}) 
	@Pattern(regexp="^\\w{5,}$", groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private String username;
	
	@NotBlank
	@Size(min=5, max= 100, groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private String name;
	
	@NotBlank(groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	//@Pattern(regexp="^\\S+$", groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	@Size(min=5, max=20, groups={FormValidationGroup.class})
	private String password;
	private String oldPassword;
	
	@ValidEmail(min=6, groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private String email;
	
	private boolean enabled;
	private String authority;
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
	private List<Logbook> logbooks;
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
	private List<PilotDetail> pilotDetails;
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
	private List<Aircraft> aircraft;
	
	public User() {}
	
	public User(String username, String name, String password, String email, boolean enabled, String authority) {
		this.username = username;
		this.name = name;
		this.password = password;
		this.email = email;
		this.enabled = enabled;
		this.authority = authority;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public String getAuthority() {
		return authority;
	}
	
	public void setAuthority(String authority) {
		this.authority = authority;
	}
		
	public List<Logbook> getLogbooks() {
		return logbooks;
	}

	public void setLogbooks(List<Logbook> logbooks) {
		this.logbooks = logbooks;
	}

	public List<PilotDetail> getPilotDetails() {
		return pilotDetails;
	}

	public void setPilotDetails(List<PilotDetail> pilotDetails) {
		this.pilotDetails = pilotDetails;
	}

	public List<Aircraft> getAircraft() {
		return aircraft;
	}

	public void setAircraft(List<Aircraft> aircraft) {
		this.aircraft = aircraft;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", name=" + name + ", password=" + password + ", oldPassword="
				+ oldPassword + ", email=" + email + ", enabled=" + enabled + ", authority=" + authority + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authority == null) ? 0 : authority.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((oldPassword == null) ? 0 : oldPassword.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		User other = (User) obj;
		if (authority == null) {
			if (other.authority != null)
				return false;
		} else if (!authority.equals(other.authority))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (enabled != other.enabled)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (oldPassword == null) {
			if (other.oldPassword != null)
				return false;
		} else if (!oldPassword.equals(other.oldPassword))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
}
