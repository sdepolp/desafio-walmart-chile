package cl.walmart.desafio.adapters.out.persistence.repository;

import cl.walmart.desafio.adapters.out.persistence.entity.DispatchWindowEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SpringDispatchWindowRepository extends JpaRepository<DispatchWindowEntity, String> {

    @Query("select w from DispatchWindowEntity w where w.date between :from and :to")
    List<DispatchWindowEntity> findInRange(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Modifying
    @Query("update DispatchWindowEntity w set w.remainingTotal = w.remainingTotal - 1 " +
           "where w.id = :windowId and w.remainingTotal > 0")
    int tryConsumeTotal(@Param("windowId") String windowId);

    @Modifying
    @Query("update DispatchWindowEntity w set w.remainingTotal = w.remainingTotal + 1 " +
           "where w.id = :windowId")
    int releaseTotal(@Param("windowId") String windowId);
}
