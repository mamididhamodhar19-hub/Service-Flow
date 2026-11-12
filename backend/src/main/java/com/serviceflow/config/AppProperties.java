package com.serviceflow.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@Getter @Setter
public class AppProperties {

    private Jwt jwt = new Jwt();
    private Sla sla = new Sla();
    private Upload upload = new Upload();

    @Getter @Setter
    public static class Jwt {
        private String secret;
        private long expirationMs;
    }

    @Getter @Setter
    public static class Sla {
        private int low    = 48;
        private int medium = 24;
        private int high   = 4;
        private int critical = 2;
    }

    @Getter @Setter
    public static class Upload {
        private String dir = "./uploads";
        private long maxFileSize = 10_485_760L;
    }
}
