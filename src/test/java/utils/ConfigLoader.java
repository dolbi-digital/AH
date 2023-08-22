package utils;

import java.util.Properties;

public class ConfigLoader {
    private static Properties properties;
    private static ConfigLoader configLoader;

    private ConfigLoader() {
        properties = PropertyUtils.propertyLoader("src/test/resources/config.properties");
    }

    public static ConfigLoader getInstance() {
        if(configLoader == null) {
            configLoader = new ConfigLoader();
        }
        return configLoader;
    }

    public String getApiKey() {
        String prop = properties.getProperty("api_key");
        if (prop != null) return prop;
        else throw new RuntimeException("property api_key is not specified in the config.properties file");
    }

    public String getBaseUri() {
        String prop = properties.getProperty("base_uri");
        if (prop != null) return prop;
        else throw new RuntimeException("property base_uri is not specified in the config.properties file");
    }
}