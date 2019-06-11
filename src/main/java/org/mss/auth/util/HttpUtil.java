package org.mss.auth.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 *
 */
public final class HttpUtil {
    private HttpHeaders requestHeaders;
    private RestTemplate restTemplate;
    private String url;
    private UriComponentsBuilder uriBuilder;

    private HttpUtil() {
    }

    public static HttpUtil create(RestTemplate restTemplate) {
        HttpUtil instance = new HttpUtil();
        instance.restTemplate = restTemplate;
        instance.requestHeaders = new HttpHeaders();
        instance.requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        instance.requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return instance;
    }

    public HttpUtil withConverter(HttpMessageConverter<?> messageConverter) {
        boolean converterNotExist = false;
        for (HttpMessageConverter<?> converter : this.restTemplate.getMessageConverters()) {
            if (!converter.getClass().equals(messageConverter.getClass())) {
                converterNotExist = true;
            }
        }
        if (converterNotExist) {
            this.restTemplate.getMessageConverters().add(messageConverter);
        }
        return this;
    }

    public HttpUtil url(String url) {
        this.url = url;
        this.uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        return this;
    }

    public HttpUtil withHeader(String k, String v) {
        this.requestHeaders.add(k, v);
        return this;
    }

    public HttpUtil withHeaders(Map<String, String> headerParams) {
        if (Objects.isNull(headerParams) || headerParams.isEmpty()) {
            return this;
        }
        headerParams.forEach((k, v) -> withHeader(k, v));
        return this;
    }

    public HttpUtil withParam(String k, String v) {
        this.uriBuilder.queryParam(k, v);
        return this;
    }

    public HttpUtil withParams(Map<String, String> queryParams) {
        if (Objects.isNull(queryParams) || queryParams.isEmpty()) {
            return this;
        }
        queryParams.forEach((k, v) -> withParam(k, v));
        return this;
    }

    public <E> E post(Object requestObject, Class<E> clazz) {
        try {
            ResponseEntity<E> responseEntity = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.POST,
                    new HttpEntity<>(requestObject, requestHeaders),
                    clazz
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                System.out.println("user response retrieved ");
                return responseEntity.getBody();
            } else {
                throw new RuntimeException("An error occur when calling url: " + url);
            }
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RuntimeException("An error occur when calling url: " + url);
        } finally {
            this.restTemplate = null;
            this.requestHeaders.clear();
            this.requestHeaders = null;
        }
    }

    public String get() {
        return this.get(String.class);
    }

    public <E> E get(Class<E> clazz) {
        try {
            ResponseEntity<E> response = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    new HttpEntity<>(requestHeaders),
                    clazz);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("user response retrieved ");
                return response.getBody();
            } else {
                throw new RuntimeException("An error occur when calling url: " + url);
            }
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RuntimeException("An error occur when calling url: " + url);
        } finally {
            this.restTemplate = null;
            this.requestHeaders.clear();
            this.requestHeaders = null;
        }
    }

}
