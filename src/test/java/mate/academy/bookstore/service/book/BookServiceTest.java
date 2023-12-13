package mate.academy.bookstore.service.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mate.academy.bookstore.dto.book.external.CreateBookRequestDto;
import mate.academy.bookstore.dto.book.internal.BookDto;
import mate.academy.bookstore.dto.book.internal.BookDtoWithoutCategoryIds;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.impl.BookMapperImpl;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapperImpl bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Find valid books by category id")
    void findAllByCategoryId_WithAvailableCategoryId_ShouldReturnValidBooks() {
        List<BookDtoWithoutCategoryIds> expected = new ArrayList<>();
        expected.add(new BookDtoWithoutCategoryIds()
                .setId(1L)
                .setTitle("History"));
        expected.add(new BookDtoWithoutCategoryIds()
                .setId(2L)
                .setTitle("Biology"));

        Category science = new Category();
        science.setId(1L);
        science.setName("Science");
        Set<Category> scienceCategories = new HashSet<>();
        scienceCategories.add(science);

        Book history = new Book();
        history.setId(1L);
        history.setTitle("History");
        history.setCategories(scienceCategories);

        Book biology = new Book();
        biology.setId(2L);
        biology.setTitle("Biology");
        biology.setCategories(scienceCategories);

        List<Book> books = new ArrayList<>();
        books.add(history);
        books.add(biology);

        when(bookRepository.findAllByCategoryId(ArgumentMatchers.anyLong())).thenReturn(books);
        when(bookMapper.toDtoWithoutCategories(history)).thenReturn(expected.get(0));
        when(bookMapper.toDtoWithoutCategories(biology)).thenReturn(expected.get(1));

        List<BookDtoWithoutCategoryIds> actual = bookService
                .findAllByCategoryId(1L, PageRequest.of(0, 10));

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    @DisplayName("Saving several books at the same time")
    void saveAll_validRequestDto_Success() {

        Book triumph = new Book();
        triumph.setId(1L);
        triumph.setTitle("Triumph");
        triumph.setAuthor("Remark");
        triumph.setPrice(BigDecimal.valueOf(100.95));
        triumph.setIsbn("ISBN-13: 978-0-06-112008-5");

        Book catcher = new Book();
        catcher.setId(2L);
        catcher.setTitle("The Catcher in the Rye");
        catcher.setAuthor("J.D. Salinger");
        catcher.setPrice(BigDecimal.valueOf(19.99));
        catcher.setIsbn("ISBN-13: 978-0-316-76948-0");

        CreateBookRequestDto[] createBookRequestDtos = new CreateBookRequestDto[]{
                new CreateBookRequestDto()
                        .setTitle("Triumph")
                        .setAuthor("Remark")
                        .setPrice(BigDecimal.valueOf(100.95))
                        .setIsbn("ISBN-13: 978-0-06-112008-5"),
                new CreateBookRequestDto()
                        .setTitle("The Catcher in the Rye")
                        .setAuthor("J.D. Salinger")
                        .setPrice(BigDecimal.valueOf(19.99))
                        .setIsbn("ISBN-13: 978-0-316-76948-0")
        };
        when(bookMapper.toModel(createBookRequestDtos[0])).thenReturn(triumph);
        when(bookMapper.toModel(createBookRequestDtos[1])).thenReturn(catcher);
        when(bookRepository.save(triumph)).thenReturn(triumph);
        when(bookRepository.save(catcher)).thenReturn(catcher);
        when(bookMapper.toDto(triumph)).thenReturn(new BookDto()
                .setId(triumph.getId())
                .setTitle(triumph.getTitle())
                .setAuthor(triumph.getAuthor())
                .setPrice(triumph.getPrice())
                .setIsbn(triumph.getIsbn()));
        when(bookMapper.toDto(catcher)).thenReturn(new BookDto()
                .setId(catcher.getId())
                .setTitle(catcher.getTitle())
                .setAuthor(catcher.getAuthor())
                .setPrice(catcher.getPrice())
                .setIsbn(catcher.getIsbn()));

        String expected = "The list of books has been saved successfully";
        String actual = bookService.saveAll(createBookRequestDtos);

        assertEquals(expected, actual);

    }

    @Test
    @DisplayName("Update by not valid id")
    void update_WithNotValidId_NotSuccess() {
        BookDto bookDto = new BookDto()
                .setTitle("Math");
        Long id = 10L;

        when(bookRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            bookService.update(bookDto, id);
        });
    }

    @Test
    @DisplayName("Find by not valid id")
    void findById_WithNotValidId_NotSuccess() {
        Long id = 10L;

        when(bookRepository.findBookWithCategoriesById(id))
                .thenThrow(new EntityNotFoundException("Can't find book by id " + id));

        assertThrows(EntityNotFoundException.class, () -> {
            bookService.findById(id);
        });
    }
}
