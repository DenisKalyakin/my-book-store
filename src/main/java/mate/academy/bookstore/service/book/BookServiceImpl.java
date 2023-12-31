package mate.academy.bookstore.service.book;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.book.external.BookSearchParameters;
import mate.academy.bookstore.dto.book.external.CreateBookRequestDto;
import mate.academy.bookstore.dto.book.internal.BookDto;
import mate.academy.bookstore.dto.book.internal.BookDtoWithoutCategoryIds;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.BookMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.repository.book.BookSpecificationBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookSpecificationBuilder bookSpecificationBuilder;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findBookWithCategoriesById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto update(BookDto bookDto, Long id) {
        if (bookRepository.existsById(id)) {
            Book book = bookMapper.toModel(bookDto);
            book.setId(id);
            Book updatedBook = bookRepository.save(book);
            return bookMapper.toDto(updatedBook);
        } else {
            throw new EntityNotFoundException(
                    "Can't update, book with id " + id + " doesn't exist."
            );
        }
    }

    @Override
    public List<BookDto> search(BookSearchParameters parameters, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(parameters);
        return bookRepository.findAll(bookSpecification, pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public String saveAll(CreateBookRequestDto[] requestDtos) {
        Arrays.stream(requestDtos)
                .forEach(this::save);
        return "The list of books has been saved successfully";
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId, Pageable pageable) {
        return bookRepository.findAllByCategoryId(categoryId).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
