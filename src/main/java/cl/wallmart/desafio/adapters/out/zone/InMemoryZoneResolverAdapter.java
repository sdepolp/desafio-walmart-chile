package cl.wallmart.desafio.adapters.out.zone;

import cl.wallmart.desafio.domain.model.Address;
import cl.wallmart.desafio.domain.model.ZoneId;
import cl.wallmart.desafio.domain.ports.ZoneResolverPort;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Simplified zone resolver for the challenge: maps commune -> zoneId.
 * You can evolve this to polygons/geocoding in production.
 */
@Component
public class InMemoryZoneResolverAdapter implements ZoneResolverPort {

    private final Map<String, ZoneId> communeToZone = new HashMap<>();

    public InMemoryZoneResolverAdapter() {
        // Defaults (edit as you want). Document this mapping in your README.
        // zone-1: central / near
        add("Santiago", "zone-1");
        add("Providencia", "zone-1");
        add("Ñuñoa", "zone-1");

        add("Las Condes", "zone-2");
        add("Vitacura", "zone-2");

        add("La Florida", "zone-3");
        add("San Joaquín", "zone-3");
    }

    private void add(String commune, String zoneId) {
        communeToZone.put(normalize(commune), new ZoneId(zoneId));
    }

    @Override
    public Optional<ZoneId> resolveZone(Address address) {
        if (address == null || address.commune() == null) return Optional.empty();
        return Optional.ofNullable(communeToZone.get(normalize(address.commune())));
    }

    private String normalize(String s) {
        return s.trim().toLowerCase(Locale.ROOT);
    }
}
