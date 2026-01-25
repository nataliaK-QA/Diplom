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
import ru.iteco.fmhandroid.ui.components.AppBarComponent;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.page.AuthorizationPage;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)

@Epic("AppBar приложения")
@Feature("Позитивные проверки")

public class AppBarTest extends BaseTest {
    private AppBarComponent appBarComponent;

    @Before
    public void setUp() {
        appBarComponent = new AuthorizationPage()
                .logIn(DataHelper.getAuthInfo())
                .appBar;
    }

    @Test
    @Story("Проверка контента компонента AppBar приложения.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_AP_01")
    @Description("Проверить видимость элементов AppBar приложения.")
    public void visualAppBarPageTest() {
        appBarComponent.assertAppBarVisible();
    }

    @Test
    @Story("Проверка навигации на страницу Новости.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_AP_02")
    @Description("Проверить переход на страницу Новости и видимость элементов страницы Новости.")
    public void goNewsPageTest() {
        appBarComponent
                .goNewsPage()
                .assertNewsPageVisible();
    }

    @Test
    @Story("Проверка навигации на страницу Наша миссия.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_AP_03")
    @Description("Проверить переход на страницу Наша миссия и видимость элементов страницы Наша миссия.")
    public void goButterflyPageTest() {
        appBarComponent
                .goButterflyPage()
                .assertButterflyPageVision();
    }

    @Test
    @Story("Проверка навигации на страницу О нас.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_AP_04")
    @Description("Проверить переход на страницу О нас и видимость элементов страницы О нас.")
    public void goAboutPageTest() {
        appBarComponent
                .goAboutPage()
                .assertAboutPageVisible();
    }

    @Test
    @Story("Проверка навигации на страницы Новости-Главная.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_AP_05")
    @Description("Проверить переход на страницы Новости-Главная и видимость их элементов.")
    public void goNewsToMainPageTest() {
        appBarComponent
                .goNewsPage()
                .assertNewsPageVisible()
                .appBar
                .goMainPage()
                .assertMainPageVisible();
    }

    @Test
    @Story("Проверка навигации на страницы Новости-О нас.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_AP_06")
    @Description("Проверить переход на страницы Новости-О нас и видимость их элементов.")
    public void goNewsToAboutPageTest() {
        appBarComponent
                .goNewsPage()
                .assertNewsPageVisible()
                .appBar
                .goAboutPage()
                .assertAboutPageVisible();
    }

    @Test
    @Story("Проверка навигации на страницы О нас-Новости.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_AP_07")
    @Description("Проверить переход на страницы О нас-Новости и видимость их элементов.")
    public void goAboutToNewsPageTest() {
        appBarComponent
                .goAboutPage()
                .assertAboutPageVisible()
                .appBar
                .goNewsPage()
                .assertNewsPageVisible();
    }
}