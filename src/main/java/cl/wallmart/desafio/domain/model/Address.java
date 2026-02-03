package cl.wallmart.desafio.domain.model;

import java.util.Objects;

/**
 * Minimal address model for the challenge.
 * In a real system you would likely have: region/city, geocoding, normalized street, etc.
 */
public final class Address {
    private final String commune;
    private final String street;
    private final String number;
    private final String additionalInfo;

    public Address(String commune, String street, String number, String additionalInfo) {
        this.commune = normalize(commune);
        this.street = normalize(street);
        this.number = normalize(number);
        this.additionalInfo = normalize(additionalInfo);
    }

    public String commune() { return commune; }
    public String street() { return street; }
    public String number() { return number; }
    public String additionalInfo() { return additionalInfo; }

    private static String normalize(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address that)) return false;
        return Objects.equals(commune, that.commune)
                && Objects.equals(street, that.street)
                && Objects.equals(number, that.number)
                && Objects.equals(additionalInfo, that.additionalInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commune, street, number, additionalInfo);
    }

    @Override
    public String toString() {
        return "Address{" +
                "commune='" + commune + '\'' +
                ", street='" + street + '\'' +
                ", number='" + number + '\'' +
                ", additionalInfo='" + additionalInfo + '\'' +
                '}';
    }
}
