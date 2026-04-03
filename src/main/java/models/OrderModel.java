package models;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor

public class OrderModel {
    private List<String> ingredients;
}