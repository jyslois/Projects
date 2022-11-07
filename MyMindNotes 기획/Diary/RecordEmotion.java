import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class RecordEmotion {
    static Scanner scan = new Scanner(System.in);
    static String recordChoice = "", time = "", situation = "", thought = "", emotion = "", reflection = "";

    static void print() {
        System.out.println("===================================================================");
        System.out.println("＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊나의 마음 일지＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊");
        Pause timePause = new Pause(1000);
        // 현재 날짜/시간
        LocalDateTime now = LocalDateTime.now();
        // 현재 날짜/시간 출력 포맷팅
        String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초"));
        // 포맷팅 현재 날짜/시간 출력
        System.out.println("기록 날짜: " + formatedNow); // 2021년 12월 02일 18시 19분 36초
        timePause.pause(1000);
        System.out.println("시점 : " + time);
        timePause.pause(1000);
        System.out.println("상황 : " + situation);
        timePause.pause(1000);
        System.out.println("나의 생각 : " + thought);
        timePause.pause(1000);
        System.out.println("나의 감정 : " + emotion);
        timePause.pause(1000);
        System.out.println("회고 : " + reflection);
        timePause.pause(1000);
        System.out.println("＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊나의 마음 일지＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊");

        System.exit(0);
    }

    void record() {
        System.out.println("======================================================================================");
        System.out.println("[[[[[[[[[[감정 기록 : 나의 마음 일지]]]]]]]]]]");

        outer2 : while (true) {
            System.out.println("이야기의 시점은 과거인가요, 현재인가요?");
            System.out.println("1. 과거 2. 현재 3. 종료(Exit)");
            time = scan.nextLine();

            if (time.equals("과거") || time.equals("1")) {
                time = "과거";
                System.out.println("상황 ▶ 과거에 어떤 일이 있었나요.");
                System.out.println("상황 ▶ 감정을 최대한 배제하고 육하원칙을 생각하며 작성해 주세요. ");
                System.out.print("과거에 일어난 일 : ");
                situation = scan.nextLine();
                System.out.println("-------------------------------------------------------------------");

                System.out.println("생각 ▶ 그 상황이 발생했을 때, 나는 머릿속으로 어떤 생각을 했나요.");
                System.out.println("만약 그때 당시에 들었던 생각과 현재 생각이 다르다면, 어떻게 다른지 적어주세요.");
                System.out.print("과거 상황에 대한 생각 : ");
                thought = scan.nextLine();
                System.out.println("-------------------------------------------------------------------");

                System.out.println("감정 ▶ 그 생각으로 인해 내가 느꼈던 감정을 기록해 주세요. ");
                inner : while(true) {
                    System.out.println("감정 예시로는 기쁨, 기대, 신뢰, 놀라움, 슬픔, 혐오, 공포, 분노 등등이 있습니다.");
                    System.out.println("한 가지 이상의 감정이면 더 좋아요.");
                    System.out.print("바로 작성하고 싶다면 1을, 감정 설명서를 읽고 싶다면 2를 입력해 주세요 : ");

                    switch(recordChoice = scan.nextLine()) {
                        case "2" -> {
                            new Emotion().introduce();
                        }
                        case "1" -> {
                            System.out.print("과거 상황에 대해 든 감정(나의 마음) : ");
                            emotion = scan.nextLine();
                            break inner;
                        }
                        default -> {
                            System.out.println("잘못 입력하셨습니다. 다시 선택해 주세요.");
                        }
                    }
                }
                System.out.println("-------------------------------------------------------------------");

                System.out.println("회고 ▶ 과거의 상황이 현재까지 영향을 미치고 있지는 않은지 생각해 봅시다.");
                System.out.println(">>>>>회고는 필수는 아니며, 필요하다고 생각될 때만 작성해 주세요<<<<<");
                System.out.println("과거의 일이 현재 나의 성격, 우선순위, 사고방식, 인간관계 등등, \"나\"와 \"내 삶\"에 어떤 영향을 끼치고 있나요.");
                System.out.println("만약 그 영향이 부정적이라면, 어떻게 긍정적인 애너지로 바꿀 수 있을까요.");
                System.out.println("지금 나에게 필요한 것은 무엇일까요.");
                System.out.print("과거 상황에 대한 회고 : ");
                reflection = scan.nextLine();
                System.out.println("-------------------------------------------------------------------");
                print();
                break outer2;

            } else if (time.equals("현재") || time.equals("2")) {
                time = "현재";
                System.out.println("상황 ▶ 어떤 일이 있었나요.");
                System.out.println("상황 ▶ 감정을 최대한 배제하고 육하원칙을 생각하며 작성해 주세요. ");
                System.out.print("상황 : ");
                situation = scan.nextLine();
                System.out.println("-------------------------------------------------------------------");

                System.out.println("생각 ▶ 그 상황이 발생했을 때, 나는 머릿속으로 어떤 생각을 했나요.");
                System.out.println("만약 그때 당시에 들었던 생각과 현재 생각이 다르다면, 어떻게 다른지 적어주세요.");
                System.out.print("상황에 대한 생각 : ");
                thought = scan.nextLine();
                System.out.println("-------------------------------------------------------------------");

                System.out.println("감정 ▶ 그 생각으로 인해 내가 느꼈던 감정을 기록해 주세요. ");
                inner : while(true) {
                    System.out.println("감정 예시로는 기쁨, 기대, 신뢰, 놀라움, 슬픔, 혐오, 공포, 분노 등등이 있습니다.");
                    System.out.println("한 가지 이상의 감정이면 더 좋아요.");
                    System.out.print("바로 작성하고 싶다면 1을, 감정 설명서를 읽고 싶다면 2를 입력해 주세요 : ");

                    switch(recordChoice = scan.nextLine()) {
                        case "2" -> {
                            new Emotion().introduce();
                        }
                        case "1" -> {
                            System.out.print("상황에 대해 든 감정(나의 마음) : ");
                            emotion = scan.nextLine();
                            break inner;
                        }
                        default -> {
                            System.out.println("잘못 입력하셨습니다. 다시 선택해 주세요.");
                        }
                    }
                }
                System.out.println("-------------------------------------------------------------------");

                System.out.println("회고 ▶ 나는 왜 그런 감정이 들었을까요?");
                System.out.println(">>>>>회고는 필수는 아니며, 필요하다고 생각될 때만 작성해 주세요<<<<<");
                System.out.println("왜 그런 생각과 감정이 들었는지 스스로를 이해해 봅시다.");
                if (emotion.contains("혐오") || emotion.contains("공포") || emotion.contains("분노")) {
                    System.out.println("사람의 모든 감정은 잘못된 것이 없으며, 모든 마음은 존중받아야 마땅합니다.");
                    System.out.println("하지만 지속적인 감정이 나 자신이나 타인을 불편하게 한다면,");
                    System.out.println("나 자신이 평안하게 살아가기 위해서 한번 생각해 볼 필요가 있습니다.");
                    System.out.println("모든 감정은 생각으로부터 비롯됩니다.");
                    System.out.println("나의 생각 패턴, 사고 방식에 대해서 생각해 봅시다. 나는 왜 그런 생각이 들은 걸까요.");
                    System.out.println("나는 왜 그런 감정들을 느꼈을까요.");
                    System.out.println("혹시 마음 속 깊숙하게 숨겨져 있는 진짜 생각이나, 감정이 있지는 않나요.");
                    System.out.println("만약 그렇다면, 원인은 무엇일까요.");
                    System.out.println("달리 생각할 수 있었던 문제일까요. 달리 받아들일 수도 있는 문제였나요.");
                    System.out.println("타인의 입장에서 상황을 본다면 달리 보일 수 있는 문제인가요.");
                    System.out.println("지금 나에게 필요한 것은 무엇인가요.");
                }

                System.out.print("상황에 대한 회고 : ");
                reflection = scan.nextLine();
                System.out.println("-------------------------------------------------------------------");
                print();
                break outer2;
            } else if (time.equals("3")) {
                break outer2;
            } else {
                System.out.println("잘못 입력하셨습니다. 다시 입력해 주세요.");
            }
        }
    }

}