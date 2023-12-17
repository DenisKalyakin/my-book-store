package mate.academy.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.bookstore.dto.book.external.CreateBookRequestDto;
import mate.academy.bookstore.dto.book.internal.BookDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("databases/books/add-three-books-to-books-table.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        tearDown(dataSource);
    }

    @SneakyThrows
    private static void tearDown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("databases/books/remove-all-books-from-books-table.sql")
            );
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create a new book")
    @Sql(scripts = {
            "classpath:databases/books/delete-triumph-book-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Triumph")
                .setAuthor("Remark")
                .setIsbn("ISBN-13: 978-0-316-76948-1")
                .setPrice(BigDecimal.valueOf(100.95));

        BookDto expected = new BookDto()
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                post("/books/add")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        BookDto actual = objectMapper.readValue(contentAsString, BookDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id", "categoryIds"));
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Get all books")
    void getAll_GivenBooks_ShouldReturnAllBooks() throws Exception {
        List<BookDto> expected = new ArrayList<>();
        expected.add(new BookDto()
                .setId(1L)
                .setTitle("To Kill a Mockingbird")
                .setAuthor("Harper Lee")
                .setPrice(BigDecimal.valueOf(289.99))
                .setIsbn("ISBN-13: 978-0-06-112008-4")
                .setCategoryIds(new HashSet<>()));
        expected.add(new BookDto()
                .setId(2L)
                .setTitle("The Great Gatsby")
                .setAuthor("F. Scott Fitzgerald")
                .setPrice(BigDecimal.valueOf(24.99))
                .setIsbn("ISBN-13: 978-0-7432-7356-5")
                .setCategoryIds(new HashSet<>()));
        expected.add(new BookDto()
                .setId(3L)
                .setTitle("1984")
                .setAuthor("George Orwell")
                .setPrice(BigDecimal.valueOf(2859.99))
                .setIsbn("ISBN-13: 978-0-452-28423-4")
                .setCategoryIds(new HashSet<>()));

        MvcResult result = mockMvc.perform(
                get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(),
                BookDto[].class);
        assertEquals(3, actual.length);
        assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create a few books")
    @Sql(scripts = {
            "classpath:databases/books/delete-few-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveAll_ValidRequestDto_Success() throws Exception {
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

        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDtos);

        MvcResult result = mockMvc.perform(
                        post("/books/save-all")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        String expected = "The list of books has been saved successfully";
        String actual = result.getResponse().getContentAsString();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Search books by titles and authors")
    @WithMockUser(username = "user")
    void search_WithBookSearchParameters_ShouldReturnActualBooks() throws Exception {
        List<BookDto> expected = new ArrayList<>();
        expected.add(new BookDto()
                .setId(1L)
                .setTitle("To Kill a Mockingbird")
                .setAuthor("Harper Lee")
                .setPrice(BigDecimal.valueOf(289.99))
                .setIsbn("ISBN-13: 978-0-06-112008-4")
                .setCategoryIds(new HashSet<>()));
        expected.add(new BookDto()
                .setId(2L)
                .setTitle("The Great Gatsby")
                .setAuthor("F. Scott Fitzgerald")
                .setPrice(BigDecimal.valueOf(24.99))
                .setIsbn("ISBN-13: 978-0-7432-7356-5")
                .setCategoryIds(new HashSet<>()));

        String searchParams = "?"
                + "titles=To Kill a Mockingbird,The Great Gatsby"
                + "&"
                + "authors=F. Scott Fitzgerald,Harper Lee";

        MvcResult result = mockMvc.perform(
                        get("/books/search" + searchParams)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(),
                BookDto[].class);
        assertEquals(2, actual.length);
        assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @DisplayName("Get book by id")
    @WithMockUser(username = "user")
    void getBookById2_BookAvailable_ShouldReturnActualBook() throws Exception {
        BookDto expected = new BookDto()
                .setId(2L)
                .setTitle("The Great Gatsby")
                .setAuthor("F. Scott Fitzgerald")
                .setPrice(BigDecimal.valueOf(24.99))
                .setIsbn("ISBN-13: 978-0-7432-7356-5")
                .setCategoryIds(new HashSet<>());

        MvcResult result = mockMvc.perform(
                        get("/books/2")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Delete book by id")
    @Sql(scripts = {
            "classpath:databases/books/add-few-books-to-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:databases/books/delete-few-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_WithAvailableId_Success() throws Exception {
        MvcResult result = mockMvc.perform(
                        delete("/books/10")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();

        int expected = 204;
        int actual = result.getResponse().getStatus();
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update book by id")
    @Sql(scripts = {
            "classpath:databases/books/add-few-books-to-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:databases/books/delete-few-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_WithAvailableId_ShouldReturnUpdateBook() throws Exception {
        BookDto expected = new BookDto()
                .setId(11L)
                .setTitle("The Catcher in the Rye")
                .setAuthor("Updated Author")
                .setPrice(BigDecimal.valueOf(19.99))
                .setIsbn("ISBN-13: 978-0-316-99948-0")
                .setCategoryIds(new HashSet<>());

        String jsonRequest = objectMapper.writeValueAsString(expected);

        MvcResult result = mockMvc.perform(
                        put("/books/11")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        BookDto actual = objectMapper.readValue(contentAsString, BookDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }
}
