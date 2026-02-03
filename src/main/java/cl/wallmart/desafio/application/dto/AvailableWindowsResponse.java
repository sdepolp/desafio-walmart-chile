package cl.wallmart.desafio.application.dto;

import java.util.List;

public record AvailableWindowsResponse(
        String resolvedZoneId,
        List<WindowItem> windows
) {
    public record WindowItem(
            String windowId,
            String date,
            String start,
            String end,
            long priceCents,
            String currency,
            int remainingTotal,
            int remainingForZone
    ) {}
}
