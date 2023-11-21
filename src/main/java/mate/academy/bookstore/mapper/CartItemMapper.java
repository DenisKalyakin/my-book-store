package mate.academy.bookstore.mapper;

import java.math.BigDecimal;
import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.cartitem.internal.CartItemResponseDto;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemResponseDto toDto(CartItem cartItem);

    OrderItem toOrderItem(CartItem cartItem);

    @AfterMapping
    default void setOrderItemsPrice(
            @MappingTarget OrderItem orderItem, CartItem cartItem
    ) {
        orderItem.setPrice(cartItem.getBook().getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
    }
}
