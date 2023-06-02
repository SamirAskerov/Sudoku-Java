import javax.swing.*; // загрузка необходимых библиотек
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class SudokuGame9x9 extends SudokuAbstract { // класс, где реализация игры 9x9
    public SudokuGame9x9(JFrame jFrame) { // создание окна
        setTitle("Уровень: Нормальный");
        setSize(1080, 720);
        setResizable(false);
        ImageIcon icon = new ImageIcon("C:\\Users\\SAMIR\\IdeaProjects\\Sudoku\\icon.png"); // Установка иконки окна
        setIconImage(icon.getImage());
        super.size = 9; // размер судоку size x size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        board = new int[size][size];
        this.jFrame = jFrame;
        board1 = new int[size][size];
        grid = new JTextField[size][size];
        initialCells = new boolean[size][size];
        correctCells = new int[size][size];
        JPanel gridPanel = new JPanel(new GridLayout(size,size));
        Font font = new Font("Times New Roman", Font.BOLD, 48);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                grid[row][col] = new JTextField(1);
                grid[row][col].setFont(font);
                grid[row][col].setHorizontalAlignment(JTextField.CENTER);
                // Добавляем DocumentFilter для ограничения ввода одного символа
                ((AbstractDocument) grid[row][col].getDocument()).setDocumentFilter(new SudokuCellFilter());
                gridPanel.add(grid[row][col]);
            }
        }
        JButton solveButton = new JButton("Решить");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveSudoku("Нормальный: ","recordsNormal.txt");
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
        generateSudoku(39);
        startTimer();
    }
    void applyBlockBorders(int row, int col) { // прорисовка границ ячеек
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        if (((row + 1) % 3 == 0) && ((col + 1) % 3 == 0)) {
            border = BorderFactory.createMatteBorder(1, 1, 6, 6, Color.BLACK);
        }
        else if (((row + 1) % 3 == 0) && ((col + 1) % 3 == 1)) {
            border = BorderFactory.createMatteBorder(1, 6, 6, 1, Color.BLACK);
        }
        else if (((row + 1) % 3 == 0) && ((col + 1) % 3 == 2)) {
            border = BorderFactory.createMatteBorder(1, 1, 6, 1, Color.BLACK);
        }
        else if (((row + 1) % 3 == 1) && ((col + 1) % 3 == 0)) {
            border = BorderFactory.createMatteBorder(6, 1, 1, 6, Color.BLACK);
        }
        else if (((row + 1) % 3 == 1) && ((col + 1) % 3 == 1)) {
            border = BorderFactory.createMatteBorder(6, 6, 1, 1, Color.BLACK);
        }
        else if (((row + 1) % 3 == 1) && ((col + 1) % 3 == 2)) {
            border = BorderFactory.createMatteBorder(6, 1, 1, 1, Color.BLACK);
        }
        else if (((row + 1) % 3 == 2) && ((col + 1) % 3 == 0)) {
            border = BorderFactory.createMatteBorder(1, 1, 1, 6, Color.BLACK);
        }
        else if (((row + 1) % 3 == 2) && ((col + 1) % 3 == 1)) {
            border = BorderFactory.createMatteBorder(1, 6, 1, 1, Color.BLACK);
        }
        else if (((row + 1) % 3 == 2) && ((col + 1) % 3 == 2)) {
            border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);
        }
        grid[row][col].setBorder(border);
    }
    private class SudokuCellFilter extends DocumentFilter { // класс для ограничения ввода только цифр от 1 до 9
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            String newValue = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
            if (newValue.length() <= 1 && newValue.matches("[1-9]?")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}