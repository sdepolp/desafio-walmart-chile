package cl.wallmart.desafio.adapters.out.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dispatch_window_zone_capacity")
public class WindowZoneCapacityEntity {

    @EmbeddedId
    private WindowZoneCapacityId id;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "remaining", nullable = false)
    private int remaining;

    protected WindowZoneCapacityEntity() { }

    public WindowZoneCapacityEntity(WindowZoneCapacityId id, int capacity, int remaining) {
        this.id = id;
        this.capacity = capacity;
        this.remaining = remaining;
    }

    public WindowZoneCapacityId getId() { return id; }
    public int getCapacity() { return capacity; }
    public int getRemaining() { return remaining; }

    public void setRemaining(int remaining) { this.remaining = remaining; }
}
