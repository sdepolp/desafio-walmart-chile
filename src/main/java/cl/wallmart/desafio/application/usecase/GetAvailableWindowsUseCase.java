package cl.wallmart.desafio.application.usecase;

import cl.wallmart.desafio.application.dto.AvailableWindowsResponse;

import java.time.LocalDate;

public interface GetAvailableWindowsUseCase {
    AvailableWindowsResponse getAvailableWindows(String commune, LocalDate from, LocalDate to);
}
