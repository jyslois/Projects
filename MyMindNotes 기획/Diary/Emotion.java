import java.util.Scanner;

public class Emotion {
    void introduce() {
        Scanner scan = new Scanner(System.in);
        String emotionChoice;

        System.out.println("======================================================================================");
        System.out.println("[[[[[[[[[[감정 설명서 : 로버트 플루치크의 감정의 바퀴]]]]]]]]]]");

        outer: while(true) {
            System.out.println("설명받고 싶은 감정을 골라 주세요. (모든 감정에 대한 설명은 0, 종료는 9 입력)");
            System.out.println("0. 모든 감정 1. 기쁨 2. 기대 3. 신뢰 4. 놀라움 5. 슬픔 6. 혐오 7. 공포 8. 분노 9. Exit(종료)");
            System.out.print("내가 설명 듣고 싶은 감정은: ");
            emotionChoice = scan.nextLine();

            switch (emotionChoice) {
                case "1" -> {
                    // 기쁨
                    joy();
                }
                case "2" -> {
                    // 기대
                    anticipation();
                }
                case "3" -> {
                    // 신뢰
                    trust();
                }
                case "4" -> {
                    // 놀라움
                    surprise();
                }
                case "5" -> {
                    // 슬픔
                    sadness();
                }
                case "6" -> {
                    // 혐오
                    disgust();
                }
                case "7" -> {
                    // 공포
                    fear();
                }
                case "8" -> {
                    // 분노
                    anger();
                }

                // 설명 전체 보기
                case "0" -> {
                    // 1. 기쁨 2. 기대 3. 신뢰 4. 놀라움 5. 슬픔 6. 혐오 7. 공포 8. 분노
                    joy();
                    anticipation();
                    trust();
                    surprise();
                    sadness();
                    disgust();
                    fear();
                    anger();

                }
                case "9" -> {
                    System.out.println("감정 설명서를 종료합니다.");
                    break outer;
                }
                default -> System.out.println("잘못 입력하셨습니다. 숫자 0~9 중에 하나를 입력해 주세요.");
            }
        }
    }

    // 감정 설명 메서드
    static void joy() {
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.println("1) 기쁨(joy)");
        System.out.println("기쁨은 욕구가 충족되었을 때 가지는 흐뭇하고 흡족한 마음이나 느낌이며,");
        System.out.println("기쁨의 감정이 생겨나는 이유는 \"무엇이 중요하진 기억하기 위해\"서입니다.");
        System.out.println("나는 어떤 욕구가 충족되었기 때문에 기쁨을 느꼈을까요?");
        System.out.println("그 욕구는 나에게 얼마나 중요하며, 이유는 무엇일까요.");
        System.out.println("--------------------------------------------------------------------------------------");
    }

    static void anticipation() {
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.println("2) 기대(anticipation)");
        System.out.println("기대는 어떤 일이 원하는 대로 이루어지기를 바라는 마음이나 느낌이며, ");
        System.out.println("기대의 감정이 생겨나는 이유는 \"무언가를 소망하거나 계획하기 위해\"서입니다.");
        System.out.println("나는 무엇을 소망하고 있나요? 그 일이 어떻게 이루어지기를 바라고 있나요.");
        System.out.println("--------------------------------------------------------------------------------------");
    }

    static void trust() {
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.println("3) 신뢰(trust)");
        System.out.println("신뢰란 타인의 미래 행동이 자신에게 호의적이거나,");
        System.out.println("또는 최소한 악의적이지는 않을 가능성에 대한 기대와 믿음을 말합니다. ");
        System.out.println("즉, 신뢰는 상대가 어떻게 행동할 것이라는 믿음 하에 상대방의 협조를 기대하는 것입니다.");
        System.out.println("신뢰의 감정이 생겨나는 이유는 \"도와주는 사람과 이어지기 위해\"서입니다.");
        System.out.println("나는 어떤 사람에게 신뢰가 생겼나요? 그 이유는 무엇인가요.");
        System.out.println("내가 그 사람에게 신뢰를 얻고, 좋은 관계를 유지하려면 어떻게 해야 할까요?");
        System.out.println("--------------------------------------------------------------------------------------");
    }

    static void surprise() {
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.println("4) 놀라움(surprise)");
        System.out.println("놀라움은 기대하지 않던 일을 겪게 될 때 느끼는 감정입니다.");
        System.out.println("놀라움이 과대하면 충격에 빠지게 되는데, 놀라움은 기대와 현실 간의 차이를 나타내며,");
        System.out.println("세상 사건에 관한 우리의 추정과 기대 사이의 틈이자 그러한 사건들이 실제로 발생되는 방식입니다.");
        System.out.println("놀람의 감정이 생겨나는 이유는 \"새로운 것에 집중하게 위해\"서입니다.");
        System.out.println("나는 어떤 사실/일에 놀랐나요? 새로운 사실/일은 앞으로 나에게 어떤 영향을 미칠까요.");
        System.out.println("--------------------------------------------------------------------------------------");
    }

    static void sadness() {
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.println("5) 슬픔(sadness)");
        System.out.println("슬픔은 자신, 또는 남의 불행이나 실패의 경험, 예측 또는 회고(回顧)를 수반한 우울한 정서를 말합니다.");
        System.out.println("탈력감, 실망감이나 좌절감을 동반하고 가슴이 맺히는 등의 신체적 감각과 함께 눈물이 나오고,");
        System.out.println("표정이 굳어지며 의욕, 행동력, 운동력 저하 등이 관찰될 수 있습니다.");
        System.out.println("슬픔의 감정이 생겨나는 이유는 \"사랑하는 것과 이어지기 위해\"서입니다.");
        System.out.println("내가 슬픈 이유는 무엇인가요. 충분히 슬퍼한 다음에, 마음이 나아지려면 어떻게 해야 할까요.");
        System.out.println("--------------------------------------------------------------------------------------");
    }

    static void disgust() {
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.println("6) 혐오(disgust)");
        System.out.println("혐오는 어떠한 것을 미워하고 싫어하거나 꺼리고 기피하는 감정입니다.");
        System.out.println("심리학에서 혐오란 인류가 진화하면서 터득한 감정으로,");
        System.out.println("가까이 하면 신체적, 사회적 병해를 입게 되는 대상을 멀리하는 감정입니다.");
        System.out.println("혐오의 감정이 생겨나는 이유는 \"해로운 것을 거부하기 위해\"서입니다.");
        System.out.println("내가 그것, 또는 그 사람을 싫어하는 이유는 무엇인가요.");
        System.out.println("--------------------------------------------------------------------------------------");
    }

    static void fear() {
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.println("7) 공포(fear)");
        System.out.println("공포는 특정한 대상이나 상황에 대해 나타나는 두려움을 뜻하며,");
        System.out.println("공포의 감정이 생겨나는 이유는 \"위험으로부터 우리를 보호하기 위해\"서입니다.");
        System.out.println("그 것/사람에 대해 내가 느낀 공포는 일회성인가요, 아니면 지속적으로 일어나는 감정인가요.");
        System.out.println("나는 왜 그 사람 혹은 상황에 대해 공포를 느꼈을까요.");
        System.out.println("공포를 잘 다루기 위해서는 어떻게 해야 할까요.");
        System.out.println("--------------------------------------------------------------------------------------");
    }

    static void anger() {
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.println("8) 분노(anger)");
        System.out.println("분노는 어떤 불만, 불평에 대한 감정적인 반응입니다.");
        System.out.println("분노란 때론 생리적인 욕구를 포함하여 자신의 욕구를 방해할 경우에 생기는 것으로 알려져 있습니다.");
        System.out.println("분노의 감정이 생겨나는 이유는 \"문제에 맞서 싸우기 위해\"서입니다.");
        System.out.println("내가 분노한 이유는 무엇인가요? 나는 노여음을 표현했나요. 했다면 어떻게 표현했나요.");
        System.out.println("해결하고 싶은 문제가 있었나요. 그 문제와 맞서 싸우려면 앞으로 나는 어떻게 해야 할까요.");
        System.out.println("--------------------------------------------------------------------------------------");
    }

}
