package test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import models.UserModel;
import steps.BaseSteps;
import steps.UserSteps;

import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest extends BaseSteps {
    private UserModel user;
    private UserSteps userSteps;
    private String token;

    @Before
    public void regUser() {
        userSteps = new UserSteps();

        user = new UserModel("amir_" + System.currentTimeMillis() + "@ya.ru", "password", "Ivan");
        token = userSteps.createUser(user).path("accessToken");
    }

    @After
    public void tearDown() {
        userSteps.deleteUser(token); }

    @DisplayName("Вход под существующим пользователем")
    @Description("Успешная авторизация с корректными учётными данными")
    @Test
    public void loginSuccess() {
        userSteps.login(user)
                .then().assertThat().statusCode(HttpStatus.SC_OK).body("success", equalTo(true));
    }
    @DisplayName("Вход с неверным паролем")
    @Description("Проверка авторизации с корректным email, но неверным паролем — ожидается ошибка 401 Unauthorized")
    @Test
    public void loginincorrectPassword() {
        UserModel wrongPassUser = new UserModel(user.getEmail(), "wrong_pass", user.getName());
        userSteps.login(wrongPassUser)
                .then().assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
    @DisplayName("Вход с неверным логином")
    @Description("Проверка авторизации с некорректный email, верным паролем — ожидается ошибка 401 Unauthorized")
    @Test
    public void loginincorrectEmail() {
        UserModel wrongPassUser = new UserModel("wrong_email", user.getPassword(), user.getName());
        userSteps.login(wrongPassUser)
                .then().assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}