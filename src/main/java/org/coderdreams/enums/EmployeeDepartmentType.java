package org.coderdreams.enums;

import java.util.List;

public enum EmployeeDepartmentType implements GenericEnum {
	DEV_TEAM(1, "Dev Team"),
	HR(2, "Human resources"),
	EXEC(3, "Executive"),
	QA(4, "Quality assurance/Testing"),
	SALES(5, "Sales"),
	PROJ_MANAGEMENT(6, "Project Managers"),
	INTERNS(7, "Interns")
	;
		
	public static final List<EmployeeDepartmentType> VALUES;

	private final int id;
	private String description;
	private boolean hidden = false;
	
	EmployeeDepartmentType(int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	static {
        VALUES = GenericEnum.configure(EmployeeDepartmentType.class, EmployeeDepartmentType.values());
    }  

	@Override public void setDescription(String description) { this.description = description; }
	@Override public String getDescription() { return description; }
	@Override public int getId() { return id; }	
	@Override public boolean isHidden() { return hidden; }
	@Override public void setHidden(boolean hidden) { this.hidden = hidden; }	

	
}