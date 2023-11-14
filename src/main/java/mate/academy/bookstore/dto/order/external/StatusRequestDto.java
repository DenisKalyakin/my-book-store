package mate.academy.bookstore.dto.order.external;

import lombok.Data;
import mate.academy.bookstore.model.Order;
import mate.academy.bookstore.validation.Status;

@Data
public class StatusRequestDto {
    @Status(enumClass = Order.Status.class)
    private String status;
}
