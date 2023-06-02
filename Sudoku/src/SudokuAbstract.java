import javax.swing.*; // загрузка необходимых библиотек
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
public abstract class SudokuAbstract extends JFrame { // абстрактный класс
    protected Timer timer; // таймер для отслеживания прошедшего времени
    protected int secondsElapsed; // пройденное количество секунд
    protected boolean isSolved; // указывает на то что решена судоку или нет
    protected JTextField[][] grid; // сетка которую пользователь видит, где записываются цифры
    protected boolean[][] initialCells; // пустые ячейки или нет
    protected int[][] board; // состояние доски судоку
    protected int[][] board1; // то же состояние, но для сохранения и восстановления состояния доски
    protected int[][] correctCells; // тут правильные хранятся ответы, которые используются для проверки решения
    protected JLabel timerLabel; // отображение сколько времени прошло
    protected JFrame jFrame; // окно игры судоку
    protected int size; // размер длины сетки судоку
    private String getFormattedTime() { // форматирует время в формате чч:мм:сс и возвращает в виде строки
        int minutes = (secondsElapsed % 3600) / 60;
        int seconds = secondsElapsed % 60;
        int hours = secondsElapsed / 3600;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    private java.util.List<Integer> getAvailableNumbers(int[][] board, int row, int col) { // создание пустого списка где будут храниться числа для позиции на доске
        java.util.List<Integer> availableNumbers = new ArrayList<>();
        for (int num = 1; num <= size; num++) {
            if (isValid(board, row, col, num)) { // проверка на допустимость всех элементов
                availableNumbers.add(num);
            }
        }
        return availableNumbers; // возвращение списка
    }
    private boolean isValid(int[][] board, int row, int col, int num) { // Проверяем, что число num не повторяется в текущей строке, столбце и блоке size x size
        for (int i = 0; i < size; i++) { // Проверка по строке
            if (board[row][i] == num) {
                return false;
            }
        }
        for (int i = 0; i < size; i++) { // Проверка по столбцу
            if (board[i][col] == num) {
                return false;
            }
        }
        double block = Math.sqrt(size); // Проверка в текущем блоке size x size
        int square = (int) block;
        int blockRow = row / square * square;
        int blockCol = col / square * square;
        for (int i = 0; i < square; i++) {
            for (int j = 0; j < square; j++) {
                if (board[blockRow + i][blockCol + j] == num) {
                    return false;
                }
            }
        }
        return true; // Число num допустимо в данной позиции
    }
    private boolean solve(int[][] board) { // рекурсивное решение судоку
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] == 0) {
                    java.util.List<Integer> availableNumbers = getAvailableNumbers(board, row, col);
                    Collections.shuffle(availableNumbers); // Перемешиваем доступные числа
                    for (int num : availableNumbers) {
                        board[row][col] = num;
                        if (solve(board)) {
                            return true;
                        }
                        else {
                            board[row][col] = 0;
                        }
                    }
                    return false; // Если не удалось найти правильное число, возвращаемся на предыдущий уровень рекурсии
                }
            }
        }
        return true; // Все клетки заполнены, решение найдено
    }
    void generateSudoku(int numToRemove) { // передаём количество чисел, которые нужно удалить (можно настроить усмотрению)
        int[][] solution = new int[size][size];
        solve(solution); // рекурсивное решение
        Random random = new Random();
        for (int row = 0; row < size; row++) { // Создаем копию решенного судоку
            board1[row] = solution[row];
            System.arraycopy(solution[row], 0, this.board[row], 0, size);
        }
        while (numToRemove > 0) { // Удаляем числа из судоку
            int row = random.nextInt(size);
            int col = random.nextInt(size);
            if (this.board[row][col] != 0) {
                this.board[row][col] = 0;
                numToRemove--;
            }
        }
        for (int row = 0; row < size; row++) { // Устанавливаем значения в текстовые поля и определяем, какие ячейки были исходными
            for (int col = 0; col < size; col++) {
                if (board[row][col] != 0) {
                    grid[row][col].setText(String.valueOf(board[row][col]));
                    grid[row][col].setEditable(false);
                    grid[row][col].setForeground(Color.BLACK);
                    initialCells[row][col] = true;
                    correctCells[row][col] = board[row][col];
                }
                else {
                    grid[row][col].setText("");
                    grid[row][col].setEditable(true);
                    grid[row][col].setForeground(Color.BLACK);
                    initialCells[row][col] = false;
                    correctCells[row][col] = 0;
                }
                applyBlockBorders(row, col);
            }
        }
    }
    abstract void applyBlockBorders(int row, int col); // тут рисуются границы ячеек для красоты
    private boolean isSolutionCorrect(int[][] userSolution) { // проверка правильности решения
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (userSolution[row][col] != board1[row][col]) { // сравнение ячеек пользователя и правильно сгенерированной ячейки
                    return false;
                }
            }
        }
        return true;
    }
    protected void startTimer() { // инициализация и запуск таймера
        secondsElapsed = 0;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsElapsed++;
                timerLabel.setText("Время: " + getFormattedTime());
            }
        });
        timer.start();
    }
    private void stopTimer() { // остановка таймера
        timer.stop();
    }
    private String getBestTime(String name_file) { // считывание лучших времён и возврат лучшего времени из файлов их в виде
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\SAMIR\\IdeaProjects\\Sudoku\\" + name_file))) {
            return reader.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private boolean isGridEmpty() { // проверка на пустоту доски судоку
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                String value = grid[row][col].getText();
                if (value.isEmpty()) {
                    return true; // если хотя бы 1 ячейка пустая
                }
            }
        }
        return false;
    }
    protected void solveSudoku(String text, String name_file) { // решение судоку которое предоставил пользователь
        int[][] userSolution = new int[size][size];
        for (int row = 0; row < size; row++) { // Получаем значения, введенные пользователем
            for (int col = 0; col < size; col++) {
                String value = grid[row][col].getText();
                if (!value.isEmpty()) {
                    userSolution[row][col] = Integer.parseInt(value);
                }
                else {
                    userSolution[row][col] = 0;
                }
            }
        }
        if (isGridEmpty()) {
            JOptionPane.showMessageDialog(this, "Заполните все клетки перед решением судоку!"); // предупреждение о том что есть минимум 1 пустая клетка
            return;
        }
        if (solve(userSolution)) { // Проверяем, является ли решение правильным
            if (isSolutionCorrect(userSolution)) {
                stopTimer();
                isSolved = true;
                String currentTime = getFormattedTime();
                String bestTime = getBestTime(name_file);
                if (bestTime == null || currentTime.compareTo(bestTime) < 0) {
                    JOptionPane.showMessageDialog(this, "Поздравляю! Вы победили!\nНовый рекорд на уровне " + text + ": " + currentTime); // решено верно и побит рекорд
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\SAMIR\\IdeaProjects\\Sudoku\\" + name_file))) {
                        writer.write(currentTime);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    JOptionPane.showMessageDialog(this, "Поздравляю! Вы победили!\nВаше время: " + currentTime + "\nРекорд на уровне " + text + bestTime); // когда решено верно, но не побит рекорд
                }
                dispose();
                jFrame.setVisible(true);
            }
            else {
                JOptionPane.showMessageDialog(this, "Неправильное решение!"); // минимум 1 ошибка
            }
        }
        else {
            JOptionPane.showMessageDialog(this, "Нет решений для данного судоку!"); // если нет решений
        }
    }
}