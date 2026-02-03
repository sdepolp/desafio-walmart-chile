package cl.walmart.desafio.it;

import cl.walmart.desafio.application.dto.ReserveWindowRequest;
import cl.walmart.desafio.adapters.out.persistence.repository.SpringReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies POST /api/reservations endpoint end-to-end.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationApiIT {

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private SpringReservationRepository reservationRepository;

    @BeforeEach
    void cleanReservations() {
        reservationRepository.deleteAll();
    }

    @Test
    void reserveShouldReturn201() {
        ReserveWindowRequest req = new ReserveWindowRequest(
                "ORD-HTTP-1",
                new ReserveWindowRequest.AddressDto("Ñuñoa", "Calle X", "123", null),
                "w-20260128-1"
        );

        ResponseEntity<String> res = restTemplate.postForEntity("/api/reservations", req, String.class);

        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        assertEquals(1, reservationRepository.count());
    }

    @Test
    void reserveShouldReturn409WhenZoneCapacitySoldOut() {
        // Exhaust zone-1 capacity for w-20260128-1 (3 slots)
        for (int i = 0; i < 3; i++) {
            ReserveWindowRequest req = new ReserveWindowRequest(
                    "ORD-HTTP-" + i,
                    new ReserveWindowRequest.AddressDto("Ñuñoa", null, null, null),
                    "w-20260128-1"
            );
            restTemplate.postForEntity("/api/reservations", req, String.class);
        }

        ReserveWindowRequest req4 = new ReserveWindowRequest(
                "ORD-HTTP-4",
                new ReserveWindowRequest.AddressDto("Ñuñoa", null, null, null),
                "w-20260128-1"
        );

        ResponseEntity<String> res4 = restTemplate.postForEntity("/api/reservations", req4, String.class);

        assertEquals(HttpStatus.CONFLICT, res4.getStatusCode());
        assertEquals(3, reservationRepository.count());
    }
}
