package cl.walmart.desafio.adapters.out.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "reservation",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_reservation_order", columnNames = {"order_id"})
       })
public class ReservationEntity {

    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "order_id", nullable = false, length = 128)
    private String orderId;

    @Column(name = "window_id", nullable = false, length = 64)
    private String windowId;

    @Column(name = "zone_id", nullable = false, length = 64)
    private String zoneId;

    @Column(name = "commune", nullable = false, length = 128)
    private String commune;

    @Column(name = "street", length = 256)
    private String street;

    @Column(name = "street_number", length = 64)
    private String number;

    @Column(name = "additional_info", length = 256)
    private String additionalInfo;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected ReservationEntity() { }

    public ReservationEntity(String id, String orderId, String windowId, String zoneId,
                             String commune, String street, String number, String additionalInfo,
                             Instant createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.windowId = windowId;
        this.zoneId = zoneId;
        this.commune = commune;
        this.street = street;
        this.number = number;
        this.additionalInfo = additionalInfo;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public String getOrderId() { return orderId; }
    public String getWindowId() { return windowId; }
    public String getZoneId() { return zoneId; }
    public String getCommune() { return commune; }
    public String getStreet() { return street; }
    public String getNumber() { return number; }
    public String getAdditionalInfo() { return additionalInfo; }
    public Instant getCreatedAt() { return createdAt; }
}
