package com.example.demo.service;

import com.example.demo.entity.Reminder;
import com.example.demo.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;

    @Autowired
    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    public Reminder createReminder(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    public void deleteReminder(Long id) {
        reminderRepository.deleteById(id);
    }

    public List<Reminder> getAllReminders() {
        return reminderRepository.findAll();
    }

    public List<Reminder> getSortedReminders(String sortBy) {
        return reminderRepository.findAll(Sort.by(sortBy));
    }

    public List<Reminder> filterReminders(String date, String time) {
        return reminderRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Фильтр по дате yyyy-MM-dd
            if (date != null) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.function("FORMATDATETIME", String.class, root.get("remind"), criteriaBuilder.literal("yyyy-MM-dd")),
                        date
                ));
            }

            // Фильтр по времени HH:mm
            if (time != null) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.function("FORMATDATETIME", String.class, root.get("remind"), criteriaBuilder.literal("HH:mm")),
                        time + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }


    // Пагинация для списка
    public List<Reminder> getRemindersWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reminderRepository.findAll(pageable).getContent();
    }
    public long getTotalReminders() {
        return reminderRepository.count();
    }


}



