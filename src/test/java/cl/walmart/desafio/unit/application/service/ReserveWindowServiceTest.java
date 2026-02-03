package cl.walmart.desafio.unit.application.service;

import cl.walmart.desafio.application.dto.ReserveWindowRequest;
import cl.walmart.desafio.application.dto.ReserveWindowResponse;
import cl.walmart.desafio.application.service.ReserveWindowService;
import cl.walmart.desafio.domain.exception.DuplicateReservationException;
import cl.walmart.desafio.domain.exception.WindowSoldOutException;
import cl.walmart.desafio.domain.exception.ZoneNotCoveredException;
import cl.walmart.desafio.domain.model.*;
import cl.walmart.desafio.domain.model.*;
import cl.walmart.desafio.domain.ports.DispatchWindowCapacityPort;
import cl.walmart.desafio.domain.ports.ReservationPort;
import cl.walmart.desafio.domain.ports.ZoneResolverPort;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReserveWindowServiceTest {

    @Test
    void shouldReserveWhenCapacityAvailable() {
        ZoneResolverPort zoneResolverPort = mock(ZoneResolverPort.class);
        DispatchWindowCapacityPort capacityPort = mock(DispatchWindowCapacityPort.class);
        ReservationPort reservationPort = mock(ReservationPort.class);

        when(reservationPort.findByOrderId("ORD-1")).thenReturn(Optional.empty());
        when(zoneResolverPort.resolveZone(any())).thenReturn(Optional.of(new ZoneId("zone-1")));
        when(capacityPort.tryConsume(new WindowId("w-1"), new ZoneId("zone-1"))).thenReturn(true);

        // capture and return same reservation
        when(reservationPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ReserveWindowService svc = new ReserveWindowService(zoneResolverPort, capacityPort, reservationPort);

        ReserveWindowRequest req = new ReserveWindowRequest(
                "ORD-1",
                new ReserveWindowRequest.AddressDto("Ñuñoa", "Calle X", "123", null),
                "w-1"
        );

        ReserveWindowResponse resp = svc.reserve(req);

        assertNotNull(resp.reservationId());
        assertEquals("ORD-1", resp.orderId());
        assertEquals("w-1", resp.windowId());
        assertEquals("zone-1", resp.zoneId());

        verify(reservationPort).findByOrderId("ORD-1");
        verify(zoneResolverPort).resolveZone(any(Address.class));
        verify(capacityPort).tryConsume(new WindowId("w-1"), new ZoneId("zone-1"));
        verify(reservationPort).save(any(Reservation.class));
    }

    @Test
    void shouldThrowDuplicateReservation() {
        ZoneResolverPort zoneResolverPort = mock(ZoneResolverPort.class);
        DispatchWindowCapacityPort capacityPort = mock(DispatchWindowCapacityPort.class);
        ReservationPort reservationPort = mock(ReservationPort.class);

        Reservation existing = new Reservation(
                ReservationId.of("r-1"),
                "ORD-1",
                new WindowId("w-1"),
                new ZoneId("zone-1"),
                new Address("Ñuñoa", null, null, null),
                Instant.now()
        );

        when(reservationPort.findByOrderId("ORD-1")).thenReturn(Optional.of(existing));

        ReserveWindowService svc = new ReserveWindowService(zoneResolverPort, capacityPort, reservationPort);

        ReserveWindowRequest req = new ReserveWindowRequest(
                "ORD-1",
                new ReserveWindowRequest.AddressDto("Ñuñoa", null, null, null),
                "w-1"
        );

        assertThrows(DuplicateReservationException.class, () -> svc.reserve(req));

        verify(reservationPort).findByOrderId("ORD-1");
        verifyNoInteractions(zoneResolverPort, capacityPort);
        verify(reservationPort, never()).save(any());
    }

    @Test
    void shouldThrowWhenZoneNotCovered() {
        ZoneResolverPort zoneResolverPort = mock(ZoneResolverPort.class);
        DispatchWindowCapacityPort capacityPort = mock(DispatchWindowCapacityPort.class);
        ReservationPort reservationPort = mock(ReservationPort.class);

        when(reservationPort.findByOrderId("ORD-1")).thenReturn(Optional.empty());
        when(zoneResolverPort.resolveZone(any())).thenReturn(Optional.empty());

        ReserveWindowService svc = new ReserveWindowService(zoneResolverPort, capacityPort, reservationPort);

        ReserveWindowRequest req = new ReserveWindowRequest(
                "ORD-1",
                new ReserveWindowRequest.AddressDto("Comuna Inventada", null, null, null),
                "w-1"
        );

        assertThrows(ZoneNotCoveredException.class, () -> svc.reserve(req));

        verify(reservationPort).findByOrderId("ORD-1");
        verify(zoneResolverPort).resolveZone(any(Address.class));
        verifyNoInteractions(capacityPort);
        verify(reservationPort, never()).save(any());
    }

    @Test
    void shouldThrowSoldOutWhenCapacityNotAvailable() {
        ZoneResolverPort zoneResolverPort = mock(ZoneResolverPort.class);
        DispatchWindowCapacityPort capacityPort = mock(DispatchWindowCapacityPort.class);
        ReservationPort reservationPort = mock(ReservationPort.class);

        when(reservationPort.findByOrderId("ORD-1")).thenReturn(Optional.empty());
        when(zoneResolverPort.resolveZone(any())).thenReturn(Optional.of(new ZoneId("zone-1")));
        when(capacityPort.tryConsume(new WindowId("w-1"), new ZoneId("zone-1"))).thenReturn(false);

        ReserveWindowService svc = new ReserveWindowService(zoneResolverPort, capacityPort, reservationPort);

        ReserveWindowRequest req = new ReserveWindowRequest(
                "ORD-1",
                new ReserveWindowRequest.AddressDto("Ñuñoa", null, null, null),
                "w-1"
        );

        assertThrows(WindowSoldOutException.class, () -> svc.reserve(req));

        verify(reservationPort).findByOrderId("ORD-1");
        verify(zoneResolverPort).resolveZone(any(Address.class));
        verify(capacityPort).tryConsume(new WindowId("w-1"), new ZoneId("zone-1"));
        verify(reservationPort, never()).save(any());
    }
}
