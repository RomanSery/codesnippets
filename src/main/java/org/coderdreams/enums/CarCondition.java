package org.coderdreams.enums;

import java.util.List;

public enum CarCondition implements GenericEnum {
	BAD(1, "Bad"),
	NORMAL(2, "Normal"),
	EXCELLENT(3, "Excellent")
	;

	public static final List<CarCondition> VALUES;

	private final int id;
	private String description;
	private boolean hidden = false;

	CarCondition(int id, String description) {
		this.id = id;
		this.description = description;
	}

	static {
        VALUES = GenericEnum.configure(CarCondition.class, CarCondition.values());
    }

	@Override public void setDescription(String description) { this.description = description; }
	@Override public String getDescription() { return description; }
	@Override public int getId() { return id; }	
	@Override public boolean isHidden() { return hidden; }
	@Override public void setHidden(boolean hidden) { this.hidden = hidden; }	

	
}