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
import ru.iteco.fmhandroid.ui.components.CreateNewComponent;
import ru.iteco.fmhandroid.ui.components.NewsComponent;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.matchers.RecyclerViewMatcher;
import ru.iteco.fmhandroid.ui.page.AdminPanelPage;
import ru.iteco.fmhandroid.ui.page.AuthorizationPage;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)

@Epic("Форма создания новости")
@Feature("Позитивные проверки")

public class CreateNewComponentTest extends BaseTest {

    private CreateNewComponent createNewComponent;

    @Before
    public void setUp() {
        createNewComponent = new AuthorizationPage()
                .logIn(DataHelper.getAuthInfo())
                .appBar.goNewsPage()
                .appBarNews.clickEditButton()
                .appBarNews.clickAddNewsButton();
    }


    @Test
    @Story("Попытка создания новости с пустыми полями.")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC_CC_01")
    @Description("Открыть форму нажать сохранить ")
    public void negativeCreateNewsTest() {
        createNewComponent
                .assertCreateNewPageVisible()
                .clickSaveNewButton();

        createNewComponent.assertAllFieldsHaveError()
                .clickCancelCreateNewButton();

        createNewComponent.clickCancelDialog()
                .assertCreateNewPageVisible();
    }

    @Test
    @Story("Создание новости с валидными даннымии и ее поиск по содержимому.")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC_CC_02")
    @Description("Создать новость и найти ее по контенту")
    public void positiveCreateNewsTest() {
        DataHelper.NewInfo newsInfo = DataHelper.getFirstTestNewsInfo();

        createNewComponent
                .assertCreateNewPageVisible()
                .createNew(newsInfo)
                .clickSaveNewButton()

                .as(AdminPanelPage.class)
                .assertAdminPanelPageVisible()
                .findVerifyAndExpandNews(newsInfo, AdminPanelPage.NewsStatus.ACTIVE)

                .deleteNews(newsInfo)
                .clickDeleteNewOk()
                .assertAdminPanelPageVisible()

                .as(NewsComponent.class)
                .waitUntilDoesNotExistNewComp(newsInfo, 10000);
    }

    @Test
    @Story("Создание новости с валидными даннымии и поиск ее по номеру позиции.")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC_CC_03 флак")
    @Description("Создания новости с поиском ее по номеру позиции")
    public void createAndVerifyNews() {
        DataHelper.NewInfo newsData = DataHelper.getFirstTestNewsInfo();

        createNewComponent
                .createNew(newsData)
                .clickSaveNewButton();

        DataHelper.NewsPreview expectedPreview = newsData.toPreview();

        String actualTitle = RecyclerViewMatcher.getTextAtPosition(DataHelper.NEWS_LIST_RECYCLER, 0, DataHelper.DATA_TITLE_LABEL);
        String actualDate = RecyclerViewMatcher.getTextAtPosition(DataHelper.NEWS_LIST_RECYCLER, 0, DataHelper.PUBLICATION_DATE_FIELD);

        expectedPreview.assertMatches(actualTitle, actualDate);
    }
}
