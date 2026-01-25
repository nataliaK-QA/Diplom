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
import ru.iteco.fmhandroid.ui.components.FilterComponent;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.page.AuthorizationPage;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)

@Epic("Форма фильтра приложения")
@Feature("Негативные проверки")


public class FilterComponentNegativeTest extends BaseTest {
    private FilterComponent filterComponent;

    @Before
    public void setUp() {
        filterComponent = new AuthorizationPage()
                .logIn(DataHelper.getAuthInfo())
                .appBar.goNewsPage()
                .appBarNews.clickIconFilter();
    }

    @Test
    @Story("Проверка отображения формы фильтра.")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC_FL_01")
    @Description("Открыть форму фильтра.")
    public void visualFilterPageTest() {
        filterComponent.assertFilterPageVisible();
    }


}
