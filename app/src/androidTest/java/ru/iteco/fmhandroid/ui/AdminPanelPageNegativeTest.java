package ru.iteco.fmhandroid.ui;

import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
import ru.iteco.fmhandroid.ui.components.AppBarNewsComponent;
import ru.iteco.fmhandroid.ui.components.BaseComponent;
import ru.iteco.fmhandroid.ui.components.CreateNewComponent;
import ru.iteco.fmhandroid.ui.components.FilterComponent;
import ru.iteco.fmhandroid.ui.components.NewsComponent;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.page.AdminPanelPage;
import ru.iteco.fmhandroid.ui.page.AuthorizationPage;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)

@Epic("Страница Контрольная панель")
@Feature("Негативные проверки")
public class AdminPanelPageNegativeTest extends BaseTest {
    private AdminPanelPage adminPanelPage;

    @Before
    public void setUp() {
        adminPanelPage = new AuthorizationPage()
                .logIn(DataHelper.getAuthInfo())
                .appBar.goNewsPage()
                .appBarNews.clickEditButton();
    }




    @Test
    @Story("Попытка создания новости без заполнения обязательного поля «Заголовок».")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_NEWS_04")
    @Description("Не заполнять поле «Заголовок».")
    public void shouldCreateNewsWithoutFillingHeadlineTest() {
        DataHelper.NewInfo testNewToday = DataHelper.getFirstTestNewsInfo().withTitle(null);

        adminPanelPage.assertAdminPanelPageVisible()
                .appBarNews.clickAddNewsButton()
                .assertCreateNewPageVisible()
                .createNew(testNewToday)
                .clickSaveNewButton()

                .as(CreateNewComponent.class)
                .assertCreateNewPageVisible()
                .assertEmptyFieldErrorMessage(getDecorView())

                .clickCancelCreateNewButton()
                .clickOkDialog()
                .assertAdminPanelPageVisible()
                .newsComponent.waitUntilDoesNotExistNewComp(testNewToday, 10000);
        ;
    }

    @Test
    @Story("Отмена создания новости для проверки того, что данные не сохраняются.")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("TC_NEWS_05")
    @Description("Заполнить все поля валидными данными и нажать кнопку «Отмена».")
    public void shouldCancelNewsCreationTest() {
        DataHelper.NewInfo testNewToday = DataHelper.getFirstTestNewsInfo().withTitle(null);

        adminPanelPage.assertAdminPanelPageVisible()
                .appBarNews.clickAddNewsButton()
                .assertCreateNewPageVisible()
                .createNew(testNewToday)
                .clickCancelCreateNewButton()
                .clickOkDialog()

                .assertAdminPanelPageVisible()
                .newsComponent.waitUntilDoesNotExistNewComp(testNewToday, 10000);

    }

    @Test
    @Story("Установка даты «До» раньше даты «От» в фильтре новостей контрольной панели.")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC_NEWS_06 ")
    @Description("Установить дату «До» раньше даты «От» и нажать кнопку 'Фильтровать'")
    public void shouldSetToDateEarlierFromDateControlPanelFilterTest() {
        DataHelper.FilterInfo invalidFilter = new DataHelper.FilterInfo(
                BaseComponent.NewsCategory.ANNOUNCEMENT.getText(),
                DataHelper.getDateRangeFilter(0, 1).getToDate(),
                DataHelper.getDateRangeFilter(30, 0).getFromDate());

        adminPanelPage.assertAdminPanelPageVisible()
                .appBarNews.clickIconFilter()
                .fillFilterField(invalidFilter)
                .clickFilterButton()

                .as(FilterComponent.class)
                .assertErrorFilterMessage(getDecorView());
    }

    @Test
    @Story("Попытка создания новости без заполнения поля «Категория».")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC_NEWS_07")
    @Description("Не заполнять поле «Категория», остальные поля заполнить валидными данными.")
    public void shouldCreateNewsWithoutFillingCategoryTest() {
        DataHelper.NewInfo testNewToday = DataHelper.getFirstTestNewsInfo().withCategory(null);

        adminPanelPage.assertAdminPanelPageVisible()
                .appBarNews.clickAddNewsButton()
                .assertCreateNewPageVisible()
                .createNew(testNewToday)
                .clickSaveNewButton()

                .as(CreateNewComponent.class)
                .assertCreateNewPageVisible()
                .assertEmptyFieldErrorMessage(getDecorView())

                .clickCancelCreateNewButton()
                .clickOkDialog()
                .assertAdminPanelPageVisible()
                .newsComponent.waitUntilDoesNotExistNewComp(testNewToday, 10000);
    }

    @Test
    @Story("Проверка сброса выбранных параметров фильтрации при нажатии кнопки «Отмена».")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC_NEWS_12")
    @Description("Установить чекбокс «Неактивна» и нажать кнопку «Отмена»")
    public void shouldVerifyNewsFilteringAndStatusCancelationTest() {
        DataHelper.NewInfo firstNews = DataHelper.getFirstTestNewsInfo()
                .withCategory(BaseComponent.NewsCategory.HELP_NEEDED.getText());

        DataHelper.NewInfo secondNews = DataHelper.getSecondTestNewsInfo()
                .withCategory(BaseComponent.NewsCategory.HELP_NEEDED.getText());

        adminPanelPage
                .assertAdminPanelPageVisible()
                .deleteAllNews()

                .as(AdminPanelPage.class)
                .assertAdminPanelPageVisible();

        List.of(firstNews, secondNews)
                .forEach(news -> adminPanelPage.appBarNews.clickAddNewsButton()
                        .createNew(news)
                        .clickSaveNewButton());

        adminPanelPage
                .assertAdminPanelPageVisible()
                .findVerifyAndExpandNews(firstNews, AdminPanelPage.NewsStatus.ACTIVE)
                .findVerifyAndExpandNews(secondNews, AdminPanelPage.NewsStatus.ACTIVE)

                //изменение второй новости -не активна
                .editNews(secondNews, secondNews, AdminPanelPage.NewsStatus.ACTIVE)
                .clickCheckBox()
                .clickSaveNewButton()

                //проверка фильтра по первой - активной новости и не видно второй
                .as(AdminPanelPage.class)
                .assertAdminPanelPageVisible()
                .appBarNews.clickIconFilter()
                .assertFilterPageVisible()
                .filteringNews(firstNews, firstNews.getDate())
                .clickCheckBoxInactiveButton()
                .clickFilterButton()

                .as(AdminPanelPage.class)
                .assertAdminPanelPageVisible()
                .findVerifyAndExpandNews(firstNews, AdminPanelPage.NewsStatus.ACTIVE)
                .as(NewsComponent.class)
                .waitUntilDoesNotExistNewComp(secondNews, 10000)

                //проверка фильтра второй - не активной новости
                .as(AdminPanelPage.class)
                .appBarNews.clickIconFilter()
                .assertFilterPageVisible()
                .filteringNews(secondNews, secondNews.getDate())
                .clickCheckBoxActiveButton()
                .clickFilterButton()

                .as(AdminPanelPage.class)
                .assertAdminPanelPageVisible()
                .findVerifyAndExpandNews(secondNews, AdminPanelPage.NewsStatus.NOT_ACTIVE)
                .as(NewsComponent.class)
                .waitUntilDoesNotExistNewComp(firstNews, 10000)

                //далее проверка что статус не поменялся при отмене
                //настраиваем фильтр чтобы увидеть новость
                .as(AdminPanelPage.class)
                .appBarNews.clickIconFilter()
                .filteringNews(firstNews, firstNews.getDate())
                .clickCheckBoxInactiveButton()
                .clickFilterButton()

                //пробуем изменить статус не сохраняя
                .as(AdminPanelPage.class)
                .editNews(firstNews, firstNews, AdminPanelPage.NewsStatus.ACTIVE)
                .clickCheckBox()
                .clickCancelCreateNewButton()
                .clickOkDialog()

                //настраиваем фильтр чтобы увидеть
                .assertAdminPanelPageVisible()
                .appBarNews.clickIconFilter()
                .assertFilterPageVisible()
                .filteringNews(firstNews, firstNews.getDate())
                .clickCheckBoxInactiveButton()
                .clickFilterButton()

                //проверяем что первая на месте а второй нет
                .as(AdminPanelPage.class)
                .assertAdminPanelPageVisible()
                .findVerifyAndExpandNews(firstNews, AdminPanelPage.NewsStatus.ACTIVE)
                .as(NewsComponent.class)
                .waitUntilDoesNotExistNewComp(secondNews, 10000)

                //удаляем за собой
                .as(AdminPanelPage.class)
                .assertAdminPanelPageVisible()
                .appBarNews.clickIconFilter()
                .assertFilterPageVisible()
                .filteringNews(firstNews, firstNews.getDate())
                .clickFilterButton()

                .as(AdminPanelPage.class)
                .findVerifyAndExpandNews(firstNews, AdminPanelPage.NewsStatus.ACTIVE)
                .findVerifyAndExpandNews(secondNews, AdminPanelPage.NewsStatus.NOT_ACTIVE)
                .deleteAllNews();
    }

    @Test
    @Story("Попытка создания новости с датой публикации в прошлом.")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC_NEWS_13")
    @Description("Создать новость с датой публикации из прошлого")
    public void shouldCreateNewsWithPastPublicationDateTest() {
        DataHelper.NewInfo testNewToday = DataHelper.getFirstTestNewsInfo()
                .withDate(DataHelper.getDateRangeFilter(7, 0).getFromDate());

        adminPanelPage
                .appBarNews.clickAddNewsButton()
                .assertCreateNewPageVisible()
                .createNew(testNewToday)
                .clickSaveNewButton()

                .as(CreateNewComponent.class)
                .assertCreateNewPageVisible()
                .assertEmptyFieldErrorMessage(getDecorView());
    }


    @Test
    @Story("Проверка, что после нажатия «Отмена» при следующем открытии форма создания пустая.")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("TC_NEWS_18")
    @Description("Пользователь заполнил «Заголовок» и нажал «Отмена», повторно нажать на кнопку создания новости - форма очистилась")
    public void shouldClearingInputWindowAfterReopeningTest() {
        DataHelper.NewInfo testNewToday = DataHelper.getThirdTestNewsInfo();

        adminPanelPage
                .appBarNews.clickAddNewsButton()
                .assertCreateNewPageVisible()
                .createNew(testNewToday)
                .clickCancelCreateNewButton()
                .clickOkDialog()

                .as(AppBarNewsComponent.class)
                .clickAddNewsButton()
                .assertCreateNewPageVisible();
    }

    @Test
    @Story("Проверка корректности сохранения и отображения новости с очень длинным заголовоком " +
            "и экстремально длинным текстом(граничные значения).")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC_NEWS_20")
    @Description("Вставить в поле «Заголовок» строку из 255 символов. " +
            "В поле «Описание» вставить текст объемом 2000+ символов")
    public void shouldCorrectlySaveNewsWithExtremelyLongTextTest() {
        DataHelper.NewInfo testNewToday = DataHelper.getThirdTestNewsInfo()
                .withTitle(DataHelper.getRandomString(255))
                .withDescription(DataHelper.getRandomString(2001));


        adminPanelPage
                .appBarNews.clickAddNewsButton()
                .assertCreateNewPageVisible()
                .createNew(testNewToday)
                .clickSaveNewButton()

                .as(CreateNewComponent.class)
                .assertCreateNewPageVisible()
                .assertEmptyFieldErrorMessage(getDecorView());
    }

//    @Ignore("Тест временно отключен: вызывает Native Crash (SIGABRT) и блокирует выполнение пула тестов. Баг заведен Isues #10")
    @Test
    @Story("Попытка создания новости с форматом даты дд.мм.гг")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC_NEWS_31")
    @Description("Создать новость с датой публикации в формате дд.мм.гг")
    public void shouldCreateNewsWithPublicationDateFormatDdMmYyTest() {
        DataHelper.NewInfo testNewToday = DataHelper.getFirstTestNewsInfo()
                .withDate("01.01.25");

        adminPanelPage
                .appBarNews.clickAddNewsButton()
                .assertCreateNewPageVisible()
                .createNew(testNewToday);

        try {
            adminPanelPage
                    .as(CreateNewComponent.class)
                    .clickSaveNewButton();

            adminPanelPage.as(CreateNewComponent.class)
                    .assertEmptyFieldErrorMessage(getDecorView());

        } catch (Throwable e) {
            Allure.step("Крэш приложения: " + e.toString());

            System.gc();

            Assert.fail("Баг воспроизведен: Crash при сохранении даты ДД.ММ.ГГ. Ошибка: " + e.getMessage());
        }

    }
}
