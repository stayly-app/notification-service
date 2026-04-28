package com.ciberaccion.stayly.notificationservice.listener;

import com.ciberaccion.stayly.notificationservice.service.NotificationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingEventListener {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @SqsListener("${booking.events.queue.url}")
    public void onBookingConfirmed(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            JsonNode payload = root.path("payload");

            String userId = payload.path("userId").asText();
            String bookingId = payload.path("bookingId").asText();
            String checkIn = payload.path("checkIn").asText();
            String checkOut = payload.path("checkOut").asText();
            String totalAmount = payload.path("totalAmount").asText();

            notificationService.sendBookingConfirmation(userId, bookingId, checkIn, checkOut, totalAmount);

        } catch (Exception e) {
            log.error("Failed to process BookingConfirmed event", e);
        }
    }
}