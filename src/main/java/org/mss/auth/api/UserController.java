package org.mss.auth.api;

import org.mss.auth.model.Cart;
import org.mss.auth.model.User;
import org.mss.auth.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth/user")
public class UserController {

    String url = "http://localhost:38112/cart/";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RestTemplate httpsRestTemplate;

    @GetMapping("/me")
    public User getUser(UsernamePasswordAuthenticationToken authentication) {
        return (User) authentication.getPrincipal();
    }

    @GetMapping("/get")
    public void callGet() {
        Map<String, String> params = new HashMap<>();
        params.put("id", "10");
        params.put("name", "11");
        Object res = HttpUtil.create(httpsRestTemplate)
                .url(url)
                .withParams(params)
                .withHeader(HttpHeaders.AUTHORIZATION, "bearer token ASDLJHASLDJKHASLDJ_ASDJASLKDJHALSJKDH_ASD")
                .get(Object.class);
        System.out.println(res);
    }

    @GetMapping("/post")
    @PreAuthorize("hasRole(MASTER)")
    public void postRequest() throws Exception {
        Cart cart = new Cart();
        cart.setId("request id: ");
        cart.setName("request name: ");
        cart.setDetail("detail, " + UUID.randomUUID().toString());

        Cart response = HttpUtil.create(httpsRestTemplate)
                .url(url)
                .withHeader(HttpHeaders.AUTHORIZATION, "bearer token ASDLJHASLDJKHASLDJ_ASDJASLKDJHALSJKDH_ASD")
                .post(cart, Cart.class);
        System.out.println(response);
    }

}
