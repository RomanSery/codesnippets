package org.coderdreams.hibernate;

import org.coderdreams.enums.StatusType;


public class StatusTypeEnumUserType extends BaseEnumUserType<StatusType> {

	private static final long serialVersionUID = 1L;
	public StatusTypeEnumUserType() {
		super(StatusType.class);
	}	

	@Override
	protected StatusType getById(int id) {
		for(StatusType value : StatusType.VALUES) {
            if(id == value.getId()) return value;
        }
		return null;
	}
	@Override
	protected int getValue(StatusType obj) {
		return obj.getId();
	}  
}