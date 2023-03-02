package com.quarke5.ttplayer.service.impl;

import com.google.cloud.firestore.WriteResult;
import com.quarke5.ttplayer.dto.request.CategoryDTO;
import com.quarke5.ttplayer.dto.request.PersonDTO;
import com.quarke5.ttplayer.dto.response.ResponseSearchCategoryDto;
import com.quarke5.ttplayer.exception.CategoryException;
import com.quarke5.ttplayer.exception.PersonException;
import com.quarke5.ttplayer.mapper.CategoryMapper;
import com.quarke5.ttplayer.model.Applicant;
import com.quarke5.ttplayer.model.Category;
import com.quarke5.ttplayer.repository.impl.CategoryDAO;
import com.quarke5.ttplayer.service.interfaces.CategoryService;
import com.quarke5.ttplayer.util.Errors;
import com.quarke5.ttplayer.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private static final int NEXT_ID = 1;

    @Autowired private CategoryDAO categoryRepository;
    @Autowired private CategoryMapper categoryMapper;
    @Autowired private MessageSource messageSource;
    @Autowired private Validator validCategory;
    @Autowired private Errors errors;

    @Override
    public ResponseEntity<?> getById(Long id) {
        try{
            Category category = categoryRepository.findById(String.valueOf(id));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(categoryMapper.toResponsePerson(category, messageSource.getMessage("category.response.success", null,null)));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("category.response.failed " + e.getMessage(), new Object[] {id}, null));
            errors.logError(messageSource.getMessage("category.response.failed " + e.getMessage(), new Object[] {id}, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("category.response.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> update(Long id, CategoryDTO categoryDTO) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return id > 0L ? updateCategory(id, categoryDTO) : create(categoryDTO);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try{
            Category category = getCategory(id);
            category.setDeleted(true);
            categoryRepository.update(category);
            return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("category.delete.success", new Object[] {id},null));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("category.delete.failed " + e.getMessage(), new Object[] {id}, null));
            errors.logError(messageSource.getMessage("category.delete.failed " + e.getMessage(), new Object[] {id}, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("category.delete.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> getAll() {
        try{
            List<Category> categories = categoryRepository.getAllEntities();
            return ResponseEntity.status(HttpStatus.OK).body(categoryMapper.toCategoriesList(categories));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("category.lists.failed " + e.getMessage(), null, null));
            errors.logError(messageSource.getMessage("category.lists.failed " + e.getMessage(), null, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("category.lists.failed", null, null));
        }
    }

    @Override
    public ResponseEntity<?> getFiltersAllCategories() {
        try{
            List<Category> categories = categoryRepository.getAllEntities();
            List<ResponseSearchCategoryDto> lists = categoryMapper.toCategoriesListForFilters(categories);
            return ResponseEntity.status(HttpStatus.OK).body(lists);
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("category.lists.failed " + e.getMessage(), null, null));
            errors.logError(messageSource.getMessage("category.lists.failed " + e.getMessage(), null, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("category.lists.failed", null, null));
        }
    }

    private ResponseEntity<?> updateCategory(Long id, CategoryDTO categoryDTO) {
        try{
            Category newCategory = categoryMapper.toModelUpdate(getCategory(id), categoryDTO);
            validCategory.validCategory(newCategory);
            categoryRepository.update(newCategory);
            return ResponseEntity.status(HttpStatus.OK).body(categoryMapper.toResponsePerson(newCategory, messageSource.getMessage("category.update.success", null,null)));
        }catch (CategoryException e){
            LOGGER.error(messageSource.getMessage("category.update.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            errors.logError(messageSource.getMessage("category.update.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(messageSource.getMessage("category.update.failed",new Object[] {e.getMessage()}, null));
        }
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(String.valueOf(id));
    }

    private ResponseEntity<?> create(CategoryDTO categoryDTO) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if(!verifyIsExists(categoryDTO.getName())){
            return getCreateEntityResponseDTO(categoryDTO);
        }else {
            LOGGER.error(messageSource.getMessage("category.isExists", null,null));
            errors.logError(messageSource.getMessage("category.isExists", null,null));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("category.isExists",null, null));
        }
    }

    private boolean verifyIsExists(String element) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        boolean result = false;
        List<Category> categoryList = categoryRepository.getAllEntities();
        for(Category ele : categoryList){
            if (ele.getName().equals(element)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private ResponseEntity<?> getCreateEntityResponseDTO(CategoryDTO categoryDTO) {
        try{
            Category category = categoryMapper.toModel(categoryDTO, getLastId());
            validCategory.validCategory(category);
            categoryRepository.update(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.toResponsePerson(category, messageSource.getMessage("category.created.success", null,null)));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("category.created.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            errors.logError(messageSource.getMessage("category.created.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("category.created.failed",new Object[] {e.getMessage()}, null));
        }
    }
    private int getLastId() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return categoryRepository.getAllEntities().size() + NEXT_ID;
    }

}
