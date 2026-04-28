# notification-service

Microservicio de notificaciones del sistema **Stayly**. Consume el evento `BookingConfirmed` de SQS y envía confirmaciones al usuario. Es el servicio más simple del sistema — consumidor puro de eventos, sin base de datos propia ni lógica de negocio compleja.

---

## ¿Cómo encaja en Stayly?

```
hotel-service → search-service → booking-service → payment-service → notification-service
```

`notification-service` es el último eslabón del flujo — se construye al final porque solo depende de eventos. No tiene dependencias con otros servicios ni expone endpoints de escritura.

---

## Endpoints

Este servicio no expone endpoints de negocio — es un consumidor puro de eventos SQS.

---

## Flujo de notificación

```
SQS: BookingConfirmed
    → notification-service consume evento
        → extrae userId, bookingId, checkIn, checkOut, totalAmount
            → envía email de confirmación (simulado)
```

---

## Decisiones de diseño

**Sin base de datos** — `notification-service` no persiste nada. Su única responsabilidad es enviar notificaciones. Si se necesitara historial de envíos en el futuro, se agregaría una DB en ese momento.

**Sin endpoints de escritura** — Los eventos llegan via SQS, no via HTTP. No tiene sentido exponer un endpoint para crear notificaciones manualmente.

**Notificación simulada** — El método `sendBookingConfirmation` simula el envío via logs. En producción aquí se integraría AWS SES, SendGrid, Firebase Cloud Messaging u otro proveedor. La arquitectura ya está lista para ese cambio sin modificar el listener.

**Separación `listener` / `service`** — El listener solo parsea el evento y extrae los datos. El `NotificationService` maneja el envío. Esta separación facilita testear el servicio de notificación de forma independiente.

---

## Integraciones futuras

El método `sendBookingConfirmation` en `NotificationService` es el único punto de cambio para integrar un proveedor real:

```java
// Simulado (actual)
log.info("📧 Sending email to userId: {}", userId);

// Con AWS SES (futuro)
sesClient.sendEmail(SendEmailRequest.builder()
    .destination(...)
    .message(...)
    .build());

// Con SendGrid (futuro)
sendGrid.api(new Mail(...));
```

---

## Stack

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 17 |
| Framework | Spring Boot 3.x |
| Mensajería | Amazon SQS |
| Contenedor | Docker |
| Registry | Amazon ECR Public |
| CI/CD | GitHub Actions |

---

## Correr localmente

**1. Configurar credenciales AWS en `.env`:**
```
AWS_ACCESS_KEY_ID=your_access_key
AWS_SECRET_ACCESS_KEY=your_secret_key
```

**2. Correr la aplicación:**
```bash
./mvnw spring-boot:run
```

La app corre en `http://localhost:8084`.

---

## CI/CD

Cada push a `main` ejecuta el pipeline en GitHub Actions que:
1. Compila el proyecto con Maven
2. Construye la imagen Docker
3. Hace push a Amazon ECR Public con dos tags: `latest` y el SHA del commit
