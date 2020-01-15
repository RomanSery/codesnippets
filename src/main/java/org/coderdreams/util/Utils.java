package org.coderdreams.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public final class Utils {

	private static String customer = null;
	private static Properties mergedEnumProperties = null;

    private static final Pattern replaceNonAlphaNumericExceptSpace = Pattern.compile("[^a-zA-Z0-9\\s]");

    private Utils() {
        throw new IllegalStateException("Utility class");
    }


    public static Set<Class<?>> getCustomerSpecificTypes(String packageName) {
        TypeAnnotationsScanner s2 = new TypeAnnotationsScanner();
        s2.setResultFilter(s -> Objects.equals(s, CustomerSpecific.class.getName()));

        ConfigurationBuilder config = new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(new SubTypesScanner(), s2);

        Reflections reflections = new Reflections(config);

        Set<Class<?>> annotatedTypes = reflections.getTypesAnnotatedWith(CustomerSpecific.class);
        return annotatedTypes == null ? Collections.emptySet() : annotatedTypes;
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

    public static String getVariable(String name) {
        String result = System.getProperty(name);
        if (StringUtils.isBlank(result)) {
            result = System.getenv(name);
        }
        return result;
    }

    public static String mapToString(PageParameters params) {
        if(params == null || params.isEmpty()) {
            return StringUtils.EMPTY;
        }

        StringBuilder sb = new StringBuilder();
        for (String key : params.getNamedKeys()) {
            if (sb.length() > 0) sb.append('&');

            StringValue value = params.get(key);
            if(value != null) {
                sb.append(key).append('=').append(value);
            }
        }

        return sb.toString();
    }

    public static PageParameters getParamsFromMap(Map<String, String> map) {
        PageParameters params = new PageParameters();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            params.add(entry.getKey(), entry.getValue());
        }
        return params;
    }

    public static PageParameters stringToParams(String input) {
        if(input == null || input.length() == 0) {
            return new PageParameters();
        }

        PageParameters params = new PageParameters();

        String[] nameValuePairs = input.split("&");
        for (String nameValuePair : nameValuePairs) {
            String[] nameValue = nameValuePair.split("=");
            params.add(nameValue[0], nameValue.length > 1 ? nameValue[1] : "");
        }

        return params;
    }

    public static String getApproximateMatchSearchTerm(String term) {
        if(StringUtils.isBlank(term)) {
            return term;
        }

        term = term.trim();
        term = replaceNonAlphaNumericExceptSpace.matcher(term).replaceAll("");
        return term;
    }
}
