package cl.wallmart.desafio.it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies GET /api/windows endpoint end-to-end.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WindowsApiIT {

    @Autowired private TestRestTemplate restTemplate;

    @Test
    void shouldReturnWindowsForCommune() {
        ResponseEntity<String> res = restTemplate.getForEntity(
                "/api/windows?commune=Ñuñoa&from=2026-01-28&to=2026-01-29",
                String.class
        );

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertTrue(res.getBody().contains("zone-1"));
        assertTrue(res.getBody().contains("w-20260128-1"));
    }

    @Test
    void shouldReturn422WhenCommuneNotCovered() {
        ResponseEntity<String> res = restTemplate.getForEntity(
                "/api/windows?commune=ComunaInventada&from=2026-01-28&to=2026-01-29",
                String.class
        );

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, res.getStatusCode());
    }
}
