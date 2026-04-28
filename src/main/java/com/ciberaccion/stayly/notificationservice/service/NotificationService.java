package com.ciberaccion.stayly.notificationservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    public void sendBookingConfirmation(String userId, String bookingId, String checkIn, String checkOut, String totalAmount) {
        // Aquí se integraría SendGrid, SES, Firebase, etc.
        log.info("📧 Sending booking confirmation email to userId: {}", userId);
        log.info("   Booking ID : {}", bookingId);
        log.info("   Check-in   : {}", checkIn);
        log.info("   Check-out  : {}", checkOut);
        log.info("   Total      : ${}", totalAmount);
        log.info("✅ Email sent successfully to userId: {}", userId);
    }

    public void sendPaymentConfirmation(String userId, String bookingId, String totalAmount) {
        // Aquí se integraría SendGrid, SES, Firebase, etc.
        log.info("📧 Sending payment confirmation email to userId: {}", userId);
        log.info("   Booking ID : {}", bookingId);
        log.info("   Total      : ${}", totalAmount);
        log.info("✅ Payment confirmation sent to userId: {}", userId);
    }
}