package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static guru.qa.niffler.data.Databases.*;
import static java.sql.Connection.TRANSACTION_READ_UNCOMMITTED;

public class UserDbClient {
    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public UserJson createUserSpringJdbc(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl())).create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(e -> {
            AuthorityEntity ae = new AuthorityEntity();
            ae.setUser(createdAuthUser);
            ae.setAuthority(e);
            return ae;
        }).toArray(AuthorityEntity[]::new);

        new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl())).create(authorityEntities);

        return UserJson.fromUserEntity(
                new UserdataUserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl()))
                        .create(UserEntity.fromJson(user))
        );
    }

    public UserJson createUser(UserJson userJson) {

        //сначала создаем в БД userdata
        //потом в БД auth

        Databases.XaFunction<UserJson> xaFunctionUserData = new Databases.XaFunction<>(connection -> {
            UserEntity userEntity = UserEntity.fromJson(userJson);
            return UserJson.fromUserEntity(new UserdataUserDAOJdbc(connection).create(userEntity));
        }, CFG.userdataJdbcUrl());

        Databases.XaFunction<UserJson> xaFunctionAuth = new Databases.XaFunction<>(connection -> {
            AuthUserEntity authUserEntity = new AuthUserDaoJdbc(connection).create(AuthUserEntity.fromJson(userJson));
            new AuthAuthorityDaoJdbc(connection).create((AuthorityEntity[]) authUserEntity.getAuthorities().toArray());

            return UserJson.fromAuthUserEntity(authUserEntity);
        }, CFG.authJdbcUrl());

        return xaTransaction(TRANSACTION_READ_UNCOMMITTED, xaFunctionAuth, xaFunctionUserData);
    }
}
