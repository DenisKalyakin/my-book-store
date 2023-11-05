package mate.academy.bookstore.repository.cartitem;

import mate.academy.bookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT ci FROM CartItem ci WHERE ci.id = :cartItemId")
    CartItem getById(Long cartItemId);
}
