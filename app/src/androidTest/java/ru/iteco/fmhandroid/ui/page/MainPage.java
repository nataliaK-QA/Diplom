package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.test.espresso.contrib.RecyclerViewActions;

import java.util.List;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.components.AppBarComponent;
import ru.iteco.fmhandroid.ui.components.BaseComponent;
import ru.iteco.fmhandroid.ui.data.ActiveHelper;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.matchers.RecyclerViewMatcher;

public class MainPage extends BaseComponent {

    public AppBarComponent appBar = new AppBarComponent();
    @IdRes
    public static final int MAIN_SCREEN_CONTAINER = R.id.container_custom_app_bar_include_on_fragment_main;
    @IdRes
    public static final int EXPAND_NEWS_BUTTON = R.id.expand_material_button;
    @IdRes
    public static final int ALL_NEWS_LINK = R.id.all_news_text_view;
    @StringRes
    public static final int ALL_NEWS_LINK_TEXT = R.string.all_news;
    @IdRes
    public static final int NEWS_IN_MAIN_CONTAINER = R.id.container_list_news_include_on_fragment_main;

    public MainPage() {
    }


    @Step("Проверка видимости Главной страницы")
    public MainPage assertMainPageVisible() {
        Allure.step("Проверка видимости Главной страницы");
        waitAndAssert(withId(MAIN_SCREEN_CONTAINER), 5000);
        assertTextInContainerDisplayed(NEWS_IN_MAIN_CONTAINER, DataHelper.NEWS_TEXT_RES);
        assertTextInContainerDisplayed(NEWS_IN_MAIN_CONTAINER, ALL_NEWS_LINK_TEXT);
        return this;
    }

    @Step("Сверка первых трех новостей")
    public MainPage verifyTopThreeNewsMatch(List<DataHelper.NewsPreview> expectedNews) {
        Allure.step("Сверка первых трех новостей");
        for (int i = 0; i < expectedNews.size(); i++) {
            DataHelper.NewsPreview data = expectedNews.get(i);

            onView(withId(DataHelper.NEWS_LIST_RECYCLER))
                    .perform(RecyclerViewActions.scrollToPosition(i));

            RecyclerViewMatcher.assertTextAtPositionDisplayed(DataHelper.NEWS_LIST_RECYCLER, i, data.title);
            RecyclerViewMatcher.assertTextAtPositionDisplayed(DataHelper.NEWS_LIST_RECYCLER, i, data.date);

            clickItemChildElement(DataHelper.NEWS_LIST_RECYCLER, i, DataHelper.NEWS_CARD_CONTAINER);

            onView(withId(DataHelper.NEWS_LIST_RECYCLER))
                    .check(matches(RecyclerViewMatcher.atPosition(i, hasDescendant(allOf(
                            withId(DataHelper.DESCRIPTION_TEXT_FIELD),
                            withText(data.description),
                            isDisplayed()
                    )))));

            clickItemChildElement(DataHelper.NEWS_LIST_RECYCLER, i, DataHelper.NEWS_CARD_CONTAINER);
        }
        return this;
    }

    @Step("Проверка видимости ссылки 'Все новости': {visible}")
    public MainPage assertAllNewsLinkVisible(boolean visible) {
        Allure.step("Проверка видимости ссылки 'Все новости': {visible}");
        if (visible) {
            waitAndAssert(withId(ALL_NEWS_LINK), 5000);
        } else {
            if (ActiveHelper.isElementDisplayedBoolean(withId(ALL_NEWS_LINK))) {
                throw new AssertionError("Ссылка 'Все новости' не должна быть видна!");
            }
        }
        return this;
    }

    @Step("Нажатие кнопки раскрывающей список новостей'")
    public MainPage clickAExpandNewsButton() {
        Allure.step("Нажатие кнопки раскрывающей список новостей'");
        clickOn(EXPAND_NEWS_BUTTON);
        return new MainPage();
    }

    @Step("Нажатие ссылки 'Все новости'")
    public NewsPage clickAllNewsLink() {
        Allure.step("Нажатие ссылки 'Все новости'");
        clickOn(ALL_NEWS_LINK);
        return new NewsPage();
    }
}

