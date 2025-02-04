package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

@WebTest
public class RegisterTest {
    private static final Config CFG = Config.getInstance();
    private static final String MESSAGE_ALREADY_EXISTS = "Username `%s` already exists";
    private static final String MESSAGE_NOT_EQUAL_PASSWORDS = "Passwords should be equal";


    @Test
    void shouldRegisterNewUser() {
        String username = RandomDataUtils.randomUsername();
        String password = "userPassword";
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .goRegister()
                .register(username, password)
                .loginSuccess(username, password)
                .checkLoadPage();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .goRegister()
                .setUsernameInput(CFG.username())
                .setPasswordInput(CFG.password())
                .setPasswordSubmitInput(CFG.password())
                .submitRegistration();

        new RegisterPage().checkErrorMessage(String.format(MESSAGE_ALREADY_EXISTS, CFG.username()));
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .goRegister()
                .setUsernameInput(RandomDataUtils.randomUsername())
                .setPasswordInput(CFG.password())
                .setPasswordSubmitInput(CFG.password() + "111")
                .submitRegistration();

        new RegisterPage().checkErrorMessage(MESSAGE_NOT_EQUAL_PASSWORDS);
    }
}
