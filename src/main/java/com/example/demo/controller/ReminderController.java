package com.example.demo.controller;

import com.example.demo.entity.Reminder;
import com.example.demo.entity.User;
import com.example.demo.repository.ReminderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ReminderService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1")
public class ReminderController {

    private final ReminderService reminderService;
    private final UserRepository userRepository;
    private final ReminderRepository reminderRepository;

    @Autowired
    public ReminderController(ReminderService reminderService, UserRepository userRepository, ReminderRepository reminderRepository) {
        this.reminderService = reminderService;
        this.userRepository = userRepository;
        this.reminderRepository = reminderRepository;
    }

    @PostMapping("/reminder/create")
    public ResponseEntity<Reminder> createReminder(@RequestBody Reminder reminder) {
        return ResponseEntity.ok(reminderService.createReminder(reminder));
    }

    @DeleteMapping("/reminder/delete/{id}") //удаляем данные по id
    public ResponseEntity<String> deleteReminder(@PathVariable Long id) {
        reminderService.deleteReminder(id);
        return ResponseEntity.ok("ДАННЫЕ УДАЛЕНЫ УСПЕШНО!");
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllRemindersWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<Reminder> reminders = reminderService.getRemindersWithPagination(page, size);
        Map<String, Object> response = new HashMap<>();
        response.put("total", reminderService.getTotalReminders()); //всего записей
        response.put("current", page); //текущая page
        response.put("data", reminders);//данные с текущей page

        return ResponseEntity.ok(response);
    }


    @GetMapping("/sort")
    public ResponseEntity<List<Reminder>> getSortedReminders(@RequestParam(defaultValue = "remind") String by) {
        return ResponseEntity.ok(reminderService.getSortedReminders(by));
    }

    @GetMapping("/filtr")
    public ResponseEntity<List<Reminder>> filterReminders(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String time) {
        System.out.println("ПОИСК: date = " + date + ", time = " + time);
        List<Reminder> result = reminderService.filterReminders(date, time);
        System.out.println("РЕЗУЛЬТАТ: " + result.size() + " НАПОМИНАНИЙ НАЙДЕНО");
        return ResponseEntity.ok(result);
    }



    // тестовые данные
    @PostConstruct
    public void initTestData() {
        // пользователи тест
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@test.ru");
        userRepository.save(user);

        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("1111@test.ru");
        userRepository.save(user1);

        // напоминания тест
        Reminder reminder1 = new Reminder();
        reminder1.setTitle("Тестовое напоминание 1");
        reminder1.setDescription("Описание тестового напоминания");
        reminder1.setRemind(LocalDateTime.of(2024, 12, 1, 10, 0));
        reminder1.setUser(user);
        reminderRepository.save(reminder1);

        Reminder reminder2 = new Reminder();
        reminder2.setTitle("Тестовое напоминание 2");
        reminder2.setDescription("Описание тестового напоминания");
        reminder2.setRemind(LocalDateTime.of(2024, 12, 10, 10, 0));
        reminder2.setUser(user);
        reminderRepository.save(reminder2);

        Reminder reminder3 = new Reminder();
        reminder3.setTitle("Тестовое напоминание 3");
        reminder3.setDescription("Описание тестового напоминания Описание тестового напоминания Описание тестового напоминания Описание тестового напоминания");
        reminder3.setRemind(LocalDateTime.of(2024, 1, 11, 10, 0));
        reminder3.setUser(user1);
        reminderRepository.save(reminder3);


        System.out.println("ТЕСТОВЫЕ ДАННЫЕ ДОБАВЛЕНЫ");
    }
}

