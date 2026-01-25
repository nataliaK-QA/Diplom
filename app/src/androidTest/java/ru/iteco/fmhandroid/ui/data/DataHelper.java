package ru.iteco.fmhandroid.ui.data;

import androidx.annotation.CheckResult;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.BuildConfig;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.components.BaseComponent;

public class DataHelper {

    @StringRes
    public static final int NEWS_TEXT_RES = R.string.news;
    @StringRes
    public static final int CANCEL_BUTTON_TEXT = R.string.cancel;
    @IdRes
    public static final int DIALOG_OK_BUTTON = android.R.id.button1;
    @IdRes
    public static final int DIALOG_CANCEL_BUTTON = android.R.id.button2;
    @IdRes
    public static final int NEWS_ITEM_CATEGORY_TEXT = R.id.news_item_category_text_auto_complete_text_view;
    @IdRes
    public static final int NEWS_ITEM_CATEGORY_FIELD = R.id.news_item_category_text_input_layout;
    @IdRes
    public static final int CANCEL_BUTTON = R.id.cancel_button;
    @IdRes
    public static final int NEWS_LIST_RECYCLER = R.id.news_list_recycler_view;
    @IdRes
    public static final int DATA_TITLE_LABEL = R.id.news_item_title_text_view; //ЛЭЙБЛ ЗАГОЛОВКА
    @IdRes
    public static final int DESCRIPTION_TEXT_FIELD = R.id.news_item_description_text_view; // раскрывающееся ПОЛЕ описаниЯе(в нем текст который мы пишем)
    @IdRes
    public static final int NEWS_DOWN_DATE_TEXT = R.id.news_item_date_text_view;
    @IdRes
    public static final int NEWS_CARD_CONTAINER = R.id.news_item_material_card_view;
    @StringRes
    public static final int REFRESH_BUTTON_TEXT = R.string.refresh;
    @StringRes
    public static final int MESSAGE_ERROR = R.string.error;
    @IdRes
    public static final int PUBLICATION_DATE_FIELD = R.id.news_item_publication_date_text_view; // Поле даты


    private DataHelper() {
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo(BuildConfig.USER_LOGIN, BuildConfig.USER_PASSWORD);
    }

    public static String getRandomString(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return randomString(length, chars);
    }


    public static AuthInfo getRandomAuthInfo(int lengthLogin, int lengthPass) {
        String login = "user_" + randomString(lengthLogin, "abcdefghijklmnopqrstuvwxyz0123456789");
        String password = randomString(lengthPass, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+");
        return new AuthInfo(login, password);
    }

    public static AuthInfo getRandomAuthInfoWithDisallowedChars(int loginLength, int passwordLength) {
        String dirtyChars = "!#$%&*()|/`'\\\"\t\n\r";
        String alphaNum = "abcdefghijklmnopqrstuvwxyz0123456789";
        String allChars = dirtyChars + alphaNum;

        Random random = new Random();

        StringBuilder login = new StringBuilder();
        for (int i = 0; i < loginLength; i++) {
            login.append(dirtyChars.charAt(random.nextInt(dirtyChars.length())));
        }

        StringBuilder password = new StringBuilder();
        for (int i = 0; i < passwordLength; i++) {
            password.append(allChars.charAt(random.nextInt(dirtyChars.length())));
        }

        return new AuthInfo(login.toString(), password.toString());
    }

    public static NewInfo getFirstTestNewsInfo() {
        String timeStamp = java.time.LocalTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
        String uniqueTitle = "Тест1 " + timeStamp;
        return new NewInfo(
                "День рождения",
                uniqueTitle,
                getTodayDateFormatted(),
                getCurrentTimeFormatted(),
                "Тест1"
        );
    }

    public static NewInfo getSecondTestNewsInfo() {
        String timeStamp = java.time.LocalTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
        String uniqueTitle = "Тест2 " + timeStamp;
        return new NewInfo(
                "Зарплата",
                uniqueTitle,
                getTodayDateFormatted(),
                getCurrentTimeFormatted(),
                "Тест2"
        );
    }

    public static NewInfo getThirdTestNewsInfo() {
        String timeStamp = java.time.LocalTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
        String uniqueTitle = "Тест3 " + timeStamp;
        return new NewInfo(
                "Зарплата",
                uniqueTitle,
                getTodayDateFormatted(),
                getCurrentTimeFormatted(),
                "Тест3"
        );
    }

    @Step("Новость  минус № дня от текущего")
    public static NewInfo getOldTestNewsInfo(int day) {
        String timeStamp = java.time.LocalTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
        String uniqueTitle = "Новость из прошлого " + timeStamp;

        String dateAMonthAgo = java.time.LocalDate.now()
                .minusDays(day)
                .format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        return new NewInfo(
                "Благодарность",
                uniqueTitle,
                dateAMonthAgo, // Используем новую дату
                getCurrentTimeFormatted(),
                "Привет из прошлого"
        );
    }

    @Step("Новость  плюс № месяц от текущего")
    public static NewInfo getFutureTestNewsInfo(int month) {
        String timeStamp = java.time.LocalTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
        String uniqueTitle = "Новость из будущего " + timeStamp;

        String dateAMonthAgo = java.time.LocalDate.now()
                .plusMonths(month)
                .format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        return new NewInfo(
                "Праздник",
                uniqueTitle,
                dateAMonthAgo, // Используем новую дату
                getCurrentTimeFormatted(),
                "Привет из будущего"
        );
    }

    @Step("Фильтр категория Объявление, даты сегодня")
    public static FilterInfo getDefaultFilterInfo() {
        return new FilterInfo(BaseComponent.NewsCategory.ANNOUNCEMENT.getText(),
                getTodayDateFormatted(), getTodayDateFormatted());
    }

    @Step("Фильтр с датой 'До' и датой 'От'")
    public static FilterInfo getDateRangeFilter(int less, int more) {
        String toDay = java.time.LocalDate.now()
                .plusDays(more)
                .format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String fromDay = java.time.LocalDate.now()
                .minusDays(less)
                .format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        return new FilterInfo(
                BaseComponent.NewsCategory.ANNOUNCEMENT.getText(),
                fromDay,
                toDay
        );
    }

    public static String getTodayDateFormatted() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return today.format(formatter);
    }

    public static String getCurrentTimeFormatted() {
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return now.format(formatter);
    }

    private static String randomString(int length, String alphabet) {
        StringBuilder sb = new StringBuilder(length);
        java.util.Random rnd = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    public static class AuthInfo {
        private final String login;
        private final String password;

        public AuthInfo(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }


    public static class NewInfo {
        private final String category;
        private final String title;
        private final String date;
        private final String time;
        private final String description;

        public NewInfo(String category, String title, String date, String time, String description) {
            this.category = category;
            this.title = title;
            this.date = date;
            this.time = time;
            this.description = description;
        }

        public NewsPreview toPreview() {
            return new NewsPreview(this.title, this.date, this.description, this.category);
        }

        public String getDate() {
            return date;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getCategory() {
            return category;
        }

        public String getTime() {
            return time;
        }

        public NewInfo withTitle(String title) {
            return new NewInfo(this.category, title, this.date, this.time, this.description);
        }

        public NewInfo withCategory(String category) {
            return new NewInfo(category, this.title, this.date, this.time, this.description);
        }

        public NewInfo withDate(String date) {
            return new NewInfo(this.category, this.title, date, this.time, this.description);
        }

        public NewInfo withDescription(String description) {
            return new NewInfo(this.category, this.title, this.date, this.time, description);
        }
    }


    public static class NewsPreview {
        public final String title, date, description, category;

        public NewsPreview(String title, String date, String description, String category) {
            this.title = title;
            this.date = date;
            this.description = description;
            this.category = category;
        }

        public NewsPreview(String title, String date, String description) {
            this.title = title;
            this.date = date;
            this.description = description;
            this.category = null; // Категория нам тут не важна
        }

        @Step("Сравнение данных превью новости")
        public void assertMatches(String actualTitle, String actualDate) {
            if (this.title == null || !this.title.equals(actualTitle)) {
                throw new AssertionError("Ошибка заголовка! Ожидали: " + this.title + ", нашли: " + actualTitle);
            }
            if (this.date == null || !this.date.equals(actualDate)) {
                throw new AssertionError("Ошибка даты! Ожидали: " + this.date + ", нашли: " + actualDate);
            }
            io.qameta.allure.kotlin.Allure.step("Данные новости совпали: " + actualTitle);
        }
    }


    public static class FilterInfo {
        private final String category;
        private final String fromDate;
        private final String toDate;

        public String getToDate() {
            return toDate;
        }

        public String getFromDate() {
            return fromDate;
        }

        public String getCategory() {
            return category;
        }

        public FilterInfo(String category, String fromDate, String toDate) {
            this.category = category;
            this.fromDate = fromDate;
            this.toDate = toDate;
        }

        public FilterInfo withCategory(String category) {
            return new FilterInfo(category, this.fromDate, this.toDate);
        }

    }


}
