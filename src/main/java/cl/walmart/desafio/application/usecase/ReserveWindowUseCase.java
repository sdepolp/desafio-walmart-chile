package cl.walmart.desafio.application.usecase;

import cl.walmart.desafio.application.dto.ReserveWindowRequest;
import cl.walmart.desafio.application.dto.ReserveWindowResponse;

public interface ReserveWindowUseCase {
    ReserveWindowResponse reserve(ReserveWindowRequest request);
}
