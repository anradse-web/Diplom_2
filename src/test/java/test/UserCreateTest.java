package test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.UserModel;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.BaseSteps;
import steps.OrderSteps;
import steps.UserSteps;

import static org.hamcrest.Matchers.equalTo;

public class UserCreateTest extends BaseSteps {
    private String token;
    private UserSteps userSteps;
    private OrderSteps orderSteps;

    @Before
    public void setUp() {
        // Инициализация шагов перед использованием
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
    }
    @After
    public void tearDown() {
        if (token != null) {
            userSteps.deleteUser(token);
        }
    }

    @DisplayName("Создание уникального пользователя")
    @Description("Успешное создание пользователя со всеми обязательными полями")
    @Test
    public void CreateUniqueUserTest() {
        UserModel user = new UserModel("amir_" + System.currentTimeMillis() + "@ya.ru", "password", "Jillian_");
        Response response = userSteps.createUser(user);
        token = response.path("accessToken");

        response.then().assertThat().statusCode(HttpStatus.SC_OK)
                .and().body("success", equalTo(true));
    }

    @DisplayName("Создание пользователя , который уже зарегистрирован")
    @Description("Повторное создание пользователя в системе (одни и те же данные): ожидаем ошибку 409 Конфликт")
    @Test
    public void createTwiceUserTest() {
        UserModel user = new UserModel("amir_" + System.currentTimeMillis() + "@ya.ru", "password", "Jillian_");
        Response firstResponse = userSteps.createUser(user);
        firstResponse.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
        token = firstResponse.path("accessToken");

        // Второе создание — ошибка 409
        userSteps.createUser(user)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo("User already exists"));
    }

    @DisplayName("Создание пользователя без пароля")
    @Description("Проверка ошибки 403 при отсутствии поля password")
    @Test
    public void CreateUserWithoutPasswordTest() {
        UserModel user = new UserModel("user_" + System.currentTimeMillis() + "@ya.ru", null, "Jillian_");
        userSteps.createUser(user)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @DisplayName("Создание пользователя без логина (email)")
    @Description("Проверка ошибки 403 при отсутствии поля email")
    @Test
    public void CreateUserWithoutEmailTest() {
        UserModel user = new UserModel(null, "password", "Jillian_");
        userSteps.createUser(user)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and().body("message", equalTo("Email, password and name are required fields"));
    }

    @DisplayName("Создание пользователя без имени (name)")
    @Description("Проверка ошибки 403 при отсутствии поля name")
    @Test
    public void CreateUserWithoutNameTest() {
        UserModel user = new UserModel("user_" + System.currentTimeMillis() + "@ya.ru", "password", null);
        userSteps.createUser(user)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}