package dev.java10x.MagicFridgeAi.FoodItem.Repository;

import dev.java10x.MagicFridgeAi.FoodItem.Model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<FoodItem,Long> {
}
