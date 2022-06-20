package com.quarke5.ttplayer.controller;

import com.quarke5.ttplayer.mapper.PlayerMapper;
import com.quarke5.ttplayer.repository.PlayerDAO;
import com.quarke5.ttplayer.service.PlayerService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ContextConfiguration {
    @Bean
    public MockMvc mockMvc() {
        return MockMvcBuilders
                .standaloneSetup(new PlayerController())
                .build();
    }

    @Bean
    public PlayerService playerService() {
        return Mockito.mock(PlayerService.class);
    }

    @Bean
    public PlayerMapper playerMapper() {
        return Mockito.mock(PlayerMapper.class);
    }

    @Bean
    public PlayerDAO playerDAO() {
        return Mockito.mock(PlayerDAO.class);
    }
}
