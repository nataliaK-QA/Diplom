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
import ru.iteco.fmhandroid.ui.page.AuthorizationPage;
import ru.iteco.fmhandroid.ui.page.ButterflyPage;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)

@Epic("Страница Наша миссия приложения")
@Feature("Позитивные проверки")

public class ButterflyPageTest extends BaseTest {

    private ButterflyPage butterflyPage;

    @Before
    public void setUp() {
        butterflyPage = new AuthorizationPage()
                .logIn(DataHelper.getAuthInfo())
                .appBar.goButterflyPage();
    }

    @Test
    @Story("Проверка перехода в раздел и отображения главного заголовка.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_BUTTERFLY_01")
    @Description("Убедиться, что на открывшемся экране отображается заголовок «ГЛАВНОЕ ЖИТЬ ЛЮБЯ».")
    public void shouldVerifyOurMissionPageContent() {
        butterflyPage.assertButterflyPageVision();

    }
}
