package mate.academy.bookstore.service.book;

import java.util.List;
import mate.academy.bookstore.dto.book.external.BookSearchParameters;
import mate.academy.bookstore.dto.book.external.CreateBookRequestDto;
import mate.academy.bookstore.dto.book.internal.BookDto;
import mate.academy.bookstore.dto.book.internal.BookDtoWithoutCategoryIds;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    List<BookDto> getAll(Pageable pageable);

    void deleteById(Long id);

    BookDto update(BookDto bookDto, Long id);

    List<BookDto> search(BookSearchParameters parameters, Pageable pageable);

    String saveAll(CreateBookRequestDto[] requestDtos);

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long id, Pageable pageable);
}
