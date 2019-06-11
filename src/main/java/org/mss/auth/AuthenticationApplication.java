package org.mss.auth;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.TimeZone;

@SpringBootApplication
public class AuthenticationApplication implements WebMvcConfigurer {
    private final long MAX_AGE_SECS = 3600;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AuthenticationApplication.class, args);
    }

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Bean
    public MappingJackson2HttpMessageConverter defaultMessageConverter(){
        return new MappingJackson2HttpMessageConverter();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(Arrays.asList(defaultMessageConverter()));
    }

    @Bean
    public RestTemplate httpsRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        RestTemplate httpsRest = new RestTemplate(Arrays.asList(defaultMessageConverter()));
        char[] password = "password".toCharArray();
        SSLContext sslContext = SSLContextBuilder
                .create()
                .loadTrustMaterial(null, (certificate, authType) -> true)
//                .loadKeyMaterial(ResourceUtils.getFile("classpath:keystore.jks"), password, password)
//                .loadTrustMaterial(ResourceUtils.getFile("classpath:truststore.jks"), password)
                .build();

        RequestConfig TIMEOUT_CONFIG = RequestConfig.custom()
                .setConnectTimeout(100000) //the time to establish the connection with the remote host
                .setConnectionRequestTimeout(100000)
                .setSocketTimeout(100000)
                .build();


        HttpClientBuilder httpsClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(TIMEOUT_CONFIG)
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier());

        httpsRest.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpsClient.build()));
        return httpsRest;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .maxAge(MAX_AGE_SECS);
    }
}
