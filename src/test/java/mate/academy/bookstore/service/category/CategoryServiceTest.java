package mate.academy.bookstore.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import mate.academy.bookstore.dto.category.external.CreateCategoryRequestDto;
import mate.academy.bookstore.dto.category.internal.CategoryDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.impl.CategoryMapperImpl;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapperImpl categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Save a new category")
    void save_withValidRequest_ShouldReturnCategoryDto() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto()
                .setName("Science");

        Category categoryWithoutId = new Category();
        categoryWithoutId.setName(requestDto.getName());

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName(categoryWithoutId.getName());

        CategoryDto expected = new CategoryDto()
                .setId(savedCategory.getId())
                .setName(savedCategory.getName());

        when(categoryMapper.toEntity(requestDto)).thenReturn(categoryWithoutId);
        when(categoryRepository.save(categoryWithoutId)).thenReturn(savedCategory);
        when(categoryMapper.toDto(savedCategory)).thenReturn(expected);

        CategoryDto actual = categoryService.save(requestDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update category by valid id")
    void update_WithValidId_Success() {
        Long id = 1L;
        CategoryDto categoryDtoWithoutId = new CategoryDto()
                .setName("Science");

        Category category = new Category();
        category.setId(id);
        category.setName(categoryDtoWithoutId.getName());

        CategoryDto expected = new CategoryDto()
                .setId(id)
                .setName(categoryDtoWithoutId.getName());

        when(categoryRepository.existsById(id)).thenReturn(true);
        when(categoryMapper.toEntity(categoryDtoWithoutId)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryDto actual = categoryService.update(categoryDtoWithoutId, id);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update by not valid id")
    void update_WithNotValidId_NotSuccess() {
        CategoryDto categoryDto = new CategoryDto()
                .setName("Math");
        Long id = 10L;

        when(categoryRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            categoryService.update(categoryDto, id);
        });
    }

    @Test
    @DisplayName("Find by not valid id")
    void findById_WithNotValidId_NotSuccess() {
        Long id = 10L;

        when(categoryRepository.findById(id))
                .thenThrow(new EntityNotFoundException("Can't find category by id " + id));

        assertThrows(EntityNotFoundException.class, () -> {
            categoryService.getById(id);
        });
    }
}
