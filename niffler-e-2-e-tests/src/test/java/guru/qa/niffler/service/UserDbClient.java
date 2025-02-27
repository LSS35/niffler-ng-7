package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserdataUserDAO;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

public class UserDbClient {
    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();
    private final UserdataUserDAO udUserDao = new UserdataUserDaoSpringJdbc();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSources.dataSource(CFG.authJdbcUrl())
            )
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate (
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public UserJson createUser(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
            AuthUserEntity authUser = new AuthUserEntity();
            authUser.setUsername(user.username());
            authUser.setPassword(pe.encode("12345"));
            authUser.setEnabled(true);
            authUser.setAccountNonExpired(true);
            authUser.setAccountNonLocked(true);
            authUser.setCredentialsNonExpired(true);

            AuthUserEntity createdAuthUser = authUserDao.create(authUser);

            AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(e -> {
                AuthorityEntity ae = new AuthorityEntity();
                ae.setUser(createdAuthUser);
                ae.setAuthority(e);
                return ae;
            }).toArray(AuthorityEntity[]::new);

            authAuthorityDao.create(authorityEntities);
            return UserJson.fromUserEntity(
                    udUserDao.create(UserEntity.fromJson(user))
            );
        });

    }

//    public UserJson createUser(UserJson userJson) {
//
//        //сначала создаем в БД userdata
//        //потом в БД auth
//
//        Databases.XaFunction<UserJson> xaFunctionUserData = new Databases.XaFunction<>(connection -> {
//            UserEntity userEntity = UserEntity.fromJson(userJson);
//            return UserJson.fromUserEntity(new UserdataUserDaoJdbc(connection).create(userEntity));
//        }, CFG.userdataJdbcUrl());
//
//        Databases.XaFunction<UserJson> xaFunctionAuth = new Databases.XaFunction<>(connection -> {
//            AuthUserEntity authUserEntity = new AuthUserDaoJdbc(connection).create(AuthUserEntity.fromJson(userJson));
//            new AuthAuthorityDaoJdbc(connection).create((AuthorityEntity[]) authUserEntity.getAuthorities().toArray());
//
//            return UserJson.fromAuthUserEntity(authUserEntity);
//        }, CFG.authJdbcUrl());
//
//        return xaTransaction(TRANSACTION_READ_UNCOMMITTED, xaFunctionAuth, xaFunctionUserData);
//    }
}
