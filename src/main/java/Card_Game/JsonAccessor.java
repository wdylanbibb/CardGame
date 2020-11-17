package Card_Game;

import Card_Game.Abilities.Ability;
import Card_Game.CardContainers.Deck;
import Card_Game.Cards.Card;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;

public class JsonAccessor {

    private static Gson gson;

    public static Map<String, Class<? extends Ability>> abilList;

    public static void fillAbils() throws IOException {
        if (gson == null) gson = new Gson();
        abilList = new HashMap<>();
        Reader reader = Files.newBufferedReader(Paths.get("data/jsons/abilities.json"));
        JsonObject obj = gson.fromJson(reader, JsonObject.class);
        JsonArray array = obj.getAsJsonArray("abilities");
        array.forEach(jsonElement -> {
            try {
                Class cls = Class.forName(JsonAccessor.class.getPackageName() + ".Abilities." + jsonElement.getAsJsonObject().get("class").getAsString());
                if (Ability.class.isAssignableFrom(cls)) {
                    abilList.put(jsonElement.getAsJsonObject().get("name").getAsString().toLowerCase(), cls);
                }
            } catch (ClassNotFoundException | ClassCastException e) {
                e.printStackTrace();
            }
        });
    }

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
                    Card card;
                    Class<? extends Card> cls = UtilMaps.getInstance().getCardTypeByStr(obj.get("type").getAsString().toLowerCase());
                    if (cls != null) {
                        card = gson.fromJson(obj, cls);
                    } else {
                        card = gson.fromJson(obj, Card.class);
                    }
//                    Pair<Image, String> image = ImgAccessor.getImage(obj.get("name").getAsString().toLowerCase().replace(" ", "_"));
//                        card.setImage(image.getLeft());
//                    card.setImageName(image.getRight());
                    card.setParams(obj);
                    card.setPlayer(player);
                    card.setAbils(new ArrayList<>());
                    JsonArray abilities = obj.getAsJsonArray("abilities");
                    applyAbil(abilities, card);
                    deck.add(card);
                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void applyAbil(JsonArray abilities, Card card){
        if (abilities != null) {
            for (JsonElement ability : abilities) {
                if (ability.isJsonObject()) {
                    try {
                        if (abilList.containsKey(ability.getAsJsonObject().get("abil").getAsString().toLowerCase())) {
                            Ability abil = abilList.get(ability.getAsJsonObject().get("abil").getAsString().toLowerCase()).getConstructor(JsonArray.class, Card.class).newInstance(ability.getAsJsonObject().getAsJsonArray("args"), card);
                            if(ability.getAsJsonObject().get("scene") != null) {
                                abil.setRunScen(UtilMaps.getInstance().getAbilRunScen(ability.getAsJsonObject().get("scene").getAsString().toLowerCase()));
                            }
                            card.addAbility(abil);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
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

    public static Class<? extends Ability> getAbil(String s) {
        return abilList.getOrDefault(s, null);
    }
}
