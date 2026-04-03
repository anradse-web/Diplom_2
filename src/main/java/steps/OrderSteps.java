package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.OrderModel;
import steps.BaseSteps;

import static data.OrderData.INGREDIENTS;
import static data.OrderData.ORDERS;

public class OrderSteps extends BaseSteps {

    @Step("Получить список ингредиентов")
    public Response getIngredients() {
        return getSpec().get(INGREDIENTS);
    }

    @Step("Создать заказ")
    public Response createOrder(OrderModel order, String token) {
        RequestSpecification request = getSpec();
        if (token != null) request.header("Authorization", token);
        return request.body(order).post(ORDERS);
    }
}