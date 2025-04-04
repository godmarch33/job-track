package co.uk.offerland.job_track.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class KCRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("resource_access");

        if (realmAccess == null || !realmAccess.containsKey("job-track-client")) {
            return new ArrayList<>();
        }
        Map<String, Object> client = (Map<String, Object> )realmAccess.get("job-track-client");

        Object rolesObject = client.get("roles");
        if (!(rolesObject instanceof List<?> rolesList)) {
            return new ArrayList<>();
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        for (Object role : rolesList) {
            if (role instanceof String roleName) {
                roles.add(new SimpleGrantedAuthority("ROLE_" + roleName));
            }
        }

        return roles;
    }
}
