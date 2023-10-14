package mate.academy.bookstore.service.book;

import java.util.List;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.book.BookSearchParameters;
import mate.academy.bookstore.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    List<BookDto> getAll(Pageable pageable);

    void deleteById(Long id);

    BookDto update(BookDto bookDto, Long id);

    List<BookDto> search(BookSearchParameters parameters, Pageable pageable);

    String saveAll(CreateBookRequestDto[] requestDtos);
}
