import java.util.Scanner;

public class MainOption {

    public static void main(String[] args) {
        System.out.println("＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊나의 마음 일지＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊");
        Pause timePause = new Pause(500);
        System.out.println("반가워요.");
        timePause.pause(1000);
        System.out.println("오늘 하루도 고생했어요.");
        timePause.pause(1000);
        System.out.println("원하는 메뉴를 선택해 주세요.");

        // 메인
        outer: while (true) {
            System.out.println("---------------------------------------------------------------");
            System.out.println("1. 감정 기록하기 2. 감정 설명서 읽기 3. 종료");
            System.out.println("---------------------------------------------------------------");
            System.out.print("무엇을 하고 싶으신가요? : ");

            Scanner scan = new Scanner(System.in);
            String choice = scan.nextLine();

            switch (choice) {
                case "1" -> {
                    // 기록
                   RecordEmotion recordEmo = new RecordEmotion();
                   recordEmo.record();
                }
                case "2" -> {
                    // 감정 설명서
                    Emotion introduceEmo = new Emotion();
                    introduceEmo.introduce();
                }
                case "3" -> {
                    // 종료
                    break outer;
                }
                default -> System.out.println("잘못 입력하셨습니다. 숫자 1~3 중에 하나를 선택해 입력하세요.");
            }
        }
        System.out.println("오늘 하루도 고생했어요.");
        System.out.println("See you tomorrow!");
    }

}
