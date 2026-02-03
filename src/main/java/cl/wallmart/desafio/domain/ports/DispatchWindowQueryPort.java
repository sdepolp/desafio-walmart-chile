package cl.wallmart.desafio.domain.ports;

import cl.wallmart.desafio.domain.model.DispatchWindowAvailability;
import cl.wallmart.desafio.domain.model.ZoneId;

import java.time.LocalDate;
import java.util.List;

public interface DispatchWindowQueryPort {
    List<DispatchWindowAvailability> findAvailableForZone(ZoneId zoneId, LocalDate from, LocalDate to);
}
