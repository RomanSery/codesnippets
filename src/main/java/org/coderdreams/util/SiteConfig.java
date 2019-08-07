package org.coderdreams.util;

import java.util.Properties;

import org.apache.commons.lang3.BooleanUtils;

public enum SiteConfig {
    SHOW_INACTIVE_USERS_AS_TEMP("no"),
    USER_DISPLAY_TYPE("LAST_FIRST")
    ;

    private String value;
    SiteConfig(String value) {
        this.value = value;
    }

    public boolean getBoolean() {
        return BooleanUtils.toBoolean(value);
    }

    public <T extends Enum<T>> T getEnum(final Class<T> enumType) {
        return Enum.valueOf(enumType, value);
    }


    static {
        String customer = Utils.getCustomer();
        if (customer != null) {
            loadFromProperties(Utils.loadProperties("site_config_" + customer + ".properties"));
        }
    }

    private static void loadFromProperties(Properties customerProps) {
        if (customerProps != null) {
            SiteConfig[] arr = SiteConfig.values();
            for (SiteConfig s : arr) {
                if (customerProps.containsKey(s.name())) {
                    s.value = customerProps.getProperty(s.name());
                }
            }
        }
    }
}
