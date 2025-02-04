package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

    private final SpendDbClient spendDBClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .filter(annoUser -> annoUser.spendings().length > 0)
                .ifPresent(annoUser -> {
                    Spending anno = annoUser.spendings()[0];
                    SpendJson spend = new SpendJson(
                            null,
                            new Date(),
                            new CategoryJson(
                                    spendDBClient.findCategoryByUsernameAndCategoryName(annoUser.username(), anno.category())
                                            .map(CategoryJson::id)
                                            .stream().findFirst()
                                            .orElse(null)
                                    ,
                                    anno.category(),
                                    annoUser.username(),
                                    false
                            ),
                            CurrencyValues.RUB,
                            anno.amount(),
                            anno.description(),
                            annoUser.username()
                    );
                    SpendJson createdSpend = spendDBClient.createSpend(spend);
                    context.getStore(NAMESPACE).put(
                            context.getUniqueId(),
                            createdSpend
                    );
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), SpendJson.class);
    }
}
