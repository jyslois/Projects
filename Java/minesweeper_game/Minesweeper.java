package study.minesweeper_game;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Minesweeper {

    public static void main(String[] args) {
        // 폭탄은 *, 열린 칸은 -, 깃발은 &

        // 1. 2차원 배열 선언과 초기화
        char[][] land = new char[5][5];
        // 1-1. 플레이어용 배열 선언
        char[][] player_land = new char[5][5];

        // 2. 폭탄수 정하기(최소 1부터 최대 5까지)
        // - Random 클래스 객체 만들고 폭탄수 랜덤으로 설정하기
        Random random = new Random();
        int bomb_num = random.nextInt(1, 5);
        System.out.println("랜덤 폭탄 수 (1~4): " + bomb_num);

        // 깃발의 최대 갯수는 랜덤 폭탄 수 + 1
        int max_flag = bomb_num + 1;
        System.out.println("사용 가능한 깃발 수 : " + max_flag);

        // 3. 랜덤으로 설정한 폭탄수만큼 랜덤 위치에 폭탄 설치하기
        // 랜덤으로 설정한 폭탄수만큼 랜덤 위치 뽑고, 그 랜덤 위치와 일치하면 1
        // 아니면 0으로 배열 값 초기화하기

        ArrayList<Integer> row = new ArrayList<>();
        ArrayList<Integer> column = new ArrayList<>();

        for (int b = 1; b <= bomb_num; b++) {
            // 랜덤으로 설정한 폭탄수 만큼 랜덤 위치를 뽑는데,
            // 그 랜덤 위치가 겹치지 않기 위해서 각 row와 column 어레이리스트를 만들어
            // 두 어레이리스트에 존재하면 다시 뽑아 값을 저장하고 (그 후에 값을 각각 어레이리스트에 추가하고),
            // 존재하지 않는다면 그대로 저장하는 것 (그대로 어레리스트에 추가)
            // 랜덤으로 row와 column 인덱스 뽑기
            int random_row = random.nextInt(0, 4);
            int random_column = random.nextInt(0, 4);
            // 랜덤으로 뽑은 것이 어레이리스트에 존재하지 않을 때까지 새로 뽑아 저장하기
            while (row.contains(random_row) && column.contains(random_column)) {
                random_row = random.nextInt(0, 4);
                random_column = random.nextInt(0, 4);
            }
            // 어레이리스트에 추가하기
            row.add(random_row);
            column.add(random_column);

            for (int i = 0; i < land.length; i++) { // 행
                for (int j = 0; j < land.length; j++) { // 열
                    if (i == random_row && j == random_column) {
                        // 폭탄이 설치되어 있지 않은 경우, 행과 열이 렌덤인덱스와 일치한 경우
                        land[i][j] = '*';
                    } else if (land[i][j] == '\u0000') {
                        land[i][j] = '-';
                    }
                }
            }
        }
//        // 폭탄 배열 출력하기
//        for (char[] temp : land) {
//            for (char i : temp) {
//                System.out.print(i + " ");
//            }
//            System.out.println();
//        }

        System.out.println("=======게임 시작========");

        // player_land 출력
        for (char[] temp : player_land) {
            for (char i : temp) {
                System.out.print(i + " ");
            }
            System.out.println();
        }

        // 좌표 입력 받기
        int x = 0;
        int y = 0;

        outer:
        while (true) {
            Scanner scan = new Scanner(System.in);
            System.out.print("열어 보고 싶은 칸의 좌표를 입력하세요 : ");
            String coordinates = scan.nextLine();

            if (coordinates.length() == 2) {
                x = coordinates.charAt(0) - '0';
                y = coordinates.charAt(1) - '0';
            }

            // 따로 검사해야 하기 때문에 else if가 아닌 if문 사용
            if (coordinates.length() != 2 || x < 0 || y < 0 || x >= land.length || y >= land[0].length) {
                System.out.println("잘못된 입력입니다. 다시 입력해 주세요.");
                continue;
            }

            // 입력 받은 좌표에 폭탄(*)이 있으면 gameover 출력,
            // 입력 받은 좌표 근처 8칸에 폭탄(*)이 있으면 좌표에 해당 폭탄 수만큼 나타내기.
            // 좌표 근처 8칸이 범위에서 벗어나면 if문 안에 boolean을 이용해서 예외처리 해 주기.
            int bomb = 0;
            int peri_bomb = 0;

            boolean error = true;

            if (land[x][y] == '*') {
                System.out.println("폭탄입니다! Game Over");
                System.out.println("폭탄 위치: ");
                for (char[] tmp : land) {
                    for (char i : tmp) {
                        System.out.print(i + " ");
                    }
                    System.out.println();
                }
                break;
            } else {
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        if (i < 0 || i >= land.length || j < 0 || j >= land[0].length) {
                            error = false;    // 범위를 벗어났을 경우에 예외처리를 위한 boolean
                        } else {
                            if (land[i][j] == '*') {
                                bomb++;
                            }
                        }
                    }
                }
            }


            if (bomb != 0) { // x,y 주변에 폭탄이 하나라도 있다면
                player_land[x][y] = (char) (bomb + '0');  // 폭탄 숫자를 문자로 변경해서 넣기
            } else { // x, y 주변에 폭탄이 하나도 없으면 (빈칸)
                player_land[x][y] = '-';
                // x, y 주변 8칸의 주변 8칸을 검사한다
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        peri_bomb = 0;
                        if (i < 0 || i >= land.length || j < 0 || j >= land[0].length) {
                            error = false;    // 범위를 벗어났을 경우에 예외처리를 위한 boolean
                        } else {
                            for (int k = i - 1; k <= i + 1; k++) {
                                for (int q = j - 1; q <= j + 1; q++) {
                                    if (k < 0 || k >= land.length || q < 0 || q >= land[0].length) {
                                        error = false;    // 범위를 벗어났을 경우에 예외처리를 위한 boolean
                                    } else {  // 만약 주변 8칸의 8칸에 폭탄이 있다면,
                                        if (land[k][q] == '*') {
                                            peri_bomb++;  // 폭탄 수를 늘려주고
                                        }
                                    }
                                }
                            }
                            if (peri_bomb != 0) { // 새로운 주변8칸을 탐색하기 전에 만약 폭탄이 하나라도 있으면
                                if (player_land[i][j] == player_land[x][y]) {
                                    error = false; // 일단 x,y는 제외시켜야 하니까 빼주고
                                } else {
                                    player_land[i][j] = (char) (peri_bomb + '0'); // 폭탄 수를 그 주변 칸에 입력
                                }
                            } else {
                                player_land[i][j] = '-'; // 폭탄이 없다면 '-'로 표시해서 칸 열어주기
                            }


                        }
                    }
                }

            }

            // 배열 출력하기
            for (char[] tem : player_land) {
                for (char i : tem) {
                    System.out.print(i + " ");
                }
                System.out.println();
            }

            // 깃발 설치
            // 깃발 좌표 a, b
            int a = 0;
            int b = 0;


            // 깃발 수가 0이 될 때까지 반복
            while (max_flag > 0) {
                System.out.println("남은 깃발 수 : " + max_flag + " 개.");
                System.out.print("깃발을 설치하시겠습니까? (예는 y, 아니요는 n 입력): ");
                String answer = scan.nextLine();

                if (answer.equalsIgnoreCase("n")) {
                    break;
                } else if (answer.equalsIgnoreCase("y")) {
                    System.out.print("깃발의 좌표를 입력해 주세요: ");
                    String flag = scan.nextLine();

                    if (flag.length() == 2) {
                        a = flag.charAt(0) - '0';
                        b = flag.charAt(1) - '0';
                    }

                    if (flag.length() != 2 || a < 0 || b < 0 || a >= land.length || b >= land[0].length || !(0 <= a && a <= 9) || !(0 <= b && b <= 9)) {
                        System.out.println("잘못된 입력입니다. 다시 입력해 주세요.");
                        continue;
                    }

                    if ('0' <= player_land[a][b] && player_land[a][b] <= '9' || player_land[a][b] == '-') {
                        System.out.println("해당 위치에는 깃발을 설치할 수가 없습니다.");
                        continue;
                    }

                    if (player_land[a][b] == '&') {
                        System.out.println("이미 깃발이 꽂힌 자리입니다.");
                        continue;
                    }

                    player_land[a][b] = '&';
                    max_flag--;  // 깃발 수 빼 주기

                    // 배열 출력하기
                    for (char[] tmp : player_land) {
                        for (char i : tmp) {
                            System.out.print(i + " ");
                        }
                        System.out.println();
                    }

                } else {
                    System.out.println("잘못 입력하셨습니다.");
                }

                // 승리조건: 모든 폭탄의 위치에 깃발이 존재한다면 승리.
                int bomb_count = 0;

                // row와 column엔 각각 폭탄의 x,y좌표가 담겨 있음
                for (int i = 0; i < row.size(); i++) {
                    if (player_land[row.get(i)][column.get(i)] == '&') {
                        bomb_count++;
                    }
                }
                if (bomb_count == row.size()) {
                    System.out.println("승리하셨습니다~! Victory!");
                    System.out.println("폭탄 위치: ");
                    for (char[] tmp : land) {
                        for (char i : tmp) {
                            System.out.print(i + " ");
                        }
                        System.out.println();
                    }
                    break outer;
                }

                // flag의 갯수가 0이면서 모든 폭탄의 위치에 깃발이 없다면 Game Over
                if (max_flag == 0 && bomb_count != row.size()) {
                    System.out.println("깃발을 모두 소진했지만 폭탄을 모두 찾지 못했습니다! Game Over");
                    System.out.println("폭탄 위치: ");
                    for (char[] tmp : land) {
                        for (char i : tmp) {
                            System.out.print(i + " ");
                        }
                        System.out.println();
                    }
                    break outer;

                }


            }
        }
    }
}