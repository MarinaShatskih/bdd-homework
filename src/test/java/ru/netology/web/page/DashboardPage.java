package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {

    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private SelenideElement heading = $("[data-test-id='dashboard']");

    public DashboardPage() {
        heading.shouldBe(visible, Duration.ofSeconds(15));
    }

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        var text = cards.findBy(Condition.text(cardInfo.getCardNumber().substring(15))).getText();
        return extractBalance(text);
    }

    public void checkCardBalance(DataHelper.CardInfo cardInfo, int expectedBalance) {
        cards.findBy(Condition.text(cardInfo.getCardNumber().substring(15)))
                .shouldHave(text(balanceStart + expectedBalance + balanceFinish), Duration.ofSeconds(15));
    }

    public DashboardPage reloadDashboardPage() {
        refresh();
        heading.shouldBe(visible, Duration.ofSeconds(15));
        return this;
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public TransferPage selectCardToTransfer(DataHelper.CardInfo cardInfo) {
        cards.findBy(attribute("data-test-id", cardInfo.getTestID())).$("button").click();
        return new TransferPage();
    }
}