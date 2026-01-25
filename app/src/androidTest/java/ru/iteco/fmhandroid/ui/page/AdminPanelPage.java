package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static ru.iteco.fmhandroid.ui.data.ActiveHelper.waitUntilVisible;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;

import org.hamcrest.Matcher;
import org.junit.Assert;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.components.AppBarComponent;
import ru.iteco.fmhandroid.ui.components.AppBarNewsComponent;
import ru.iteco.fmhandroid.ui.components.BaseComponent;
import ru.iteco.fmhandroid.ui.components.CreateNewComponent;
import ru.iteco.fmhandroid.ui.components.NewsComponent;
import ru.iteco.fmhandroid.ui.data.ActiveHelper;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.matchers.RecyclerViewMatcher;

public class AdminPanelPage extends BaseComponent {
    public AppBarComponent appBar = new AppBarComponent();
    public AppBarNewsComponent appBarNews = new AppBarNewsComponent();
    public NewsComponent newsComponent = new NewsComponent();

    // Внутренние элементы карточки новости
    @StringRes
    public static final int DATA_TITLE_FIELD_TEXT = R.string.news_item_title; //ТЕКСТ ЛЭЙБЛ ЗАГОЛОВКА ДЕФОЛТНЫЙ
    @IdRes
    public static final int PUBLICATION_DATE_LABEL = R.id.news_item_publication_text_view; // ЛЭЙБЛ  "Дата публикации"
    @IdRes
    public static final int PUBLICATION_DATE_LABEL_TEXT = R.string.news_item_publication; // Текст ЛЭЙБЛА "Дата публикации"

    @StringRes
    public static final int DEFOLT_DATE_FIELD_TEXT = R.string.news_item_date_format; // Текст дефолт в поле даты
    @IdRes
    public static final int CREATION_DATE_LABEL = R.id.news_item_creation_text_view; // Лэйбл "Дата создания"
    @StringRes
    public static final int CREATION_DATE_LABEL_TEXT = R.string.news_item_creation; // Текст в ЛЭЙБЛЕ  "Дата создания"
    @IdRes
    public static final int CREATION_DATE_FIELD = R.id.news_item_create_date_text_view; // Поле значение даты создания

    // Индикаторы статуса
    @IdRes
    public static final int STATUS_TEXT_CONTAINER = R.id.news_item_published_text_view; // АКТИВНА / НЕ АКТИВНА
    @StringRes
    public static final int NOT_ACTIVE_STATUS_TEXT = R.string.news_control_panel_not_active; // ТЕКСТ НЕ АКТИВНА
    @StringRes
    public static final int ACTIVE_STATUS_TEXT = R.string.news_control_panel_active; // ТЕКСТ АКТИВНА

    @IdRes
    public static final int STATUS_ICON = R.id.news_item_published_icon_image_view; //картинка статуса

    // Кнопки действий
    @IdRes
    public static final int DELETE_DATA_BUTTON = R.id.delete_news_item_image_view;
    @IdRes
    public static final int EDIT_BUTTON = R.id.edit_news_item_image_view;
    @IdRes
    public static final int EXPAND_DATA_BUTTON = R.id.view_news_item_image_view;
    @IdRes
    public static final int REFRESH_ADMIN_PAGE_BUTTON = R.id.control_panel_news_retry_material_button;

    public AdminPanelPage() {
    }

    @Step("Проверка панели управления (контент или кнопка обновления)")
    public AdminPanelPage assertAdminPanelPageVisible() {
        Allure.step("Короткая проверка панели управления");

        if (ActiveHelper.isElementDisplayedBoolean(withId(DataHelper.NEWS_CARD_CONTAINER))) {

            onView(withId(DataHelper.NEWS_LIST_RECYCLER)).check(matches(isDisplayed()));

        } else {

            Allure.step("Контент не найден, проверяем кнопку 'ОБНОВИТЬ'");

            onView(allOf(
                    withId(REFRESH_ADMIN_PAGE_BUTTON),
                    withText(DataHelper.REFRESH_BUTTON_TEXT),
                    isDisplayed()
            )).check(matches(isDisplayed()));
        }

        return this;
    }

    @Step("Нажатие кнопки раскрывающей описание")
    public void clickDescriptionButton() {
        Allure.step("Нажатие кнопки раскрывающей описание");

        clickOn(DataHelper.DESCRIPTION_TEXT_FIELD);
    }

    @Step("Нажатие кнопки 'Редактировать новость' с заголовком: {title}")
    public CreateNewComponent clickEditNewButton(String title) {
        Allure.step("Нажатие кнопки 'Редактировать новость' с заголовком: {title}");
        onView(allOf(
                withId(DataHelper.NEWS_CARD_CONTAINER),
                hasDescendant(withText(title))
        )).perform(ActiveHelper.internalClickAction(EDIT_BUTTON));

        return new CreateNewComponent();
    }

    @Step("Поиск и проверка новости: {info.title}")
    public AdminPanelPage findVerifyAndExpandNews(DataHelper.NewInfo info, NewsStatus status) {
        Allure.step("Поиск и проверка всех полей новости: " + info.getTitle());
        onView(isRoot()).perform(waitUntilVisible(withId(DataHelper.DATA_TITLE_LABEL), 10000));

        Matcher<View> cardMatcher = hasDescendant(allOf(
                withId(DataHelper.DATA_TITLE_LABEL),
                withText(info.getTitle()),

                hasSibling(allOf(
                        withId(DataHelper.PUBLICATION_DATE_FIELD),
                        withText(containsString(info.getDate()))
                ))
        ));

        onView(withId(DataHelper.NEWS_LIST_RECYCLER))
                .perform(RecyclerViewActions.scrollTo(cardMatcher));

        onView(allOf(
                withId(DataHelper.DATA_TITLE_LABEL),
                withText(info.getTitle()),
                isCompletelyDisplayed()
        )).perform(click());


        waitAndAssert(allOf(withText(info.getDescription()), isDisplayed()), 5000);

        onView(allOf(
                withId(DataHelper.DATA_TITLE_LABEL),
                withText(info.getTitle()),
                isCompletelyDisplayed()
        )).perform(click());

        return new AdminPanelPage();
    }

    @Step("Удаление найденной новости: {title}")
    public AdminPanelPage deleteNews(DataHelper.NewInfo info) {
        Allure.step("Удаление найденной новости: {title}");
        Matcher<View> cardMatcher = allOf(
                withId(DataHelper.NEWS_CARD_CONTAINER),
                hasDescendant(withText(info.getTitle()))
        );

        onView(withId(DataHelper.NEWS_LIST_RECYCLER)).perform(RecyclerViewActions.scrollTo(cardMatcher));

        onView(allOf(withId(DELETE_DATA_BUTTON), isDescendantOfA(cardMatcher))).perform(click());

        return this;
    }

    @Step("Нажатие кнопки Ок диалога 'Удалить новость'")
    public AdminPanelPage clickDeleteNewOk() {
        Allure.step("Нажатие кнопки Ок диалога 'Удалить новость'");
        clickOn(DataHelper.DIALOG_OK_BUTTON);
        return this;
    }

    @Step("Отмена удаления найденной новости: {title}")
    public AdminPanelPage cancelDeleteNews(DataHelper.NewInfo info) {
        Allure.step("Отмена удаления найденной новости: {title}");

        onView(allOf(
                withId(DELETE_DATA_BUTTON),
                hasSibling(withText(info.getTitle())),
                isDisplayed()
        )).perform(click());

        clickOn(DataHelper.DIALOG_CANCEL_BUTTON);

        newsComponent.waitUntilDoesNotExistNewComp(info, 5000);

        return this;
    }

    @Step("Редактирование новости '{oldInfo.title}'")
    public CreateNewComponent editNews(DataHelper.NewInfo oldInfo, DataHelper.NewInfo newInfo, NewsStatus status) {
        Allure.step("Редактирование новости '{oldInfo.title}'");
        findVerifyAndExpandNews(oldInfo, status);

        return clickEditNewButton(oldInfo.getTitle()).createNew(newInfo);
    }

    @Step("Удаление всех новостей ")
    public AdminPanelPage deleteAllNews() {
        Allure.step("Начинаем очистку списка новостей");

        while (!ActiveHelper.isElementDisplayedBoolean(withId(REFRESH_ADMIN_PAGE_BUTTON))) {
            try {
                waitUntilLoaderDoesNotExist(3000);
                onView(withId(DataHelper.NEWS_LIST_RECYCLER))
                        .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                                ActiveHelper.internalClickAction(DELETE_DATA_BUTTON)));


                onView(isRoot()).perform(waitUntilVisible(withId(DataHelper.DIALOG_OK_BUTTON), 5000));

                onView(isRoot()).perform(new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return isRoot();
                    }

                    @Override
                    public String getDescription() {
                        return "loop main thread for dialog";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        uiController.loopMainThreadForAtLeast(1000);
                    }
                });
                waitUntilLoaderDoesNotExist(3000);
                clickOn(DataHelper.DIALOG_OK_BUTTON);

                onView(isRoot()).perform(new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return isRoot();
                    }

                    @Override
                    public String getDescription() {
                        return "Пауза для обновления списка";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        uiController.loopMainThreadForAtLeast(1000);
                    }
                });

            } catch (Throwable e) {
                break;
            }
        }
        return this;
    }

    @Step("Скролл списка")
    public AdminPanelPage scrollToStart(int pos) {
        Allure.step("Скролл списка");
        onView(withId(DataHelper.NEWS_LIST_RECYCLER))
                .perform(RecyclerViewActions.scrollToPosition(pos));
        return this;
    }


    @Step("Проверка новости на позиции ")
    public AdminPanelPage verifyNewsAt(int position, DataHelper.NewInfo news) {
        Allure.step("Проверка новости на позиции ");
        RecyclerViewMatcher.verifyNewsAtPosition(position, news);
        return this;
    }

    @Step("Сравнение дат создания и публикации новости")
    public AdminPanelPage equalDateCreateToPublication(DataHelper.NewInfo info, NewsStatus status) {
        Allure.step("Сравнение дат создания и публикации новости");
        onView(withId(DataHelper.NEWS_LIST_RECYCLER))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(info.getTitle()))));

        Matcher<View> myCard = hasDescendant(withText(info.getTitle()));

        String pubDate = getTextFromMatcher(RecyclerViewMatcher.first(allOf(
                withId(DataHelper.PUBLICATION_DATE_FIELD),
                isDescendantOfA(myCard),
                isCompletelyDisplayed()
        )));

        String creDate = getTextFromMatcher(RecyclerViewMatcher.first(allOf(
                withId(CREATION_DATE_FIELD),
                isDescendantOfA(myCard),
                isCompletelyDisplayed()
        )));

        Assert.assertEquals("Дата создания не совпадает с публикацией!", pubDate, creDate);

        return this;
    }

    @Step("Клик по кнопке 'Обновить' страницы Контрольная панель")
    public AdminPanelPage clickRefreshButton() {
        Allure.step("Клик по кнопке 'Обновить' страницы Контрольная панель");
        clickOn(REFRESH_ADMIN_PAGE_BUTTON);
        return this;
    }

    public enum NewsStatus {
        ACTIVE(ACTIVE_STATUS_TEXT),
        NOT_ACTIVE(NOT_ACTIVE_STATUS_TEXT);

        private final int resourceId;

        NewsStatus(@StringRes int resourceId) {
            this.resourceId = resourceId;
        }

        @StringRes
        public int getResourceId() {
            return resourceId;
        }
    }

}
