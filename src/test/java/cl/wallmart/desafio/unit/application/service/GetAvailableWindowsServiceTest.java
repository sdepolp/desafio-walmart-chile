package cl.wallmart.desafio.unit.application.service;

import cl.wallmart.desafio.application.dto.AvailableWindowsResponse;
import cl.wallmart.desafio.application.service.GetAvailableWindowsService;
import cl.wallmart.desafio.domain.exception.ZoneNotCoveredException;
import cl.wallmart.desafio.domain.model.DispatchWindowAvailability;
import cl.wallmart.desafio.domain.model.Money;
import cl.wallmart.desafio.domain.model.WindowId;
import cl.wallmart.desafio.domain.model.ZoneId;
import cl.wallmart.desafio.domain.ports.DispatchWindowQueryPort;
import cl.wallmart.desafio.domain.ports.ZoneResolverPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAvailableWindowsServiceTest {

    @Test
    void shouldReturnWindowsForCoveredCommune() {
        ZoneResolverPort zoneResolverPort = mock(ZoneResolverPort.class);
        DispatchWindowQueryPort queryPort = mock(DispatchWindowQueryPort.class);

        when(zoneResolverPort.resolveZone(any())).thenReturn(Optional.of(new ZoneId("zone-1")));

        when(queryPort.findAvailableForZone(eq(new ZoneId("zone-1")), any(), any()))
                .thenReturn(List.of(
                        new DispatchWindowAvailability(
                                new WindowId("w-1"),
                                LocalDate.parse("2026-01-28"),
                                LocalTime.parse("09:00"),
                                LocalTime.parse("11:00"),
                                Money.clp(1990),
                                5,
                                3
                        )
                ));

        GetAvailableWindowsService svc = new GetAvailableWindowsService(zoneResolverPort, queryPort);

        AvailableWindowsResponse resp = svc.getAvailableWindows(
                "Ñuñoa",
                LocalDate.parse("2026-01-28"),
                LocalDate.parse("2026-01-29")
        );

        assertEquals("zone-1", resp.resolvedZoneId());
        assertEquals(1, resp.windows().size());
        assertEquals("w-1", resp.windows().get(0).windowId());

        verify(zoneResolverPort, times(1)).resolveZone(any());
        verify(queryPort, times(1)).findAvailableForZone(eq(new ZoneId("zone-1")), any(), any());
        verifyNoMoreInteractions(queryPort);
    }

    @Test
    void shouldThrowWhenCommuneNotCovered() {
        ZoneResolverPort zoneResolverPort = mock(ZoneResolverPort.class);
        DispatchWindowQueryPort queryPort = mock(DispatchWindowQueryPort.class);

        when(zoneResolverPort.resolveZone(any())).thenReturn(Optional.empty());

        GetAvailableWindowsService svc = new GetAvailableWindowsService(zoneResolverPort, queryPort);

        assertThrows(ZoneNotCoveredException.class, () ->
                svc.getAvailableWindows("Comuna Inventada", LocalDate.now(), LocalDate.now().plusDays(1))
        );

        verify(zoneResolverPort, times(1)).resolveZone(any());
        verifyNoInteractions(queryPort);
    }
}
