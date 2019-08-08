package org.coderdreams.webapp;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.wicket.WicketRuntimeException;
import org.coderdreams.util.CustomerSpecific;
import org.coderdreams.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PanelFactory {
	private final static Logger logger = LoggerFactory.getLogger(PanelFactory.class);

	private Map<Class<?>, Constructor<?>> map = null;
	private static final String packageName = "org.coderdreams.webapp";
	
	public void initFactory() {
		if(Utils.getCustomer() == null) {
			return;
		}
		map = new HashMap<>(0);

		Set<Class<?>> annotatedTypes = Utils.getCustomerSpecificTypes(packageName);
		for (Class<?> c : annotatedTypes) {
			try {
				CustomerSpecific cs = c.getAnnotation(CustomerSpecific.class);
				if(!Utils.getCustomer().equals(cs.customer())) {
					continue;
				}

				Constructor<?> construct = c.getDeclaredConstructor(String.class);
				if (construct != null) {
					Class<?> replaceClass = cs.replace();
					if(map.containsKey(replaceClass)) {
						throw new WicketRuntimeException(replaceClass.getName() + " has more than 1 @CustomerSpecific implementation");
					}
					map.put(replaceClass, construct);
				}
			} catch (NoSuchMethodException | SecurityException e) {
				logger.error("Failed to get constructor for {}", c.getName(), e);
			}
		}
	}


	public <T> T newInstance(Class<T> panelClass, String id, Supplier<T> defaultPanel) {
		if(Utils.getCustomer() == null) {
			return defaultPanel.get();
		}
		
		Constructor<?> c = map.getOrDefault(panelClass, null);
		if (c == null) {
			return defaultPanel.get();
		}
		try {
			return (T) c.newInstance(id);
		} catch (Exception e) {
			logger.error("Failed newInstance for {} using defaultPanel", c.getName(), e);
			return defaultPanel.get();
		}
	}

}
