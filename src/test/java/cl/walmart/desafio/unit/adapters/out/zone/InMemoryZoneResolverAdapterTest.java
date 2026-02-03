package cl.walmart.desafio.unit.adapters.out.zone;

import cl.walmart.desafio.adapters.out.zone.InMemoryZoneResolverAdapter;
import cl.walmart.desafio.domain.model.Address;
import cl.walmart.desafio.domain.model.ZoneId;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryZoneResolverAdapterTest {

    @Test
    void shouldResolveKnownCommune() {
        InMemoryZoneResolverAdapter adapter = new InMemoryZoneResolverAdapter();
        Optional<ZoneId> z = adapter.resolveZone(new Address("Ñuñoa", null, null, null));
        assertTrue(z.isPresent());
        assertEquals("zone-1", z.get().value());
    }

    @Test
    void shouldReturnEmptyForUnknownCommune() {
        InMemoryZoneResolverAdapter adapter = new InMemoryZoneResolverAdapter();
        Optional<ZoneId> z = adapter.resolveZone(new Address("Comuna Inventada", null, null, null));
        assertTrue(z.isEmpty());
    }

    @Test
    void shouldBeCaseInsensitiveAndTrim() {
        InMemoryZoneResolverAdapter adapter = new InMemoryZoneResolverAdapter();
        Optional<ZoneId> z = adapter.resolveZone(new Address("  ÑUÑOA ", null, null, null));
        assertTrue(z.isPresent());
        assertEquals("zone-1", z.get().value());
    }
}
