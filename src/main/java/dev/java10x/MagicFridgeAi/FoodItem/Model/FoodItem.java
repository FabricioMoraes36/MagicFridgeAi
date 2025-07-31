package dev.java10x.MagicFridgeAi.FoodItem.Model;


import dev.java10x.MagicFridgeAi.FoodItem.Categoria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Food_Item")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private Integer quantidade;

    private LocalDateTime validade;
}
