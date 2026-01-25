package ru.iteco.fmhandroid.ui;

import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Description;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Feature;
import io.qameta.allure.kotlin.Severity;
import io.qameta.allure.kotlin.SeverityLevel;
import io.qameta.allure.kotlin.Story;
import io.qameta.allure.kotlin.junit4.DisplayName;
import io.qameta.allure.kotlin.model.Status;
import ru.iteco.fmhandroid.ui.components.AppBarComponent;
import ru.iteco.fmhandroid.ui.components.AppBarNewsComponent;
import ru.iteco.fmhandroid.ui.components.BaseComponent;
import ru.iteco.fmhandroid.ui.components.NewsComponent;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.page.AdminPanelPage;
import ru.iteco.fmhandroid.ui.page.AuthorizationPage;
import ru.iteco.fmhandroid.ui.page.MainPage;
import ru.iteco.fmhandroid.ui.page.NewsPage;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)

@Epic("Страница Контрольная панель")
@Feature("Позитивные проверки")

public class AdminPanelPagePositiveTest extends BaseTest {
    private AdminPanelPage adminPanelPage;

    @Before
    public void setUp() {
        adminPanelPage = new AuthorizationPage()
                .logIn(DataHelper.getAuthInfo())
                .appBar.goNewsPage()
                .appBarNews.clickEditButton();
    }



    @Test
    @Story("Видимость страницы Контрольная панель")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC_NEWS_26")
    @Description("Перейти на страницу Контрольная панель и проверить отображение всех ее элементов")
    public void visualAppbarNewsTest() {
        adminPanelPage.appBarNews.assertAppBarNewsControlPanelVisible();
        adminPanelPage.assertAdminPanelPageVisible();
    }

    @Test
    @Story("Обновление пустой страницы Контрольной панели")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_NEWS_21")
    @Description("Удалить все новости и обновить страницу свайпом")
    public void shouldDeleteAllNewsTest() {
        adminPanelPage.deleteAllNews()
                .swipe(DataHelper.NEWS_LIST_RECYCLER)

                .as(AdminPanelPage.class)
                .assertAdminPanelPageVisible();
    }

    @Test
    @Story("Проверка даты создания и публикации созданной новости")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_NEWS_23")
    @Description("Создать новость и проверить совпадение даты создания и публикации")
    public void shouldEqualPublicationAtCreationDateTest() {
        DataHelper.NewInfo testNewFirst = DataHelper.getFirstTestNewsInfo();

        adminPanelPage.appBarNews.assertAppBarNewsControlPanelVisible()

                .as(AdminPanelPage.class)
                .assertAdminPanelPageVisible()
                .appBarNews.clickAddNewsButton()
                .createNew(testNewFirst)
                .clickSaveNewButton()


                .as(AdminPanelPage.class)
                .assertAdminPanelPageVisible()

                .equalDateCreateToPublication(testNewFirst, AdminPanelPage.NewsStatus.ACTIVE);
    }

    @Test
    @Story("Поиск новостей по параметрам, под которые не подходит ни одна запись.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_NEWS_17")
    @Description("Проверить работу фильтра с различными статусами и категориями E2E")
    public void editNewTest() {
        DataHelper.NewInfo testNewFirst = DataHelper.getFirstTestNewsInfo();
        DataHelper.NewInfo testNewSecond = DataHelper.getSecondTestNewsInfo();
        DataHelper.NewInfo testNewThird = DataHelper.getThirdTestNewsInfo();


        adminPanelPage.appBarNews.assertAppBarNewsControlPanelVisible()

//создание
                .as(AdminPanelPage.class)
                .assertAdminPanelPageVisible()
                .appBarNews.clickAddNewsButton()
                .createNew(testNewFirst)
                .clickSaveNewButton()
//проверка на админ панели
                .as(AdminPanelPage.class)
                .findVerifyAndExpandNews(testNewFirst, AdminPanelPage.NewsStatus.ACTIVE)

//проверка на странице новости
                .appBar.goNewsPage()
                .assertNewsPageVisible()
                .findVerifyAndCollapseNews(testNewFirst)

//редактирование
                .as(AppBarNewsComponent.class)
                .clickEditButton()
                .editNews(testNewFirst, testNewSecond, AdminPanelPage.NewsStatus.ACTIVE)
                .clickCheckBox()
                .clickSaveNewButton()

//проверка на админ панели
                .as(AdminPanelPage.class)
                .findVerifyAndExpandNews(testNewSecond, AdminPanelPage.NewsStatus.NOT_ACTIVE)

// проверка в новостях что такой новости нет
                .appBar.goNewsPage()
                .as(NewsComponent.class)
                .waitUntilDoesNotExistNewComp(testNewSecond, 10000)

                //редактирование новости в админ панели
                .as(AppBarNewsComponent.class)
                .clickEditButton()
                .editNews(testNewSecond, testNewThird, AdminPanelPage.NewsStatus.NOT_ACTIVE)
                .clickCheckBox()
                .clickSaveNewButton()

                //проверка на странице новости что она есть
                .as(AppBarComponent.class)
                .goNewsPage()
                .findVerifyAndCollapseNews(testNewThird)
                .appBarNews.clickEditButton()

                //удаление из админ панели и проверка удаления
                .deleteNews(testNewThird)
                .clickDeleteNewOk()

                //проверка удаления со страницы новости
                .appBar.goNewsPage()
                .as(NewsComponent.class)
                .waitUntilDoesNotExistNewComp(testNewFirst, 10000)
                .waitUntilDoesNotExistNewComp(testNewSecond, 10000)
                .waitUntilDoesNotExistNewComp(testNewThird, 10000);

    }

    @Test
    @Story("Проверка отображения созданных новостей на странице Контрольная панель")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_NEWS_24")
    @Description("Создать 5 новостей и проверить их отображения на странице Контрольная панель в прямом и обратном порядке")
    public void shouldVisibleNewsDirectReverseOrderAdminPageTest() {
        DataHelper.NewInfo futureNews = DataHelper.getFutureTestNewsInfo(1);
        DataHelper.NewInfo todayNews = DataHelper.getFirstTestNewsInfo();
        DataHelper.NewInfo oldOneNews = DataHelper.getOldTestNewsInfo(1);
        DataHelper.NewInfo oldTwoDaysNews = DataHelper.getOldTestNewsInfo(2);
        DataHelper.NewInfo oldSevenDaysNews = DataHelper.getOldTestNewsInfo(7);
        DataHelper.NewInfo oldThirtyDaysNews = DataHelper.getOldTestNewsInfo(30);

        adminPanelPage
                .assertAdminPanelPageVisible()
                .deleteAllNews();

        // 1. Создаем новости
        List.of(futureNews, todayNews, oldOneNews, oldTwoDaysNews, oldSevenDaysNews, oldThirtyDaysNews)
                .forEach(news -> adminPanelPage.appBarNews.clickAddNewsButton()
                        .createNew(news)
                        .clickSaveNewButton());

// 2. ПРОВЕРКА В АДМИНКЕ (По умолчанию новые сверху)
        adminPanelPage
                .assertAdminPanelPageVisible()

                .verifyNewsAt(0, futureNews)
                .verifyNewsAt(1, todayNews)
                .verifyNewsAt(2, oldOneNews)
                .verifyNewsAt(3, oldTwoDaysNews)
                .verifyNewsAt(4, oldSevenDaysNews)
                .verifyNewsAt(5, oldThirtyDaysNews)


                .appBarNews.clickSortButton()
                .as(AdminPanelPage.class)
                .scrollToStart(0)

                .verifyNewsAt(0, oldThirtyDaysNews)
                .verifyNewsAt(1, oldSevenDaysNews)
                .verifyNewsAt(2, oldSevenDaysNews)
                .verifyNewsAt(3, oldTwoDaysNews)
                .scrollToStart(4)
                .verifyNewsAt(4, oldOneNews)//теряет ее
                .scrollToStart(5)
                .verifyNewsAt(5, todayNews)//смещает на 4 позицию

                .as(MainPage.class)
                .clickAllNewsLink()
                .appBarNews.clickEditButton()
                .assertAdminPanelPageVisible()
                .deleteAllNews()
                .clickRefreshButton()

                .as(NewsComponent.class)
                .waitUntilDoesNotExistNewComp(futureNews, 10000)
                .waitUntilDoesNotExistNewComp(todayNews, 10000)
                .waitUntilDoesNotExistNewComp(oldOneNews, 10000)
                .waitUntilDoesNotExistNewComp(oldTwoDaysNews, 10000)
                .waitUntilDoesNotExistNewComp(oldSevenDaysNews, 10000)
                .waitUntilDoesNotExistNewComp(oldThirtyDaysNews, 10000);
    }

    @Test
    @Story("Проверка отображения созданных новостей на странице Новости")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_NEWS_25")
    @Description("Создать 5 новостей и проверить их отображения на странице Новости в прямом и обратном порядке")
    public void shouldVisibleNewsDirectReverseOrderNewsPageTest() {

        DataHelper.NewInfo futureNews = DataHelper.getFutureTestNewsInfo(1);
        DataHelper.NewInfo todayNews = DataHelper.getFirstTestNewsInfo();
        DataHelper.NewInfo oldOneNews = DataHelper.getOldTestNewsInfo(1);
        DataHelper.NewInfo oldTwoDaysNews = DataHelper.getOldTestNewsInfo(2);
        DataHelper.NewInfo oldSevenDaysNews = DataHelper.getOldTestNewsInfo(7);
        DataHelper.NewInfo oldThirtyDaysNews = DataHelper.getOldTestNewsInfo(30);

        adminPanelPage
                .assertAdminPanelPageVisible()
                .deleteAllNews();

        List.of(futureNews, todayNews, oldOneNews, oldTwoDaysNews, oldSevenDaysNews, oldThirtyDaysNews)
                .forEach(news -> adminPanelPage.appBarNews.clickAddNewsButton()
                        .createNew(news)
                        .clickSaveNewButton());
        adminPanelPage
                .appBar.goNewsPage()
                .assertNewsPageVisible()

                .as(AdminPanelPage.class)
                .verifyNewsAt(0, todayNews)
                .verifyNewsAt(1, oldOneNews)
                .verifyNewsAt(2, oldTwoDaysNews)
                .verifyNewsAt(3, oldSevenDaysNews)

                .as(NewsComponent.class)
                .waitUntilDoesNotExistNewComp(futureNews, 10000)
                .waitUntilDoesNotExistNewComp(oldThirtyDaysNews, 10000)


                .as(AdminPanelPage.class)
                .appBarNews.clickSortButton()
                .as(AdminPanelPage.class)
                .scrollToStart(0)

                .as(NewsPage.class)
                .findVerifyAndCollapseNews(todayNews)
                .findVerifyAndCollapseNews(oldOneNews)
                .findVerifyAndCollapseNews(oldTwoDaysNews)
                .findVerifyAndCollapseNews(oldSevenDaysNews)

                .as(NewsComponent.class)
                .waitUntilDoesNotExistNewComp(futureNews, 10000)
                .waitUntilDoesNotExistNewComp(oldThirtyDaysNews, 10000)

                .as(AppBarNewsComponent.class)
                .clickEditButton()
                .assertAdminPanelPageVisible()
                .deleteAllNews()
                .clickRefreshButton()

                .as(NewsComponent.class)
                .waitUntilDoesNotExistNewComp(futureNews, 10000)
                .waitUntilDoesNotExistNewComp(todayNews, 10000)
                .waitUntilDoesNotExistNewComp(oldOneNews, 10000)
                .waitUntilDoesNotExistNewComp(oldTwoDaysNews, 10000)
                .waitUntilDoesNotExistNewComp(oldSevenDaysNews, 10000)
                .waitUntilDoesNotExistNewComp(oldThirtyDaysNews, 10000);
    }

    @Test
    @Story("Проверка отображения созданных новостей на странице Главная")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_NEWS_27 флак из-за шума")
    @Description("Создать 5 новостей и проверить их отображения на странице Новости в прямом и обратном порядке")
    public void shouldVisibleNewsDirectReverseOrderMainPageTest() {

        DataHelper.NewInfo futureNews = DataHelper.getFutureTestNewsInfo(1);
        DataHelper.NewInfo todayNews = DataHelper.getFirstTestNewsInfo();
        DataHelper.NewInfo oldOneNews = DataHelper.getOldTestNewsInfo(1);
        DataHelper.NewInfo oldTwoDaysNews = DataHelper.getOldTestNewsInfo(2);
        DataHelper.NewInfo oldSevenDaysNews = DataHelper.getOldTestNewsInfo(7);
        DataHelper.NewInfo oldThirtyDaysNews = DataHelper.getOldTestNewsInfo(30);

        adminPanelPage
                .assertAdminPanelPageVisible()
                .deleteAllNews();

        List.of(futureNews, todayNews, oldOneNews, oldTwoDaysNews, oldSevenDaysNews, oldThirtyDaysNews)
                .forEach(news -> adminPanelPage.appBarNews.clickAddNewsButton()
                        .createNew(news)
                        .clickSaveNewButton());
        adminPanelPage
                .appBar
                .goMainPage()

                .assertMainPageVisible()

                .as(AdminPanelPage.class)
                .verifyNewsAt(0, todayNews)
//                .verifyNewsAt(1, oldOneNews)//не стабильно из-за шума
//                .verifyNewsAt(2, oldTwoDaysNews)//не стабильно из-за шума

                .as(NewsComponent.class)
                .waitUntilDoesNotExistNewComp(futureNews, 10000)
                .waitUntilDoesNotExistNewComp(oldSevenDaysNews, 10000)
                .waitUntilDoesNotExistNewComp(oldThirtyDaysNews, 10000)


//5.удалить новости
                .as(MainPage.class)
                .clickAllNewsLink()
                .appBarNews.clickEditButton()
                .assertAdminPanelPageVisible()
                .deleteAllNews()
                .clickRefreshButton()

                .as(NewsComponent.class)
                .waitUntilDoesNotExistNewComp(futureNews, 10000)
                .waitUntilDoesNotExistNewComp(todayNews, 10000)
                .waitUntilDoesNotExistNewComp(oldOneNews, 10000)
                .waitUntilDoesNotExistNewComp(oldTwoDaysNews, 10000)
                .waitUntilDoesNotExistNewComp(oldSevenDaysNews, 10000)
                .waitUntilDoesNotExistNewComp(oldThirtyDaysNews, 10000);
    }


    @Test//комплексная проверка
    @Story("Проверка отображения созданных новостей на всех страницах E2E")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC_NEWS_28")
    @Description("Создать 5 новостей и проверить их отображения на страницах")
    public void newsE2ESortingTest() {
        DataHelper.NewInfo futureNews = DataHelper.getFutureTestNewsInfo(1);
        DataHelper.NewInfo todayNews = DataHelper.getFirstTestNewsInfo();
        DataHelper.NewInfo oldOneNews = DataHelper.getOldTestNewsInfo(1);
        DataHelper.NewInfo oldTwoDaysNews = DataHelper.getOldTestNewsInfo(2);
        DataHelper.NewInfo oldSevenDaysNews = DataHelper.getOldTestNewsInfo(7);
        DataHelper.NewInfo oldThirtyDaysNews = DataHelper.getOldTestNewsInfo(30);

        adminPanelPage
                .assertAdminPanelPageVisible()
                .deleteAllNews();

        // 1. Создаем новости
        List.of(futureNews, todayNews, oldOneNews, oldTwoDaysNews, oldSevenDaysNews, oldThirtyDaysNews)
                .forEach(news -> adminPanelPage.appBarNews.clickAddNewsButton()
                        .createNew(news)
                        .clickSaveNewButton());

// 2. ПРОВЕРКА В АДМИНКЕ (По умолчанию новые сверху)
        adminPanelPage
                .assertAdminPanelPageVisible()

                .verifyNewsAt(0, futureNews)
                .verifyNewsAt(1, todayNews)
                .verifyNewsAt(2, oldOneNews)
                .verifyNewsAt(3, oldTwoDaysNews)
                .verifyNewsAt(4, oldSevenDaysNews)
                .verifyNewsAt(5, oldThirtyDaysNews)


                .appBarNews.clickSortButton()
                .as(AdminPanelPage.class)
                .scrollToStart(0)

                .verifyNewsAt(0, oldThirtyDaysNews)
                .verifyNewsAt(1, oldSevenDaysNews)
                .verifyNewsAt(2, oldSevenDaysNews)
                .verifyNewsAt(3, oldTwoDaysNews)
                .verifyNewsAt(4, oldOneNews)
                .verifyNewsAt(5, todayNews)


// 3. ПРОВЕРКА НА СТРАНИЦЕ НОВОСТЕЙ нет новостей из будущего и старше 7 дней
                .appBar.goNewsPage()
                .assertNewsPageVisible()

                .as(AdminPanelPage.class)
                .verifyNewsAt(0, todayNews)
                .verifyNewsAt(1, oldOneNews)
                .verifyNewsAt(2, oldTwoDaysNews)
                .verifyNewsAt(3, oldSevenDaysNews)

                .as(NewsComponent.class)
                .waitUntilDoesNotExistNewComp(futureNews, 10000)
                .waitUntilDoesNotExistNewComp(oldThirtyDaysNews, 10000)


                .as(AdminPanelPage.class)
                .appBarNews.clickSortButton()
                .as(AdminPanelPage.class)
                .scrollToStart(0)

                .as(NewsPage.class)
                .findVerifyAndCollapseNews(todayNews)//не видит
                .findVerifyAndCollapseNews(oldOneNews)//не видит
                .findVerifyAndCollapseNews(oldTwoDaysNews)
                .findVerifyAndCollapseNews(oldSevenDaysNews)

                .as(NewsComponent.class)
                .waitUntilDoesNotExistNewComp(futureNews, 10000)
                .waitUntilDoesNotExistNewComp(oldThirtyDaysNews, 10000)

// 4. ПРОВЕРКА СОРТИРОВКИ НА СТРАНИЦЕ главная отображаются новости от сегодня минус неделя
                .as(AppBarComponent.class)
                .goMainPage()

                .assertMainPageVisible()

                .as(AdminPanelPage.class)
                .verifyNewsAt(0, todayNews)
                .verifyNewsAt(1, oldOneNews)
                .verifyNewsAt(2, oldTwoDaysNews)

                .as(NewsComponent.class)
                .waitUntilDoesNotExistNewComp(futureNews, 10000)
                .waitUntilDoesNotExistNewComp(oldSevenDaysNews, 10000)
                .waitUntilDoesNotExistNewComp(oldThirtyDaysNews, 10000)


//5.удалить новости
                .as(MainPage.class)
                .clickAllNewsLink()
                .appBarNews.clickEditButton()
                .assertAdminPanelPageVisible()
                .deleteAllNews()
                .clickRefreshButton()

                .as(NewsComponent.class)
                .waitUntilDoesNotExistNewComp(futureNews, 10000)
                .waitUntilDoesNotExistNewComp(todayNews, 10000)
                .waitUntilDoesNotExistNewComp(oldOneNews, 10000)
                .waitUntilDoesNotExistNewComp(oldTwoDaysNews, 10000)
                .waitUntilDoesNotExistNewComp(oldSevenDaysNews, 10000)
                .waitUntilDoesNotExistNewComp(oldThirtyDaysNews, 10000);
    }


    @Test
    @Story("Проверка полного цикла создания новости со статусом «Активна» и её отображения в общем списке.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_NEWS_01")
    @Description("Создать новость в категории Объявление текущей датой")
    public void shouldCreateNewsItemInAnnouncementCategory() {
        DataHelper.NewInfo testNewToday = DataHelper.getFirstTestNewsInfo();

        adminPanelPage.assertAdminPanelPageVisible()
                .appBarNews.clickAddNewsButton()
                .createNew(testNewToday)
                .clickSaveNewButton()

                .as(AdminPanelPage.class)
                .assertAdminPanelPageVisible()
                .findVerifyAndExpandNews(testNewToday, AdminPanelPage.NewsStatus.ACTIVE)

                .deleteNews(testNewToday)
                .clickDeleteNewOk()

                .assertAdminPanelPageVisible()
                .newsComponent.waitUntilDoesNotExistNewComp(testNewToday, 10000);
    }

    @Test
    @Story("Создание новости, которая не должна отображаться на странице Новости")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC_NEWS_02")
    @Description("Создание новости со статусом 'НЕ АКТИВНА'")
    public void shouldCreatingNewsItemStatusNotActive() {
        DataHelper.NewInfo testNewToday = DataHelper.getFirstTestNewsInfo();

        adminPanelPage
                .assertAdminPanelPageVisible()
                .appBarNews.clickAddNewsButton()
                .createNew(testNewToday)
                .clickSaveNewButton()

                .as(AdminPanelPage.class)
                .assertAdminPanelPageVisible()
                .findVerifyAndExpandNews(testNewToday, AdminPanelPage.NewsStatus.ACTIVE)
                .clickEditNewButton(testNewToday.getTitle())
                .clickCheckBox()
                .clickSaveNewButton()

                .as(AdminPanelPage.class)
                .assertAdminPanelPageVisible()
                .findVerifyAndExpandNews(testNewToday, AdminPanelPage.NewsStatus.NOT_ACTIVE)
                .appBar.goNewsPage()
                .assertNewsPageVisible()

                .as(NewsComponent.class)
                .waitUntilDoesNotExistNewComp(testNewToday, 10000)

                .as(AppBarNewsComponent.class)
                .clickEditButton()
                .assertAdminPanelPageVisible()
                .deleteNews(testNewToday)
                .clickDeleteNewOk()
                .assertAdminPanelPageVisible()
                .newsComponent.waitUntilDoesNotExistNewComp(testNewToday, 10000);
    }


    @Test
    @Story("Проверка фильтрации новостей по категории в админ-панели.")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC_NEWS_03")
    @Description("Проверка фильтрации новостей по категории в админ-панели.")
    public void CheckingFilteringByCategoryControlPanelTest() {
        DataHelper.NewInfo testNewToday = DataHelper.getFirstTestNewsInfo();

        adminPanelPage
                .assertAdminPanelPageVisible()
                .appBarNews
                .clickAddNewsButton()
                .createNew(testNewToday)
                .selectCategory(BaseComponent.NewsCategory.BIRTHDAY)
                .clickSaveNewButton()

                .as(AppBarNewsComponent.class)
                .clickIconFilter()
                .selectCategory(BaseComponent.NewsCategory.BIRTHDAY)
                .clickFilterButton()

                .as(AdminPanelPage.class)
                .findVerifyAndExpandNews(testNewToday, AdminPanelPage.NewsStatus.ACTIVE)

                .deleteNews(testNewToday)
                .clickDeleteNewOk()
                .assertAdminPanelPageVisible()
                .newsComponent
                .waitUntilDoesNotExistNewComp(testNewToday, 10000);
    }

    @Test
    @Story("Попытка фильтровать без выбора категории из выпадающего списка.")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("TC_NEWS_22")
    @Description("Поле «Категория» оставить невыбранным, остальные поля заполнить валидными данными")
    public void shouldLeaveCategoryUnselectedfilterTest() {
        DataHelper.FilterInfo invalidFilter = DataHelper.getDefaultFilterInfo().withCategory(null);

        adminPanelPage
                .assertAdminPanelPageVisible()
                .appBarNews
                .clickIconFilter()
                .fillFilterField(invalidFilter)
                .clickFilterButton()

                .as(AdminPanelPage.class)
                .assertAdminPanelPageVisible();
    }

    @Test
    @Story("Проверка изменения порядка отображения новостей при нажатии на кнопку сортировки.")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC_NEWS_09")
    @Description("Проверка порядка новостей после нажатия на кнопку сортировки")
    public void shouldChangingListNewsDisplayByClickingSortButtonTest() {
        DataHelper.NewInfo todayNews = DataHelper.getFirstTestNewsInfo();
        DataHelper.NewInfo oldOneNews = DataHelper.getOldTestNewsInfo(1);
        DataHelper.NewInfo oldTwoDaysNews = DataHelper.getOldTestNewsInfo(2);


        adminPanelPage
                .assertAdminPanelPageVisible()
                .deleteAllNews()

                .as(AdminPanelPage.class)
                .assertAdminPanelPageVisible();

        List.of(todayNews, oldOneNews, oldTwoDaysNews)
                .forEach(news -> adminPanelPage.appBarNews.clickAddNewsButton()
                        .createNew(news)
                        .clickSaveNewButton());

        adminPanelPage
                .assertAdminPanelPageVisible()
                .verifyNewsAt(0, todayNews)
                .verifyNewsAt(1, oldOneNews)
                .verifyNewsAt(2, oldTwoDaysNews)

                .appBarNews
                .clickSortButton()
                .as(AdminPanelPage.class)
                .assertAdminPanelPageVisible()
                .scrollToStart(0)

                .verifyNewsAt(0, oldTwoDaysNews)
                .verifyNewsAt(1, oldOneNews)
                .verifyNewsAt(2, todayNews)

                .deleteAllNews();
    }

    @Test
    @Story("Проверка принудительного обновления списка новостей.")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("TC_NEWS_10")
    @Description("Нажать кнопку «ОБНОВИТЬ» на странице Контрольная панель")
    public void shouldClickRefreshButtonAdminPanelPageTest() {
        adminPanelPage
                .assertAdminPanelPageVisible()
                .deleteAllNews()
                .clickRefreshButton()

                .as(AdminPanelPage.class)
                .waitUntilLoaderDoesNotExist(5000)

                .as(AdminPanelPage.class)
                .assertAdminPanelPageVisible();
    }

    @Test
    @Story("Проверка контента после обновленяи страницы.")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_CP_04")
    @Description("Создать новую новость, ОБНОВИТЬ страницу, проверить видимость созданного.")
    public void shouldCreateNewsClickUpdateTest() {
        DataHelper.NewInfo todayNews = DataHelper.getThirdTestNewsInfo();

        adminPanelPage
                .assertAdminPanelPageVisible()

                .appBarNews.clickAddNewsButton()
                .assertCreateNewPageVisible()
                .createNew(todayNews)
                .clickSaveNewButton()

                .as(AdminPanelPage.class)
                .assertAdminPanelPageVisible()
                .scrollToStart(0)
                .findVerifyAndExpandNews(todayNews, AdminPanelPage.NewsStatus.ACTIVE)

                .swipe(DataHelper.NEWS_LIST_RECYCLER)
                .as(AdminPanelPage.class)
                .findVerifyAndExpandNews(todayNews, AdminPanelPage.NewsStatus.ACTIVE)
                .deleteNews(todayNews)
                .clickDeleteNewOk()


                .as(NewsComponent.class)
                .waitUntilDoesNotExistNewComp(todayNews, 10000);
    }
}
