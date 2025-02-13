package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.Optional;

import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {
    private static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson spendJson) {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spendJson);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc(connection).create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(new SpendDaoJdbc(connection).create(spendEntity));
                },
                CFG.spendJdbcUrl()
        );
    }

    public CategoryJson addCategories(CategoryJson categoryJson) {
        return transaction(connection -> {
                    return CategoryJson.fromEntity(new CategoryDaoJdbc(connection).create(CategoryEntity.fromJson(categoryJson)));
                },
                CFG.spendJdbcUrl()
        );
    }

    public CategoryJson updateCategory(CategoryJson categoryJson) {
        return transaction(connection -> {
                    return CategoryJson.fromEntity(new CategoryDaoJdbc(connection).update(CategoryEntity.fromJson(categoryJson)));
                },
                CFG.spendJdbcUrl()
        );
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return transaction(connection -> {
                    Optional<CategoryEntity> findedCategory = new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(username, categoryName);

                    return findedCategory.map(CategoryJson::fromEntity).stream().findFirst();
                },
                CFG.spendJdbcUrl()
        );
    }
}
