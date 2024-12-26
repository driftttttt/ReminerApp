package com.example.demo.service;

import com.example.demo.entity.Reminder;
import com.example.demo.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис управления напоминаниями
 *
 * Доступ через {@link ReminderRepository}
 */
@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;

    /**
     * Создает новое напоминание
     * @param reminder объект, который надо сохранить
     * @return сохраненный объект
     */
    public Reminder createReminder(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    /**
     * Удаляет напоминание
     * @param id напоминания
     */
    public void deleteReminder(Long id) {
        reminderRepository.deleteById(id);
    }

    /**
     * Возвращает все напоминания
     * @return List со всеми напоминаниями
     */
    public List<Reminder> getAllReminders() {
        return reminderRepository.findAll();
    }

    /**
     * Сортирует напоминания
     * @param sortBy параметр по которому будет сортировка
     * @return отсортированный список
     */
    public List<Reminder> getSortedReminders(String sortBy) {
        return reminderRepository.findAll(Sort.by(sortBy));
    }

    /**
     * Поиск напоминаний
     * @param date дата для поиска yyyy-MM-dd (может быть null)
     * @param time время для поиска HH:mm (может быть null)
     * @return список, удовлетворяющий критериям
     */
    public List<Reminder> filterReminders(String date, String time) {
        return reminderRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (date != null) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.function("FORMATDATETIME", String.class, root.get("remind"), criteriaBuilder.literal("yyyy-MM-dd")),
                        date
                ));
            }

            if (time != null) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.function("FORMATDATETIME", String.class, root.get("remind"), criteriaBuilder.literal("HH:mm")),
                        time + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    /**
     * Список с учетом пагинации
     * @param page кол-во страниц (по умолчанию 0)
     * @param size кол-во записей на странице (по умолчанию 10)
     * @return список напоминаний
     */
    public List<Reminder> getRemindersWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reminderRepository.findAll(pageable).getContent();
    }

    /**
     * Всего напоминаний
     * @return возращает кол-во всех напоминаний
     */
    public long getTotalReminders() {
        return reminderRepository.count();
    }


}



