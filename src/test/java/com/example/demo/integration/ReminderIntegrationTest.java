package com.example.demo.integration;

import com.example.demo.TestSecurityConfig;
import com.example.demo.entity.User;
import com.example.demo.repository.ReminderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ReminderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционные тесты для проверки API
 *
 * <p>Для каждого теста создается новый пользователь</p>
 *
 * <ul>
 *     <li>Профиль: test</li>
 *     <li>База данных: меняем на встроенную БД</li>
 * </ul>
 */
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ReminderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReminderRepository reminderRepository;

    private User testUser;

    /**
     * Настройка перед тестом
     *
     * <p>Очищаем тестовую бд, создаем тестового пользователя</p>
     */
    @BeforeEach
    public void setUp() {
        reminderRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("testuser@test.com");
        userRepository.save(testUser);
    }

    /**
     * Проверяет создание напоминания и получения списка напоминаний
     *
     * Шаги выполнения:
     * <ol>
     *     <li>Создать новое напоминание</li>
     *     <li>Получить список напоминаний</li>
     *     <li>Проверить, что созданное напоминание есть в списке</li>
     * </ol>
     */
    @Test
    public void testCreateAndFetchReminders() throws Exception {
        // Создаем напоминание
        mockMvc.perform(post("/api/v1/reminder/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Интеграционный тест\", \"description\": \"Напоминание интеграционный тест.\", \"remind\": \"2024-12-31T23:59\", \"user\": { \"id\": " + testUser.getId() + " } }"))
                .andExpect(status().isOk());

        // Проверяем список напоминаний
        mockMvc.perform(get("/api/v1/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Интеграционный тест"));
    }
}
