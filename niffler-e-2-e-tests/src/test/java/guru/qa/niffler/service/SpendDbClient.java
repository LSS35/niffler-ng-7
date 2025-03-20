package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.Optional;

import static java.sql.Connection.TRANSACTION_READ_UNCOMMITTED;

public class SpendDbClient {
    private static final Config CFG = Config.getInstance();
    public static final int ISOLATION_LEVEL = TRANSACTION_READ_UNCOMMITTED;

    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendJson createSpend(SpendJson spendJson) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spendJson);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(spendDao.create(spendEntity));
                }
        );
    }

    public CategoryJson addCategories(CategoryJson categoryJson) {
        return jdbcTxTemplate.execute(() -> {
                    return CategoryJson.fromEntity(categoryDao.create(CategoryEntity.fromJson(categoryJson)));
                }
        );
    }

    public CategoryJson updateCategory(CategoryJson categoryJson) {
        return jdbcTxTemplate.execute(() -> {
                    return CategoryJson.fromEntity(categoryDao.update(CategoryEntity.fromJson(categoryJson)));
                }
        );
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return jdbcTxTemplate.execute(() -> {
                    Optional<CategoryEntity> findedCategory = categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName);

                    return findedCategory.map(CategoryJson::fromEntity).stream().findFirst();
                }
        );
    }
}
