package cl.walmart.desafio.domain.model;

import java.util.Objects;

public final class WindowId {
    private final String value;

    public WindowId(String value) {
        this.value = Objects.requireNonNull(value, "windowId must not be null").trim();
        if (this.value.isEmpty()) throw new IllegalArgumentException("windowId must not be blank");
    }

    public String value() { return value; }

    @Override public String toString() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WindowId windowId)) return false;
        return value.equals(windowId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
