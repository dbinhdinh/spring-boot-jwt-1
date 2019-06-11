package org.mss.auth.api;

import org.mss.auth.exception.ResourceNotFoundException;
import org.mss.auth.model.TokenInfo;
import org.mss.auth.model.User;
import org.mss.auth.repository.UserRepository;
import org.mss.auth.service.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private AuthServiceImpl authService;

    @PostMapping("/login")
    public TokenInfo authenticateUser(@RequestParam String userName, @RequestParam String password) {
        userRepository.findByEmail(userName).orElseThrow(() -> new ResourceNotFoundException("User", "username", ""));

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authService.generateToken((User) authentication.getPrincipal());
    }

    /**
     * Re-login by using refresh token
     *
     * @return
     */
    @PostMapping("/token/refresh")
    public TokenInfo refreshToken(@RequestParam String refreshToken, HttpServletRequest request) {

        if (!this.authService.validateToken(refreshToken)) {
            throw new RuntimeException("Token Invalid");
        }

        Long userId = this.authService.getUserIdFromJWT(refreshToken);
        return authService.generateToken(authService.loadUserById(userId));
    }

    /**
     * Revoke/delete/remove/ token
     */
    @PostMapping("/token/revoke")
    public Boolean revokeToken(@RequestParam String token) {
        //TODO: remove token
        return true;
    }

}
