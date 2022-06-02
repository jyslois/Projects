package com.espresso.cardgame;

import java.util.Scanner;

public class Game {

    public static void play() {

        // 내 닉네임 입력
        System.out.print("내가 사용할 닉네임을 입력해 주세요: ");
        Scanner scan = new Scanner(System.in);
        String my_name = scan.nextLine() + "(본인)";

        // 딜러 생성
        Dealer dealer = new Dealer();

        // 함께 플레이 할 플레이어 닉네임 입력
        String answer;
        while (true) {
            System.out.print("함께 플레이 할 Player를 추가하시겠습니까? (yes / no): ");
            answer = scan.nextLine();
            if (answer.equalsIgnoreCase("yes")) {
                System.out.print("Player의 닉네임을 입력해 주세요: ");
                new Player(scan.nextLine());
            } else if (answer.equalsIgnoreCase("no")) {
                break;
            } else {
                System.out.println("잘못된 입력입니다. 다시 입력해 주세요.");
            }
        }

        // 나 생성
        new Player(my_name);

        // 게임
        dealer.gambling();
    }

    public static void main(String[] args){
        play();
    }

}