import javax.swing.*; // загрузка необходимых библиотек
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class SudokuGame4x4 extends SudokuAbstract { // класс, где реализация игры 4x4
    public SudokuGame4x4(JFrame jFrame) { // создание окна
        setTitle("Уровень: Лёгкий"); // заголовок
        setSize(1080, 720); // размер окна
        setResizable(false); // запрет изменения размера окна
        ImageIcon icon = new ImageIcon("C:\\Users\\SAMIR\\IdeaProjects\\Sudoku\\icon.png"); // Установка иконки окна
        setIconImage(icon.getImage());
        size = 4; // размер судоку 4x4
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // завершение при закрытии
        super.board = new int[size][size]; // инициализация доски
        this.jFrame = jFrame;
        board1 = new int[size][size]; // инициализация ещё одной доски для проверки
        grid = new JTextField[size][size]; // инициализация массива текстовых полей для ячеек
        initialCells = new boolean[size][size]; // Массив для отслеживания начальных ячеек судоку
        correctCells = new int[size][size]; // Массив для отслеживания правильных значений в ячейках
        JPanel gridPanel = new JPanel(new GridLayout(size,size)); // Панель сетки для размещения ячеек судоку
        Font font = new Font("Times New Roman", Font.BOLD, 108); // Шрифт для текстовых полей
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                grid[row][col] = new JTextField(1);
                grid[row][col].setFont(font);
                grid[row][col].setHorizontalAlignment(JTextField.CENTER);
                ((AbstractDocument) grid[row][col].getDocument()).setDocumentFilter(new SudokuCellFilter()); // Добавляем DocumentFilter для ограничения ввода одного символа
                gridPanel.add(grid[row][col]);
            }
        }
        JButton solveButton = new JButton("Решить"); // создание кнопки решить и обработчик действий
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveSudoku("Легкий: ","recordsEasy.txt");
            }
        });
        JButton backButton = new JButton("Назад"); // создание кнопки назад и обработчик действий
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(true);
                dispose();
            }
        });
        JPanel buttonPanel = new JPanel(); // создание панели сетки для размещения кнопок и метки времени
        buttonPanel.add(solveButton);
        buttonPanel.add(backButton);
        timerLabel = new JLabel("Время: 00:00:00");
        buttonPanel.add(timerLabel);
        add(gridPanel, BorderLayout.CENTER); // добавление панели сетки и панель кнопок
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true); // видимость окна
        generateSudoku(7); // генерация количества пустых ячеек
        startTimer(); // запуск таймера
    }
    void applyBlockBorders(int row, int col) { // прорисовка границ ячеек
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        if (((row + 1) % 2 == 0) && ((col + 1) % 2 == 0)) {
            border = BorderFactory.createMatteBorder(1, 1, 6, 6, Color.BLACK);
        }
        else if (((row + 1) % 2 == 1) && ((col + 1) % 2 == 1)) {
            border = BorderFactory.createMatteBorder(6, 6, 1, 1, Color.BLACK);
        }
        else if ((row + 1) % 2 == 0) {
            border = BorderFactory.createMatteBorder(1, 6, 6, 1, Color.BLACK);
        }
        else if ((row + 1) % 2 == 1) {
            border = BorderFactory.createMatteBorder(6, 1, 1, 6, Color.BLACK);
        }
        grid[row][col].setBorder(border);
    }
    protected class SudokuCellFilter extends DocumentFilter { // класс для ограничения ввода только цифр от 1 до 4
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            String newValue = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
            if (newValue.length() <= 1 && newValue.matches("[1-4]?")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}