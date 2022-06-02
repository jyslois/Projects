package com.espresso.cardgame;

public class Card {
    // 숫자 number: A,2,3,4,5,6,7,8,9,10(X),J,Q,K
    // 문양 shape: clover(1), heart(2), diamond(3), spade(4)
    // 모든 인스턴스가 공통적으로 보유하고 있어야 하고(static)
    // 값이 변하지 않아야 하므로(final) static final변수로 만들어 준다.
    static final int CLOVER = 1;
    static final int HEART = 2;
    static final int DIAMOND = 3;
    static final int SPADE = 4;

    // Deck에서 사용하기 위해 static으로 카드 무늬 수와 무늬별 카드 수를
    // 멤버변수로 선언
    static final int SHAPE_MAX = 4;
    static final int NUM_MAX = 13;

    int shape;
    int number;

    Card(int shape, int number) {
        this.shape = shape;
        this.number = number;
    }

    Card() {
        this(1, 1);
    }

    public String toString() {
        String[] kinds = {"", "CLOVER", "HEART", "DIAMOND", "SPADE"};
        // 숫자는 1부터 시작하지만 배열의 인덱스는 0부터 시작함. (0, 1234)

        String numbers = "-A23456789XJQK";
        // 숫자는 1(A)부터 13까지. 10은 X로 표현

        // 10이면 X대신 숫자로 표현하기 위한 코드
        String is_ten;

        if (this.number == 10) {
            is_ten = "10";
        } else {
            is_ten = numbers.charAt(this.number) + "";
            // char을 String 타입인 is_ten으로 바꾸기 위해 + ""
        }

        return "숫자: " + is_ten + ", 문양: " + kinds[this.shape];
    }

}
