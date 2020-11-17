package Card_Game;

import Card_Game.CardContainers.Deck;
import Card_Game.Cards.Card;
import Card_Game.Cards.Monster;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class JsonAccessor {

    private static final String BASE_DIR = "carddata/jsons/";
    private static final String CARD_DIR = BASE_DIR + "cards/";
    private static final String DECK_DIR = BASE_DIR + "decks/";

    private static Gson gson;


    public static void fillDeck(String deckName, Deck deck, Player player) throws IOException {
        if (gson == null) gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get(DECK_DIR + deckName + ".json"));
        JsonObject object = gson.fromJson(reader, JsonObject.class);
        JsonArray array = object.getAsJsonArray("deck");
        array.iterator().forEachRemaining(jsonElement -> {
            if (jsonElement.isJsonPrimitive()) {
                String fileName = jsonElement.getAsString().toLowerCase().replace(" ", "_") + ".json";
                try {
                    Reader fileReader = Files.newBufferedReader(Paths.get(CARD_DIR + fileName));
                    JsonObject obj = gson.fromJson(fileReader, JsonObject.class);
                    if (obj.get("type").getAsString().equals("monster")) {
                        Monster monster = gson.fromJson(obj, Monster.class);
                        monster.setPlayer(player);
                        monster.setAlive(true);
                        Pair<Image, String> image = ImgAccessor.getImage(obj.get("name").getAsString().toLowerCase().replace(" ", "_"));
                        monster.setImage(image.getLeft());
                        monster.setImageName(image.getRight());
                        deck.add(monster);
                    } else {
                        Card card = gson.fromJson(obj, Card.class);
                        card.setPlayer(player);
                        Pair<Image, String> image = ImgAccessor.getImage(obj.get("name").getAsString().toLowerCase().replace(" ", "_"));
                        card.setImage(image.getLeft());
                        card.setImageName(image.getRight());
                        deck.add(card);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static List<String> pickDecks() throws IOException {
        if (gson == null) gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get(BASE_DIR + "setdecks.json"));
        JsonObject object = gson.fromJson(reader, JsonObject.class);
        JsonArray array = object.getAsJsonArray("players");
        List<String> decks = new ArrayList<>();
        array.iterator().forEachRemaining(jsonElement -> {
            if (jsonElement.isJsonObject()) {
                String name = ((JsonObject) jsonElement).get("deck").getAsString();
                if (new File(DECK_DIR + name).exists()) decks.add(name); else decks.add(pickRandomDeck());
            }
        });
        return decks;
    }

    private static String pickRandomDeck() {
        Random random = new Random();
        List<String> possibleNames = new ArrayList<>();
        File[] files = new File(DECK_DIR).listFiles();
        if (files != null) Arrays.stream(files).forEach(file -> possibleNames.add(file.getName().split(".json")[0]));
        return Iterables.get(possibleNames, random.nextInt(possibleNames.size()));
    }
}
