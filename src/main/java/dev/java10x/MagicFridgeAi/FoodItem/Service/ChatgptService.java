package dev.java10x.MagicFridgeAi.FoodItem.Service;

import dev.java10x.MagicFridgeAi.FoodItem.Model.FoodItem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatgptService {

    private final WebClient webClient;

    public ChatgptService(WebClient webClient) {
        this.webClient = webClient;
    }

    private final String apiKey = System.getenv("OPEN_AI_KEY");

    // Exemplo da requisição padrão da OpenAI (curl):
    /*
      curl https://api.openai.com/v1/chat/completions \
        -H "Content-Type: application/json" \
        -H "Authorization: Bearer $OPENAI_API_KEY" \
        -d '{
          "model": "gpt-4o",
          "messages": [
            { "role": "system", "content": "Você é um assistente útil." },
            { "role": "user", "content": "Me dê uma receita simples com ovos e arroz." }
          ]
        }'
    */

    //aqui passamos uma list como paramentro para que os itens do nosso db sejam usados no metodo quando ele for chamado la no controller
    public Mono<String> generateRecipe(List<FoodItem>foodItems) {

        //Aqui criamos a variavel ingredientes para que o nosso prompt use ela e usamos o map tambem para passar a formatação que queremos
        String ingredientes = foodItems.stream()
                .map(item -> String.format("%s (%s) - Quantidade: %d, Validade: %s",
                        item.getNome(),item.getCategoria(),item.getQuantidade(),item.getValidade()))
                .collect(Collectors.joining("\n"));

        // Essa é a pergunta que o usuário faria para o ChatGPT
        String prompt = "Me mande uma receita com base nos meus ingredientes que são:" + ingredientes;

        // Esse é o corpo da requisição (requestBody), que envia as mensagens para a OpenAI
        // A primeira mensagem com role "system" define o comportamento da IA
        // A segunda com role "user" é o que o usuário quer perguntar (nosso prompt)
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", List.of(
                        Map.of("role", "system", "content", "Crie uma receita com base nos ingredientes mais comuns"),
                        Map.of("role", "user", "content", prompt)
                )
        );

        // Aqui começa a chamada à API da OpenAI usando o WebClient
        return webClient.post()
                // Cabeçalho informando que estamos enviando JSON
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                // Cabeçalho de autenticação (Bearer token), usando nossa API key
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                // Enviamos o corpo da requisição (nosso Map requestBody)
                .bodyValue(requestBody)
                // Dispara a requisição
                .retrieve()
                // Converte a resposta recebida para um Map genérico (já que o JSON vira um mapa de chave/valor)
                .bodyToMono(Map.class)
                // Agora vamos extrair o conteúdo da resposta
                .map(response -> {
                    // A chave "choices" da resposta contém uma lista de mapas com as opções de resposta da IA
                    var choices = (List<Map<String, Object>>) response.get("choices");

                    // Se a lista "choices" existir e não estiver vazia
                    if (choices != null && !choices.isEmpty()) {
                        // Pegamos o primeiro item da lista (poderia haver mais de um)
                        // e acessamos o mapa "message" que está dentro dele
                        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

                        // Dentro de "message", pegamos o campo "content" (que é a resposta da IA em si)
                        return message.get("content").toString();
                    }

                    // Se não houver nenhuma resposta da IA, retornamos um aviso padrão
                    return "Nenhuma receita foi gerada";
                });
    }
}
