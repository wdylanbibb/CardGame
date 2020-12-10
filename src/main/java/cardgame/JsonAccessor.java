package cardgame;

import cardgame.abilities.Ability;
import cardgame.attributes.Attribute;
import cardgame.cardcontainers.Deck;
import cardgame.cards.Card;
import cardgame.rules.Rule;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

public class JsonAccessor {

    private static final String DATA_DIR = "external_data";
    private static Gson gson;

    private static NodeList<ImportDeclaration> imports;

    public static void fillMaps() throws IOException {
        if (gson == null) gson = new Gson();
        Map<String, Class<? extends Ability>> abilList = new HashMap<>();
        Map<String, Class<? extends Attribute>> attrList = new HashMap<>();
        Map<String, String> decks = new HashMap<>();
        Map<String, String> cards = new HashMap<>();
        File externalData = new File(DATA_DIR);
        Reader sampleReader = Files.newBufferedReader(Paths.get("src/main/java/cardgame/abilities/abils/SampleAbility.java"));
        CompilationUnit sampleUnit = StaticJavaParser.parse(sampleReader);
        imports = sampleUnit.getImports();
        if (externalData.isDirectory()) {
            Arrays.asList(Objects.requireNonNull(externalData.listFiles())).forEach(file -> {
                if (file.isDirectory()) {
                    Arrays.asList(Objects.requireNonNull(file.listFiles())).forEach(modFile -> {
                        if (modFile.isDirectory()) {
                                switch (modFile.getName()) {
                                    case "abilities":
                                        Arrays.asList(Objects.requireNonNull(modFile.listFiles())).forEach(javaClass -> {
                                            if (javaClass.isFile() && javaClass.getName().endsWith(".java") && !javaClass.getName().equals("SampleAbility.java")) {
                                                try {
                                                    Reader reader = Files.newBufferedReader(Paths.get(javaClass.getPath()));
                                                    CompilationUnit compilationUnit = StaticJavaParser.parse(reader);
                                                    compilationUnit.setImports(imports);
                                                    compilationUnit.setPackageDeclaration("cardgame.abilities.abils." + file.getName());
                                                    try {
                                                        File folder = new File("src/main/java/cardgame/abilities/abils/" + file.getName() + "/");
                                                        folder.mkdir();
                                                            final String fileName = "src/main/java/cardgame/abilities/abils/" + file.getName() + "/" + javaClass.getName();
                                                            Writer fileWriter = Files.newBufferedWriter(Paths.get(fileName));
                                                            fileWriter.write(compilationUnit.toString());
                                                            fileWriter.close();
                                                            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                                                            int compResult = compiler.run(null, null, null, fileName);
                                                            if (compResult == 0) {
                                                                Logger.getLogger("files").info("Compilation successful!");
                                                            } else {
                                                                Logger.getLogger("files").info("Compilation failed");
                                                            }
                                                            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { new File("src/main/java").toURI().toURL() });
                                                            Class cls = Class.forName("cardgame.abilities.abils." + file.getName() + "." + javaClass.getName().split(".java")[0], true, classLoader);
                                                            if (Ability.class.isAssignableFrom(cls)) {
                                                                abilList.put(javaClass.getName().split(".java")[0].toLowerCase(), cls);
                                                            }
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                } catch (ClassCastException | IOException | ClassNotFoundException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        break;
                                    case "attributes":
                                        Arrays.asList(Objects.requireNonNull(modFile.listFiles())).forEach((javaClass -> {
                                            if (javaClass.isFile() && javaClass.getName().endsWith(".java") && !javaClass.getName().equals("SampleAttribute.java")) {
                                                try {
                                                    Reader reader = Files.newBufferedReader(Paths.get(javaClass.getPath()));
                                                    CompilationUnit compilationUnit = StaticJavaParser.parse(reader);
                                                    compilationUnit.setImports(imports);
                                                    compilationUnit.setPackageDeclaration("cardgame.attributes.attr." + file.getName());
                                                    try {
                                                        File folder = new File("src/main/java/cardgame/attributes/attr/" + file.getName() + "/");
                                                        folder.mkdir();
                                                            final String fileName = "src/main/java/cardgame/attributes/attr/" + file.getName() + "/" + javaClass.getName();
                                                            Writer fileWriter = Files.newBufferedWriter(Paths.get(fileName));
                                                            fileWriter.write(compilationUnit.toString());
                                                            fileWriter.close();
                                                            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                                                            int compResult = compiler.run(null, null, null, fileName);
                                                            if (compResult == 0) {
                                                                Logger.getLogger("files").info("Compilation successful!");
                                                            } else {
                                                                Logger.getLogger("files").info("Compilation failed");
                                                            }
                                                            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { new File("src/main/java").toURI().toURL() });
                                                            Class cls = Class.forName("cardgame.attributes.attr." + file.getName() + "." + javaClass.getName().split(".java")[0], true, classLoader);
                                                            if (Attribute.class.isAssignableFrom(cls)) {
                                                                attrList.put(javaClass.getName().split(".java")[0].toLowerCase(), cls);
                                                            }
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                } catch (ClassCastException | IOException | ClassNotFoundException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }));
                                    case "decks":
                                        Arrays.asList(Objects.requireNonNull(modFile.listFiles())).forEach(jsonFile -> {
                                            if (jsonFile.isFile() && jsonFile.getName().endsWith(".json")) {
                                                decks.put(jsonFile.getName().split(".json")[0], jsonFile.getPath());
                                            }
                                        });
                                        break;
                                    case "cards":
                                        Arrays.asList(Objects.requireNonNull(modFile.listFiles())).forEach(jsonFile -> {
                                            if (jsonFile.isFile() && jsonFile.getName().endsWith(".json")) {
                                                cards.put(jsonFile.getName().split(".json")[0], jsonFile.getPath());
                                            }
                                        });
                                        break;
                                }
                        }
                    });
                }
            });
        }
        UtilMaps.getInstance().fillAbilList(abilList);
        UtilMaps.getInstance().fillAttrList(attrList);
        UtilMaps.getInstance().fillDeckList(decks);
        UtilMaps.getInstance().fillCardList(cards);
    }

    public static void fillDeck(String deckName, Deck deck, Player player) throws IOException {
        if (gson == null) gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get(deckName));
        JsonObject object = gson.fromJson(reader, JsonObject.class);
        JsonArray array = object.getAsJsonArray("deck");
        array.iterator().forEachRemaining(jsonElement -> {
            if (jsonElement.isJsonPrimitive()) {
                String fileName = jsonElement.getAsString().toLowerCase().replace(" ", "_");
                try {
                    Reader fileReader = Files.newBufferedReader(Paths.get(UtilMaps.getInstance().getCardByName(fileName)));
                    JsonObject obj = gson.fromJson(fileReader, JsonObject.class);
                    Card card;
                    Class<? extends Card> cls = UtilMaps.getInstance().getCardClassByStr(obj.get("type").getAsString().toLowerCase());
                    if (cls != null) {
                        card = gson.fromJson(obj, cls);
                    } else {
                        card = gson.fromJson(obj, Card.class);
                    }
                    card.setParams(obj);
                    card.setEmissionListeners();
                    card.setPlayer(player);
                    card.setAbils(new ArrayList<>());
                    JsonArray abilities = obj.getAsJsonArray("abilities");
                    applyAbil(abilities, card);

                    card.setAttr(new ArrayList<>());
                    JsonArray attributes = obj.getAsJsonArray("attributes");
                    applyAttr(attributes, card);

                    JsonArray rules = obj.getAsJsonArray("rules");
                    applyRules(rules, card);
                    card.verifyImage();
                    for(Ability ability : card.getAbils()){
                        System.out.println("Target type: " + ability.getTargetType());
                    }
                    deck.add(card);
                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void applyRules(JsonArray rules, Card card) {
        card.setRules(new ArrayList<>());
        if (rules != null) {
            for (JsonElement rule : rules) {
                if (rule.isJsonObject()) {
                    try {

                        Rule r = gson.fromJson(rule, Rule.class);
                        r.setEnums();

                        card.addRule(r);
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    public static void applyAttr(JsonArray attributes, Card card){
        card.setAttr(new ArrayList<>());
        if (attributes != null){
            for (JsonElement attr : attributes){
                if(attr.isJsonObject()){
                    try{
                        if (UtilMaps.getInstance().getAttrByString(attr.getAsJsonObject().get("attr").getAsString().toLowerCase()) != null) {
                            Attribute attribute = UtilMaps
                                    .getInstance()
                                    .getAttrByString(attr
                                            .getAsJsonObject()
                                            .get("attr")
                                            .getAsString()
                                            .toLowerCase()
                                    )
                                    .getConstructor(JsonObject.class, Card.class)
                                    .newInstance(attr.getAsJsonObject().get("args").getAsJsonObject(), card);
                            if(attr.getAsJsonObject().get("target") != null) {
                                attribute.setTarget(UtilMaps.getInstance().getPlayerByString(attr.getAsJsonObject().get("target").getAsString().toLowerCase()));
                            }
                            if(attr.getAsJsonObject().get("event") != null) {
                                attribute.setEvent(UtilMaps.getInstance().getCardEvent(attr.getAsJsonObject().get("event").getAsString().toLowerCase()));
                            }
                            if(attr.getAsJsonObject().get("type") != null) {
                                attribute.setType(UtilMaps.getInstance().getCardTypeByStr(attr.getAsJsonObject().get("type").getAsString().toLowerCase()));
                            }
                            card.addAttr(attribute);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void applyAbil(JsonArray abilities, Card card){
        if (abilities != null) {
            for (JsonElement ability : abilities) {
                if (ability.isJsonObject()) {
                    try {
                        if (UtilMaps.getInstance().getAbilityByString(ability.getAsJsonObject().get("abil").getAsString().toLowerCase()) != null) {
                            Ability abil = UtilMaps
                                    .getInstance()
                                    .getAbilityByString(ability
                                            .getAsJsonObject()
                                            .get("abil")
                                            .getAsString()
                                            .toLowerCase()
                                    )
                                    .getConstructor(JsonObject.class, Card.class)
                                    .newInstance(ability.getAsJsonObject().get("args").getAsJsonObject(), card);
                            if(ability.getAsJsonObject().get("event") != null) {
                                abil.setCardEvent(UtilMaps.getInstance().getCardEvent(ability.getAsJsonObject().get("event").getAsString().toLowerCase()));
                            }
                            if(ability.getAsJsonObject().get("target") != null){
                                abil.setCardType(UtilMaps.getInstance().getCardTypeByStr(ability.getAsJsonObject().get("target").getAsString().toLowerCase()));
                            }
                            if(ability.getAsJsonObject().get("type") != null){
                                abil.setCardType(UtilMaps.getInstance().getCardTypeByStr(ability.getAsJsonObject().get("type").getAsString().toLowerCase()));
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
        Reader reader = Files.newBufferedReader(Paths.get(DATA_DIR + "/setup.json"));
        JsonObject object = gson.fromJson(reader, JsonObject.class);
        JsonArray array = object.getAsJsonArray("players");
        List<String> decks = new ArrayList<>();
        array.iterator().forEachRemaining(jsonElement -> {
            if (jsonElement.isJsonObject()) {
                String name = ((JsonObject) jsonElement).get("deck").getAsString();
                if (name.startsWith("%")) {
                    name = name.substring(1);
                    if (UtilMaps.getInstance().getDeckByName(name) != null) {
                        decks.add(UtilMaps.getInstance().getDeckByName(name));
                    } else {
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
        List<String> possibleNames = UtilMaps.getInstance().getDeckPaths();
        return Iterables.get(possibleNames, random.nextInt(possibleNames.size()));
    }
}
