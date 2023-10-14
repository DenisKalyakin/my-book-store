package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.book.BookSearchParameters;
import mate.academy.bookstore.dto.book.CreateBookRequestDto;
import mate.academy.bookstore.service.book.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;

    @Operation(summary = "Create a new book", description = "Create a new book")
    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @Operation(summary = "Get all books", description = "Get a list of all available books")
    @GetMapping
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.getAll(pageable);
    }

    @Operation(summary = "Get a book by id", description = "Get a book by id")
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @Operation(summary = "Delete a book by id", description = "Soft delete a book by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @Operation(summary = "Update a book by id", description = "Update a book by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public BookDto update(@RequestBody BookDto bookDto, @PathVariable Long id) {
        return bookService.update(bookDto, id);
    }

    @Operation(summary = "Search books by author or title", description
            = "Search for available books with specified parameters such as: title, author")
    @GetMapping("/search")
    public List<BookDto> search(BookSearchParameters searchParameters, Pageable pageable) {

        return bookService.search(searchParameters, pageable);
    }

    @Operation(summary = "Create all books", description = "Saves several books to the database")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/save-all")
    @ResponseStatus(HttpStatus.CREATED)
    public String saveAll(@RequestBody @Valid CreateBookRequestDto[] requestDtos) {
        return bookService.saveAll(requestDtos);
    }
}
