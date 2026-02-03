package cl.walmart.desafio.adapters.out.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "dispatch_window")
public class DispatchWindowEntity {

    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime start;

    @Column(name = "end_time", nullable = false)
    private LocalTime end;

    @Column(name = "price_cents", nullable = false)
    private long priceCents;

    @Column(name = "currency", nullable = false, length = 8)
    private String currency;

    @Column(name = "capacity_total", nullable = false)
    private int capacityTotal;

    @Column(name = "remaining_total", nullable = false)
    private int remainingTotal;

    protected DispatchWindowEntity() { }

    public DispatchWindowEntity(String id, LocalDate date, LocalTime start, LocalTime end,
                                long priceCents, String currency, int capacityTotal, int remainingTotal) {
        this.id = id;
        this.date = date;
        this.start = start;
        this.end = end;
        this.priceCents = priceCents;
        this.currency = currency;
        this.capacityTotal = capacityTotal;
        this.remainingTotal = remainingTotal;
    }

    public String getId() { return id; }
    public LocalDate getDate() { return date; }
    public LocalTime getStart() { return start; }
    public LocalTime getEnd() { return end; }
    public long getPriceCents() { return priceCents; }
    public String getCurrency() { return currency; }
    public int getCapacityTotal() { return capacityTotal; }
    public int getRemainingTotal() { return remainingTotal; }

    public void setRemainingTotal(int remainingTotal) { this.remainingTotal = remainingTotal; }
}
