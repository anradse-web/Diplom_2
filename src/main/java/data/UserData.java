package data;

public class UserData {
    public static final String BASE_URI = "https://stellarburgers.education-services.ru";;
    public static final String LOGIN = "amir_" + System.currentTimeMillis()+ "@yandex.ru";
    public static final String PASSWORD = "password";
    public static final String NAME = "Jillian_";

    public static String USER_CREATE = "/api/auth/register"; // Создать пользователя
    public static String USER_LOGIN = "/api/auth/login"; // Логин пользователя
    public static String USER_DELETE = "/api/auth/user"; // Удаление пользователя
}
