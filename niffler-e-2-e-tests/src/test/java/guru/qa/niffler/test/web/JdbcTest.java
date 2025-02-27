package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.model.AuthJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JdbcTest {

    @Test
    void xaTransactionsCorrectDataTest() {
        UserDbClient userDbClient = new UserDbClient();
        String username = RandomDataUtils.randomUsername();

        UserJson user = userDbClient.createUser(
                new UserJson(
                        null,
                        username,
                        "SuccessJdbc" + RandomDataUtils.randomName(),
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
        System.out.println(user.toString());
        assertNotNull(user.id()); //возвращается часть Userdata и если есть id значит создалась запись
    }

    @Test
    void xaTransactionsWrongDataTest() {
        UserDbClient userDbClient = new UserDbClient();
        String username = RandomDataUtils.randomUsername();
        try {
            UserJson user = userDbClient.createUser(
                    new UserJson(
                            null,
                            "failed." + username,
                            RandomDataUtils.randomString(266, 267),//превышает допустимую длину
                            RandomDataUtils.randomSurname(),
                            RandomDataUtils.randomSentence(2),
                            CurrencyValues.RUB,
                            null,
                            null,
                            new AuthJson(
                                    null,
                                    "failed." + username,
                                    RandomDataUtils.randomSentence(1),
                                    true,
                                    true,
                                    true,
                                    true,
                                    Arrays.asList(Authority.read, Authority.write)
                            )
                    )
            );
            System.out.println(user.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void springJdbcTest() {
        UserDbClient usersDbClient = new UserDbClient();
        UserJson user = usersDbClient.createUser(
                new UserJson(
                        null,
                        "valentin-3",
                        RandomDataUtils.randomString(266, 267),//превышает допустимую длину
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );
        System.out.println(user);
    }
}
