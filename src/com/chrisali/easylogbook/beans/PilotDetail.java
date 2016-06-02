package com.chrisali.easylogbook.beans;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.chrisali.easylogbook.beans.enums.CategoryRatings;
import com.chrisali.easylogbook.beans.enums.ClassRatings;
import com.chrisali.easylogbook.beans.enums.PilotExaminations;
import com.chrisali.easylogbook.beans.enums.PilotLicenses;
import com.chrisali.easylogbook.beans.enums.PilotMedicals;
import com.chrisali.easylogbook.validation.FormValidationGroup;
import com.chrisali.easylogbook.validation.PersistenceValidationGroup;

@Entity
@Table(name="pilot_details")
public class PilotDetail implements Serializable {

	private static final long serialVersionUID = 5576110348083539516L;
	
	@Id
	@GeneratedValue
	private int id;
	
	@ManyToOne
	@JoinColumn(name="username", referencedColumnName="username")
	private User user;
	
	@NotBlank(groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private String date;
	
	private PilotExaminations pilotExaminations;
	private PilotLicenses pilotLicenses;
	private ClassRatings classRatings;
	private CategoryRatings categoryRatings;
	private PilotMedicals pilotMedicals;
	
	@Size(min=0, max=60, groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private String endorsement;
	
	@Size(min=0, max=60, groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private String typeRating;
	
	public PilotDetail() {
		this.user = new User();
	}
	
	public PilotDetail(User user, String date) {
		this.user = user;
		this.date = date;
	}

	public PilotDetail(int id, User user, String date, PilotExaminations pilotExaminations, PilotLicenses pilotLicenses,
			ClassRatings classRatings, CategoryRatings categoryRatings, PilotMedicals pilotMedicals, String endorsement,
			String typeRating) {
		this.id = id;
		this.user = user;
		this.date = date;
		this.pilotExaminations = pilotExaminations;
		this.pilotLicenses = pilotLicenses;
		this.classRatings = classRatings;
		this.categoryRatings = categoryRatings;
		this.pilotMedicals = pilotMedicals;
		this.endorsement = endorsement;
		this.typeRating = typeRating;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public PilotExaminations getPilotExaminations() {
		return pilotExaminations;
	}

	public void setPilotExaminations(PilotExaminations pilotExaminations) {
		this.pilotExaminations = pilotExaminations;
	}

	public PilotLicenses getPilotLicenses() {
		return pilotLicenses;
	}

	public void setPilotLicenses(PilotLicenses pilotLicenses) {
		this.pilotLicenses = pilotLicenses;
	}

	public ClassRatings getClassRatings() {
		return classRatings;
	}

	public void setClassRatings(ClassRatings classRatings) {
		this.classRatings = classRatings;
	}

	public CategoryRatings getCategoryRatings() {
		return categoryRatings;
	}

	public void setCategoryRatings(CategoryRatings categoryRatings) {
		this.categoryRatings = categoryRatings;
	}

	public PilotMedicals getPilotMedicals() {
		return pilotMedicals;
	}

	public void setPilotMedicals(PilotMedicals pilotMedicals) {
		this.pilotMedicals = pilotMedicals;
	}

	public String getEndorsement() {
		return endorsement;
	}

	public void setEndorsement(String endorsement) {
		this.endorsement = endorsement;
	}

	public String getTypeRating() {
		return typeRating;
	}

	public void setTypeRating(String typeRating) {
		this.typeRating = typeRating;
	}

	@Override
	public String toString() {
		return "PilotDetail [id=" + id + ", user=" + user + ", date=" + date + ", pilotExaminations="
				+ pilotExaminations + ", pilotLicenses=" + pilotLicenses + ", classRatings=" + classRatings
				+ ", categoryRatings=" + categoryRatings + ", pilotMedicals=" + pilotMedicals + ", endorsement="
				+ endorsement + ", typeRating=" + typeRating + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoryRatings == null) ? 0 : categoryRatings.hashCode());
		result = prime * result + ((classRatings == null) ? 0 : classRatings.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((endorsement == null) ? 0 : endorsement.hashCode());
		result = prime * result + id;
		result = prime * result + ((pilotExaminations == null) ? 0 : pilotExaminations.hashCode());
		result = prime * result + ((pilotLicenses == null) ? 0 : pilotLicenses.hashCode());
		result = prime * result + ((pilotMedicals == null) ? 0 : pilotMedicals.hashCode());
		result = prime * result + ((typeRating == null) ? 0 : typeRating.hashCode());
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
		PilotDetail other = (PilotDetail) obj;
		if (categoryRatings != other.categoryRatings)
			return false;
		if (classRatings != other.classRatings)
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (endorsement == null) {
			if (other.endorsement != null)
				return false;
		} else if (!endorsement.equals(other.endorsement))
			return false;
		if (id != other.id)
			return false;
		if (pilotExaminations != other.pilotExaminations)
			return false;
		if (pilotLicenses != other.pilotLicenses)
			return false;
		if (pilotMedicals != other.pilotMedicals)
			return false;
		if (typeRating == null) {
			if (other.typeRating != null)
				return false;
		} else if (!typeRating.equals(other.typeRating))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
}
