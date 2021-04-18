package cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaguetteOrderRepository extends JpaRepository<BaguetteOrder, Long> {

    BaguetteOrder findByStateAndCustomer_Email(int state, String email);
}
