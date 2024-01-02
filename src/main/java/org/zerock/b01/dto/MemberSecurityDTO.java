package org.zerock.b01.dto;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@ToString
public class MemberSecurityDTO extends User  implements OAuth2User {

    private String mid,mpw, email;

    private boolean del, social;

    private Map<String , Object> props;


    public MemberSecurityDTO(String username, String password, String email, Boolean del, Boolean social,
                            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        mid = username;
        mpw = password;
        this.email = email;
        this.del = del;
        this.social = social;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return  getProps();
    }

    @Override
    public String getName() {
        return null;
    }
}
