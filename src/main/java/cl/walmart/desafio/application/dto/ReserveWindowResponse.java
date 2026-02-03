package cl.walmart.desafio.application.dto;

public record ReserveWindowResponse(
        String reservationId,
        String orderId,
        String windowId,
        String zoneId
) {}
