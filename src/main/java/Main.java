import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("ZipFileEncryptor");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        GUIform form = new GUIform();
        frame.add(form.getRootPanel());
        frame.setResizable(false);
        frame.setVisible(true);
    }
}