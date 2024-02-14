package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.example.Resource.ResourceType;
import com.example.strategy.Strategy;
import com.example.strategy.boardReorganisation.NaiveBoardReorganisation;
import com.example.strategy.build.NaiveBuild;
import com.example.strategy.build.RandomBuild;
import com.example.strategy.chooseCard.NaiveChooseCard;
import com.example.strategy.chooseCard.SmartChooseCard;
import com.example.strategy.keepCard.KeepStrongestCard;
import com.example.strategy.keepCard.NaiveKeepCard;
import com.example.strategy.boardReorganisation.DiscardAllBoardReorganisation;
import com.example.strategy.pickCard.NaivePickCard;
import com.example.strategy.playCard.NaivePlayCard;
import com.example.strategy.playCard.PlayStrongCard;
import com.example.utils.Utils;

public class App {
    public static void main(String[] args) {
        Utils.setStartLogs(false);
        Utils.setEndLogs(true);
        int gamesPlayed = 1;

        List<Strategy> strategies = createStrategies();

        List<Stat> stats = new ArrayList<Stat>();
        IntStream.range(0, strategies.size())
                .forEachOrdered(i -> stats.add(new Stat(i)));

        for (int i = 0; i < gamesPlayed; i++) {
            List<Player> players = Utils.createPlayers(strategies.size(), strategies);

            // ------------- Setup the game ------------
            Deck deck = new Deck(Utils.createCards());
            deck.shuffle();
            List<Card> discardPile = new ArrayList<Card>();

            List<Building> buildings = Utils.createBuildings();

            for (Player player : players) {
                player.setBuildings(buildings);
                player.setResource(ResourceType.GOLD, 3);
            }

            // ------------- Play the game ------------
            List<Result> results = playGame(players, deck, discardPile);

            // ------------- Print results ------------
            // printGameResult(results);

            // ------------- Compute stats ------------
            for (Player player : players) {
                Result result = results.stream().filter(tmpResult -> tmpResult.getPlayerId() == player.getId())
                        .findFirst().get();
                stats.stream().filter(stat -> stat.getPlayerId() == player.getId()).findFirst().get()
                        .addGame(result.getPlayerPlace(), result.getPlayerPoint());
            }
        }

        for (Stat stat : stats) {
            System.out.println("Player " + stat.getPlayerId() + " has played " +
                    stat.gamesPlayed
                    + " games : avg. place : " + stat.averagePlace()
                    + ", avg. points : " + stat.averagePoints());
        }
    }

    public static List<Strategy> createStrategies() {
        List<Strategy> strategies = new ArrayList<Strategy>();
        // Naive strategy
        strategies.add(new Strategy(
                new NaivePickCard(),
                new NaiveBoardReorganisation(),
                new NaivePlayCard(),
                new NaiveKeepCard(),
                new NaiveBuild(),
                new NaiveChooseCard()));
        // ?
        strategies.add(new Strategy(
                new NaivePickCard(),
                new NaiveBoardReorganisation(),
                new NaivePlayCard(),
                new NaiveKeepCard(),
                new RandomBuild(),
                new SmartChooseCard()));
        // Smart strategy keeping board
        strategies.add(new Strategy(
                new NaivePickCard(),
                new NaiveBoardReorganisation(),
                new PlayStrongCard(),
                new KeepStrongestCard(),
                new RandomBuild(),
                new SmartChooseCard()));
        // Smart strategy discarding board
        strategies.add(new Strategy(
                new NaivePickCard(),
                new DiscardAllBoardReorganisation(),
                new PlayStrongCard(),
                new KeepStrongestCard(),
                new RandomBuild(),
                new SmartChooseCard()));

        return strategies;
    }

    public static List<Result> playGame(List<Player> players, Deck deck, List<Card> discardPile) {
        System.out.println("----------------- Game -----------------");
        IntStream.range(0, 4).forEachOrdered(i -> {
            System.out.println("----------------- Turn " + (i + 1) + " -----------------");
            Utils.phase1(players, deck);
            Utils.phase2(players, deck, discardPile);
            Utils.phase3(players);
            Utils.phase4(players);
            Utils.phase5(players);
            Utils.phase6(players, discardPile);
        });
        return Utils.endGame(players);
    }

    public static void printGameResult(List<Result> results) {
        for (Result result : results)
            System.out.println("Player " + result.getPlayerId() + " has "
                    + result.getPlayerPoint() + " points and "
                    + result.getPlayerGold() + " golds");
    }
}
