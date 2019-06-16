package com.nvbac.beam.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConf {

    private Properties props = new Properties();

    public AppConf() {
        try (InputStream input = AppConf.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Failed to load config.properties in your classpath. App will start with default values");
            }
            props.load(input);
        } catch (IOException e) {
            System.out.printf("Failed to load config. Exception follows. %s", e);
        }
    }

    public String get(String conf, String defaultValue) {
        return this.props.getProperty(conf, defaultValue);
    }
}