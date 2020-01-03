package org.coderdreams.enums;

import java.util.List;

public enum StatusType implements GenericEnum {
	ACTIVE(1, "Active"),
	HIDDEN(2, "Hidden"),
	PENDING(3, "Pending")
	;

	public static final List<StatusType> VALUES;

	private final int id;
	private String description;
	private boolean hidden = false;

	StatusType(int id, String description) {
		this.id = id;
		this.description = description;
	}

	static {
        VALUES = GenericEnum.configure(StatusType.class, StatusType.values());
    }

	@Override public void setDescription(String description) { this.description = description; }
	@Override public String getDescription() { return description; }
	@Override public int getId() { return id; }	
	@Override public boolean isHidden() { return hidden; }
	@Override public void setHidden(boolean hidden) { this.hidden = hidden; }	

	
}