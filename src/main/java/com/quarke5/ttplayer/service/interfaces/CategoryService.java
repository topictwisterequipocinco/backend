package com.quarke5.ttplayer.service.interfaces;

import com.quarke5.ttplayer.dto.request.CategoryDTO;
import com.quarke5.ttplayer.service.crud.Removable;
import com.quarke5.ttplayer.service.crud.Writeable;
import org.springframework.http.ResponseEntity;

public interface CategoryService extends Removable, Writeable<CategoryDTO> {
    ResponseEntity<?> getById(Long id);

    ResponseEntity<?> getAll();

    ResponseEntity<?> getFiltersAllCategories();
}
