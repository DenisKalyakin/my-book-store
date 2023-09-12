package mate.academy.bookstore;

import java.math.BigDecimal;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {

        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book arka = new Book();
            arka.setAuthor("Erich Maria Remarque");
            arka.setTitle("Arch of Triumph");
            arka.setIsbn("333-1-2-666666-1");
            arka.setPrice(BigDecimal.valueOf(350));

            Book constitution = new Book();
            constitution.setAuthor("VR vid im. Ukra'ins'kogo narodu");
            constitution.setTitle("Constytuciya Ukra'iny");
            constitution.setIsbn("978-617-722-841-6");
            constitution.setPrice(BigDecimal.valueOf(120));

            bookService.save(arka);
            bookService.save(constitution);

            System.out.println(bookService.findAll());
        };
    }
}
