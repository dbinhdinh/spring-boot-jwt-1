package org.mss.auth.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenInfo {
    //    @JsonProperty(value = "access_token")
    private String accessToken;

    //    @JsonProperty(value = "token_type")
    private String tokenType;

    //    @JsonProperty(value = "expires_in")
    private long expireAt;

    //    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    private long refreshTokenExpireAt;
}
