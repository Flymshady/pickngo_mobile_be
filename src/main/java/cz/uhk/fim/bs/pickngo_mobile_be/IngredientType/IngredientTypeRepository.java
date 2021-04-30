package cz.uhk.fim.bs.pickngo_mobile_be.IngredientType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientTypeRepository extends JpaRepository<IngredientType, Long> {
    Optional<IngredientType> findIngredientTypeByName(String name);
}
