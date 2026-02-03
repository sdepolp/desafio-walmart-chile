package cl.walmart.desafio.adapters.in.rest;

import cl.walmart.desafio.application.dto.AvailableWindowsResponse;
import cl.walmart.desafio.application.usecase.GetAvailableWindowsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(
        name = "Dispatch Windows",
        description = "Consulta de ventanas de despacho disponibles según cobertura geográfica."
)
@Validated
@RestController
@RequestMapping("/api/windows")
public class DispatchWindowController {

    private final GetAvailableWindowsUseCase useCase;

    public DispatchWindowController(GetAvailableWindowsUseCase useCase) {
        this.useCase = useCase;
    }

    @Operation(
            summary = "Listar ventanas disponibles para una comuna",
            description = "Resuelve la zona a partir de la comuna y devuelve ventanas con capacidad disponible (total y por zona)."
    )
    @GetMapping
    public AvailableWindowsResponse getAvailable(
            @Parameter(
                    description = "Comuna para resolver zona (ej: Ñuñoa, Providencia, Las Condes).",
                    required = true,
                    example = "Ñuñoa"
            )
            @RequestParam @NotBlank String commune,

            @Parameter(description = "Fecha inicio (inclusive). ISO yyyy-MM-dd.", example = "2026-01-28")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @Parameter(description = "Fecha fin (inclusive). ISO yyyy-MM-dd.", example = "2026-01-29")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ){
    LocalDate f = (from != null) ? from : LocalDate.now();
        LocalDate t = (to != null) ? to : f.plusDays(7);
        return useCase.getAvailableWindows(commune, f, t);
    }
}
