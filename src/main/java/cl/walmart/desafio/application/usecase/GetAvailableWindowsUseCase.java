package cl.walmart.desafio.application.usecase;

import cl.walmart.desafio.application.dto.AvailableWindowsResponse;

import java.time.LocalDate;

public interface GetAvailableWindowsUseCase {
    AvailableWindowsResponse getAvailableWindows(String commune, LocalDate from, LocalDate to);
}
