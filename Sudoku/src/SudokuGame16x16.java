import javax.swing.*; // загрузка необходимых библиотек
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class SudokuGame16x16 extends SudokuAbstract { // класс, где реализация игры 16x16
    public SudokuGame16x16(JFrame jFrame)  { // создание окна
        setTitle("Уровень: Сложный");
        setSize(1080, 720);
        setResizable(false);
        ImageIcon icon = new ImageIcon("C:\\Users\\SAMIR\\IdeaProjects\\Sudoku\\icon.png"); // Установка иконки окна
        setIconImage(icon.getImage());
        size = 16;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        board = new int[size][size];
        this.jFrame = jFrame;
        board1 = new int[size][size];
        grid = new JTextField[size][size];
        initialCells = new boolean[size][size];
        correctCells = new int[size][size];
        JPanel gridPanel = new JPanel(new GridLayout(size,size));
        Font font = new Font("Times New Roman", Font.BOLD, 24);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                grid[row][col] = new JTextField(1);
                grid[row][col].setFont(font);
                grid[row][col].setHorizontalAlignment(JTextField.CENTER);
                ((AbstractDocument) grid[row][col].getDocument()).setDocumentFilter(new SudokuCellFilter()); // Добавляем DocumentFilter для ограничения ввода одного символа
                gridPanel.add(grid[row][col]);
            }
        }
        JButton solveButton = new JButton("Решить");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveSudoku("Сложный: ","recordsHard.txt");
            }
        });
        JButton backButton = new JButton("Назад");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(true);
                dispose();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(solveButton);
        buttonPanel.add(backButton);
        timerLabel = new JLabel("Время: 00:00:00");
        buttonPanel.add(timerLabel);
        add(gridPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
        generateSudoku(126);
        startTimer();
    }
    void applyBlockBorders(int row, int col) { // прорисовка границ ячеек
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        if ((row + 1) % 4 == 0 && (col + 1) % 4 == 0) {
            border = BorderFactory.createMatteBorder(1, 1, 6, 6, Color.BLACK);
        }
        else if (((row + 1) % 4 == 0) && ((col + 1) % 4 == 1)) {
            border = BorderFactory.createMatteBorder(1, 6, 6, 1, Color.BLACK);
        }
        else if (((row + 1) % 4 == 0) && ((col + 1) % 4 == 2)) {
            border = BorderFactory.createMatteBorder(1, 1, 6, 1, Color.BLACK);
        }
        else if (((row + 1) % 4 == 0) && ((col + 1) % 4 == 3)) {
            border = BorderFactory.createMatteBorder(1, 1, 6, 1, Color.BLACK);
        }
        else if (((row + 1) % 4 == 1) && ((col + 1) % 4 == 0)) {
            border = BorderFactory.createMatteBorder(6, 1, 1, 6, Color.BLACK);
        }
        else if (((row + 1) % 4 == 1) && ((col + 1) % 4 == 1)) {
            border = BorderFactory.createMatteBorder(6, 6, 1, 1, Color.BLACK);
        }
        else if (((row + 1) % 4 == 1) && ((col + 1) % 4 == 2)) {
            border = BorderFactory.createMatteBorder(6, 1, 1, 1, Color.BLACK);
        }
        else if (((row + 1) % 4 == 1) && ((col + 1) % 4 == 3)) {
            border = BorderFactory.createMatteBorder(6, 1, 1, 1, Color.BLACK);
        }
        else if (((row + 1) % 4 == 2) && ((col + 1) % 4 == 0)) {
            border = BorderFactory.createMatteBorder(1, 1, 1, 6, Color.BLACK);
        }
        else if (((row + 1) % 4 == 2) && ((col + 1) % 4 == 1)) {
            border = BorderFactory.createMatteBorder(1, 6, 1, 1, Color.BLACK);
        }
        else if (((row + 1) % 4 == 2) && ((col + 1) % 4 == 2)) {
            border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);
        }
        else if (((row + 1) % 4 == 2) && ((col + 1) % 4 == 3)) {
            border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);
        }
        else if (((row + 1) % 4 == 3) && ((col + 1) % 4 == 0)) {
            border = BorderFactory.createMatteBorder(1, 1, 1, 6, Color.BLACK);
        }
        else if (((row + 1) % 4 == 3) && ((col + 1) % 4 == 1)) {
            border = BorderFactory.createMatteBorder(1, 6, 1, 1, Color.BLACK);
        }
        else if (((row + 1) % 4 == 3) && ((col + 1) % 4 == 2)) {
            border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);
        }
        else if (((row + 1) % 4 == 3) && ((col + 1) % 4 == 3)) {
            border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);
        }
        grid[row][col].setBorder(border);
    }
    private class SudokuCellFilter extends DocumentFilter { // класс для ограничения ввода только чисел от 1 до 16
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            String newValue = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
            if (newValue.length() <= 2 && newValue.matches("(1[0-6]?|[1-9])")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}