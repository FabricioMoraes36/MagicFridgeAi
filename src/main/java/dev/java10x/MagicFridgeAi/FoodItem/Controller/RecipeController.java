package dev.java10x.MagicFridgeAi.FoodItem.Controller;
import dev.java10x.MagicFridgeAi.FoodItem.Model.FoodItem;
import dev.java10x.MagicFridgeAi.FoodItem.Service.ChatgptService;
import dev.java10x.MagicFridgeAi.FoodItem.Service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/recipe")
public class RecipeController {

    private final ChatgptService chatgptService;
    private FoodService service;

    @Autowired
    public RecipeController(ChatgptService chatgptService, FoodService service) {
        this.chatgptService = chatgptService;
        this.service = service;
    }

    public RecipeController(ChatgptService chatgptService) {
        this.chatgptService = chatgptService;
    }

    @GetMapping("/gerarReceita")
    public Mono<ResponseEntity<String>> generateRecipe(){
        List<FoodItem> ingredientes = service.listar();

        return chatgptService.generateRecipe(ingredientes)
                .map(recipe -> ResponseEntity.ok(recipe))
                //se estiver vazio vai retonar o noContent();
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
}
