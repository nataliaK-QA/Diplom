package ru.iteco.fmhandroid.ui.components;

import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.ActiveHelper;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.page.AdminPanelPage;

public class CreateNewComponent extends BaseComponent {
    @IdRes
    public static final int TITLE_LABEL = R.id.custom_app_bar_title_text_view;
    @StringRes
    public static final int TITLE_LABEL_TEXT = R.string.creating;
    @IdRes
    public static final int SUB_TITLE_LABEL = R.id.custom_app_bar_sub_title_text_view;
    @StringRes
    public static final int SUB_TITLE_LABEL_TEXT = R.string.news;
    @StringRes
    public static final int CATEGORY_FIELD_DEF_TEXT = R.string.news_item_category;

    @IdRes
    public static final int TITLE_ITEM_FIELD = R.id.news_item_title_text_input_edit_text;
    @IdRes
    public static final int TITLE_ITEM_CONTAINER = R.id.news_item_title_text_input_layout;
    @IdRes
    public static final int CREATE_DATE_CONTAINER = R.id.news_item_create_date_text_input_layout;


    @StringRes
    public static final int TITLE_ITEM_FIELD_DEF_TEXT = R.string.news_item_title;
    @IdRes
    public static final int PUBLISH_DATE_FIELD = R.id.news_item_publish_date_text_input_edit_text;
    @StringRes
    public static final int PUBLISH_DATE_FIELD_DEF_TEXT = R.string.news_item_publish_date;
    @IdRes
    public static final int PUBLISH_TIME_FIELD = R.id.news_item_publish_time_text_input_edit_text;
    @IdRes
    public static final int PUBLISH_TIME_CREATE_CONTAINER = R.id.news_item_publish_time_text_input_layout;
    @StringRes
    public static final int PUBLISH_TIME_FIELD_DEF_TEXT = R.string.news_item_publish_time;
    @IdRes
    public static final int DESCRIPTION_FIELD = R.id.news_item_description_text_input_edit_text;
    @IdRes
    public static final int DESCRIPTION_FIELD_CONTAINER = R.id.news_item_description_text_input_layout;
    @StringRes
    public static final int DESCRIPTION_FIELD_DEF_TEXT = R.string.news_item_description;
    @IdRes
    public static final int CHECK_BOX_ACTIVE = R.id.switcher;
    @StringRes
    public static final int CHECK_BOX_ACTIVE_TEXT = R.string.news_item_active;
    @IdRes
    public static final int SAVE_BUTTON = R.id.save_button;
    @StringRes
    public static final int SAVE_BUTTON_TEXT = R.string.save;
    @StringRes
    public static final int MESSAGE_EMPTY_TEXT = R.string.empty_fields;
    @IdRes
    public static final int FIELD_ERROR_ICON_ID = com.google.android.material.R.id.text_input_end_icon;
    @StringRes
    public static final int ERROR_MESSAGE_EMPTY_FIELD = R.string.empty_fields; // ТЕКСТ ошибки пустые поля

    public CreateNewComponent() {
    }

    @Step("Проверка корректности открытия страницы создания новости")
    public CreateNewComponent assertCreateNewPageVisible() {
        Allure.step("Проверка корректности открытия страницы создания новости");
        assertAllVisible(
                TITLE_ITEM_FIELD,
                PUBLISH_DATE_FIELD,
                PUBLISH_TIME_FIELD,
                DESCRIPTION_FIELD,
                SAVE_BUTTON
        );

        assertHintInIdResDisplayed(TITLE_ITEM_FIELD, TITLE_ITEM_FIELD_DEF_TEXT);
        assertHintInIdResDisplayed(PUBLISH_DATE_FIELD, PUBLISH_DATE_FIELD_DEF_TEXT);

        return this;
    }

    @Step("Проверка всех полей на наличие ошибок валидации")
    public CreateNewComponent assertAllFieldsHaveError() {
        Allure.step("Проверка всех полей на наличие ошибок валидации");
        int[] layoutIds = {
                DataHelper.NEWS_ITEM_CATEGORY_FIELD,
                TITLE_ITEM_CONTAINER,
                CREATE_DATE_CONTAINER,
                PUBLISH_TIME_CREATE_CONTAINER,
                DESCRIPTION_FIELD_CONTAINER
        };

        for (int id : layoutIds) {
            waitAndAssert(
                    allOf(withId(FIELD_ERROR_ICON_ID),
                            isDescendantOfA(withId(id)),
                            isDisplayed()),
                    2000
            );
        }
        return this;
    }

    @Step("Нажатие кнопки 'Сохранить'")
    public BaseComponent clickSaveNewButton() {
        Allure.step("Нажатие кнопки 'Сохранить'");
        closeSoftKeyboard();
        clickOn(SAVE_BUTTON);
        return this;
    }

    @Step("Нажатие кнопки 'Отмена'")
    public CreateNewComponent clickCancelCreateNewButton() {
        Allure.step("Нажатие кнопки 'Отмена'");
        clickOn(DataHelper.CANCEL_BUTTON);
        return this;
    }

    @Step("Нажатие кнопки диалога 'Отмена'")
    public CreateNewComponent clickCancelDialog() {
        Allure.step("Нажатие кнопки диалога 'Отмена'");
        clickOn(DataHelper.DIALOG_CANCEL_BUTTON);
        return this;
    }

    @Step("Нажатие кнопки диалога 'Ок'")
    public AdminPanelPage clickOkDialog() {
        Allure.step("Нажатие кнопки диалога 'Ок'");
        clickOn(DataHelper.DIALOG_OK_BUTTON);
        return new AdminPanelPage();
    }

    @Step("Нажатие кнопки статуса новости 'АКТИВНА / НЕ АКТИВНА'")
    public CreateNewComponent clickCheckBox() {
        Allure.step("Нажатие кнопки статуса новости 'АКТИВНА / НЕ АКТИВНА'");
        clickOn(CHECK_BOX_ACTIVE);
        return this;
    }

    @Step("Заполнение полей новости: {newInfo.title}")
    public CreateNewComponent createNew(DataHelper.NewInfo newInfo) {
        Allure.step("Заполнение полей: " + newInfo.getTitle());

        assertCreateNewPageVisible();

        // Ждем только первый элемент, подтверждая загрузку формы
        waitAndAssert(withId(SAVE_BUTTON), 10000);

        // Используем унифицированный метод ввода (replaceText + closeKeyboard)
        fill(DataHelper.NEWS_ITEM_CATEGORY_TEXT, newInfo.getCategory());
        fill(TITLE_ITEM_FIELD, newInfo.getTitle());
        fill(PUBLISH_DATE_FIELD, newInfo.getDate());
        fill(PUBLISH_TIME_FIELD, newInfo.getTime());
        fill(DESCRIPTION_FIELD, newInfo.getDescription());

        return this;
    }

    @Step("Заполнение поля категория Id текстом на странице Create: {category}")
    public CreateNewComponent selectCategory(NewsCategory category) {
        Allure.step("Заполнение поля категория Id текстом на странице Create: {category}");
        fill(DataHelper.NEWS_ITEM_CATEGORY_TEXT, category.getText());
        return this;
    }

    @Step("Проверка сообщения о пустом поле")
    public CreateNewComponent assertEmptyFieldErrorMessage(View decorView) {
        Allure.step("Проверка сообщения о пустом поле");
        ActiveHelper.checkToast(ERROR_MESSAGE_EMPTY_FIELD, decorView);
        return this;
    }

    @Step("Клик по полю 'Время' страницы Создание новости")
    public CreateNewComponent clickTimeField() {
        Allure.step("Клик по полю 'Время' страницы Создание новости");
        clickOn(PUBLISH_TIME_FIELD);
        return this;
    }

    @Step("Извлечение времени из поля 'Время'")
    public String getPublishTime() {
        Allure.step("Извлечение времени из поля 'Время'");
        return getTextFromMatcher(withId(PUBLISH_TIME_FIELD));
    }
}
