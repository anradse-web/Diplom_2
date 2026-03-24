package test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import models.OrderModel;
import models.UserModel;
import steps.BaseSteps;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.List;
import static org.hamcrest.Matchers.equalTo;

public class OrderCreateTest extends BaseSteps {
    private String token;
    private List<String> ingredientIds;

    private UserSteps userSteps;
    private OrderSteps orderSteps;
    @Before
    public void prepare() {
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();

        UserModel user = new UserModel("order_user_" + System.currentTimeMillis() + "@ya.ru", "password", "OrderBot");
        token = userSteps.createUser(user).path("accessToken");


        ingredientIds = orderSteps.getIngredients().path("data._id");
    }

    @After
    public void cleanup() {
        if (token != null)
            userSteps.deleteUser(token);
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    @Description("Успешное создание заказа при наличии валидного токена")
    public void AuthorizedUserCreatesOrderTest() {
        OrderModel order = new OrderModel(List.of(ingredientIds.get(0), ingredientIds.get(1)));
        orderSteps.createOrder(order, token)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void UnauthorizedUserCreatesOrderTest () {
        OrderModel order = new OrderModel(List.of(ingredientIds.get(0)));
        orderSteps.createOrder(order, null)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка ошибки 400 при создании заказа без передачи ингридиента")
    public void EmptyIngredientsOrderTest () {
        OrderModel order = new OrderModel(List.of());
        orderSteps.createOrder(order, token)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверка ошибки 500 песли передать невадидный Id ингридиента")
    public void OrderCreateWrongHashTest () {
        OrderModel order = new OrderModel(List.of("invalid_hash_123"));
        orderSteps.createOrder(order, token)
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}