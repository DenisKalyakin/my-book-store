package mate.academy.bookstore.service.category;

import java.util.List;
import mate.academy.bookstore.dto.category.CategoryDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto save(CategoryDto categoryDto);

    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    void deleteById(Long id);

    CategoryDto update(CategoryDto categoryDto, Long id);
}
