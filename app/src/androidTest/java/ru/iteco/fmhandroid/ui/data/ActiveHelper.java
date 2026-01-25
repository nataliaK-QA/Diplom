package ru.iteco.fmhandroid.ui.data;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CheckResult;
import androidx.annotation.StringRes;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;

public class ActiveHelper {

    private ActiveHelper() {
    }

    @Step("Клик по внутреннему элементу")
    public static ViewAction internalClickAction(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(View.class);
            }

            @Override
            public String getDescription() {
                return "Click on child with id: " + id;
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                if (v != null) {
                    v.performClick();
                } else {
                    throw new RuntimeException("Не удалось найти View с ID " + id + " внутри контейнера");
                }
            }
        };
    }

    public static ViewAction waitUntilDoesNotExist(final Matcher<View> matcher, final long timeout) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "ожидание исчезновения: " + matcher;
            }

            @Override
            public void perform(UiController uiController, View view) {
                long endTime = System.currentTimeMillis() + timeout;
                while (System.currentTimeMillis() < endTime) {
                    if (!checkViewExists(view, matcher)) return;
                    // Для Android 10 это критично: даем системе обработать события
                    uiController.loopMainThreadForAtLeast(100);
                }
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription("Root View")
                        .withCause(new RuntimeException("Элемент не исчез за " + timeout + " мс"))
                        .build();
            }

            private boolean checkViewExists(View view, Matcher<View> matcher) {
                if (view == null) return false;
                if (matcher.matches(view) && view.isShown()) return true;
                if (view instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) view;
                    for (int i = 0; i < group.getChildCount(); i++) {
                        if (checkViewExists(group.getChildAt(i), matcher)) return true;
                    }
                }
                return false;
            }
        };
    }

    @Step("Ожидание появления элемента")
    public static ViewAction waitUntilVisible(final Matcher<View> matcher, final long timeout) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for " + matcher;
            }

            @Override
            public void perform(UiController uiController, View view) {
                long endTime = System.currentTimeMillis() + timeout;
                while (System.currentTimeMillis() < endTime) {
                    if (findView(view, matcher)) return;
                    uiController.loopMainThreadForAtLeast(300);
                }

                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription("Root View")
                        .withCause(new RuntimeException("Элемент не найден"))
                        .build();
            }

            private boolean findView(View view, Matcher<View> matcher) {
                if (matcher.matches(view) && view.isShown()) return true;
                if (view instanceof ViewGroup) {
                    ViewGroup g = (ViewGroup) view;
                    for (int i = 0; i < g.getChildCount(); i++) {
                        if (findView(g.getChildAt(i), matcher)) return true;
                    }
                }
                return false;
            }
        };
    }

    public static void waitAndAssert(Matcher<View> matcher, int timeout) {
        onView(isRoot()).perform(waitUntilVisible(matcher, timeout));
        onView(matcher).check(matches(isDisplayed()));
    }

    @Step("Жесткая пауза")
    public static void waitFor(long millis) {
        onView(isRoot()).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        });
    }

    @Step("Проверка появления Toast-сообщения по ресурсу")
    public static void checkToast(@StringRes int textResId, View decorView) {
        Allure.step("Проверка появления Toast-сообщения по ресурсу");
        Context context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext();
        String expectedText = context.getString(textResId);

        android.os.SystemClock.sleep(500);

        onView(withText(containsString(expectedText)))
                .inRoot(withDecorView(not(is(decorView))))
                .check(matches(isDisplayed()));
    }

    @CheckResult
    public static boolean isElementDisplayedBoolean(Matcher<View> matcher) {
        try {
            onView(isRoot()).perform(waitUntilVisible(matcher, 5000));
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
}
