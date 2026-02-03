package cl.walmart.desafio.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * A window plus current remaining capacity for total and for a specific zone.
 */
public record DispatchWindowAvailability(
        WindowId windowId,
        LocalDate date,
        LocalTime start,
        LocalTime end,
        Money price,
        int remainingTotal,
        int remainingForZone
) {}
