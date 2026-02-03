package cl.wallmart.desafio.domain.ports;

import cl.wallmart.desafio.domain.model.Reservation;

import java.util.Optional;

public interface ReservationPort {
    Reservation save(Reservation reservation);
    Optional<Reservation> findByOrderId(String orderId);
}
