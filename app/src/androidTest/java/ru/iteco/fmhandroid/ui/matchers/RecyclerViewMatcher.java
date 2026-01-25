package ru.iteco.fmhandroid.ui.matchers;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.ActiveHelper;
import ru.iteco.fmhandroid.ui.data.DataHelper;

public class RecyclerViewMatcher {
    @Step("Поиск элемента в списке на позиции {position}")
    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        Allure.step("Поиск элемента в списке на позиции {position}");
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) return false;
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    @Step("Проверка текста {text} в списке на позиции {pos}")
    public static void assertTextAtPositionDisplayed(@IdRes int listId, int pos, String text) {
        Allure.step("Сверка текста в списке: позиция " + pos + ", ожидаем: " + text);

        onView(isRoot()).perform(ActiveHelper.waitUntilVisible(withId(listId), 5000));

        onView(withId(listId)).check(matches(atPosition(pos, hasDescendant(withText(text)))));
    }

    @Step("Метод, чтобы превратить то, что Espresso «видит», в строку String, которую можно сохранить.")
    public static String getTextAtPosition(final int recyclerViewId, final int position, final int targetViewId) {
        Allure.step("Метод, чтобы превратить то, что Espresso «видит», в строку String, которую можно сохранить.");
        final String[] holder = {null};
        onView(withId(recyclerViewId)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(RecyclerView.class);
            }

            @Override
            public String getDescription() {
                return "Извлечение текста из позиции " + position;
            }

            @Override
            public void perform(UiController uiController, View view) {
                RecyclerView rv = (RecyclerView) view;
                // Находим нужный ViewHolder по позиции в адаптере
                RecyclerView.ViewHolder vh = rv.findViewHolderForAdapterPosition(position);
                if (vh != null) {
                    TextView tv = vh.itemView.findViewById(targetViewId);
                    if (tv != null) {
                        holder[0] = tv.getText().toString();
                    }
                }
            }
        });
        return holder[0];
    }

    @Step("Проверка новости на позиции {position}: {info.title}")
    public static void verifyNewsAtPosition(int position, DataHelper.NewInfo info) {
        Allure.step("Проверка новости на позиции " + position + ": " + info.getTitle());
        onView(isRoot()).perform(ActiveHelper.waitUntilVisible(withId(R.id.news_list_recycler_view), 5000));

        Matcher<View> activeRecycler = allOf(
                withId(R.id.news_list_recycler_view),
                isDisplayed()
        );

        onView(activeRecycler).perform(scrollToPosition(position));

        String actualTitle = "";
        long timeout = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < timeout) {
            actualTitle = getTextAtPosition(R.id.news_list_recycler_view, position, R.id.news_item_title_text_view);
            if (actualTitle != null && !actualTitle.isEmpty()) break;

            android.os.SystemClock.sleep(200);
        }

        if (actualTitle == null || !actualTitle.contains(info.getTitle())) {
            throw new AssertionError("Ошибка! Ожидали: " + info.getTitle() + ", нашли: " + actualTitle);
        }
    }

    @Step("Матчер чтобы отсеч клюки espresso с данными в памяти")
    public static <T> Matcher<T> first(final Matcher<T> matcher) {
        return new BaseMatcher<T>() {
            boolean isFirst = true;

            @Override
            public boolean matches(Object item) {
                if (isFirst && matcher.matches(item)) {
                    isFirst = false;
                    return true;
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("берем первый подходящий элемент");
            }
        };
    }


    public static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}