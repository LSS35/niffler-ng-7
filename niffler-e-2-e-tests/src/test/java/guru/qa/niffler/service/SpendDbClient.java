package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.Optional;

public class SpendDbClient {
    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    public SpendJson createSpend(SpendJson spendJson) {
        SpendEntity spendEntity = SpendEntity.fromJson(spendJson);
        if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }
        return SpendJson.fromEntity(spendDao.create(spendEntity));
    }

    public CategoryJson addCategories(CategoryJson categoryJson) {

        return CategoryJson.fromEntity(categoryDao.create(CategoryEntity.fromJson(categoryJson)));
    }

    public CategoryJson updateCategory(CategoryJson categoryJson) {

        return CategoryJson.fromEntity(categoryDao.update(CategoryEntity.fromJson(categoryJson)));
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        Optional<CategoryEntity> findedCategory = categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName);

        return findedCategory.map(CategoryJson::fromEntity).stream().findFirst();
    }
}
