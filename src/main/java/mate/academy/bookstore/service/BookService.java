package mate.academy.bookstore.service;

import java.util.List;
import mate.academy.bookstore.dto.BookDto;
import mate.academy.bookstore.dto.BookSearchParameters;
import mate.academy.bookstore.dto.CreateBookRequestDto;
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
