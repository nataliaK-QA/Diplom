package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.components.AppBarComponent;
import ru.iteco.fmhandroid.ui.components.BaseComponent;
import ru.iteco.fmhandroid.ui.data.ActiveHelper;
import ru.iteco.fmhandroid.ui.data.DataHelper;


public class AuthorizationPage extends BaseComponent {

    public AppBarComponent appBar = new AppBarComponent();

    @StringRes
    public static final int AUTH_TITLE = R.string.authorization;
    @StringRes
    public static final int MESSAGE_EMPTY_LOGIN_PASSWORD = R.string.empty_login_or_password;
    @StringRes
    public static final int ENTER_BUTTON_TEXT = R.string.sign_in;
    @IdRes
    public static final int LOGIN_FIELD_CONTAINER = R.id.login_text_input_layout;
    @IdRes
    public static final int PASSWORD_FIELD_CONTAINER = R.id.password_text_input_layout;
    @IdRes
    public static final int ENTER_BUTTON = R.id.enter_button;
    @IdRes
    public static final int LOGIN_FIELD = R.id.login_text_input_layout;
    @IdRes
    public static final int PASSWORD_FIELD = R.id.password_text_input_layout;


    public AuthorizationPage() {
    }

    @Step("Проверка видимости страницы Авторизации")
    public AuthorizationPage assertAuthPageVisible() {
        Allure.step("Проверка видимости страницы Авторизации");
        assertTextDisplayed(AUTH_TITLE);
        assertAllVisible(LOGIN_FIELD_CONTAINER, PASSWORD_FIELD_CONTAINER, ENTER_BUTTON);
        return this;
    }

    @Step("Вход в аккаунт")
    public MainPage logIn(DataHelper.AuthInfo authInfo) {
        Allure.step("Вход в аккаунт");

        ActiveHelper.waitFor(5000);
        if (appBar.isLogoutButtonVisibleBoolean()) {
            Allure.step("Мы уже в системе, ввод данных не нужен");
            return new MainPage();
        }

        waitAndAssert(withId(LOGIN_FIELD), 5000);

        fillInsideContainer(LOGIN_FIELD, authInfo.getLogin());
        fillInsideContainer(PASSWORD_FIELD, authInfo.getPassword());

        return clickEnterButton()
                .as(MainPage.class)
                .assertMainPageVisible();
    }

    @Step("Заполнение поля Логин")
    public AuthorizationPage typeLogin(String login) {
        Allure.step("Заполнение поля Логин");
        fillInsideContainer(LOGIN_FIELD, login);
        return this;
    }

    @Step("Заполнение поля Пароль")
    public AuthorizationPage typePassword(String password) {
        Allure.step("Заполнение поля Пароль");
        fillInsideContainer(PASSWORD_FIELD, password);
        return this;
    }

    @Step("Попытка входа")
    public AuthorizationPage tryToLogIn(DataHelper.AuthInfo authInfo) {
        Allure.step("Попытка входа с невалидными данными");
        if (appBar.isLogoutButtonVisibleBoolean()) {
            Allure.step("Мы в системе - выходим");
            appBar.logOut();
        }
        Allure.step("Попытка входа с невалидными данными");
        waitAndAssert(withId(LOGIN_FIELD), 5000);

        fillInsideContainer(LOGIN_FIELD, authInfo.getLogin());
        fillInsideContainer(PASSWORD_FIELD, authInfo.getPassword());

        clickEnterButton();

        return this;
    }

    @Step("Клик по кнопке 'Войти'")
    public AuthorizationPage clickEnterButton() {
        Allure.step("Клик по кнопке 'Войти'");
        clickOn(ENTER_BUTTON);
        return this;
    }

    @Step("Проверка сообщения о пустом логине/пароле")
    public AuthorizationPage assertEmptyLoginPasswordMessage(View decorView) {
        Allure.step("Проверка сообщения о пустом логине/пароле");
        ActiveHelper.checkToast(MESSAGE_EMPTY_LOGIN_PASSWORD, decorView);
        return this;
    }

    @Step("Проверка сообщения об ошибке страница авторизации")
    public AuthorizationPage assertErrorMessage(View decorView) {
        Allure.step("Проверка сообщения об ошибке страница авторизации");
        ActiveHelper.checkToast(DataHelper.MESSAGE_ERROR, decorView);
        return this;
    }

    @Step("Проверка кнопки 'Войти' и ее кликабельность")
    public AuthorizationPage assertClickableEnterButton() {
        Allure.step("Проверка кнопки 'Войти' и ее кликабельность");
        waitAndAssertClickable(ENTER_BUTTON, ENTER_BUTTON_TEXT);
        return this;
    }

    @Step("Проверка кнопки 'Войти' и ее некликабельность")
    public AuthorizationPage assertNotClickableEnterButton() {
        Allure.step("Проверка кнопки 'Войти' и ее некликабельность");
        assertNotClickable(ENTER_BUTTON, ENTER_BUTTON_TEXT);
        return this;
    }
}



