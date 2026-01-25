package ru.iteco.fmhandroid.ui;

import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

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
import ru.iteco.fmhandroid.ui.page.NewsPage;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)

@Epic("Страница Новости приложения")
@Feature("Позитивные проверки")

public class NewsPageTest extends BaseTest {
    private NewsPage newsPage;

    @Before
    public void setUp() {
        newsPage = new AuthorizationPage()
                .logIn(DataHelper.getAuthInfo())
                .appBar.goNewsPage();
    }

    @Test
    @Story("Проверка отображения страницы новости.")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC_NEWS_29")
    @Description("Ищем кнопку обновить на странице 'новости' или карточки с новостями")
    public void visualNewsPageTest() {
        newsPage.assertNewsPageVisible();
    }


    @Test
    @Story("Проверка кликабельности фильтра страницы новости.")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC_NEWS_30")
    @Description("Страница новости-проверка, фильтруем, проверка страница новости")
    public void shouldPositiveFilteringNewsTest() {
        DataHelper.NewInfo testNew = DataHelper.getFirstTestNewsInfo();
        newsPage.assertNewsPageVisible()
                .appBarNews.clickIconFilter()
                .assertFilterPageVisible()
                .filteringNews(testNew, DataHelper.getTodayDateFormatted())
                .clickFilterButton()

                .as(NewsPage.class).assertNewsPageVisible();
    }

    @Test
    @Story("Проверка синхронизации контента страниц Главная и Новости.")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC_NEWS_32 флак")
    @Description("Сверка интеграции: сверяем первые 3 новости в общем списке на странице Новости и на Главной")
    public void newsIntegrationTest() {
        List<DataHelper.NewsPreview> expectedNews = newsPage
                .assertNewsPageVisible()
                .getFirstThreeNews();

        newsPage.appBar.goMainPage()
                .verifyTopThreeNewsMatch(expectedNews);
    }
}
