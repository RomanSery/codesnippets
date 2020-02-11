package org.coderdreams.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ApplicationDetailsService {

    private static final Properties gitProperties;
    private static final String version;
    private static final String branch;

    static {
        gitProperties = getProperties();
        branch = gitProperties.getProperty("git.branch");
        version = gitProperties.getProperty("git.build.version");
    }

    public static String getVersion() { return version; }

    public static String getBranch() {
        return branch;
    }

    private static Properties getProperties() {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String defaultConfigPath = rootPath + "git.properties";
        Properties defaultProps = new Properties();
        try {
        	defaultProps.load(new FileInputStream(defaultConfigPath));
        } catch(IOException e) {

        }
        
        return defaultProps;
    }
}
