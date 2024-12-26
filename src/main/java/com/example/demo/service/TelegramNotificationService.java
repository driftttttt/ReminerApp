package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;

@Service
public class TelegramNotificationService {

    private static final String BOT_TOKEN = "BOT_TOKEN"; //  токен бота
    private static final Long CHAT_ID = 0L; // chatId

    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot" + BOT_TOKEN;

    public void sendNotification(String message) {
        try {
            String url = TELEGRAM_API_URL + "/sendMessage?chat_id=" + CHAT_ID + "&text=" + message;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            System.err.println("Ошибка отправки уведомления в Telegram: " + e.getMessage());
        }
    }

}
