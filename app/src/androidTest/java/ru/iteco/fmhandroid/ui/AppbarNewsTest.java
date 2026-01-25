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
import ru.iteco.fmhandroid.ui.components.AppBarNewsComponent;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.page.AuthorizationPage;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)

@Epic("AppBar страницы новости приложения")
@Feature("Позитивные проверки")

public class AppbarNewsTest extends BaseTest {

    private AppBarNewsComponent appBarNews;

    @Before
    public void setUp() {
        appBarNews = new AuthorizationPage()
                .logIn(DataHelper.getAuthInfo())
                .appBar.goNewsPage()
                .appBarNews;
    }

    @Test
    @Story("Проверка контента компонента AppBar страницы новости.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_APN_01")
    @Description("Проверить видимость элементов AppBar страницы новости.")
    public void visualAppbarNewsTest() {
        appBarNews
                .clickEditButton()

                .as(AppBarNewsComponent.class)
                .assertAppBarNewsControlPanelVisible();
    }
}
