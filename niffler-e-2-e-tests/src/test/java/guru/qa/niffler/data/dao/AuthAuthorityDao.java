package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.List;

public interface AuthAuthorityDao {
    AuthorityEntity create(AuthorityEntity authorityEntity);

    void create(AuthorityEntity... authority);

    List<AuthorityEntity> findAll();
}
