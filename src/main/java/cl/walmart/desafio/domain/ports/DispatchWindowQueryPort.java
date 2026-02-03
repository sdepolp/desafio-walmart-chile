package cl.walmart.desafio.domain.ports;

import cl.walmart.desafio.domain.model.DispatchWindowAvailability;
import cl.walmart.desafio.domain.model.ZoneId;

import java.time.LocalDate;
import java.util.List;

public interface DispatchWindowQueryPort {
    List<DispatchWindowAvailability> findAvailableForZone(ZoneId zoneId, LocalDate from, LocalDate to);
}
