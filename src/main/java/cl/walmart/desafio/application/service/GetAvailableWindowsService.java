package cl.walmart.desafio.application.service;

import cl.walmart.desafio.application.dto.AvailableWindowsResponse;
import cl.walmart.desafio.application.usecase.GetAvailableWindowsUseCase;
import cl.walmart.desafio.domain.exception.ZoneNotCoveredException;
import cl.walmart.desafio.domain.model.Address;
import cl.walmart.desafio.domain.model.DispatchWindowAvailability;
import cl.walmart.desafio.domain.model.ZoneId;
import cl.walmart.desafio.domain.ports.DispatchWindowQueryPort;
import cl.walmart.desafio.domain.ports.ZoneResolverPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GetAvailableWindowsService implements GetAvailableWindowsUseCase {

    private final ZoneResolverPort zoneResolverPort;
    private final DispatchWindowQueryPort dispatchWindowQueryPort;

    public GetAvailableWindowsService(ZoneResolverPort zoneResolverPort, DispatchWindowQueryPort dispatchWindowQueryPort) {
        this.zoneResolverPort = zoneResolverPort;
        this.dispatchWindowQueryPort = dispatchWindowQueryPort;
    }

    @Override
    public AvailableWindowsResponse getAvailableWindows(String commune, LocalDate from, LocalDate to) {
        Address address = new Address(commune, null, null, null);
        ZoneId zoneId = zoneResolverPort.resolveZone(address)
                .orElseThrow(() -> new ZoneNotCoveredException("No coverage for commune: " + commune));

        List<DispatchWindowAvailability> availabilities = dispatchWindowQueryPort.findAvailableForZone(zoneId, from, to);

        List<AvailableWindowsResponse.WindowItem> windows = availabilities.stream()
                .map(w -> new AvailableWindowsResponse.WindowItem(
                        w.windowId().value(),
                        w.date().toString(),
                        w.start().toString(),
                        w.end().toString(),
                        w.price().amountCents(),
                        w.price().currency(),
                        w.remainingTotal(),
                        w.remainingForZone()
                ))
                .toList();

        return new AvailableWindowsResponse(zoneId.value(), windows);
    }
}
