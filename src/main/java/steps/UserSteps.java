package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.UserModel;

import static data.UserData.*;
import static io.restassured.RestAssured.given;


public class UserSteps extends BaseSteps {

    @Step("Создать пользователя")
    public Response createUser(UserModel user) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(user).post(USER_CREATE);
    }

    @Step("Логин пользователя")
    public Response login(UserModel user) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(user).post(USER_LOGIN);
    }

    @Step("Удалить пользователя")
    public Response deleteUser(String token) {
        if (token == null) return null;
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .header("Authorization", token).delete(USER_DELETE);
    }
}

