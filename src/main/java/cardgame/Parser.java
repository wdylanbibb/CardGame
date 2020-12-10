package cardgame;

import cardgame.cards.Card;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Parser {

    static ArrayList<String> parser(){
        Scanner scanner = new Scanner(System.in);
        String statementIn = scanner.nextLine();
        ArrayList<String> wordList = new ArrayList<>();
        Scanner tokenizer = new Scanner(statementIn);
        while(tokenizer.hasNext()){
            wordList.add(tokenizer.next());
        }
        return wordList;
    }

    static Card parseCardFromHand(Player player, List<String> words) {
        String word = StringUtils.join(words, " ");
        return parseCard(player.getHand(), word);
    }

    static Card parseCardFromField(Player player, List<String> words) {
        List<Card> collection = new ArrayList<>(GameComponents.getInstance().getSelfFieldCards(player));
        String word = StringUtils.join(words, " ");
        return parseCard(collection, word);
    }

    static Card parseCardFromAnyField(List<String> words){
        List<Card> collection = new ArrayList<>(GameComponents.getInstance().getAllFieldCards());
        String word = StringUtils.join(words, " ");
        return parseCard(collection, word);
    }

    static Card parseCardFromFieldOrHand(Player player, List<String> words) {
        List<Card> collection = new ArrayList<>(player.getHand());
        collection.addAll(GameComponents.getInstance().getAllFieldCards());
        String word = StringUtils.join(words, " ");
        return parseCard(collection, word);
    }

    static Card parseCardFromList(List<Card> collection, List<String> words){
        String word = StringUtils.join(words, " ");
        return parseCard(collection, word);
    }

    static Card parseCard(List<Card> collection, String words) {
        return collection
                .stream()
                .filter(card -> card.getName().equalsIgnoreCase(words))
                .collect(Collectors.toList())
                .get(0);
    }
}
