package ru.iteco.fmhandroid.ui;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;

public abstract class BaseTest {
//заглушка
//    private MockWebServer mockWebServer;

//    @Rule
//    public ActivityScenarioRule<AppActivity> mActivityScenarioRule =
//            new ActivityScenarioRule<>(AppActivity.class);

//    @Before
//    public void setupMockServer() throws IOException {
//        mockWebServer = new MockWebServer();
//
//        // пустой список
//        String emptyResponse = "{\"pages\": 1, \"elements\": []}";
//
//        // Настраиваем сервер всегда отвечать 200 OK с пустыми данными
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(200)
//                .setBody(emptyResponse)
//                .addHeader("Content-Type", "application/json"));
//
//        // Запускаем сервер на порту 8080 (или другом)
//        mockWebServer.start(8080);
//    }
//
//    @After
//    public void tearDownMockServer() throws IOException {
//        if (mockWebServer != null) {
//            mockWebServer.shutdown();
//        }
//    }


    @Rule
    public ActivityScenarioRule<AppActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);

    @org.junit.After
    public void globalTearDown() {
        System.gc();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        android.util.Log.i("BASE_TEST", "Принудительная очистка памяти выполнена успешно");
    }

    protected View getDecorView() {
        final View[] view = new View[1];
        mActivityScenarioRule.getScenario().onActivity(activity -> {
            view[0] = activity.getWindow().getDecorView();
        });
        return view[0];
    }
}
