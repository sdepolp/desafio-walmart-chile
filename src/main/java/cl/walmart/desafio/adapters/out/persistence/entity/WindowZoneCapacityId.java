package cl.walmart.desafio.adapters.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class WindowZoneCapacityId implements Serializable {

    @Column(name = "window_id", nullable = false, length = 64)
    private String windowId;

    @Column(name = "zone_id", nullable = false, length = 64)
    private String zoneId;

    protected WindowZoneCapacityId() {}

    public WindowZoneCapacityId(String windowId, String zoneId) {
        this.windowId = windowId;
        this.zoneId = zoneId;
    }

    public String getWindowId() { return windowId; }
    public String getZoneId() { return zoneId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WindowZoneCapacityId that)) return false;
        return Objects.equals(windowId, that.windowId) && Objects.equals(zoneId, that.zoneId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(windowId, zoneId);
    }
}
