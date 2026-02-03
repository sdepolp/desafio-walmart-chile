package cl.walmart.desafio.domain.model;

/**
 * Minimal money representation.
 * Using integer cents avoids floating point issues.
 */
public record Money(long amountCents, String currency) {
    public Money {
        if (amountCents < 0) throw new IllegalArgumentException("amountCents must be >= 0");
        if (currency == null || currency.isBlank()) throw new IllegalArgumentException("currency must not be blank");
    }

    public static Money clp(long amountCents) {
        return new Money(amountCents, "CLP");
    }
}
