package com.espresso.cardgame;

public class Player extends Gamer {
    Player(String name) {
        super(name);
        Dealer.candidates.add(this);
    }

}