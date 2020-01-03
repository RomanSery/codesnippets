package org.coderdreams.dom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.coderdreams.enums.StatusType;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "institution")
public class Institution extends BaseEntity {
	private static final long serialVersionUID=1L;

	@Column(name = "name", nullable = false)
	private String name;

	@Type(type="org.coderdreams.hibernate.StatusTypeEnumUserType")
	@Column(name = "status_type", nullable = false)
	private StatusType status;

	public Institution() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StatusType getStatus() {
		return status;
	}

	public void setStatus(StatusType status) {
		this.status = status;
	}
}