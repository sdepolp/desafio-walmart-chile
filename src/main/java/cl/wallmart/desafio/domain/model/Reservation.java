package cl.wallmart.desafio.domain.model;

import java.time.Instant;
import java.util.Objects;

public final class Reservation {
    private final ReservationId id;
    private final String orderId;
    private final WindowId windowId;
    private final ZoneId zoneId;
    private final Address address;
    private final Instant createdAt;

    public Reservation(ReservationId id, String orderId, WindowId windowId, ZoneId zoneId, Address address, Instant createdAt) {
        this.id = Objects.requireNonNull(id);
        this.orderId = Objects.requireNonNull(orderId, "orderId must not be null").trim();
        if (this.orderId.isEmpty()) throw new IllegalArgumentException("orderId must not be blank");
        this.windowId = Objects.requireNonNull(windowId);
        this.zoneId = Objects.requireNonNull(zoneId);
        this.address = Objects.requireNonNull(address);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public ReservationId id() { return id; }
    public String orderId() { return orderId; }
    public WindowId windowId() { return windowId; }
    public ZoneId zoneId() { return zoneId; }
    public Address address() { return address; }
    public Instant createdAt() { return createdAt; }
}
