package cz.uhk.fim.bs.pickngo_mobile_be.BaguetteOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaguetteOrderRepository extends JpaRepository<BaguetteOrder, Long> {

    Optional<BaguetteOrder> findBaguetteOrderByStateAndCustomer_Email(int state, String email);
    Optional<List<BaguetteOrder>> findBaguetteOrderByCustomer_Email(String email);
    Optional<List<BaguetteOrder>> findAllByStateAndCustomer_Email(int state, String email);
    Optional<BaguetteOrder> findBaguetteOrderByIdAndCustomer_Email(Long baguetteOrderId, String email);
}
