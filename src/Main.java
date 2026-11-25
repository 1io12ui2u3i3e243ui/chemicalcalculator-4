import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Main extends JFrame {

    public Main() {
        setTitle("Химический калькулятор");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showMenu();
        setVisible(true);
    }

    Color background = new Color(220, 235, 250);
    public void showMenu() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(background);
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttons.setBackground(background);
        JButton start = new JButton("Калькулятор");
        JButton exit = new JButton("Выход");
        start.setPreferredSize(new Dimension(140, 40));
        exit.setPreferredSize(new Dimension(140, 40));
        buttons.add(start);
        buttons.add(exit);
        panel.add(buttons);
        start.addActionListener(e -> showCalculator());
        exit.addActionListener(e -> System.exit(0));
        setContentPane(panel);
        revalidate();
        repaint();
    }
    public void showCalculator() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(background);

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout(0, 15));
        content.setPreferredSize(new Dimension(620, 270));
        content.setBackground(background);
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        inputPanel.setBackground(background);
        JLabel label = new JLabel("Введите формулу:");
        JTextField input = new JTextField(20);
        JButton enter = new JButton("Ввод");
        enter.setPreferredSize(new Dimension(100, 35));
        inputPanel.add(label);
        inputPanel.add(input);
        inputPanel.add(enter);
        JTextArea result = new JTextArea();
        result.setEditable(false);
        result.setLineWrap(true);
        result.setWrapStyleWord(true);
        result.setText("Введите формулу и нажмите «Ввод».\n"
                + "Примеры: H2O, CO2, Fe2O3, CH3COOH.\n"
                + "Пока поддерживаются формулы без скобок.");

        JScrollPane scrollPane = new JScrollPane(result);

        JPanel bottomPanel = new JPanel();
        JButton back = new JButton("Назад");
        back.setPreferredSize(new Dimension(140, 40));
        bottomPanel.add(back);

        bottomPanel.setBackground(background);

        content.add(inputPanel, BorderLayout.NORTH);
        content.add(scrollPane, BorderLayout.CENTER);
        content.add(bottomPanel, BorderLayout.SOUTH);
        panel.add(content);
        enter.addActionListener(e -> {
            String formula = input.getText().trim();
            result.setText(countAtoms(formula));
        });
        input.addActionListener(e -> enter.doClick());
        back.addActionListener(e -> showMenu());

        setContentPane(panel);
        revalidate();
        repaint();
    }
    public static String countAtoms(String formula) {
        if (formula.isEmpty()) {
            return "Ошибка: сначала введите химическую формулу.";
        }
        ArrayList<String> elements = new ArrayList<>();
        ArrayList<Integer> counts = new ArrayList<>();

        int i = 0;
        while (i < formula.length()) {
            char letter = formula.charAt(i);

            // Каждый новый элемент начинается с заглавной латинской буквы.
            if (letter < 'A' || letter > 'Z') {
                return "Ошибка в формуле. Пример правильного ввода: H2O.\n"
                        + "Пока нельзя вводить скобки, реакции и пробелы внутри формулы.\n"
                        + "Используйте латиницу.";
            }

            String element = "" + letter;
            i++;
            if (i < formula.length()) {
                char next = formula.charAt(i);
                if (next >= 'a' && next <= 'z') {
                    element += next;
                    i++;
                }
            }
            int quantity = 0;
            boolean hasNumber = false;
            while (i < formula.length()) {
                char digit = formula.charAt(i);

                if (digit < '0' || digit > '9') {
                    break;
                }

                if (!hasNumber && digit == '0') {
                    return "Ошибка: число атомов элемента не должно начинаться с нуля!";
                }

                hasNumber = true;
                if (quantity > 100000) {
                    return "Ошибка: слишком большое количество атомов.";
                }
                quantity = quantity * 10 + (digit - '0');
                i++;
            }
            if (!hasNumber) {
                quantity = 1;
            }
            if (quantity == 0) {
                return "Ошибка: число атомов не может быть нулём.";
            }
            int index = elements.indexOf(element);
            if (index == -1) {
                elements.add(element);
                counts.add(quantity);
            } else {
                counts.set(index, counts.get(index) + quantity);
            }
        }

        String answer = "В формуле " + formula + ":\n\n";
        for (int j = 0; j < elements.size(); j++) {
            int count = counts.get(j);
            answer += "Элемент " + elements.get(j) + " — " + count
                    + " " + atomWord(count) + "\n";
        }
        return answer;
    }
    public static String atomWord(int count) {
        if (count % 100 >= 11 && count % 100 <= 14) {
            return "атомов";
        }
        if (count % 10 == 1) {
            return "атом";
        }
        if (count % 10 >= 2 && count % 10 <= 4) {
            return "атома";
        }
        return "атомов";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}
