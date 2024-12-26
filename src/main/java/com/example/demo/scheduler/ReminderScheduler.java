package com.example.demo.scheduler;

import com.example.demo.entity.Reminder;
import com.example.demo.repository.ReminderRepository;
import com.example.demo.service.TelegramNotificationService;
import com.example.demo.service.EmailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReminderScheduler {

    private final ReminderRepository reminderRepository;
    private final TelegramNotificationService telegramNotificationService;
    private final EmailNotificationService emailNotificationService;

    @Autowired
    public ReminderScheduler(ReminderRepository reminderRepository,
                             TelegramNotificationService telegramNotificationService,
                             EmailNotificationService emailNotificationService) {
        this.reminderRepository = reminderRepository;
        this.telegramNotificationService = telegramNotificationService;
        this.emailNotificationService = emailNotificationService;
    }

    /**
     * <ul>
     *     <li>Проверка напоминаний раз в 10 сек</li>
     *     <li>Получаем напоминания, которые нужно отправить</li>
     *     <li>Обрабатываем каждое напоминание</li>
     *     <li>Отправка уведомления в Telegram</li>
     *     <li>Помечаем как отправленное</li>
     * </ul>
     */
    @Scheduled(fixedRate = 10000)
    public void sendReminders() {
        List<Reminder> reminders = reminderRepository.findAll().stream()
                .filter(reminder -> reminder.getRemind().isBefore(LocalDateTime.now()) && !reminder.isSent())
                .toList();

        for (Reminder reminder : reminders) {
            String message = "Напоминание!\n" +
                    "Заголовок: " + reminder.getTitle() + "\n" +
                    "Описание: " + reminder.getDescription() + "\n" +
                    "Время: " + reminder.getRemind();

            try {
                telegramNotificationService.sendNotification(message);
            } catch (Exception e) {
                System.err.println("Ошибка отправки уведомления в Telegram: " + e.getMessage());
            }

            try {
                emailNotificationService.sendEmail(
                        reminder.getUser().getEmail(),
                        "Напоминание: " + reminder.getTitle(),
                        message
                );
            } catch (Exception e) {
                System.err.println("Ошибка отправки уведомления на Email: " + e.getMessage());
            }

            reminder.setSent(true);
            reminderRepository.save(reminder);
        }
    }
}
