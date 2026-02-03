package cl.walmart.desafio.domain.ports;

import cl.walmart.desafio.domain.model.WindowId;
import cl.walmart.desafio.domain.model.ZoneId;

/**
 * Capacity changes (reservation) must be concurrency-safe.
 */
public interface DispatchWindowCapacityPort {
    /**
     * Tries to consume 1 capacity from the window total and from the zone capacity.
     * Returns true if successful, false if sold out (either total or zone).
     */
    boolean tryConsume(WindowId windowId, ZoneId zoneId);

    /**
     * Optional compensating operation (used for cancel flows).
     */
    void release(WindowId windowId, ZoneId zoneId);
}
