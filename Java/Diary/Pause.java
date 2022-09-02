public class Pause {
    int time;

    Pause(int time) {
        this.time = time;
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void pause(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
