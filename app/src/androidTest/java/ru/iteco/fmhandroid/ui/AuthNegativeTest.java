package ru.iteco.fmhandroid.ui;

import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Description;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Feature;
import io.qameta.allure.kotlin.Severity;
import io.qameta.allure.kotlin.SeverityLevel;
import io.qameta.allure.kotlin.Story;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.iteco.fmhandroid.test.BuildConfig;
import ru.iteco.fmhandroid.ui.components.AppBarComponent;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.page.AuthorizationPage;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)

@Epic("Авторизация")
@Feature("Негативные проверки")

public class AuthNegativeTest extends BaseTest {
    private AuthorizationPage authorizationPage;

    @Before
    public void setUp() {
        authorizationPage = new AppBarComponent()
                .logOut();
    }


    @Test
    @Story("Неуспешная авторизация - вход с невалидным паролем")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC_AUTH_02")
    @Description("Сценарий: Пользователь вводит валидный логин и невалидный пароль, нажимает «Войти» и не попадает в систему.")
    public void shouldNotLoginWithInvalidPasswordTest() {
        DataHelper.AuthInfo client = new DataHelper.AuthInfo(
                BuildConfig.USER_LOGIN
                , DataHelper.getRandomAuthInfo(5, 8).getPassword());

        authorizationPage
                .tryToLogIn(client)
                .assertAuthPageVisible()
                .assertErrorMessage(getDecorView());
    }

    @Test
    @Story("Неуспешная авторизация с не заполненными полями логин и пароль")
    @DisplayName("TC_AUTH_03")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Оставить поля логина и пароля пустыми и попытаться войти")
    public void shouldCheckButtonDisabledWhenFieldsEmptyTest() {

        authorizationPage
                .assertAuthPageVisible()
                .clickEnterButton()
                .assertAuthPageVisible()
                .assertEmptyLoginPasswordMessage(getDecorView());
    }

    @Test
    @Story("Неуспешная авторизация длинный логин превышающий допустимую длину и попытаться войти.")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC_AUTH_08")
    @Description("Ввести логин 160 символов и валидный пароль.")
    public void shouldNotLoginWithExceedingLengthLoginTest() {
        DataHelper.AuthInfo client = new DataHelper.AuthInfo(
                DataHelper.getRandomAuthInfo(160, 8).getLogin()
                , BuildConfig.USER_PASSWORD);

        authorizationPage
                .assertAuthPageVisible()
                .tryToLogIn(client)
                .assertAuthPageVisible()
                .assertErrorMessage(getDecorView());
    }

    @Test
    @Story("Неуспешная авторизация длинный пароль и попробовать войти")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC_AUTH_09")
    @Description("Ввести валидный логин и пароль 160 символов.")
    public void shouldNotLoginWithExceedingLengthPasswordTest() {
        DataHelper.AuthInfo client = new DataHelper.AuthInfo(
                BuildConfig.USER_LOGIN
                , DataHelper.getRandomAuthInfo(8, 160).getPassword());

        authorizationPage
                .assertAuthPageVisible()
                .tryToLogIn(client)
                .assertAuthPageVisible()
                .assertErrorMessage(getDecorView());
    }

    @Test
    @Story("Неуспешная авторизация с не валидными данными в полях логин и пароль")
    @DisplayName("TC_AUTH_14")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Заполнить поля логина и пароля рандомными данными")
    public void loginWithRandomAuthInfoTest() {

        authorizationPage
                .assertAuthPageVisible()
                .tryToLogIn(DataHelper.getRandomAuthInfo(5, 8))
                .assertAuthPageVisible()
                .assertErrorMessage(getDecorView());
    }

    @Test
    @Story("Неуспешная авторизация с не валидными данными в полях логин и пароль")
    @DisplayName("TC_AUTH_15")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Заполнить поля логина и пароля спецсимволами")
    public void loginWithDisallowedCharsTest() {

        authorizationPage
                .tryToLogIn(DataHelper.getRandomAuthInfoWithDisallowedChars(20, 30))
                .assertErrorMessage(getDecorView())
                .assertAuthPageVisible();
    }

    @Test
    @Story("Неуспешная авторизация - вход с незаполненым полем логин")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC_AUTH_16")
    @Description("Не заполнять поле логин, пароль - валидный")
    public void shouldLoginFieldsFilledEmptyTest() {

        authorizationPage
                .assertAuthPageVisible()
                .typeLogin("")
                .typePassword(DataHelper.getAuthInfo().getPassword())
                .clickEnterButton()
                .assertAuthPageVisible()
                .assertEmptyLoginPasswordMessage(getDecorView());
    }

    @Test
    @Story("Неуспешная авторизация - вход с незаполненым полем пароль")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC_AUTH_17")
    @Description("Не заполнять поле пароль, логин - валидный")
    public void shouldPasswordFieldsFilledEmptyTest() {

        authorizationPage
                .assertAuthPageVisible()
                .typeLogin(DataHelper.getAuthInfo().getLogin())
                .typePassword("")
                .clickEnterButton()
                .assertAuthPageVisible()
                .assertEmptyLoginPasswordMessage(getDecorView());
    }
}
