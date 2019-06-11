package org.mss.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "cus.security")
@Component
public class AuthProperties {
    private List<AuthObject> auth;

    public List<AuthObject> getAuth() {
        return auth;
    }

    public void setAuth(List<AuthObject> auth) {
        this.auth = auth;
    }
}