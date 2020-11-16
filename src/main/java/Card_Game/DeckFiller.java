package Card_Game;

import Card_Game.CardContainers.Deck;
import Card_Game.Cards.Card;
import Card_Game.Cards.Monster;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DeckFiller {
    public static void fillDeck(String deckName, Deck deck, Player player) throws IOException {
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get("cards.json"));
        JsonObject object = gson.fromJson(reader, JsonObject.class);
        JsonArray array = object.getAsJsonArray(deckName);
        array.iterator().forEachRemaining(jsonElement -> {
            if (jsonElement.isJsonObject()) {
                if (((JsonObject) jsonElement).get("type").getAsString().equals("monster")) {
                    Monster monster = gson.fromJson(jsonElement, Monster.class);
                    monster.setPlayer(player);
                    deck.add(monster);
                } else {
                    Card card = gson.fromJson(jsonElement, Card.class);
                    card.setPlayer(player);
                    deck.add(card);
                }
            }
        });
    }
}
