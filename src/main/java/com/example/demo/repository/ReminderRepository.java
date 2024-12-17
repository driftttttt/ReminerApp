package com.example.demo.repository;

import com.example.demo.entity.Reminder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long>, JpaSpecificationExecutor<Reminder> {
    @EntityGraph(attributePaths = "user") //Чтобы убрать избыточные запросы к бд
    @Cacheable("reminders")
    List<Reminder> findAll();
}