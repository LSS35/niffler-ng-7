package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;

import static guru.qa.niffler.data.Databases.transaction;
import static guru.qa.niffler.data.Databases.xaTransaction;

public class UserDbClient {
    private static final Config CFG = Config.getInstance();

    public UserJson createUser(UserJson userJson) {

        //сначала создаем в БД userdata
        //потом в БД auth

        Databases.XaFunction<UserJson> xaFunctionUserData = new Databases.XaFunction<>(
                connection -> {
                    UserEntity userEntity = UserEntity.fromJson(userJson);
                    return UserJson.fromUserEntity(new UserdataUserDAOJdbc(connection).createUser(userEntity));
                },
                CFG.userdataJdbcUrl()
        );

        Databases.XaFunction<UserJson> xaFunctionAuth = new Databases.XaFunction<>(
                connection -> {
                    AuthUserEntity authUserEntity = new AuthUserDaoJdbc(connection).create(AuthUserEntity.fromJson(userJson));
                    new AuthAuthorityDaoJdbc(connection).create(authUserEntity);

                    return UserJson.fromAuthUserEntity(authUserEntity);
                },
                CFG.authJdbcUrl()
        );

        return xaTransaction(xaFunctionAuth, xaFunctionUserData);
    }
}
