package ru.iteco.fmhandroid.ui.page;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.components.AppBarComponent;
import ru.iteco.fmhandroid.ui.components.BaseComponent;

public class AboutPage extends BaseComponent {

    public AppBarComponent appBar = new AppBarComponent();
    @IdRes
    public static final int ABOUT_SCREEN_CONTAINER = R.id.container_custom_app_bar_include_on_fragment_about;
    @IdRes
    public static final int ABOUT_VERSION_TITLE = R.id.about_version_title_text_view;
    @IdRes
    public static final int ABOUT_VERSION_VALUE = R.id.about_version_value_text_view;
    @IdRes
    public static final int ABOUT_POLICY_LABEL_TEXT = R.id.about_privacy_policy_label_text_view;
    @IdRes
    public static final int ABOUT_POLICY_VALUE_TEXT = R.id.about_privacy_policy_value_text_view;
    @IdRes
    public static final int ABOUT_TERMS_LABEL_TEXT = R.id.about_terms_of_use_label_text_view;
    @IdRes
    public static final int ABOUT_TERMS_VALUE_TEXT = R.id.about_terms_of_use_value_text_view;
    @IdRes
    public static final int ABOUT_COMPANY_LABEL_TEXT = R.id.about_company_info_label_text_view;
    @IdRes
    public static final int ABOUT_BACK_BUTTON = R.id.about_back_image_button;
    @StringRes
    public static final int PRIVACY_POLICY_URL = R.string.privacy_policy_url;
    @StringRes
    public static final int TERMS_OF_USE_URL = R.string.terms_of_use_url;

    public AboutPage() {
    }

    @Step("Проверка видимости элементов страницы 'About'")
    public AboutPage assertAboutPageVisible() {
        appBar.assertAppBarForAboutVisible()

                .as(AboutPage.class)
                .assertAllVisible(
                        ABOUT_SCREEN_CONTAINER,
                        ABOUT_VERSION_TITLE,
                        ABOUT_VERSION_VALUE,
                        ABOUT_POLICY_LABEL_TEXT,
                        ABOUT_POLICY_VALUE_TEXT,
                        ABOUT_TERMS_LABEL_TEXT,
                        ABOUT_TERMS_VALUE_TEXT,
                        ABOUT_COMPANY_LABEL_TEXT,
                        ABOUT_BACK_BUTTON
                );
        return this;
    }

    @Step("Проверка ссылки на Политику конфиденциальности")
    public AboutPage assertPrivacyPolicyText() {
        Allure.step("Проверка ссылки на Политику конфиденциальности");
        waitAndAssertClickable(ABOUT_POLICY_VALUE_TEXT, PRIVACY_POLICY_URL);
        return this;
    }

    @Step("Проверка ссылки на Пользовательское соглашение")
    public AboutPage assertTermsOfUseText() {
        Allure.step("Проверка ссылки на Пользовательское соглашение");
        waitAndAssertClickable(ABOUT_TERMS_VALUE_TEXT, TERMS_OF_USE_URL);
        return this;
    }

    @Step("Возврат назад из 'About'")
    public <T extends BaseComponent> T clickAboutBack(Class<T> clazz) {
        clickOn(ABOUT_BACK_BUTTON);
        return as(clazz);
    }
}


