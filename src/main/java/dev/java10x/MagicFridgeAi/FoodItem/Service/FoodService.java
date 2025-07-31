package dev.java10x.MagicFridgeAi.FoodItem.Service;

import dev.java10x.MagicFridgeAi.FoodItem.Model.FoodItem;
import dev.java10x.MagicFridgeAi.FoodItem.Repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService {

    private FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    //POST
    public FoodItem salvar(FoodItem foodItem){
        return foodRepository.save(foodItem);
    }
    //GET
    public List<FoodItem>listar(){
        return foodRepository.findAll();
    }
    //GET BY ID
    public FoodItem listarPorId(Long id) {
        Optional<FoodItem> food = foodRepository.findById(id);
        return food.orElse(null);
    }
    //PUT
    public FoodItem alterarPorId(FoodItem foodItem,Long id){
        if (foodRepository.existsById(id)){
            return foodRepository.save(foodItem);
        }
        return null;
    }

    //DELETE
    public void deletarPorId(Long id){
        foodRepository.deleteById(id);
    }

}




