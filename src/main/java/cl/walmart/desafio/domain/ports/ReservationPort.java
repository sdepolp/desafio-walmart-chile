package cl.walmart.desafio.domain.ports;

import cl.walmart.desafio.domain.model.Reservation;

import java.util.Optional;

public interface ReservationPort {
    Reservation save(Reservation reservation);
    Optional<Reservation> findByOrderId(String orderId);
}
