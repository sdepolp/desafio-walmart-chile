package cl.wallmart.desafio.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReserveWindowRequest(
        @NotBlank String orderId,
        @NotNull AddressDto address,
        @NotBlank String windowId
) {
    public record AddressDto(
            @NotBlank String commune,
            String street,
            String number,
            String additionalInfo
    ) {}
}
