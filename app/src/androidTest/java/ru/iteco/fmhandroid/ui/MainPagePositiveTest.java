package ru.iteco.fmhandroid.ui;

import androidx.test.espresso.NoActivityResumedException;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Description;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Feature;
import io.qameta.allure.kotlin.Severity;
import io.qameta.allure.kotlin.SeverityLevel;
import io.qameta.allure.kotlin.Story;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.page.AuthorizationPage;
import ru.iteco.fmhandroid.ui.page.MainPage;


@LargeTest
@RunWith(AllureAndroidJUnit4.class)

@Epic("Страница Главная приложения")
@Feature("Позитивные проверки")


public class MainPagePositiveTest extends BaseTest {

    private MainPage mainPage;

    @Before
    public void setUp() {
        mainPage = new AuthorizationPage()
                .logIn(DataHelper.getAuthInfo());
    }

    @Test
    @Story("Проверка элементов страницы Главная.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_MAIN_01")
    @Description("Проверка элементов страницы и кликабельности кнопки, переход в новости и видимость ссылки на страницу новостей")
    public void goMainToNewsPageTest() {


        mainPage
                .assertMainPageVisible()
                .clickAExpandNewsButton()
                .assertAllNewsLinkVisible(false)

                .clickAExpandNewsButton()
                .assertAllNewsLinkVisible(true)
                .clickAllNewsLink()
                .assertNewsPageVisible();
    }

    @Test
    @Story("Проверка возможности скрыть блок новостей на главной странице.")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("TC_NEWS_08")
    @Description("На странице главная нажать на кнопку «Скрыть» (стрелка сворачивания).")
    public void shouldHideNewsBlockMainPageTest() {
        mainPage.assertMainPageVisible()
                .clickAExpandNewsButton()

                .assertAllNewsLinkVisible(false);
    }

    @Test
    @Story("Проверка невозможности вернуться в личный кабинет через кнопку «Назад» после выхода.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_LOGOUT_03")
    @Description("Проверка невозможности вернуться в личный кабинет через кнопку «Назад»  после выхода.")
    public void shouldReturnAccountBackAfterLogoutTest() {
        mainPage
                .assertMainPageVisible()
                .appBar.logOut()

                .as(AuthorizationPage.class)
                .assertAuthPageVisible();

        try {
            androidx.test.espresso.Espresso.pressBack();
            if (mainPage.appBar.isLogoutButtonVisibleBoolean()) {
                throw new AssertionError("Баг! После Back юзер вернулся в личный кабинет");
            }
        } catch (NoActivityResumedException e) {
            Allure.step("Успех: Стек очищен, приложение закрыто кнопкой Back");
        }
    }
}
