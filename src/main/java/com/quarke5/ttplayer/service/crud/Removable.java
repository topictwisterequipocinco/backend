package com.quarke5.ttplayer.service.crud;

import org.springframework.http.ResponseEntity;

public interface Removable {
    ResponseEntity<?> delete(Long id);
}
