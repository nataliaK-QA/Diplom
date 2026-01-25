package ru.iteco.fmhandroid.ui.components;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.page.AdminPanelPage;

public class AppBarNewsComponent extends BaseComponent {
    @IdRes
    public static final int SORT_NEWS_BUTTON = R.id.sort_news_material_button;
    @IdRes
    public static final int FILTER_NEWS_BUTTON = R.id.filter_news_material_button;
    @IdRes
    public static final int EDIT_NEWS_BUTTON = R.id.edit_news_material_button;
    @IdRes
    public final int APPBAR_NEWS_CONTAINER = R.id.container_list_news_include;
    @IdRes
    public final int ADD_NEWS_BUTTON = R.id.add_news_image_view;
    @IdRes
    public final int CONTROL_PANEL_CONTAINER = R.id.container_custom_app_bar_include_on_fragment_news_control_panel;
    @StringRes
    public final int CONTROL_PANEL_TITLE = R.string.news_control_panel;

    public AppBarNewsComponent() {
    }

    @Step("Проверка видимости элементов AppBarNews страницы 'Новости': {visible}")
    public AppBarNewsComponent assertAppBarNewsVisible() {
        Allure.step("Проверка видимости элементов AppBarNews страницы 'Новости': {visible}");

        onView(withId(APPBAR_NEWS_CONTAINER))
                .check(matches(isDisplayed()));

        assertTextInContainerDisplayed(APPBAR_NEWS_CONTAINER, DataHelper.NEWS_TEXT_RES);

        onView(withId(SORT_NEWS_BUTTON)).check(matches(isDisplayed()));
        onView(withId(FILTER_NEWS_BUTTON)).check(matches(isDisplayed()));
        onView(withId(EDIT_NEWS_BUTTON)).check(matches(isDisplayed()));
        return this;
    }

    @Step("Проверка видимости элементов AppBarControlPanel 'Панель управления': {visible}")
    public AppBarNewsComponent assertAppBarNewsControlPanelVisible() {
        Allure.step("Проверка видимости элементов AppBarControlPanel 'Панель управления': {visible}");

        assertTextDisplayedFlexible(CONTROL_PANEL_TITLE);
        onView(withId(SORT_NEWS_BUTTON)).check(matches(isDisplayed()));
        onView(withId(FILTER_NEWS_BUTTON)).check(matches(isDisplayed()));
        onView(withId(ADD_NEWS_BUTTON)).check(matches(isDisplayed()));
        return this;
    }


    @Step("Нажатие кнопки 'Сортировка'")
    public BaseComponent clickSortButton() {
        Allure.step("Нажатие кнопки 'Сортировка'");
        clickOn(SORT_NEWS_BUTTON);
        return this;
    }

    @Step("Нажатие кнопки 'Фильтровать'")
    public FilterComponent clickIconFilter() {
        Allure.step("Нажатие кнопки 'Фильтровать'");
        clickOn(FILTER_NEWS_BUTTON);
        return new FilterComponent();
    }

    @Step("Нажатие кнопки перехода на страницу 'Создать новость'")
    public AdminPanelPage clickEditButton() {
        Allure.step("Нажатие кнопки перехода на страницу 'Создать новость'");
        clickOn(EDIT_NEWS_BUTTON);
        return new AdminPanelPage();
    }

    @Step("Нажатие кнопки добавления новости")
    public CreateNewComponent clickAddNewsButton() {
        Allure.step("Нажатие кнопки добавления новости");
        clickOn(ADD_NEWS_BUTTON);
        return new CreateNewComponent();
    }
}
