package com.example.meettify.config.oauth;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.util.Map;

@RequiredArgsConstructor
@AllArgsConstructor
public abstract class OAuthProviderUser implements OAuthUserInfo {
    private final Map<String, Object> attributes;
    private final ClientRegistration clientRegistration;

    @Override
    public String getProviderId() {
        return clientRegistration.getRegistrationId();
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
