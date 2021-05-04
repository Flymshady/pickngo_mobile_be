package cz.uhk.fim.bs.pickngo_mobile_be.Item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<List<Item>> findAllByBaguetteItem_Id(Long baguetteItemId );
    Optional<List<Item>> findAllBySpecialOffer_Id(Long specialOfferId );
    Optional<List<Item>> findByIngredient_IngredientType_NameAndBaguetteItem_Id(String ingredientTypeName, Long baguetteItemId);
}
