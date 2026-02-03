package cl.wallmart.desafio.application.usecase;

import cl.wallmart.desafio.application.dto.ReserveWindowRequest;
import cl.wallmart.desafio.application.dto.ReserveWindowResponse;

public interface ReserveWindowUseCase {
    ReserveWindowResponse reserve(ReserveWindowRequest request);
}
