package cl.walmart.desafio.domain.model;

import java.util.Objects;

public final class ZoneId {
    private final String value;

    public ZoneId(String value) {
        this.value = Objects.requireNonNull(value, "zoneId must not be null").trim();
        if (this.value.isEmpty()) throw new IllegalArgumentException("zoneId must not be blank");
    }

    public String value() { return value; }

    @Override public String toString() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ZoneId zoneId)) return false;
        return value.equals(zoneId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
