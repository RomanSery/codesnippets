package org.coderdreams.dom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "institution")
public class Institution extends BaseEntity {
	private static final long serialVersionUID=1L;

	@Column(name = "name", nullable = false)
	private String name;

	public Institution() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}