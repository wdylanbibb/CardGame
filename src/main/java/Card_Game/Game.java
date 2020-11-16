package Card_Game;


import Card_Game.CardContainers.Field;
import Card_Game.Cards.Card;

import java.util.Scanner;
import java.io.IOException;
import java.util.stream.Collectors;

public class Game {

    Player[] players;
    Field[] board;
    Scanner scanner;

    public Game(Scanner scanner) throws IOException {
        this.scanner = scanner;

        players = new Player[]{new Player("deck1"), new Player("deck1")};
        GameComponents.newInstance(players);
        board = new Field[]{players[0].getField(), players[1].getField()};
    }

    public static void main(String[] args) throws IOException {
        Game game = new Game(new Scanner(System.in));
        game.run();
    }

    public void run(){
        // Players set, hands set, board set
        int playerTurn = 0;
        String statementIn = "";
        while(!statementIn.equalsIgnoreCase("quit")) {
            while (players[playerTurn].getMana() > 0) {
                System.out.println("Player " + (playerTurn + 1) + "'s Turn" + "\nYour hand:");
                for (Card card : players[playerTurn].getHand()) {
                    System.out.println("  " + card.getName());
                }
                statementIn = scanner.nextLine();
                String word1 = null;
                String word2 = null;

                Scanner tokenizer = new Scanner(statementIn);
                if (tokenizer.hasNext()) {
                    word1 = tokenizer.next();
                    if (tokenizer.hasNext()) {
                        word2 = tokenizer.next();
                    }
                }
                switch (word1) {
                    case "play":
                        String finalWord = word2;
                        try {
                            Card cardPlayed = players[playerTurn]
                                    .getHand()
                                    .stream()
                                    .filter(card -> card.getName().equalsIgnoreCase(finalWord))
                                    .collect(Collectors.toList())
                                    .get(0);
                            if(players[playerTurn].play(cardPlayed)){
                                System.out.println("Played card");
                            }else{
                                System.out.println("Did not play");
                            }
                        }catch (IndexOutOfBoundsException e){
                            System.out.println("Card not found");
                        }

                        break;
                    case "draw":
                        players[playerTurn].draw();
                        break;
                    case "end":
                        players[playerTurn].endTurn();
                        break;
                }
            }
            players[playerTurn].setMana(Player.MAX_MANA);
            playerTurn = playerTurn + 1 >= players.length ? 0 : playerTurn + 1;
        }
    }
}
