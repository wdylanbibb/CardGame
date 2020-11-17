package Card_Game;

import Card_Game.CardContainers.Deck;
import Card_Game.Cards.Card;
import Card_Game.Cards.Monster;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class JsonAccessor {

    private static Gson gson;


    public static void fillDeck(String deckName, Deck deck, Player player) throws IOException {
        if (gson == null) gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get(deckName));
        JsonObject object = gson.fromJson(reader, JsonObject.class);
        JsonArray array = object.getAsJsonArray("deck");
        array.iterator().forEachRemaining(jsonElement -> {
            if (jsonElement.isJsonPrimitive()) {
                String fileName = jsonElement.getAsString().toLowerCase().replace(" ", "_") + ".json";
                try {
                    Reader fileReader = Files.newBufferedReader(Paths.get(deckName.split("/decks/")[0] + "/cards/" + fileName));
                    JsonObject obj = gson.fromJson(fileReader, JsonObject.class);
                    if (obj.get("type").getAsString().equals("monster")) {
                        Monster monster = gson.fromJson(obj, Monster.class);
                        monster.setPlayer(player);
                        monster.setAlive(true);
                        Pair<Image, String> image = ImgAccessor.getImage(obj.get("name").getAsString().toLowerCase().replace(" ", "_"));
//                        monster.setImage(image.getLeft());
                        monster.setImageName(image.getRight());
                        deck.add(monster);
                    } else {
                        Card card = gson.fromJson(obj, Card.class);
                        card.setPlayer(player);
                        Pair<Image, String> image = ImgAccessor.getImage(obj.get("name").getAsString().toLowerCase().replace(" ", "_"));
//                        card.setImage(image.getLeft());
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
        Reader reader = Files.newBufferedReader(Paths.get("data/setup.json"));
        JsonObject object = gson.fromJson(reader, JsonObject.class);
        JsonArray array = object.getAsJsonArray("players");
        List<String> decks = new ArrayList<>();
        array.iterator().forEachRemaining(jsonElement -> {
            if (jsonElement.isJsonObject()) {
                String name = ((JsonObject) jsonElement).get("deck").getAsString();
                if (name.startsWith("%")) {
                    name = name.substring(1);
                    JsonArray dirArray = object.getAsJsonArray("deck_dirs");
                    boolean found = false;
                    for (JsonElement dir : dirArray) {
                        if (dir.isJsonPrimitive()) {
                            if (new File(dir.getAsString() + name + ".json").exists()) {
                                decks.add(dir.getAsString() + name + ".json");
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        try {
                            decks.add(pickRandomDeck());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        decks.add(pickRandomDeck());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return decks;
    }

    private static String pickRandomDeck() throws IOException {
        Random random = new Random();
        List<String> possibleNames = new ArrayList<>();
        Reader reader = Files.newBufferedReader(Paths.get("data/setup.json"));
        JsonObject object = gson.fromJson(reader, JsonObject.class);
        JsonArray array = object.getAsJsonArray("deck_dirs");
        for (JsonElement dir : array) {
            if (dir.isJsonPrimitive()) {
                for (File file : Objects.requireNonNull(new File(dir.getAsString()).listFiles())) {
                    possibleNames.add(dir.getAsString() + file.getName());
                }
            }
        }
        return Iterables.get(possibleNames, random.nextInt(possibleNames.size()));
    }
}
