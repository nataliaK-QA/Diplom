package ru.iteco.fmhandroid.ui.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.test.espresso.contrib.RecyclerViewActions;

import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.components.AppBarComponent;
import ru.iteco.fmhandroid.ui.components.BaseComponent;
import ru.iteco.fmhandroid.ui.matchers.RecyclerViewMatcher;

public class ButterflyPage extends BaseComponent {

    public AppBarComponent appBar = new AppBarComponent();
    @IdRes
    public static final int OUR_MISSION_TITLE = R.id.our_mission_title_text_view;
    @IdRes
    public static final int OUR_MISSION_LIST = R.id.our_mission_item_list_recycler_view;
    @IdRes
    public static final int OUR_MISSION_ITEM_IMAGE = R.id.our_mission_item_image_view;
    @IdRes
    public static final int OUR_MISSION_ITEM_BUTTON = R.id.our_mission_item_open_card_image_button;
    @IdRes
    public static final int OUR_MISSION_ITEM_TEXT = R.id.our_mission_item_title_text_view;
    @IdRes
    public static final int OUR_MISSION_ITEM_DESCRIPTION = R.id.our_mission_item_description_text_view;

    public ButterflyPage() {
    }

    @Step("Проверка видимости страницы 'Our_mission'")
    public ButterflyPage assertButterflyPageVision() {
        waitAndAssert(withId(OUR_MISSION_TITLE), 5000);
        waitAndAssert(withId(OUR_MISSION_LIST), 5000);

        onView(withId(OUR_MISSION_LIST)).perform(RecyclerViewActions.scrollToPosition(0));

        onView(withId(OUR_MISSION_LIST))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        waitAndAssert(allOf(
                withId(OUR_MISSION_ITEM_DESCRIPTION),
                isDescendantOfA(allOf(
                        RecyclerViewMatcher.atPosition(0, isAssignableFrom(View.class)),
                        isDisplayed() // УТОЧНЯЕМ: только та карточка, что реально на экране
                )),
                isDisplayed()
        ), 5000);

        onView(withId(OUR_MISSION_LIST))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        return this;
    }
}




