package cl.walmart.desafio.adapters.out.persistence;

import cl.walmart.desafio.adapters.out.persistence.entity.DispatchWindowEntity;
import cl.walmart.desafio.adapters.out.persistence.entity.WindowZoneCapacityEntity;
import cl.walmart.desafio.adapters.out.persistence.repository.SpringDispatchWindowRepository;
import cl.walmart.desafio.adapters.out.persistence.repository.SpringWindowZoneCapacityRepository;
import cl.walmart.desafio.domain.model.DispatchWindowAvailability;
import cl.walmart.desafio.domain.model.Money;
import cl.walmart.desafio.domain.model.WindowId;
import cl.walmart.desafio.domain.model.ZoneId;
import cl.walmart.desafio.domain.ports.DispatchWindowCapacityPort;
import cl.walmart.desafio.domain.ports.DispatchWindowQueryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DispatchWindowPersistenceAdapter implements DispatchWindowQueryPort, DispatchWindowCapacityPort {

    private final SpringDispatchWindowRepository windowRepository;
    private final SpringWindowZoneCapacityRepository zoneCapacityRepository;

    public DispatchWindowPersistenceAdapter(SpringDispatchWindowRepository windowRepository,
                                           SpringWindowZoneCapacityRepository zoneCapacityRepository) {
        this.windowRepository = windowRepository;
        this.zoneCapacityRepository = zoneCapacityRepository;
    }

    @Override
    public List<DispatchWindowAvailability> findAvailableForZone(ZoneId zoneId, LocalDate from, LocalDate to) {
        List<DispatchWindowEntity> windows = windowRepository.findInRange(from, to);

        Map<String, Integer> remainingByWindowIdForZone = new HashMap<>();
        for (DispatchWindowEntity w : windows) {
            List<WindowZoneCapacityEntity> zoneCaps = zoneCapacityRepository.findByWindowAndZone(w.getId(), zoneId.value());
            int remaining = zoneCaps.isEmpty() ? 0 : zoneCaps.get(0).getRemaining();
            remainingByWindowIdForZone.put(w.getId(), remaining);
        }

        return windows.stream()
                .map(w -> new DispatchWindowAvailability(
                        new WindowId(w.getId()),
                        w.getDate(),
                        w.getStart(),
                        w.getEnd(),
                        new Money(w.getPriceCents(), w.getCurrency()),
                        w.getRemainingTotal(),
                        remainingByWindowIdForZone.getOrDefault(w.getId(), 0)
                ))
                .filter(a -> a.remainingTotal() > 0 && a.remainingForZone() > 0)
                .toList();
    }

    @Override
    @Transactional
    public boolean tryConsume(WindowId windowId, ZoneId zoneId) {
        int updatedTotal = windowRepository.tryConsumeTotal(windowId.value());
        if (updatedTotal == 0) {
            return false;
        }

        int updatedZone = zoneCapacityRepository.tryConsumeZone(windowId.value(), zoneId.value());
        if (updatedZone == 0) {
            // Compensate because we decided not to throw (so transaction would not rollback).
            windowRepository.releaseTotal(windowId.value());
            return false;
        }

        return true;
    }

    @Override
    @Transactional
    public void release(WindowId windowId, ZoneId zoneId) {
        windowRepository.releaseTotal(windowId.value());
        zoneCapacityRepository.releaseZone(windowId.value(), zoneId.value());
    }
}
