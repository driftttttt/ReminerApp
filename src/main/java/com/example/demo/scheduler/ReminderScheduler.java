package com.example.demo.scheduler;

import com.example.demo.entity.Reminder;
import com.example.demo.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReminderScheduler {

    private final ReminderRepository reminderRepository;

    @Autowired
    public ReminderScheduler(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    // Проверка раз в 10сек
    @Scheduled(fixedRate = 10000)
    public void sendReminders() {
        //System.out.println("Проверка напоминаний: " + LocalDateTime.now());


        // Получаем напоминания
        List<Reminder> reminders = reminderRepository.findAll().stream()
                .filter(reminder -> reminder.getRemind().isBefore(LocalDateTime.now()) && !reminder.isSent()) //Наступило время + не помечены отправленными
                .toList();

        // Уведоление
        for (Reminder reminder : reminders) {
            System.out.println("НОВОЕ УВЕДОМЛЕНИЕ! ️");
            System.out.println("Заголовок: " + reminder.getTitle());
            System.out.println("Описание: " + reminder.getDescription());
            System.out.println("Время: " + reminder.getRemind());

            // email

            //Почем как отправленное
            reminder.setSent(true);
            reminderRepository.save(reminder);
        }

        //System.out.println("Проверка завершена.\n");
    }
}
