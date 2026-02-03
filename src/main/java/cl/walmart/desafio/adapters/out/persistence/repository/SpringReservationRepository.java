package cl.walmart.desafio.adapters.out.persistence.repository;

import cl.walmart.desafio.adapters.out.persistence.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringReservationRepository extends JpaRepository<ReservationEntity, String> {
    Optional<ReservationEntity> findByOrderId(String orderId);
}
