package com.example.demo.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {
    // Вывод ошибок вместо Whitelabel
    @RequestMapping("/error")
    public ResponseEntity<Object> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("error", statusCode);
            errorDetails.put("message", "ERROR");

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorDetails.put("message", "404 Not Found");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorDetails.put("message", "500 Internal Server Error");
            } else if (statusCode == HttpStatus.BAD_GATEWAY.value()) {
                errorDetails.put("message", "502 Bad Gateway");
            } else if (statusCode == HttpStatus.SERVICE_UNAVAILABLE.value()) {
                errorDetails.put("message", "503 Service Unavailable");
            } else if (statusCode == HttpStatus.GATEWAY_TIMEOUT.value()) {
                errorDetails.put("message", "504 Gateway Timeout");
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                errorDetails.put("message", "401 Unauthorized");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorDetails.put("message", "403 Forbidden");
            } else if (statusCode == HttpStatus.CONFLICT.value()) {
                errorDetails.put("message", "409 Conflict");
            }

            return new ResponseEntity<>(errorDetails, HttpStatus.valueOf(statusCode));
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
