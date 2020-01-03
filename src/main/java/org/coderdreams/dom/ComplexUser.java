package org.coderdreams.dom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.coderdreams.enums.StatusType;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "complex_user")
public class ComplexUser extends BaseEntity {
	private static final long serialVersionUID=1L;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "display_name", nullable = false)
	private String displayName;

	@Type(type= "org.coderdreams.hibernate.JsonStringType")
	@Column(name = "user_details")
	private ComplexUserDetails userDetails;

	@Type(type="org.coderdreams.hibernate.StatusTypeEnumUserType")
	@Column(name = "status_type")
	private StatusType status;

	public ComplexUser() {
		super();
	}

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getDisplayName() { return displayName; }
	public void setDisplayName(String displayName) { this.displayName = displayName; }

	public StatusType getStatus() { return status; }
	public void setStatus(StatusType status) { this.status = status; }

	public ComplexUserDetails getUserDetails() {
		if(userDetails == null) {
			userDetails = new ComplexUserDetails();
		}
		return userDetails;
	}
	public void setUserDetails(ComplexUserDetails userDetails) {
		this.userDetails = userDetails;
	}
}