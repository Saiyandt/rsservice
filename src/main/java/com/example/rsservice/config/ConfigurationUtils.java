package com.example.rsservice.config;

/**
 * Created by tnguyen on 11/19/18.
 */

import com.amazonaws.ClientConfiguration;

/**
 * Provides configuration related utilities common to both the producer and consumer.
 */
public class ConfigurationUtils {

    private static final String APPLICATION_NAME = "amazon-kinesis-sender";
    private static final String VERSION = "1.0.0";

    public static ClientConfiguration getClientConfigWithUserAgent() {
        final ClientConfiguration config = new ClientConfiguration();
        final StringBuilder userAgent = new StringBuilder(ClientConfiguration.DEFAULT_USER_AGENT);

        // Separate fields of the user agent with a space
        userAgent.append(" ");
        // Append the application name followed by version number of the sample
        userAgent.append(APPLICATION_NAME);
        userAgent.append("/");
        userAgent.append(VERSION);

        config.setUserAgentPrefix(userAgent.toString());
        config.setUserAgentSuffix(null);

        return config;
    }

}
