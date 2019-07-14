package org.romansery.enums;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.romansery.util.Utils;

public interface GenericEnum extends Serializable {
		
	default boolean equals(GenericEnum obj) {
        return obj != null && (this.getId() == obj.getId());
    }		

	String getDescription();
	void setDescription(String description);
	boolean isHidden();
	void setHidden(boolean hidden);
	int getId();

    static <T extends Enum<?> & GenericEnum> List<T> configure(Class<T> classType, T[] enumValues) {

        Properties props = Utils.getMergedEnumProperties();
        if (props != null) {
            for (T s : enumValues) {
                String desc = props.getProperty(classType.getSimpleName()+ '.' + s.name() + ".desc");
                String hidden = props.getProperty(classType.getSimpleName()+ '.' + s.name() + ".hidden");

                if (!StringUtils.isBlank(desc)) {
                    s.setDescription(desc);
                }
                if (!StringUtils.isBlank(hidden)) {
                    s.setHidden(BooleanUtils.toBoolean(hidden, "1", "0"));
                }
            }
        }
	
        Set<Integer> ids = new HashSet<>();
		for (T s : enumValues) {
			if (ids.contains(s.getId())) {
				throw new RuntimeException(s.name() + " uses existing ID");
			}
			ids.add(s.getId());
		}

		return Arrays.stream(enumValues).filter(e -> !e.isHidden()).collect(Collectors.toUnmodifiableList());
	}
	
	
}
