package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record AuthJson(
        @JsonProperty("id")
        UUID id,

        @JsonProperty("username")
        String username,

        @JsonProperty("username")
        String password,

        @JsonProperty("enabled")
        Boolean enabled,

        @JsonProperty("accountNonExpired")
        Boolean accountNonExpired,

        @JsonProperty("accountNonLocked")
        Boolean accountNonLocked,

        @JsonProperty("credentialsNonExpired")
        Boolean credentialsNonExpired,

        @JsonProperty("authorities")
        List<Authority> authorities
) {
}
