package dev.java10x.MagicFridgeAi.FoodItem.Controller;

import dev.java10x.MagicFridgeAi.FoodItem.Model.FoodItem;
import dev.java10x.MagicFridgeAi.FoodItem.Service.FoodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food")
public class FoodController {

    private FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    //POST
    @PostMapping("/criar")
    public ResponseEntity<FoodItem> criar(@RequestBody FoodItem foodItem) {
        FoodItem salvar = foodService.salvar(foodItem);
        return ResponseEntity.ok(salvar);
    }

    //GET
    @GetMapping("/listar")
    public ResponseEntity<List<FoodItem>> listar() {
        return ResponseEntity.ok(foodService.listar());
    }

    //GET BY ID
    @GetMapping("/listar/{id}")
    public ResponseEntity<FoodItem> listarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(foodService.listarPorId(id));
    }

    //PUT
    @PutMapping("/alterar/{id}")
    public ResponseEntity<?> atualizar(@RequestBody FoodItem foodItem, @PathVariable Long id) {
        FoodItem food = foodService.listarPorId(id);
        if (food != null){
           foodItem.setId(food.getId());
            food = foodService.alterarPorId(foodItem,id);
            return ResponseEntity.ok().body(food + "Item alterado com sucesso");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("item não foi encontrado no id ");
        }
    }


    //DELETE
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        foodService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}





