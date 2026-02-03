package cl.walmart.desafio.domain.ports;

import cl.walmart.desafio.domain.model.Address;
import cl.walmart.desafio.domain.model.ZoneId;

import java.util.Optional;

public interface ZoneResolverPort {
    Optional<ZoneId> resolveZone(Address address);
}
