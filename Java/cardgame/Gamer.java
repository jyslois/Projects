package com.espresso.cardgame;

import java.util.ArrayList;

public class Gamer {
    String name;
    int money;
    // 카드를 소유하기 위해 Card 타입 참조변수를 멤버변수로 포함
    Card card;

    Gamer(String name) {
        this.name = name;
        this.money = 1000000;
    }

    // 뽑은 카드를 가지는 메서드
    public void receiveCard(Card card) {
        this.card = card;
    }

    Card getCard() {
        return this.card;
    }

}