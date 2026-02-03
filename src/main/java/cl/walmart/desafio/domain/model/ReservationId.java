package cl.walmart.desafio.domain.model;

import java.util.Objects;
import java.util.UUID;

public final class ReservationId {
    private final String value;

    private ReservationId(String value) {
        this.value = Objects.requireNonNull(value, "reservationId must not be null").trim();
        if (this.value.isEmpty()) throw new IllegalArgumentException("reservationId must not be blank");
    }

    public static ReservationId newId() {
        return new ReservationId(UUID.randomUUID().toString());
    }

    public static ReservationId of(String value) {
        return new ReservationId(value);
    }

    public String value() { return value; }

    @Override public String toString() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationId that)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
