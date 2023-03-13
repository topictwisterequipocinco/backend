package com.quarke5.ttplayer.controller;

import com.quarke5.ttplayer.controller.interfaces.Controllers;
import com.quarke5.ttplayer.controller.interfaces.Creators;
import com.quarke5.ttplayer.controller.interfaces.Messages;
import com.quarke5.ttplayer.dto.request.PersonDTO;
import com.quarke5.ttplayer.exception.PersonException;
import com.quarke5.ttplayer.service.crud.Readable;
import com.quarke5.ttplayer.service.interfaces.PersonService;
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
@Api(value = "Person Controller", description = "Controlador con los endpoints que actúan sobre las Person.")
@RequestMapping("/person")
public class PersonController implements Controllers<PersonDTO>, Messages, Creators<PersonDTO> {

    @Autowired PersonService personService;

    @Override
    @ApiOperation(value = "${person.getById} - Devuelve los datos de una persona por su ID", response = ResponseEntity.class)
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
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> get(@PathVariable Long id){
        return personService.getById(id);
    }

    @ApiOperation(value = "${person.getByDni} - Devuelve los datos de una persona por su DNI", response = ResponseEntity.class)
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
    @GetMapping(value = "/identification/{identification}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getByDni(@PathVariable String identification) throws ExecutionException, InterruptedException {
        return personService.getByIdentification(identification);
    }

    /**
     * Devuelve un Publisher a traves del id del User
     * @param id del user
     * @return objeto publisher para mostrar en profile
     */
    @GetMapping(path = "/userId/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getByIdUser(@PathVariable Long id){
        return personService.getByIdUser(id);
    }

    @Override
    @ApiOperation(value = "${person.update} - Modifica o crea una persona", response = ResponseEntity.class)
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
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid PersonDTO personDTO) throws PersonException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return personService.update(id, personDTO);
    }

    @Override
    @ApiOperation(value = "${person.delete} - Elimina una persona con Baja lògica", response = ResponseEntity.class)
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
    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> delete(@PathVariable Long id){
        return personService.delete(id);
    }

    @Override
    @ApiOperation(value = "person.getAll - Devuelve La Lista de Todas las Personas", response = ResponseEntity.class)
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
    @GetMapping(value = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAll() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return personService.getAll();
    }

    @ApiOperation(value = "person.getAll - Devuelve La Lista de Todas las Personas", response = ResponseEntity.class)
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
    @GetMapping(value = "/applicants", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllApplicant(@RequestParam(name = "page",defaultValue = "0") int page) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return personService.getAllApplicant(page);
    }

    @ApiOperation(value = "person.getAll - Devuelve La Lista de Todas las Personas", response = ResponseEntity.class)
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
    @GetMapping(value = "/publishers", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllPublisher(@RequestParam(name = "page",defaultValue = "0") int page) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return personService.getAllPublisher(page);
    }

    @Override
    @ApiOperation(value = "${person.create} - Crea una Persona nueva", response = ResponseEntity.class)
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
    public ResponseEntity<?> create(@RequestBody @Valid PersonDTO personDTO) throws PersonException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return personService.update(0L, personDTO);
    }

}
