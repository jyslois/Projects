package com.espresso.cardgame;
import java.util.ArrayList;

public class Deck {
    // 나중에 한 번 뽑은 카드를 빼주기 위해 어레이 리스트 사용
    // Card객체 어레이리스트를 포함
    ArrayList<Card> cards = new ArrayList<>();


    // 생성자: 52개의 카드로 Deck의 어레이리스트 cards를 초기화
    Deck() {
        // 문양과 숫자 모두 1부터 시작해서 4/13까지.
        for (int i = 1; i <= Card.SHAPE_MAX; i++) {
            for (int j = 1; j <= Card.NUM_MAX; j++) {
                // 카드 객체를 생성해서
                Card card = new Card(i, j);
                // 배열에 추가
                cards.add(card);
            }
        }
    }

    // 카드 랜덤으로 뽑기, 뽑은 카드는 겹치지 않기 위해 리스트에서 삭제
    Card pick() {
        int random = (int) (Math.random() * cards.size());
        Card selectedCard = cards.get(random);
        // 뽑은 카드는 cards에서 제거
        cards.remove(random);
        return selectedCard;
    }
}