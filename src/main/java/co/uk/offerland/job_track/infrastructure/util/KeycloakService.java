package co.uk.offerland.job_track.infrastructure.util;

import co.uk.offerland.job_track.application.dto.user.UserRegister;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//@Service
public class KeycloakService {

    private static Keycloak keycloak;
    private static RealmResource realmResource;
    private static UsersResource usersResource;

    @Value("${keycloak.auth-server-url}")
    private String serverURL;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.resource}")
    private String clientID;
    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @PostConstruct
    public Keycloak initKeycloak() {
        if (keycloak == null) {
            keycloak = KeycloakBuilder.builder()
                    .realm(realm)
                    .serverUrl(serverURL)
                    .clientId(clientID)
                    .clientSecret(clientSecret)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .build();

            realmResource = keycloak.realm(realm);
            usersResource = realmResource.users();
        }

        return keycloak;
    }

    public Response createKeycloakUser(UserRegister user) {
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());
        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(user.getUsername());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        return usersResource.create(kcUser);
    }

    public Mono<String> addRoles(String userId, List<String> roles) {
        return Mono.defer(() -> Mono.fromCallable(() -> {
                    List<RoleRepresentation> kcRoles = new ArrayList<>();
                    for (String role : roles) {
                        RoleRepresentation roleRep = realmResource.roles().get(role).toRepresentation();
                        kcRoles.add(roleRep);
                    }
                    UserResource uniqueUserResource = usersResource.get(userId);
                    uniqueUserResource.roles().realmLevel().add(kcRoles);
                    return userId;
                })
                .subscribeOn(Schedulers.boundedElastic()));
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);

        return passwordCredentials;
    }
}
