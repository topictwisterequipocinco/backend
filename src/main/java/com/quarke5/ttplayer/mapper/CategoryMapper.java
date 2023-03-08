package com.quarke5.ttplayer.mapper;

import com.quarke5.ttplayer.dto.request.CategoryDTO;
import com.quarke5.ttplayer.dto.response.ResponseCategoryDto;
import com.quarke5.ttplayer.dto.response.ResponseSearchCategoryDto;
import com.quarke5.ttplayer.model.Category;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapper {

    public Category toModel(CategoryDTO dto, int id) {
        Category cate = Category.builder()
                .id((long) id)
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
        return cate;
    }

    public ResponseCategoryDto toResponsePerson(Category category, String message) {
        ResponseCategoryDto res = ResponseCategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .message(message)
                .build();
        return res;
    }

    public Category toModelUpdate(Category category, CategoryDTO dto) {
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }

    public List<ResponseCategoryDto> toCategoriesList(List<Category> categories) {
        List<ResponseCategoryDto> lists = new ArrayList<>();
        for(Category ele : categories){
            ResponseCategoryDto dto = toResponsePerson(ele, "");
            lists.add(dto);
        }
        return lists;
    }

    public List<ResponseSearchCategoryDto> toCategoriesListForFilters(List<Category> categories) {
        List<ResponseSearchCategoryDto> lists = new ArrayList<>();
        for (Category ele : categories){
            ResponseSearchCategoryDto dto = ResponseSearchCategoryDto.builder()
                    .name(ele.getName())
                    .build();
            lists.add(dto);
        }
        return lists;
    }
}
