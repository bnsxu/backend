package com.example.meettify.config.oauth;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class GoogleUser extends OAuthProviderUser{

    public GoogleUser(OAuth2User oAuth2User,
                      ClientRegistration clientRegistration) {
        super(oAuth2User.getAttributes(), oAuth2User, clientRegistration);
    }

    @Override
    public String getProvider() {
        return (String) getAttributes().get("get");
    }

    @Override
    public String getName() {
        return (String) getAttributes().get("name");
    }
}
