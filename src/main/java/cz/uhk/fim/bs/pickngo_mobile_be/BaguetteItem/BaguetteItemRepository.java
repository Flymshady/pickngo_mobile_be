package cz.uhk.fim.bs.pickngo_mobile_be.BaguetteItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaguetteItemRepository extends JpaRepository<BaguetteItem, Long> {

    Optional<List<BaguetteItem>> findAllByBaguetteOrder_Id(Long baguetteOrderId);
}
