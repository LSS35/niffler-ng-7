package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserJson(
        @JsonProperty("id")
        UUID id,

        @JsonProperty("username")
        String username,

        @JsonProperty("firstname")
        String firstname,

        @JsonProperty("surname")
        String surname,

        @JsonProperty("fullname")
        String fullname,

        @JsonProperty("currency")
        CurrencyValues currency,

        @JsonProperty("photo")
        String photo,

        @JsonProperty("photoSmall")
        String photoSmall,

        @JsonProperty("auth")
        AuthJson auth
) {
    public static UserJson fromUserEntity(UserEntity userEntity) {

        return new UserJson(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getFirstname(),
                userEntity.getSurname(),
                userEntity.getFullname(),
                userEntity.getCurrency(),
                userEntity.getPhoto() != null && userEntity.getPhoto().length > 0 ? new String(userEntity.getPhoto(), StandardCharsets.UTF_8) : null,
                userEntity.getPhotoSmall() != null && userEntity.getPhotoSmall().length > 0 ? new String(userEntity.getPhotoSmall(), StandardCharsets.UTF_8) : null,
                null
        );
    }

    public static UserJson fromAuthUserEntity(AuthUserEntity authUserEntity) {

        return new UserJson(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                new AuthJson(
                        authUserEntity.getId(),
                        authUserEntity.getUsername(),
                        authUserEntity.getPassword(),
                        authUserEntity.getEnabled(),
                        authUserEntity.getAccountNonExpired(),

                        authUserEntity.getAccountNonLocked(),
                        authUserEntity.getCredentialsNonExpired(),
                        authUserEntity.getAuthorities().stream().map(AuthorityEntity::getAuthority).collect(Collectors.toList())
                )
        );
    }

}
