package mate.academy.bookstore.dto.shoppingcart.internal;

import java.util.Set;
import lombok.Data;
import mate.academy.bookstore.dto.cartitem.internal.CartItemResponseDto;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
