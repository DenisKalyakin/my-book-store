package mate.academy.bookstore.mapper;

import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.category.external.CreateCategoryRequestDto;
import mate.academy.bookstore.dto.category.internal.CategoryDto;
import mate.academy.bookstore.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);

    Category toEntity(CreateCategoryRequestDto requestDto);
}
