package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.model.AuthJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SimpleTest {

    @Test
    void xaTransactionsCorrectDataTest() {
        UserDbClient userDbClient = new UserDbClient();
        String username = RandomDataUtils.randomUsername();

        UserJson user = userDbClient.createUser(
                new UserJson(
                        null,
                        username,
                        RandomDataUtils.randomName(),
                        RandomDataUtils.randomSurname(),
                        RandomDataUtils.randomSentence(2),
                        CurrencyValues.RUB,
                        null,
                        null,
                        new AuthJson(
                                null,
                                username,
                                RandomDataUtils.randomSentence(1),
                                true,
                                true,
                                true,
                                true,
                                Arrays.asList(Authority.read, Authority.write)
                        )
                )
        );

        assertNotNull(user.id()); //возвращается часть Userdata и если есть id значит создалась запись
    }

    @Test
    void xaTransactionsWrongDataTest() {
        UserDbClient userDbClient = new UserDbClient();
        String username = RandomDataUtils.randomUsername();

        UserJson user = userDbClient.createUser(
                new UserJson(
                        null,
                        username + "failed",
                        RandomDataUtils.randomName(),
                        RandomDataUtils.randomSurname(),
                        RandomDataUtils.randomSentence(2),
                        CurrencyValues.RUB,
                        null,
                        null,
                        new AuthJson(
                                null,
                                RandomDataUtils.randomString(266, 267), //превышает допустимую длину
                                RandomDataUtils.randomSentence(1),
                                true,
                                true,
                                true,
                                true,
                                Arrays.asList(Authority.read, Authority.write)
                        )
                )
        );
    }
}
