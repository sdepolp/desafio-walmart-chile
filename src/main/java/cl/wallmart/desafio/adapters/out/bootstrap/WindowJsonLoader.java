package cl.wallmart.desafio.adapters.out.bootstrap;

import cl.wallmart.desafio.adapters.out.persistence.entity.DispatchWindowEntity;
import cl.wallmart.desafio.adapters.out.persistence.entity.WindowZoneCapacityEntity;
import cl.wallmart.desafio.adapters.out.persistence.entity.WindowZoneCapacityId;
import cl.wallmart.desafio.adapters.out.persistence.repository.SpringDispatchWindowRepository;
import cl.wallmart.desafio.adapters.out.persistence.repository.SpringWindowZoneCapacityRepository;
import cl.wallmart.desafio.domain.model.Money;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * Loads initial windows from a JSON resource (like the one provided in the challenge).
 *
 * Place the file in: src/main/resources/windows.json (default) or configure:
 * desafio.seed.windows-json=classpath:/windows.json
 */
@Component
public class WindowJsonLoader implements ApplicationRunner {

    private final ObjectMapper objectMapper;
    private final SpringDispatchWindowRepository windowRepository;
    private final SpringWindowZoneCapacityRepository zoneCapacityRepository;

    @Value("${desafio.seed.windows-json:classpath:/windows.json}")
    private Resource windowsJson;

    public WindowJsonLoader(ObjectMapper objectMapper,
                            SpringDispatchWindowRepository windowRepository,
                            SpringWindowZoneCapacityRepository zoneCapacityRepository) {
        this.objectMapper = objectMapper;
        this.windowRepository = windowRepository;
        this.zoneCapacityRepository = zoneCapacityRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        if (!windowsJson.exists()) {
            return; // no seed file -> app still runs
        }

        try (InputStream is = windowsJson.getInputStream()) {
            List<WindowSeedRow> rows = objectMapper.readValue(is, new TypeReference<List<WindowSeedRow>>() {});
            for (WindowSeedRow r : rows) {
                // idempotent seed: delete and recreate
                windowRepository.deleteById(r.id());
                // delete capacities for this window
                zoneCapacityRepository.deleteAll(
                        zoneCapacityRepository.findAll().stream()
                                .filter(z -> z.getId().getWindowId().equals(r.id()))
                                .toList()
                );

                Money price = priceFor(r.start(), r.end());

                DispatchWindowEntity w = new DispatchWindowEntity(
                        r.id(),
                        LocalDate.parse(r.date()),
                        LocalTime.parse(r.start()),
                        LocalTime.parse(r.end()),
                        price.amountCents(),
                        price.currency(),
                        r.capacityTotal(),
                        r.capacityTotal()
                );
                windowRepository.save(w);

                for (Map.Entry<String, Integer> e : r.capacityByZone().entrySet()) {
                    WindowZoneCapacityEntity cap = new WindowZoneCapacityEntity(
                            new WindowZoneCapacityId(r.id(), e.getKey()),
                            e.getValue(),
                            e.getValue()
                    );
                    zoneCapacityRepository.save(cap);
                }
            }
        }
    }

    private Money priceFor(String start, String end) {
        // Simple deterministic pricing so reviewers see "blocks with cost".
        // 09-11: 1990 CLP, 11-13: 2990 CLP, else 2490 CLP.
        if ("09:00".equals(start) && "11:00".equals(end)) return Money.clp(1990);
        if ("11:00".equals(start) && "13:00".equals(end)) return Money.clp(2990);
        return Money.clp(2490);
    }

    public record WindowSeedRow(
            String id,
            String date,
            String start,
            String end,
            int capacityTotal,
            Map<String, Integer> capacityByZone
    ) {}
}
