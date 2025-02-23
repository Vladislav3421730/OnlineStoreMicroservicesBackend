package com.example.market.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InfoContributorConfig implements InfoContributor {

    @Value("${application.name}")
    private String appName;

    @Value("${application.version}")
    private String appVersion;

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> app = new HashMap<>();
        app.put("name", appName);
        app.put("version", appVersion);
        builder.withDetail("app", app);
    }
}
