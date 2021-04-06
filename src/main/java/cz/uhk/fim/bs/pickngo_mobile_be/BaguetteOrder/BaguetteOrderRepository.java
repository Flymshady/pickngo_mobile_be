package cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaguetteOrderRepository extends JpaRepository<BaguetteOrder, Long> {
}
