package cl.wallmart.desafio.domain.exception;

public class DuplicateReservationException extends DomainException {
    public DuplicateReservationException(String message) { super(message); }
}
