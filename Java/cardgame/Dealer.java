package com.espresso.cardgame;

import java.util.ArrayList;

public class Dealer extends Gamer {
    // 배팅 금액
    private int bettingAmount;
    // 뽑은 카드를 비교해 승패를 판단해야 하기 때문에 Player타입 참조변수를 포함
    Player player;
    // 게이머를 생성할 때마다 승자후보목록에 추가하기 위한 어레이리스트
    static ArrayList<Gamer> candidates = new ArrayList<>();
    // 승자를 담기 위한 참조 변수
    Gamer winner;
    // 게임 회차를 담기 위한 변수
    static int game = 0;
    // 배팅 맥스 금액
    final static int MAX_BETTING = 500000;

    Dealer() {
        super("Dealer");
        candidates.add(this);
    }

    public int getBettingAmount() {
        return bettingAmount;
    }

    // 배팅 금액 랜덤으로 선정 (1~50만원까지)하는 메서드
    void choosingBettingAmount() {
        this.bettingAmount = (int) (Math.random() * MAX_BETTING);
        System.out.println("-----------------------" + ++game + "회차 게임 시작------------------------");
        System.out.println("배팅 금액은: " + this.bettingAmount + "원 입니다.");
    }

    // 카드 뽑아서 나눠주는 메서드
    void drawCard() {
        Deck deck = new Deck();
        for (int i = 0; i < candidates.size(); i++) {
            Card card = deck.pick();
            candidates.get(i).receiveCard(card);
            System.out.println(candidates.get(i).name + "가 뽑은 카드 - " + candidates.get(i).card + " || 남은 돈: " + candidates.get(i).money);
        }
    }

    // 승자 찾아주는 메서드
    void findWinner() {
        // 리스트를 돌면서 게이머.카드.숫자를 비교해 max 찾기
        int max = 0;

        for (int i = 0; i < candidates.size(); i++) {
            if (max <= candidates.get(i).card.number) {
                max = candidates.get(i).card.number;
            }
        }

        // max를 가진 멤버는 또다른 리스트를 생성해서 추가
        ArrayList<Gamer> winningGamers = new ArrayList<>();

        for (int i = 0; i < candidates.size(); i++) {
            if (max == candidates.get(i).card.number) {
                winningGamers.add(candidates.get(i));
            }
        }

        if (winningGamers.size() != 1) {
            // 사이즈가 1보다 클 경우, 게이머.카드.문양을 비교해 제일 큰 거 찾기
            int max_shape = 0;
            for (int i = 0; i < winningGamers.size(); i++) {
                if (max_shape < winningGamers.get(i).card.shape) {
                    max_shape = winningGamers.get(i).card.shape;
                }
            }
            // max_shape 문양과 같은 문양이면, winner로 선정
            for (int i = 0; i < winningGamers.size(); i++) {
                if (max_shape == winningGamers.get(i).card.shape) {
                    winner = winningGamers.get(i);
                }
            }
            System.out.println("이번 판의 승자는: " + winner.name);
        } else {
            // 사이즈가 1일 경우 승자 선언
            winner = winningGamers.get(0);
            System.out.println("이번 판의 승자는: " + winner.name);
        }

    }

    // 승자는 보유금액에 배팅금액을 더하고, 패자는 빼주는 메서드
    void calculateMoney() {
        for (int i = 0; i < candidates.size(); i++) {
            if (candidates.get(i) == winner) {
                candidates.get(i).money += bettingAmount;
                // 원래는 candidates.get(i).money += (bettingAmount * (candidates.size() - 1))을 해주어야 하지만,
                // 게임의 회차가 너무 길어지므로 생략했다.
            } else {
                candidates.get(i).money -= bettingAmount;

            }
        }

        System.out.println("============================================================");
        for (int i = 0; i < candidates.size(); i++) {
            // 남은 금액 프린트
            System.out.println(candidates.get(i).name + ": " + candidates.get(i).money);
        }
    }

    // 보유 금액이 0보다 같거나 작다면 후보리스트에서 삭제하는 메서드.
    void sortcandidates() {
        for (int i = 0; i < candidates.size(); i++) {
            if (candidates.get(i).money <= 0) {
                candidates.remove(candidates.get(i));
                i--;
            }
        }
    }

    // 게임 진행하는 메서드
    void gambling() {
        while (true) {
            choosingBettingAmount();
            drawCard();
            findWinner();
            calculateMoney();
            sortcandidates();
            if (candidates.size() == 1) {
                break;
            }
        }

        System.out.println();
        System.out.println("★★★★★★★최종 승리자는 \"" + candidates.get(0).name + "\" 입니다.★★★★★★★");
    }


}