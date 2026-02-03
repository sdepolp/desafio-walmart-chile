package cl.wallmart.desafio.domain.ports;

import cl.wallmart.desafio.domain.model.Address;
import cl.wallmart.desafio.domain.model.ZoneId;

import java.util.Optional;

public interface ZoneResolverPort {
    Optional<ZoneId> resolveZone(Address address);
}
