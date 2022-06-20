package com.quarke5.ttplayer.controller;

import com.quarke5.ttplayer.dto.request.LoginDTO;
import com.quarke5.ttplayer.dto.request.PlayerDTO;
import com.quarke5.ttplayer.dto.response.PlayerResponseDTO;
import com.quarke5.ttplayer.mapper.PlayerMapper;
import com.quarke5.ttplayer.model.Player;
import com.quarke5.ttplayer.repository.PlayerDAO;
import com.quarke5.ttplayer.service.PlayerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MimeTypeUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@WebMvcTest(PlayerController.class)
class PlayerControllerTest {

    @Autowired PlayerService playerService;
    @Autowired PlayerMapper playerMapper;
    @Autowired PlayerDAO playerDAO;
    @Autowired MessageSource messageSource;
    @Autowired MockMvc mockMvc;

    private static final String BASE_URL = "https://topictwisterequipocinco.herokuapp.com/player/";
    private static final int NEXT_ID = 1;
    private static int NUMBERS_VICTORY_INIT = 0;
    private static int SUM_ONE = 1;

    @BeforeEach
    void setUp() {
        playerMapper = new PlayerMapper();
    }

    @Test
    void shouldCreateMockMvc(){
        assertNotNull(mockMvc);
    }

    @Test
    String get(String baseUrl) {

        return BASE_URL;
    }

    @Test
    void updateOk() throws Exception {
        Player response = playerMapper.toUpdateResponseDTO(new Player());

        ResponseEntity responseEntity = new ResponseEntity(response,HttpStatus.OK);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
        assertEquals(response.getWins(), SUM_ONE);
    }

    @Test
    void updateNotOk() throws Exception {
        final Player request = mock(Player.class);
        Player response = playerMapper.toUpdateResponseDTO(request);

        String message = (messageSource.getMessage("player.update.failed", new Object[] {request.getId()},null));
        ResponseEntity responseEntity = new ResponseEntity(message, HttpStatus.NOT_FOUND);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(message, responseEntity.getBody());
    }

    @Test
    void createIsOk() throws Exception {
        final PlayerDTO request = mock(PlayerDTO.class);
        request.setName("Pepito");
        request.setEmail("pepito@gmail.com");
        request.setPassword("Pepito$2022");
        String body = request.toString();

        mockMvc.perform(
                post(BASE_URL)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body));

        Player player = playerMapper.toModel(request, NEXT_ID);
        PlayerResponseDTO response = playerMapper.responsePlayerDtoToPlayer(player);
        ResponseEntity responseEntity = new ResponseEntity(response, HttpStatus.CREATED);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
        assertEquals(response.getName(), request.getName());
    }

    @Test
    void createIsNotOk() throws Exception {
        final PlayerDTO request = mock(PlayerDTO.class);
        request.setName("");
        request.setEmail("pepito@gmail.com");
        request.setPassword("");
        String body = request.toString();

        mockMvc.perform(
                post(BASE_URL)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body));

        Player player = playerMapper.toModel(request, NEXT_ID);
        String message = (messageSource.getMessage("player.created.failed", new Object[] {request.getName(),new Exception().getMessage()}, null));
        ResponseEntity responseEntity = new ResponseEntity(message, HttpStatus.NOT_ACCEPTABLE);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
        assertEquals(message, responseEntity.getBody());
    }


    @Test
    void getAll() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<PlayerResponseDTO> list = (List<PlayerResponseDTO>) playerService.getAll();

        ResponseEntity responseEntity = new ResponseEntity(list, HttpStatus.OK);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(list, responseEntity.getBody());
    }

    @Test
    void login() throws Exception {
        final LoginDTO request = mock(LoginDTO.class);
        request.setEmail("pepeargento@gmail.com");
        request.setPassword("Pepito$2022");
        String body = request.toString();

        mockMvc.perform(
                post(BASE_URL)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body));

        Player dto = mock(Player.class);
        dto.setName("Pepito");
        dto.setUsername(request.getEmail());
        dto.setPassword(request.getPassword());
        dto.setId("1");
        dto.setWins(0);

        //PlayerResponseDTO response = playerMapper.responsePlayerDtoToPlayer(dto);
        PlayerResponseDTO response = new PlayerResponseDTO();
        response.setName(dto.getName());
        response.setId(parseInt("1"));
        response.setWins(dto.getWins());

        ResponseEntity responseEntity = new ResponseEntity(response, HttpStatus.ACCEPTED);
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
        assertEquals(response.getName(), dto.getName());
        assertEquals(response.getWins(), dto.getWins());

    }

    @AfterEach
    void tearDown() {}

    @org.springframework.context.annotation.Configuration
    public static class ContextConfiguration extends com.quarke5.ttplayer.controller.ContextConfiguration {

    }
}