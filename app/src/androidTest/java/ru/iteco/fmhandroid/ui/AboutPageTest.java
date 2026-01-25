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
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.page.AboutPage;
import ru.iteco.fmhandroid.ui.page.AuthorizationPage;
import ru.iteco.fmhandroid.ui.page.ButterflyPage;
import ru.iteco.fmhandroid.ui.page.MainPage;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)

@Epic("Страница О приложении")
@Feature("Позитивные проверки")

public class AboutPageTest extends BaseTest {
    private AboutPage aboutPage;

    @Before
    public void setUp() {
        aboutPage = new AuthorizationPage()
                .logIn(DataHelper.getAuthInfo())
                .appBar
                .goAboutPage();
    }

    @Test
    @Story("Проверка отображения страницы О нас.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_ABOUT_08")
    @Description("Перейти на страницу О нас и проверить отображение всех элементов")
    public void aboutPageVisualTest() {
        aboutPage
                .assertAboutPageVisible()
                .assertPrivacyPolicyText()
                .assertTermsOfUseText();
    }

    @Test
    @Story("Проверка навигации страницы О нас.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_ABOUT_06")
    @Description("Переход: Butterfly -> About -> Butterfly")
    public void goAboutToButterflyPageTest() {
        aboutPage.clickAboutBack(MainPage.class)
                .assertMainPageVisible()
                .appBar
                .goButterflyPage()
                .assertButterflyPageVision()
                .appBar
                .goAboutPage()
                .assertAboutPageVisible()
                .clickAboutBack(ButterflyPage.class)
                .assertButterflyPageVision();
    }

    @Test
    @Story("Проверка навигации страницы О нас.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_ABOUT_07")
    @Description("Переход: Butterfly -> Main")
    public void aboutToMainPageTest() {
        aboutPage
                .clickAboutBack(MainPage.class)
                .assertMainPageVisible();
    }

    @Test
    @Story("Проверка ссылки на Политику конфиденциальности.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_ABOUT_01")
    @Description("Проверить кликабельность ссылки «Политика конфиденциальности».")
    public void shouldClickabilityPrivacyPolicyLinkTest() {
        aboutPage.assertPrivacyPolicyText();
    }

    @Test
    @Story("Проверка ссылки на Политику конфиденциальности.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_ABOUT_02")
    @Description("Проверить кликабельность ссылки «Политика конфиденциальности».")
    public void shouldClickabilityTermsOfUseLinkTest() {
        aboutPage.assertTermsOfUseText();

    }

    @Test
    @Story("Проверка корректности отображения версии ПО и данных разработчика.")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC_ABOUT_03")
    @Description("Убедиться, что отображается текст «Версия №» и «Айтеко, год»")
    public void shouldVisibleDisplayingSoftwareVersionDeveloperDataTest() {
        aboutPage.assertAboutPageVisible();

    }
}

