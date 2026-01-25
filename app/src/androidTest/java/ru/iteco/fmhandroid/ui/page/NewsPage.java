package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.test.espresso.contrib.RecyclerViewActions;

import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.List;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.components.AppBarComponent;
import ru.iteco.fmhandroid.ui.components.AppBarNewsComponent;
import ru.iteco.fmhandroid.ui.components.BaseComponent;
import ru.iteco.fmhandroid.ui.data.ActiveHelper;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.matchers.RecyclerViewMatcher;

public class NewsPage extends BaseComponent {
    public AppBarComponent appBar = new AppBarComponent();
    public AppBarNewsComponent appBarNews = new AppBarNewsComponent();
    @IdRes
    public static final int REFRESH_NEWS_PAGE_BUTTON = R.id.news_retry_material_button;
    @IdRes
    public static final int NEWS_PAGE_CONTAINER = R.id.all_news_cards_block_constraint_layout;

    public NewsPage() {
    }

    @Step("Проверка видимости контента на странице 'Новости'")
    public NewsPage assertNewsPageVisible() {
        Allure.step("Проверка видимости контента на странице 'Новости'");
        waitAndAssert(withId(NEWS_PAGE_CONTAINER), 10000);

        if (ActiveHelper.isElementDisplayedBoolean(withId(DataHelper.NEWS_LIST_RECYCLER))) {
            assertTextDisplayed(DataHelper.NEWS_TEXT_RES);
        } else {
            Allure.step("Контент не загружен, проверяем кнопку 'Обновить'");
            assertStringResInIdResDisplayed(REFRESH_NEWS_PAGE_BUTTON, DataHelper.REFRESH_BUTTON_TEXT);
        }

        appBar.assertAppBarVisible();
        appBarNews.assertAppBarNewsVisible();
        return this;
    }

    @Step("Поиск и полная проверка новости: {info.title}")
    public NewsPage findVerifyAndCollapseNews(DataHelper.NewInfo info) {
        Allure.step("Поиск и полная проверка новости: {info.title}");
        Matcher<View> cardMatcher = allOf(
                withId(DataHelper.NEWS_CARD_CONTAINER),
                hasDescendant(allOf(withId(DataHelper.DATA_TITLE_LABEL), withText(info.getTitle()))),
                hasDescendant(allOf(withId(DataHelper.NEWS_DOWN_DATE_TEXT), withText(info.getDate())))
        );

        onView(isRoot()).perform(ActiveHelper.waitUntilVisible(cardMatcher, 10000));
        onView(withId(DataHelper.NEWS_LIST_RECYCLER)).perform(RecyclerViewActions.scrollTo(cardMatcher));

        onView(allOf(withId(DataHelper.DATA_TITLE_LABEL), isDescendantOfA(cardMatcher))).perform(click());

        waitAndAssert(allOf(
                withId(DataHelper.DESCRIPTION_TEXT_FIELD),
                withText(containsString(info.getDescription())),
                isDescendantOfA(cardMatcher),
                isDisplayed()
        ), 5000);

        onView(allOf(withId(R.id.view_news_item_image_view), isDescendantOfA(cardMatcher))).perform(click());

        return this;
    }

    @Step("Сбор данных первых трех новостей")
    public List<DataHelper.NewsPreview> getFirstThreeNews() {
        Allure.step("Сбор данных первых трех новостей");
        List<DataHelper.NewsPreview> newsList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {

            // 1. Скроллим к нужной новости
            onView(withId(DataHelper.NEWS_LIST_RECYCLER)).perform(RecyclerViewActions.scrollToPosition(i));

            // 2. РАЗВОРАЧИВАЕМ (клик по кнопке-стрелке)
            clickItemChildElement(DataHelper.NEWS_LIST_RECYCLER, i, DataHelper.NEWS_CARD_CONTAINER);

            // 4. СОБИРАЕМ ДАННЫЕ
            String title = RecyclerViewMatcher.getTextAtPosition(DataHelper.NEWS_LIST_RECYCLER, i, DataHelper.DATA_TITLE_LABEL);
            String date = RecyclerViewMatcher.getTextAtPosition(DataHelper.NEWS_LIST_RECYCLER, i, DataHelper.NEWS_DOWN_DATE_TEXT);
            String desc = RecyclerViewMatcher.getTextAtPosition(DataHelper.NEWS_LIST_RECYCLER, i, DataHelper.DESCRIPTION_TEXT_FIELD);

            newsList.add(new DataHelper.NewsPreview(title, date, desc));

            // 5. СВОРАЧИВАЕМ ОБРАТНО (клик по той же кнопке)
            clickItemChildElement(DataHelper.NEWS_LIST_RECYCLER, i, DataHelper.NEWS_CARD_CONTAINER);

        }
        return newsList;
    }
}
