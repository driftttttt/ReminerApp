package com.example.demo;

import com.example.demo.repository.ReminderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ReminderService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;
@TestConfiguration
public class ReminderTestConfig {

        @Bean
        public ReminderService reminderService() {
            return mock(ReminderService.class);
        }

        @Bean
        public ReminderRepository reminderRepository() {
            return mock(ReminderRepository.class);
        }

        @Bean
        public UserRepository userRepository() {
            return mock(UserRepository.class);
        }


}
