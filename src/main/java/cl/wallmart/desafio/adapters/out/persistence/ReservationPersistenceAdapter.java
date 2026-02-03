package cl.wallmart.desafio.adapters.out.persistence;

import cl.wallmart.desafio.adapters.out.persistence.entity.ReservationEntity;
import cl.wallmart.desafio.adapters.out.persistence.repository.SpringReservationRepository;
import cl.wallmart.desafio.domain.model.*;
import cl.wallmart.desafio.domain.ports.ReservationPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReservationPersistenceAdapter implements ReservationPort {

    private final SpringReservationRepository repository;

    public ReservationPersistenceAdapter(SpringReservationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Reservation save(Reservation reservation) {
        ReservationEntity entity = new ReservationEntity(
                reservation.id().value(),
                reservation.orderId(),
                reservation.windowId().value(),
                reservation.zoneId().value(),
                reservation.address().commune(),
                reservation.address().street(),
                reservation.address().number(),
                reservation.address().additionalInfo(),
                reservation.createdAt()
        );

        repository.save(entity);
        return reservation;
    }

    @Override
    public Optional<Reservation> findByOrderId(String orderId) {
        return repository.findByOrderId(orderId).map(e ->
                new Reservation(
                        ReservationId.of(e.getId()),
                        e.getOrderId(),
                        new WindowId(e.getWindowId()),
                        new ZoneId(e.getZoneId()),
                        new Address(e.getCommune(), e.getStreet(), e.getNumber(), e.getAdditionalInfo()),
                        e.getCreatedAt()
                )
        );
    }
}
