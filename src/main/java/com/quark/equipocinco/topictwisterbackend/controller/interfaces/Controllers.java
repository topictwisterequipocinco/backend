package com.quark.equipocinco.topictwisterbackend.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface Controllers<I, O> {
    String OK_RESPONSE = "Operacion realizada con exito.";
    String CREATED_RESPONSE = "Creado con exito.";
    String UNAUTHORIZED_RESPONSE = "Necesita autorizacion para realizar esta operacion..";
    String FORBIDDEN_RESPONSE = "No tiene los permisos necesarios para realizar esta operacion.";
    String NOT_FOUND_RESPONSE = "El recurso buscado no existe o no se encuentra disponible.";

    ResponseEntity<?> get(@PathVariable("data") String data) throws Exception;
    ResponseEntity<?> update(@PathVariable String username, @RequestBody @Valid I entity);
    ResponseEntity<?> delete(@PathVariable("data") String data) throws Exception;
    ResponseEntity<?> create(@RequestBody @Valid I entity) throws Exception;
    ResponseEntity<?> getAll() throws Exception;
}
