package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {
    private final SelenideElement tableFriends = $("tbody#friends");
    private final SelenideElement tableRequests = $("tbody#requests");
    private final SelenideElement mainContainer = $(".MuiTableContainer-root");

    private static final String EMPTY_MESSAGE = "There are no users yet";

    public FriendsPage checkIncomeInvitation(String friendName) {
        SelenideElement requestRow = tableRequests.$$("tr").find(text(friendName));
        requestRow.should(visible);
        requestRow.shouldHave(text("Accept"));
        requestRow.shouldHave(text("Decline"));
        return this;
    }

    public FriendsPage checkEmptyTable() {
        tableRequests.should(not(exist));
        tableFriends.should(not(exist));
        mainContainer.shouldHave(text(EMPTY_MESSAGE));
        return this;
    }

    public FriendsPage checkFriendExists(String friendName) {
        SelenideElement friendRow = tableFriends.$$("tr").find(text(friendName));
        friendRow.should(visible);
        friendRow.shouldHave(text("Unfriend"));
        return this;
    }
}
