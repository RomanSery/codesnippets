package org.coderdreams.util;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(java.lang.annotation.ElementType.TYPE)
public @interface CustomerSpecific {
	/**
	 * Required attribute for specifying the class that this will replace.
	 * 
	 * @return replace attr
	 */
	Class<?> replace();
	
	/**
	 * Required attribute for specifying which customer this applies to.
	 * 
	 * @return customer attr
	 */
	String customer();
}
