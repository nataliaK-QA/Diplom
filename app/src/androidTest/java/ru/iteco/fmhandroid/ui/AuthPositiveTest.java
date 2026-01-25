package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

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
import ru.iteco.fmhandroid.ui.page.MainPage;


@LargeTest
@RunWith(AllureAndroidJUnit4.class)

@Epic("Авторизация")
@Feature("Позитивные проверки")

public class AuthPositiveTest extends BaseTest {
    private AuthorizationPage authorizationPage;

    @Before
    public void setUp() {
        authorizationPage = new AppBarComponent()
                .logOut();
    }


    @Test
    @Story("Успешная авторизация - вход с валидными данными.")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC_AUTH_01")
    @Description("Пользователь вводит валидный логин и валидный пароль, нажимает «Войти» и попадает в систему.")
    public void successfullyLoginTest() {
        authorizationPage
                .assertAuthPageVisible()
                .logIn(DataHelper.getAuthInfo())
                .assertMainPageVisible();
    }

    @Test
    @Story("Подтверждение наличия и состояния элементов: заголовок.")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC_AUTH_04")
    @Description("Проверить текст заголовка app bar equals \"Авторизация\"..")
    public void visionAuthPageTitleTest() {
        authorizationPage
                .assertAuthPageVisible()
                .assertTextDisplayed(AuthorizationPage.AUTH_TITLE);
    }

    @Test
    @Story("Подтверждение наличия и состояния элементов: поля логин.")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC_AUTH_05")
    @Description("Проверить наличие поля \"Логин\" и его доступность.")
    public void visionAuthPageLoginFieldTest() {
        authorizationPage
                .assertAuthPageVisible()
                .waitAndAssert(withId(AuthorizationPage.LOGIN_FIELD), 1000);
    }

    @Test
    @Story("Подтверждение наличия и состояния элементов: поля пароля.")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC_AUTH_06")
    @Description("Проверить наличие поля \"Пароль\" и его доступность.")
    public void visionAuthPageLoginPasswordTest() {
        authorizationPage
                .assertAuthPageVisible()
                .waitAndAssert(withId(AuthorizationPage.PASSWORD_FIELD), 1000);
    }

    @Test
    @Story("Подтверждение наличия и кликабельности элементов: кнопка входа.")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("TC_AUTH_07")
    @Description("Проверить наличие кнопки \"Войти\" и её активность/ disabled-состояние.")
    public void visionClickableEnterButtonTest() {
        authorizationPage
                .assertAuthPageVisible()
                .waitAndAssert(withId(AuthorizationPage.ENTER_BUTTON), 1000)

                .as(AuthorizationPage.class)
                .assertStringResInIdResDisplayed(AuthorizationPage.ENTER_BUTTON
                        , AuthorizationPage.ENTER_BUTTON_TEXT)

                .as(AuthorizationPage.class)
                .assertNotClickableEnterButton();
    }


    @Test
    @Story("Успешная авторизация повторный вход в систему")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("TC_AUTH_10")
    @Description("После успешного входа , пользователь ещё раз открывает приложение — автоматически попадает в систему.")
    public void shouldKeepSessionAfterRecreateTest() {
        authorizationPage
                .assertAuthPageVisible()
                .logIn(DataHelper.getAuthInfo())
                .assertMainPageVisible();

        mActivityScenarioRule.getScenario().recreate();

        new MainPage().assertMainPageVisible();
    }

    @Test
    @Story("Успешная авторизация - валидный логин с пробелами  перед и в конце него, валидный пароль.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_AUTH_11")
    @Description("Ввести логин с пробелами вначале и в конце.")
    public void shouldHandleLoginWithSpacesTest() {
        DataHelper.AuthInfo client = new DataHelper.AuthInfo(
                "   " + BuildConfig.USER_LOGIN + "   "
                , BuildConfig.USER_PASSWORD);

        authorizationPage
                .assertAuthPageVisible()
                .logIn(client)
                .assertMainPageVisible();
    }

    @Test
    @Story("Успешная авторизация валидный логин с спецсимволом в конце.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_AUTH_12 флак")
    @Description("Ввести логин со спецсимволом (например \" ' ; : / \\ | [ ] ( ) { } < > , = + * & % $ # @ ! ?) в конце.")
    public void shouldLoginWithDirtyTailInUsernameTest() {
        DataHelper.AuthInfo client = new DataHelper.AuthInfo(
                BuildConfig.USER_LOGIN
                        + DataHelper.getRandomAuthInfoWithDisallowedChars(1, 0)
                        .getLogin()
                , BuildConfig.USER_PASSWORD);

        authorizationPage
                .assertAuthPageVisible()
                .tryToLogIn(client)
                .as(MainPage.class)
                .assertMainPageVisible();

    }

    @Test
    @Story("Подтверждение наличия и состояния элементов: кнопка входа")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC_AUTH_13")
    @Description("Проверить наличие кнопки \"Войти\" и её активность/ enabled-состояние")
    public void shouldSeeLoginButtonEnabledWhenFieldsFilledTest() {
        authorizationPage
                .assertAuthPageVisible()
                .typeLogin(DataHelper.getAuthInfo().getLogin())
                .typePassword(DataHelper.getAuthInfo().getPassword())
                .assertClickableEnterButton();
    }
}