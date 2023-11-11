package mate.academy.bookstore.dto.orderitem.internal;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long id;
    private Long bookId;
    private int quantity;
}
