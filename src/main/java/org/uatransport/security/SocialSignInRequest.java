package org.uatransport.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialSignInRequest {
    private String id;
    private String email;
    private String name;
    private String provider;
}