package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class AllPeoplePage {
    private final SelenideElement tablePeople = $("tbody#all");

    public AllPeoplePage checkOutcomeInvitation(String friendName) {
        tablePeople.$$("tr").find(text(friendName)).shouldHave(text("Waiting..."));
        return this;
    }
}
