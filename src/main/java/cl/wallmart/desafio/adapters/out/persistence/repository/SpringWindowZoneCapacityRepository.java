package cl.wallmart.desafio.adapters.out.persistence.repository;

import cl.wallmart.desafio.adapters.out.persistence.entity.WindowZoneCapacityEntity;
import cl.wallmart.desafio.adapters.out.persistence.entity.WindowZoneCapacityId;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringWindowZoneCapacityRepository extends JpaRepository<WindowZoneCapacityEntity, WindowZoneCapacityId> {

    @Query("select z from WindowZoneCapacityEntity z where z.id.windowId = :windowId and z.id.zoneId = :zoneId")
    List<WindowZoneCapacityEntity> findByWindowAndZone(@Param("windowId") String windowId, @Param("zoneId") String zoneId);

    @Modifying
    @Query("update WindowZoneCapacityEntity z set z.remaining = z.remaining - 1 " +
           "where z.id.windowId = :windowId and z.id.zoneId = :zoneId and z.remaining > 0")
    int tryConsumeZone(@Param("windowId") String windowId, @Param("zoneId") String zoneId);

    @Modifying
    @Query("update WindowZoneCapacityEntity z set z.remaining = z.remaining + 1 " +
           "where z.id.windowId = :windowId and z.id.zoneId = :zoneId")
    int releaseZone(@Param("windowId") String windowId, @Param("zoneId") String zoneId);
}
