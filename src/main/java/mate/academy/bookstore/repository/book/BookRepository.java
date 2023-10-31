package mate.academy.bookstore.repository.book;

import java.util.List;
import java.util.Optional;
import mate.academy.bookstore.model.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id = :categoryId")
    List<Book> findAllByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.categories c WHERE b.id = :bookId")
    Optional<Book> findBookWithCategoriesById(@Param("bookId") Long bookId);

    @Query("FROM Book b LEFT JOIN FETCH b.categories")
    List<Book> findAllBooksWithCategories(Pageable pageable);

    @Query("FROM Book b LEFT JOIN FETCH b.categories")
    List<Book> findAllBooksWithCategories(Specification<Book> specification, Pageable pageable);
}
