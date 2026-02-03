package cl.walmart.desafio.it;

import cl.walmart.desafio.application.dto.ReserveWindowRequest;
import cl.walmart.desafio.application.usecase.ReserveWindowUseCase;
import cl.walmart.desafio.domain.exception.WindowSoldOutException;
import cl.walmart.desafio.adapters.out.persistence.repository.SpringReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test focused on concurrency: multiple threads reserve the same window simultaneously.
 *
 * Expected behavior:
 * - No over-reservations beyond zone capacity AND total capacity.
 * - For window w-20260128-1, zone-1 capacity is 3 (total is 5).
 * - When reserving from zone-1, only 3 should succeed.
 */
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // ensures seed resets across runs
class ReservationConcurrencyIT {

    @Autowired private ReserveWindowUseCase reserveWindowUseCase;
    @Autowired private SpringReservationRepository reservationRepository;

    @Test
    void shouldNotOverReserveUnderConcurrency() throws Exception {
        String windowId = "w-20260128-1";
        String commune = "Ñuñoa"; // zone-1

        int attempts = 10;

        ExecutorService pool = Executors.newFixedThreadPool(10);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(attempts);

        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger soldOut = new AtomicInteger(0);
        List<Throwable> unexpected = new CopyOnWriteArrayList<>();

        for (int i = 0; i < attempts; i++) {
            final int idx = i;
            pool.submit(() -> {
                try {
                    start.await(5, TimeUnit.SECONDS);
                    ReserveWindowRequest req = new ReserveWindowRequest(
                            "ORD-CONC-" + idx,
                            new ReserveWindowRequest.AddressDto(commune, "Calle X", String.valueOf(100 + idx), null),
                            windowId
                    );
                    reserveWindowUseCase.reserve(req);
                    success.incrementAndGet();
                } catch (WindowSoldOutException ex) {
                    soldOut.incrementAndGet();
                } catch (Throwable t) {
                    unexpected.add(t);
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        assertTrue(done.await(15, TimeUnit.SECONDS), "Threads did not finish on time");
        pool.shutdownNow();

        if (!unexpected.isEmpty()) {
            unexpected.get(0).printStackTrace();
        }
        assertTrue(unexpected.isEmpty(), "There were unexpected exceptions");

        assertEquals(3, success.get(), "Should only allow up to zone capacity (3)");
        assertEquals(attempts - 3, soldOut.get(), "Remaining attempts should fail as sold out");
        assertEquals(3, reservationRepository.count(), "DB must contain exactly 3 reservations");
    }
}
