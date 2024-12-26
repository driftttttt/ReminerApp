package com.example.demo.controller;

import com.example.demo.TestSecurityConfig;
import com.example.demo.entity.Reminder;
import com.example.demo.repository.ReminderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ReminderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <ul>
 *     <li>Профиль: test </li>
 *     <li>Проверка сценариев: создание, удаление, сортировка, фильтрация, пагинация</li>
 * </ul>
 */
@ActiveProfiles("test")
@WebMvcTest(ReminderController.class)
@Import(TestSecurityConfig.class)
public class ReminderControllerTest {

        @MockBean
        private ReminderService reminderService;

        @MockBean
        private UserRepository userRepository;

        @MockBean
        private ReminderRepository reminderRepository;

        @Autowired
        private MockMvc mockMvc;

    /**
     * Проверяет что контекст загружается
     */
    @Test
    void contextLoads() {
        }

    /**
     * Проверяет создания нового напоминания
     *
     * @throws Exception если MockMvc вызывает ошибку
     */
    @Test
    void testCreateReminder() throws Exception {
        Reminder reminder = Reminder.builder()
                .id(1L)
                .title("Тестовое напоминание")
                .description("Описание")
                .remind(LocalDateTime.now())
                .build();

        when(reminderService.createReminder(any(Reminder.class))).thenReturn(reminder);

        mockMvc.perform(post("/api/v1/reminder/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Тестовое напоминание\", \"description\": \"Описание\", \"remind\": \"2024-12-22T10:00\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Тестовое напоминание"))
                .andExpect(jsonPath("$.description").value("Описание"));
    }

    /**
     * Проверяет удаление напоминания по его id
     *
     * @throws Exception если MockMvc вызывает ошибку
     */
    @Test
    void testDeleteReminder() throws Exception {
        doNothing().when(reminderService).deleteReminder(1L);

        mockMvc.perform(delete("/api/v1/reminder/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("ДАННЫЕ УДАЛЕНЫ УСПЕШНО!"));
    }

    /**
     * Проверяет сортировку напоминаний по параметру
     *
     *  @throws Exception если MockMvc вызывает ошибку
     */
    @Test
    void testSortReminders() throws Exception {
        List<Reminder> reminders = List.of(
                Reminder.builder()
                        .id(1L)
                        .title("Тест A")
                        .description("A")
                        .remind(LocalDateTime.now())
                        .build(),
                Reminder.builder()
                        .id(2L)
                        .title("Тест B")
                        .description("B")
                        .remind(LocalDateTime.now().plusHours(1))
                        .build()
        );

        when(reminderService.getSortedReminders("title")).thenReturn(reminders);

        mockMvc.perform(get("/api/v1/sort").param("by", "title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Тест A"))
                .andExpect(jsonPath("$[1].title").value("Тест B"));
    }

    /**
     * Проверяет поиск напоминаний по параетру
     *
     * @throws Exception если MockMvc вызывает ошибку
     */
    @Test
    void testFilterReminders() throws Exception {
        List<Reminder> reminders = List.of(
                Reminder.builder()
                        .id(1L)
                        .title("Тестовое напоминание")
                        .description("Описание")
                        .remind(LocalDateTime.of(2024, 12, 22, 12, 0))
                        .build()
        );

        when(reminderService.filterReminders("2024-12-22", null)).thenReturn(reminders);

        mockMvc.perform(get("/api/v1/filtr").param("date", "2024-12-22"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Тестовое напоминание"));
    }

    /**
     * Проверяет получение списка напоминаний
     *
     * @throws Exception если MockMvc вызывает ошибку
     */
    @Test
    void testGetAllRemindersWithPagination() throws Exception {
        List<Reminder> reminders = List.of(
                Reminder.builder()
                        .id(1L)
                        .title("Тестовое напоминание")
                        .description("Описание")
                        .remind(LocalDateTime.of(2024, 12, 22, 12, 0))
                        .build()
        );

        when(reminderService.getRemindersWithPagination(0, 10)).thenReturn(reminders);
        when(reminderService.getTotalReminders()).thenReturn(1L);

        mockMvc.perform(get("/api/v1/list").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.current").value(0))
                .andExpect(jsonPath("$.data.length()").value(1));
    }

}

