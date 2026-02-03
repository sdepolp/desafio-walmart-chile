package cl.wallmart.desafio.adapters.in.rest;

import cl.wallmart.desafio.application.dto.ReserveWindowRequest;
import cl.wallmart.desafio.application.dto.ReserveWindowResponse;
import cl.wallmart.desafio.application.usecase.ReserveWindowUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Reservations",
        description = "Reserva de ventanas de despacho respetando capacidad total y por zona."
)
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReserveWindowUseCase useCase;

    public ReservationController(ReserveWindowUseCase useCase) {
        this.useCase = useCase;
    }

    @Operation(
            summary = "Reservar una ventana de despacho",
            description = "Crea una reserva consumiendo 1 cupo del total de la ventana y 1 cupo de la zona resuelta por la comuna."
    )
    @ApiResponse(responseCode = "201", description = "Reserva creada")
    @ApiResponse(responseCode = "409", description = "Ventana agotada (por total o por zona) o reserva duplicada (orderId ya reservado)")
    @ApiResponse(responseCode = "422", description = "Comuna sin cobertura")
    @ApiResponse(responseCode = "400", description = "Validación de datos fallida")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReserveWindowResponse reserve(
            @RequestBody(
                    required = true,
                    description = "Datos de la reserva",
                    content = @Content(
                            schema = @Schema(implementation = ReserveWindowRequest.class),
                            examples = @ExampleObject(
                                    name = "Reserva",
                                    value = """
                                            {
                                              "orderId": "ORD-1",
                                              "address": {
                                                "commune": "Ñuñoa",
                                                "street": "Calle X",
                                                "number": "123",
                                                "additionalInfo": "Depto 12"
                                              },
                                              "windowId": "w-20260128-1"
                                            }
                                            """
                            )
                    )
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody ReserveWindowRequest request
    ) {
        return useCase.reserve(request);
    }
}
