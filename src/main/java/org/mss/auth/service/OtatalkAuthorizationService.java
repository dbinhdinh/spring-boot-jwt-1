package org.mss.auth.service;

import org.mss.auth.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Custom Authorization service, may using for RBAC or ABAC
 */
@Service
public class OtatalkAuthorizationService {
  public boolean isValid(UsernamePasswordAuthenticationToken authentication) {
    for (GrantedAuthority authority : ((User) authentication.getPrincipal()).getAuthorities()) {
      if (authority.getAuthority().equalsIgnoreCase("ROLE_CLIENT")) {
        return true;
      }
    }
    return false;
  }


  public boolean hasRole(String expectRole) {
    for (GrantedAuthority authority : ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities()) {
      if (authority.getAuthority().equalsIgnoreCase(expectRole) || authority.getAuthority().equalsIgnoreCase("ROLE_" + expectRole)) {
        return true;
      }
    }
    return false;
  }

}
