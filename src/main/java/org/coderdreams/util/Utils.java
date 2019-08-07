package org.coderdreams.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public final class Utils {

	private static String customer = null;
	private static Properties mergedEnumProperties = null;

    private Utils() {
        throw new IllegalStateException("Utility class");
    }


    public static Properties getMergedEnumProperties() {
	    if(mergedEnumProperties == null) {
            mergedEnumProperties = Utils.getMergedProperties("enum_props.properties", "enum_props_" + Utils.getCustomer() + ".properties");
        }
        return mergedEnumProperties;
    }

	private static Properties getMergedProperties(String defaultPath, String clientPath) {
        Properties merged = new Properties();

        Properties defaultProps = loadProperties(defaultPath);
        if(defaultProps != null) {
            merged.putAll(defaultProps);
        }

        if(clientPath != null) {
            Properties clientProps = loadProperties(clientPath);
            if(clientProps != null) {
                merged.putAll(clientProps);
            }
        }

        return merged;
    }


    public static Properties loadProperties(String path) {
        Properties properties = new Properties();
        try (
                InputStream in = Utils.class.getClassLoader().getResourceAsStream(path);
                Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8.name());
        ) {
            properties.load(reader);
            return properties;
        } catch (Exception ex) {
            //ignore
        }

        return null;
    }

    public static String getCustomer() {
        if(customer == null) {
            customer = getVariable("CUSTOMER");
        }
        return customer;
    }

    private static String getVariable(String name) {
        String result = System.getProperty(name);
        if (StringUtils.isBlank(result)) {
            result = System.getenv(name);
        }
        return result;
    }
}
