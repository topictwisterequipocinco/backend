package com.quarke5.ttplayer.controller;

import com.quarke5.ttplayer.controller.interfaces.Controllers;
import com.quarke5.ttplayer.controller.interfaces.Creators;
import com.quarke5.ttplayer.controller.interfaces.Messages;
import com.quarke5.ttplayer.dto.request.PersonDTO;
import com.quarke5.ttplayer.exception.PersonException;
import com.quarke5.ttplayer.service.interfaces.PublisherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

@RestController
@Api(value = "Publisher Controller", description = "Controlador con los endpoints que actúan sobre los Publisher.")
@RequestMapping("/publisher")
public class PublisherController implements Controllers<PersonDTO>, Messages, Creators<PersonDTO> {

    @Autowired
    PublisherService publisherService;

    @Override
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> get(@PathVariable Long id) {
        return publisherService.sendGetPersonByIdRequest(id);
    }

    @ApiOperation(value = "${publisher.update} - Modifica un objeto Publisher", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 201, message = CREATED),
            @ApiResponse(code = 202, message = ACCEPTED),
            @ApiResponse(code = 304, message = NOT_MODIFIED),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE),
            @ApiResponse(code = 406, message = NOT_ACCEPTABLE),
            @ApiResponse(code = 503, message = SERVICE_UNAVAILABLE),
    })
    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid PersonDTO publisherDTO) throws PersonException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return publisherService.update(id, publisherDTO);
    }

    @Override
    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return publisherService.delete(id);
    }

    @Override
    @GetMapping(value = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAll() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return null;
    }

    @Override
    @ApiOperation(value = "${publisher.create} - Crea un objeto Publisher", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 201, message = CREATED),
            @ApiResponse(code = 202, message = ACCEPTED),
            @ApiResponse(code = 304, message = NOT_MODIFIED),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE),
            @ApiResponse(code = 406, message = NOT_ACCEPTABLE),
            @ApiResponse(code = 503, message = SERVICE_UNAVAILABLE),
    })
    @PostMapping(value = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> create(@RequestBody @Valid PersonDTO publisherDTO) throws PersonException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return publisherService.update(0L, publisherDTO);
    }
}

