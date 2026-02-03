package cl.wallmart.desafio.application.service;

import cl.wallmart.desafio.application.dto.ReserveWindowRequest;
import cl.wallmart.desafio.application.dto.ReserveWindowResponse;
import cl.wallmart.desafio.application.usecase.ReserveWindowUseCase;
import cl.wallmart.desafio.domain.exception.DuplicateReservationException;
import cl.wallmart.desafio.domain.exception.WindowSoldOutException;
import cl.wallmart.desafio.domain.exception.ZoneNotCoveredException;
import cl.wallmart.desafio.domain.model.*;
import cl.wallmart.desafio.domain.ports.DispatchWindowCapacityPort;
import cl.wallmart.desafio.domain.ports.ReservationPort;
import cl.wallmart.desafio.domain.ports.ZoneResolverPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class ReserveWindowService implements ReserveWindowUseCase {

    private final ZoneResolverPort zoneResolverPort;
    private final DispatchWindowCapacityPort capacityPort;
    private final ReservationPort reservationPort;

    public ReserveWindowService(ZoneResolverPort zoneResolverPort,
                                DispatchWindowCapacityPort capacityPort,
                                ReservationPort reservationPort) {
        this.zoneResolverPort = zoneResolverPort;
        this.capacityPort = capacityPort;
        this.reservationPort = reservationPort;
    }

    @Override
    @Transactional
    public ReserveWindowResponse reserve(ReserveWindowRequest request) {
        reservationPort.findByOrderId(request.orderId()).ifPresent(r -> {
            throw new DuplicateReservationException("Order already has a reservation: " + request.orderId());
        });

        Address address = new Address(
                request.address().commune(),
                request.address().street(),
                request.address().number(),
                request.address().additionalInfo()
        );

        ZoneId zoneId = zoneResolverPort.resolveZone(address)
                .orElseThrow(() -> new ZoneNotCoveredException("No coverage for commune: " + request.address().commune()));

        WindowId windowId = new WindowId(request.windowId());

        boolean consumed = capacityPort.tryConsume(windowId, zoneId);
        if (!consumed) {
            throw new WindowSoldOutException("Window sold out for zone " + zoneId.value() + " (or total capacity exhausted).");
        }

        Reservation reservation = new Reservation(
                ReservationId.newId(),
                request.orderId(),
                windowId,
                zoneId,
                address,
                Instant.now()
        );

        Reservation saved = reservationPort.save(reservation);

        return new ReserveWindowResponse(saved.id().value(), saved.orderId(), saved.windowId().value(), saved.zoneId().value());
    }
}
