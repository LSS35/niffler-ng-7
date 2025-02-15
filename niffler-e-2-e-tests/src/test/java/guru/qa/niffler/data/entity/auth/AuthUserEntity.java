package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AuthUserEntity implements Serializable {
    private UUID id;
    private String username;
    private String password;
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private List<AuthorityEntity> authorities = new ArrayList<>();

    public void addAuthorities(AuthorityEntity... authorities) {
        for (AuthorityEntity authority : authorities) {
            this.authorities.add(authority);
            authority.setUser(this);
        }
    }

    public void removeAuthority(AuthorityEntity authority) {
        this.authorities.remove(authority);
        authority.setUser(null);
    }

    public static AuthUserEntity fromJson(UserJson json) {
        AuthUserEntity aue = new AuthUserEntity();
        List<AuthorityEntity> aeList = new ArrayList<>();

        aue.setId(json.auth().id());
        aue.setUsername(json.auth().username());
        aue.setPassword(json.auth().password());
        aue.setEnabled(json.auth().enabled());
        aue.setAccountNonExpired(json.auth().accountNonExpired());
        aue.setAccountNonLocked(json.auth().accountNonLocked());
        aue.setCredentialsNonExpired(json.auth().credentialsNonExpired());

        for (Authority authority : json.auth().authorities()) {
            AuthorityEntity ae = new AuthorityEntity();
            ae.setAuthority(authority);
            aeList.add(ae);
        }
        aue.setAuthorities(aeList);

        return aue;
    }

}
