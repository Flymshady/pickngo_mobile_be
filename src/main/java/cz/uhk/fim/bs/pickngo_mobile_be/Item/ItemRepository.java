package cz.uhk.fim.bs.pickngo_mobile_be.Item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByBaguetteOrder_Id(Long baguetteOrderId );
}
