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

import com.chrisali.easylogbook.beans.enums.CategoryRating;
import com.chrisali.easylogbook.beans.enums.ClassRating;
import com.chrisali.easylogbook.beans.enums.PilotExamination;
import com.chrisali.easylogbook.beans.enums.PilotLicense;
import com.chrisali.easylogbook.beans.enums.PilotMedical;
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
	
	private PilotExamination pilotExamination;
	private PilotLicense pilotLicense;
	private ClassRating classRating;
	private CategoryRating categoryRating;
	private PilotMedical pilotMedical;
	
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

	public PilotDetail(int id, User user, String date, PilotExamination pilotExamination, PilotLicense pilotLicense,
			ClassRating classRating, CategoryRating categoryRating, PilotMedical pilotMedical, String endorsement,
			String typeRating) {
		this.id = id;
		this.user = user;
		this.date = date;
		this.pilotExamination = pilotExamination;
		this.pilotLicense = pilotLicense;
		this.classRating = classRating;
		this.categoryRating = categoryRating;
		this.pilotMedical = pilotMedical;
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

	public PilotExamination getPilotExamination() {
		return pilotExamination;
	}

	public void setPilotExamination(PilotExamination pilotExamination) {
		this.pilotExamination = pilotExamination;
	}

	public PilotLicense getPilotLicense() {
		return pilotLicense;
	}

	public void setPilotLicense(PilotLicense pilotLicense) {
		this.pilotLicense = pilotLicense;
	}

	public ClassRating getClassRating() {
		return classRating;
	}

	public void setClassRating(ClassRating classRating) {
		this.classRating = classRating;
	}

	public CategoryRating getCategoryRating() {
		return categoryRating;
	}

	public void setCategoryRating(CategoryRating categoryRating) {
		this.categoryRating = categoryRating;
	}

	public PilotMedical getPilotMedical() {
		return pilotMedical;
	}

	public void setPilotMedical(PilotMedical pilotMedical) {
		this.pilotMedical = pilotMedical;
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
		return "PilotDetail [id=" + id + ", user=" + user + ", date=" + date + ", pilotExamination=" + pilotExamination
				+ ", pilotLicense=" + pilotLicense + ", classRating=" + classRating + ", categoryRating="
				+ categoryRating + ", pilotMedical=" + pilotMedical + ", endorsement=" + endorsement + ", typeRating="
				+ typeRating + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoryRating == null) ? 0 : categoryRating.hashCode());
		result = prime * result + ((classRating == null) ? 0 : classRating.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((endorsement == null) ? 0 : endorsement.hashCode());
		result = prime * result + id;
		result = prime * result + ((pilotExamination == null) ? 0 : pilotExamination.hashCode());
		result = prime * result + ((pilotLicense == null) ? 0 : pilotLicense.hashCode());
		result = prime * result + ((pilotMedical == null) ? 0 : pilotMedical.hashCode());
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
		if (categoryRating != other.categoryRating)
			return false;
		if (classRating != other.classRating)
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
		if (pilotExamination != other.pilotExamination)
			return false;
		if (pilotLicense != other.pilotLicense)
			return false;
		if (pilotMedical != other.pilotMedical)
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
