package ru.iteco.fmhandroid.ui.components;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.ActiveHelper;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.page.AboutPage;
import ru.iteco.fmhandroid.ui.page.AuthorizationPage;
import ru.iteco.fmhandroid.ui.page.ButterflyPage;
import ru.iteco.fmhandroid.ui.page.MainPage;
import ru.iteco.fmhandroid.ui.page.NewsPage;

public class AppBarComponent extends BaseComponent {
    @IdRes
    private final int MAIN_MENU_BUTTON = R.id.main_menu_image_button;
    @IdRes
    private final int TRADEMARK_IMAGE = R.id.trademark_image_view;
    @IdRes
    private final int BUTTERFLY_BUTTON = R.id.our_mission_image_button;
    @IdRes
    private final int LOGOUT_BUTTON = R.id.authorization_image_button;
    @StringRes
    private final int LOGOUT_TEXT_RES = R.string.log_out;
    @StringRes
    private final int MAIN_TEXT_RES = R.string.main;
    @StringRes
    private final int ABOUT_TEXT_RES = R.string.about;

    public AppBarComponent() {
    }

    @Step("Нажатие кнопки 'Меню'")
    public AppBarComponent clickNavigationMenu() {
        Allure.step("Нажатие кнопки 'Меню'");
        clickOn(MAIN_MENU_BUTTON);
        return this;
    }

    @Step("Переход на страницу 'News'")
    public NewsPage goNewsPage() {
        Allure.step("Переход на страницу 'News'");
        clickNavigationMenu();
        clickOnStringRes(DataHelper.NEWS_TEXT_RES);
        return new NewsPage();
    }

    @Step("Переход на страницу 'About'")
    public AboutPage goAboutPage() {
        Allure.step("Переход на страницу 'About'");
        clickNavigationMenu();
        clickOnStringRes(ABOUT_TEXT_RES);
        return new AboutPage();
    }

    @Step("Выход из аккаунта")
    public AuthorizationPage logOut() {
        Allure.step("Выход из аккаунта");
        ActiveHelper.waitFor(5000);
        if (ActiveHelper.isElementDisplayedBoolean(withId(LOGOUT_BUTTON))) {
            clickLogoutButton();
            waitAndAssert(withText(LOGOUT_TEXT_RES), 3000);
            clickOnStringRes(LOGOUT_TEXT_RES);
        }
        return new AuthorizationPage();
    }

    @Step("Проверка видимости элементов AppBar")
    public AppBarComponent assertAppBarVisible() {
        Allure.step("Проверка видимости элементов AppBar");
        assertAllVisible(MAIN_MENU_BUTTON, TRADEMARK_IMAGE, BUTTERFLY_BUTTON, LOGOUT_BUTTON);
        return this;
    }

    @Step("Проверка видимости элементов Logout в AppBar")
    public AppBarComponent assertLogoutVisible() {
        Allure.step("Проверка видимости элементов Logout в AppBar");
        onView(isRoot()).perform(ActiveHelper.waitUntilVisible(withId(LOGOUT_BUTTON), 10000));

        return this;
    }

    @Step("Проверка видимости элементов AppBar на странице About")
    public AppBarComponent assertAppBarForAboutVisible() {
        Allure.step("Проверка видимости элементов AppBar на странице About");
        assertAllVisible(TRADEMARK_IMAGE);
        return this;
    }

    @Step("Переход на страницу 'Main'")
    public MainPage goMainPage() {
        Allure.step("Переход на страницу 'Main'");
        clickNavigationMenu();
        clickNavigationMenuMain();
        return new MainPage();
    }

    @Step("Переход на страницу 'Our_mission'")
    public ButterflyPage goButterflyPage() {
        Allure.step("Переход на страницу 'Our_mission'");
        clickOurMissionButton();
        return new ButterflyPage();
    }

    @Step("Нажатие кнопки 'Меню - Главная'")
    public void clickNavigationMenuMain() {
        Allure.step("Нажатие кнопки 'Меню - Главная'");
        clickOnStringRes(MAIN_TEXT_RES);
    }

    @Step("Нажатие кнопки 'Меню - Новости'")
    public void clickNavigationMenuNews() {
        Allure.step("Нажатие кнопки 'Меню - Новости'");
        clickOnStringRes(DataHelper.NEWS_TEXT_RES);
    }

    @Step("Нажатие кнопки 'Меню - О приложении'")
    public void clickNavigationMenuAbout() {
        Allure.step("Нажатие кнопки 'Меню - О приложении'");
        clickOnStringRes(ABOUT_TEXT_RES);
    }

    @Step("Нажатие кнопки 'Наша миссия'")
    public void clickOurMissionButton() {
        Allure.step("Нажатие кнопки 'Наша миссия'");
        clickOn(BUTTERFLY_BUTTON);
    }

    @Step("Нажатие кнопки 'Выйти'")
    public void clickLogoutButton() {
        Allure.step("Нажатие кнопки 'Выйти'");
        clickOn(LOGOUT_BUTTON);
    }

    public boolean isLogoutButtonVisibleBoolean() {
        return ActiveHelper.isElementDisplayedBoolean(withId(LOGOUT_BUTTON));
    }
}
