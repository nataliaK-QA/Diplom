package ru.iteco.fmhandroid.ui.components;

import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import android.view.View;

import org.hamcrest.Matcher;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.ui.data.DataHelper;

public class NewsComponent extends BaseComponent {

    public static Matcher<View> newsCardMatcher(DataHelper.NewInfo info) {
        return allOf(
                hasDescendant(allOf(withId(DataHelper.DATA_TITLE_LABEL), withText(info.getTitle()))),
                hasDescendant(allOf(withId(DataHelper.PUBLICATION_DATE_FIELD), withText(containsString(info.getDate()))))
        );
    }

    @Step("Ожидание исчезновения карточки новости: {info.title}")
    public NewsComponent waitUntilDoesNotExistNewComp(DataHelper.NewInfo info, long timeout) {
        Allure.step("Ожидание исчезновения карточки новости: {info.title}");
        waitUntilDoesNotExistMatcherBaseComp(newsCardMatcher(info), timeout);
        return this;
    }

    @Step("Проверка, что новость {info.title} отображается")
    public void assertIsDisplayedNews(DataHelper.NewInfo info) {
        Allure.step("Проверка, что новость {info.title} отображается");
        waitAndAssert(newsCardMatcher(info));
    }
}
