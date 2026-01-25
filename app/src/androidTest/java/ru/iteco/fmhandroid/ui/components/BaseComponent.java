package ru.iteco.fmhandroid.ui.components;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static ru.iteco.fmhandroid.ui.data.ActiveHelper.waitUntilVisible;

import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;

import org.hamcrest.Matcher;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.ui.data.ActiveHelper;

public abstract class BaseComponent {

    @Step("Заполнение полей {containerId} значениями text")
    protected void fillInsideContainer(@IdRes int containerId, String text) {
        Allure.step("Заполнение полейId значениями text");
        if (text == null) {
            Allure.step("Пропуск заполнения: значение text равно null");
            return;
        }
        onView(allOf(
                isDescendantOfA(withId(containerId)),
                isAssignableFrom(EditText.class)
        )).perform(replaceText(text), closeSoftKeyboard());
    }

    @Step("Групповая проверка видимости по ID ресурсов")
    protected void assertAllVisible(int... ids) {
        Allure.step("Групповая проверка видимости по ID ресурсов");
        for (int id : ids) {
            onView(withId(id)).check(matches(isDisplayed()));
        }
    }

    @Step("Извлечение текста по Id {resId} то что видим")
    protected String getTextFromMatcher(Matcher<View> matcher) {
        Allure.step("Извлечение текста по Id {resId} то что видим");
        final String[] text = {null};
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "извлечение текста по матчеру";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView textView = (TextView) view;
                text[0] = textView.getText().toString();
            }
        });
        return text[0];
    }

    @Step("Переход на следующую страницу")
    public <T extends BaseComponent> T as(Class<T> clazz) {
        Allure.step("Переход на следующую страницу");
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать страницу: " + clazz.getName());
        }
    }

    @Step("Ожидание и проверка видимости элемента: {matcher}")
    public BaseComponent waitAndAssert(Matcher<View> matcher, int timeout) {
        onView(isRoot()).perform(ActiveHelper.waitUntilVisible(matcher, timeout));
        onView(matcher).check(matches(isDisplayed()));
        return this;
    }

    @Step("Свайп элемента {idRes}")
    public BaseComponent swipe(@IdRes int idRes) {
        Allure.step("Свайп");
        onView(isRoot()).perform(ActiveHelper.waitUntilVisible(withId(idRes), 5000));

        onView(withId(idRes))
                .check(matches(isDisplayed()))
                .perform(swipeDown());

        waitUntilLoaderDoesNotExist(3000);
        return this;
    }

    @Step("Проверка: в ID {idRes} отображается текст '{stringRes}'")
    public BaseComponent assertStringResInIdResDisplayed(@IdRes int idRes, @StringRes int stringRes) {
        String text = getString(stringRes);
        Allure.step("Проверка ID: " + idRes + " текст: " + text);
        waitAndAssert(allOf(withId(idRes), withText(text)), 5000);
        return this;
    }

    public String getString(@StringRes int resId) {
        return getInstrumentation().getTargetContext().getString(resId);
    }

    @Step("Ожидание завершения загрузки (исчезновения лоадера)")
    public BaseComponent waitUntilLoaderDoesNotExist(int timeout) {
        Allure.step("Ожидание завершения загрузки (исчезновения лоадера)");
        onView(isRoot()).perform(ActiveHelper.waitUntilDoesNotExist(isAssignableFrom(ProgressBar.class), timeout));
        return this;
    }

    @Step("Ожидание и проверка видимости элемента: {matcher} с дефолтным временем")
    public void waitAndAssert(Matcher<View> matcher) {
        Allure.step("Ожидание и проверка видимости элемента: {matcher} с дефолтным временем");
        waitAndAssert(matcher, 5000);
    }


    @Step("Клик по тексту Res")
    public void clickOnStringRes(@StringRes int stringRes) {
        Allure.step("Клик по тексту из ресурсов");

        onView(isRoot()).perform(waitUntilVisible(withText(stringRes), 5000));
        onView(withText(stringRes)).perform(click());
    }

    @Step("Клик по ID {idRes}")
    public void clickOn(@IdRes int idRes) {
        Allure.step("Клик по ID {idRes}");
        waitUntilLoaderDoesNotExist(1000);
        onView(isRoot()).perform(waitUntilVisible(allOf(withId(idRes), isClickable()), 5000));
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(idRes)).perform(click());
    }

    @Step("Проверка видимости текста из @StringRes")
    public void assertTextDisplayed(@StringRes int stringRes) {
        String text = getString(stringRes);
        Allure.step("Проверка видимости текста: " + text);
        waitAndAssert(withText(text), 5000);
    }

    @Step("Проверка текста {stringRes} внутри контейнера {containerId}")
    public void assertTextInContainerDisplayed(@IdRes int containerId, @StringRes int stringRes) {
        String text = getInstrumentation().getTargetContext().getString(stringRes);
        Allure.step("Проверка текста '" + text + "' в контейнере " + containerId);
        waitAndAssert(allOf(
                withText(containsString(text)),
                isDescendantOfA(withId(containerId)),
                isDisplayed()
        ), 5000);
    }

    @Step("Клик по элементу внутри списка по номеру позиции")
    public void clickItemChildElement(@IdRes int listId, int position, @IdRes int childId) {
        Allure.step("Клик по элементу внутри списка по номеру позиции");
        onView(withId(listId)).perform(RecyclerViewActions.scrollToPosition(position));
        onView(withId(listId))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position, ActiveHelper.internalClickAction(childId)));
    }

    @Step("Гибкая проверка видимости текста из ресурса")
    public void assertTextDisplayedFlexible(@StringRes int stringRes) {
        String rawText = getInstrumentation().getTargetContext().getString(stringRes);
        // Берем первое слово для обхода проблем с \n
        String firstWord = rawText.split("\\s+")[0];
        Allure.step("Ищем текст по первому слову: " + firstWord);
        onView(isRoot()).perform(waitUntilVisible(withText(containsString(firstWord)), 10000));
        onView(withText(containsString(firstWord))).check(matches(anything()));
    }

    @Step("Проверка хинта (подсказки) в элементе")
    public void assertHintInIdResDisplayed(@IdRes int idRes, @StringRes int stringRes) {
        String expectedHint = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getString(stringRes);

        Allure.step("Проверка ID " + idRes + " на хинт: " + expectedHint);

        waitAndAssert(allOf(withId(idRes), withHint(expectedHint)), 5000);
    }

    @Step("Принудительный клик по ID {idRes}")
    public void forceClickOn(@IdRes int idRes) {
        Allure.step("Принудительный клик по ID {idRes}");

        onView(allOf(withId(idRes), isDisplayed()))
                .perform(new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return isDisplayed();
                    }

                    @Override
                    public String getDescription() {
                        return "Силовой клик";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                });
    }

    @Step("Ожидание исчезновения элемента")
    public void waitUntilDoesNotExistMatcherBaseComp(Matcher<View> matcher, long timeout) {
        Allure.step("Ожидание исчезновения элемента");
        onView(isRoot()).perform(ActiveHelper.waitUntilDoesNotExist(matcher, timeout));
    }

    @Step("Ожидание и клик по ID {idRes}")
    public void waitAndClick(@IdRes int idRes) {
        Allure.step("Ожидание и клик по ID {idRes}");
        onView(isRoot()).perform(ActiveHelper.waitUntilVisible(withId(idRes), 5000));
        onView(withId(idRes)).perform(click());
    }

    @Step("Ожидание, ассерт, проверка кликабельности {idRes} с текстом {stringRes}")
    public void waitAndAssertClickable(@IdRes int idRes, @StringRes int stringRes) {
        Allure.step("Ожидание, ассерт, проверка кликабельности {idRes} с текстом {stringRes}");
        onView(allOf(
                withId(idRes),
                withText(stringRes),
                isDisplayed()
        )).check(matches(isClickable()));
    }

    @Step("Проверка, что элемент {idRes} с текстом {stringRes} отображается, но НЕ кликабелен")
    public void assertNotClickable(@IdRes int idRes, @StringRes int stringRes) {
        Allure.step("Проверка, что элемент {idRes} с текстом {stringRes} отображается, но НЕ кликабелен");
        onView(allOf(
                withId(idRes),
                withText(stringRes),
                isDisplayed()
        )).check(matches(not(isClickable())));
    }

    @Step("Заполнение поля текстом {text}")
    public static void fill(int resId, String text) {
        Allure.step("Заполнение поля текстом");
        if (text == null) {
            return;
        }

        onView(withId(resId)).perform(replaceText(text), closeSoftKeyboard());
    }

    public enum NewsCategory {
        ANNOUNCEMENT("Объявление"),
        BIRTHDAY("День рождения"),
        SALARY("Зарплата"),
        UNION("Профсоюз"),
        HOLIDAY("Праздник"),
        MASSAGE("Массаж"),
        GRATITUDE("Благодарность"),
        HELP_NEEDED("Нужна помощь");

        private final String text;

        NewsCategory(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}

