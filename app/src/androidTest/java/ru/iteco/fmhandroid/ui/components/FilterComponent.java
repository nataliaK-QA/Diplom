package ru.iteco.fmhandroid.ui.components;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.test.espresso.action.ViewActions;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.ActiveHelper;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.page.AdminPanelPage;

public class FilterComponent extends BaseComponent {

    @IdRes
    public static final int FILTER_TITLE_LABEL = R.id.filter_news_title_text_view;
    @StringRes
    public static final int FILTER_TITLE_TEXT = R.string.filter_news;
    @IdRes
    public static final int NEWS_ITEM_START_DATE_TEXT = R.id.news_item_publish_date_start_text_input_edit_text;
    @IdRes
    public static final int NEWS_ITEM_START_DATE_FIELD = R.id.news_item_publish_date_start_text_input_layout;
    @IdRes
    public static final int NEWS_ITEM_END_DATE_TEXT = R.id.news_item_publish_date_end_text_input_edit_text;
    @IdRes
    public static final int NEWS_ITEM_END_DATE_FIELD = R.id.news_item_publish_date_end_text_input_layout;
    @IdRes
    public static final int FILTER_NAV_CONTAINER = R.id.nav_host_fragment;
    @IdRes
    public static final int FILTER_BUTTON = R.id.filter_button;
    @StringRes
    public static final int FILTER_BUTTON_TEXT = R.string.filter;
    @IdRes
    public static final int CHECKBOX_ACTIVE = R.id.filter_news_active_material_check_box;
    @IdRes
    public static final int CHECKBOX_INACTIVE = R.id.filter_news_inactive_material_check_box;


    public FilterComponent() {
    }

    @Step("Проверка элементов страницы фильтра")
    public FilterComponent assertFilterPageVisible() {
        Allure.step("Проверка элементов страницы фильтра");
        assertAllVisible(
                DataHelper.NEWS_ITEM_CATEGORY_FIELD,
                NEWS_ITEM_START_DATE_FIELD,
                NEWS_ITEM_END_DATE_FIELD,
                FILTER_BUTTON,
                DataHelper.CANCEL_BUTTON
        );

        assertStringResInIdResDisplayed(FILTER_BUTTON, FILTER_BUTTON_TEXT);
        assertTextInContainerDisplayed(FILTER_NAV_CONTAINER, FILTER_TITLE_TEXT);

        return this;
    }

    @Step("Нажатие кнопки 'ФИЛЬТРОВАТЬ'")
    public BaseComponent clickFilterButton() {
        Allure.step("Нажатие кнопки 'ФИЛЬТРОВАТЬ'");
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard());
        clickOn(FILTER_BUTTON);
        return this;
    }

    @Step("Заполнение полей фильтрации")
    public FilterComponent filteringNews(DataHelper.NewInfo newInfo, String endDate) {
        Allure.step("Заполнение полей фильтрации");
        waitAndAssert(withId(FILTER_TITLE_LABEL), 10000);
        CreateNewComponent.fill(DataHelper.NEWS_ITEM_CATEGORY_TEXT, newInfo.getCategory());
        CreateNewComponent.fill(NEWS_ITEM_START_DATE_TEXT, newInfo.getDate());
        CreateNewComponent.fill(NEWS_ITEM_END_DATE_TEXT, endDate);

        onView(isRoot()).perform(ViewActions.closeSoftKeyboard());

        return this;
    }

    @Step("Заполнение поля  категория Id текстом на странице Filter: {category}")
    public FilterComponent selectCategory(CreateNewComponent.NewsCategory category) {
        Allure.step("Заполнение поля  категория Id текстом на странице Filter: {category}");
        fill(DataHelper.NEWS_ITEM_CATEGORY_TEXT, category.getText());
        return this;
    }

    @Step("Заполнение полей  на странице Filter: {category}, ")
    public FilterComponent fillFilterField(DataHelper.FilterInfo info) {
        Allure.step("Заполнение полей  на странице Filter: {category}, ");
        fillInsideContainer(DataHelper.NEWS_ITEM_CATEGORY_FIELD, info.getCategory());
        fillInsideContainer(NEWS_ITEM_START_DATE_FIELD, info.getFromDate());
        fillInsideContainer(NEWS_ITEM_END_DATE_FIELD, info.getToDate());
        return this;
    }

    @Step("Проверка сообщения об ошибке страница фильтр")
    public FilterComponent assertErrorFilterMessage(View decorView) {
        Allure.step("Проверка сообщения об ошибке страница фильтр");
        ActiveHelper.checkToast(DataHelper.MESSAGE_ERROR, decorView);
        return this;
    }

    @Step("Нажатие чекбокса статус 'Активна'")
    public FilterComponent clickCheckBoxActiveButton() {
        Allure.step("Нажатие чекбокса статус 'Активна'");
        clickOn(CHECKBOX_ACTIVE);
        return this;
    }

    @Step("Нажатие чекбокса статус 'Не Активна'")
    public FilterComponent clickCheckBoxInactiveButton() {
        Allure.step("Нажатие чекбокса статус 'Не Активна'");
        clickOn(CHECKBOX_INACTIVE);
        return this;
    }

    @Step("Нажатие кнопки 'отмена'")
    public AdminPanelPage clickCancelFilterButon() {
        Allure.step("Нажатие кнопки 'отмена'");
        clickOn(DataHelper.CANCEL_BUTTON);
        return new AdminPanelPage();
    }

    @Step("Проверка чекбокса в статусе 'Активна'")
    public void assertActiveCheckBox() {
        Allure.step("Проверка чекбокса в статусе 'Активна'");
        ActiveHelper.waitAndAssert(allOf(withId(CHECKBOX_ACTIVE), isDisplayed()), 5000);
    }

    @Step("Проверка чекбокса в статусе 'Не активна'")
    public void assertInactiveCheckBox() {
        Allure.step("Проверка чекбокса в статусе 'Не активна'");
        ActiveHelper.waitAndAssert(allOf(withId(CHECKBOX_INACTIVE), isDisplayed()), 5000);
    }
}
