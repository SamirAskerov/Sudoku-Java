import javax.swing.*; // загрузка необходимых библиотек
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class SudokuWindow { // основной класс
    private static final String MENU_PANEL = "Меню";
    private static final String RULES_PANEL = "Правила игры";
    private static final String PLAY_PANEL = "Играть";
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { // запуск игры
            try {
                createAndShowGUI();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private static void createAndShowGUI() throws IOException {
        JFrame frame = new JFrame("Судоку"); // заголовок
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // запрет менять разрешение окна
        frame.setSize(1080, 720);
        frame.setResizable(false);
        ImageIcon icon = new ImageIcon("C:\\Users\\SAMIR\\IdeaProjects\\Sudoku\\icon.png"); // Установка иконки окна
        frame.setIconImage(icon.getImage());
        BackgroundPanel backgroundPanel = new BackgroundPanel("C:\\Users\\SAMIR\\IdeaProjects\\Sudoku\\fon.png"); // Установка фона окна
        backgroundPanel.setLayout(new CardLayout());
        frame.setContentPane(backgroundPanel);
        JPanel menuPanel = new BackgroundPanel("C:\\Users\\SAMIR\\IdeaProjects\\Sudoku\\fon.png", new GridBagLayout()); // Создание панели меню
        JButton playButton = new JButton("Играть"); // Создание кнопок
        JButton rulesButton = new JButton("Правила игры");
        JButton recordsButton = new JButton("Рекорды");
        JButton exitButton = new JButton("Выйти из игры");
        Dimension buttonSize = new Dimension(200, 50); // Настройка размеров кнопок
        playButton.setPreferredSize(buttonSize);
        rulesButton.setPreferredSize(buttonSize);
        recordsButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);
        GridBagConstraints gbc = new GridBagConstraints(); // Добавление кнопок в панель меню
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        menuPanel.add(playButton, gbc);
        gbc.gridy = 1;
        menuPanel.add(rulesButton, gbc);
        gbc.gridy = 2;
        menuPanel.add(recordsButton, gbc);
        gbc.gridy = 3;
        menuPanel.add(exitButton, gbc);
        backgroundPanel.add(menuPanel, MENU_PANEL); // Добавление панели меню на фоновую панель
        JLabel rulesPanel = new JLabel(); // Создание панели с правилами игры
        rulesPanel.setLayout(new BorderLayout());
        JButton backButton = new JButton("Назад"); // Создание кнопки Назад
        backButton.setPreferredSize(buttonSize);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) backgroundPanel.getLayout();
                cardLayout.show(backgroundPanel, MENU_PANEL);
            }
        });
        ImageIcon rulesImage = new ImageIcon("C:\\Users\\SAMIR\\IdeaProjects\\Sudoku\\rules.png");
        rulesPanel.setIcon(rulesImage); // Добавление кнопки "Назад" и изображения с правилами игры на панель с правилами
        rulesPanel.add(backButton, BorderLayout.NORTH);
        backgroundPanel.add(rulesPanel, RULES_PANEL); // Добавление панели с правилами игры на фоновую панель
        JPanel playPanel = new BackgroundPanel("C:\\Users\\SAMIR\\IdeaProjects\\Sudoku\\fon.png", new GridBagLayout()); // Создание панели игры
        JButton easyButton = new JButton("Лёгкий"); // Создание кнопок выбора уровня сложности
        JButton normalButton = new JButton("Нормальный");
        JButton hardButton = new JButton("Сложный");
        JButton backButtonPlay = new JButton("Назад в меню");
        easyButton.setPreferredSize(buttonSize); // Настройка размеров кнопок
        normalButton.setPreferredSize(buttonSize);
        hardButton.setPreferredSize(buttonSize);
        backButtonPlay.setPreferredSize(buttonSize);
        GridBagConstraints gbcPlay = new GridBagConstraints(); // Добавление кнопок выбора уровня сложности в панель игры
        gbcPlay.gridx = 0;
        gbcPlay.gridy = 0;
        gbcPlay.insets = new Insets(10, 10, 10, 10);
        gbcPlay.anchor = GridBagConstraints.CENTER;
        playPanel.add(easyButton, gbcPlay);
        gbcPlay.gridy = 1;
        playPanel.add(normalButton, gbcPlay);
        gbcPlay.gridy = 2;
        playPanel.add(hardButton, gbcPlay);
        gbcPlay.gridy = 3;
        playPanel.add(backButtonPlay, gbcPlay);
        backgroundPanel.add(playPanel, PLAY_PANEL); // Добавление панели игры на фоновую панель
        rulesButton.addActionListener(new ActionListener() { // Обработчик событий для кнопки Правила игры
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) backgroundPanel.getLayout();
                cardLayout.show(backgroundPanel, RULES_PANEL);
            }
        });
        easyButton.addActionListener((new ActionListener() { // Обработчик событий для кнопки Лёгкий
            @Override
            public void actionPerformed(ActionEvent e) {
                new SudokuGame4x4(frame);
                frame.setVisible(false);
            }
        }));
        normalButton.addActionListener(new ActionListener() { // Обработчик событий для кнопки Нормальный
            @Override
            public void actionPerformed(ActionEvent e) {
                new SudokuGame9x9(frame);
                frame.setVisible(false);
            }
        });
        hardButton.addActionListener(new ActionListener() { // Обработчик событий для кнопки Сложный
            @Override
            public void actionPerformed(ActionEvent e) {
                new SudokuGame16x16(frame);
                frame.setVisible(false);
            }
        });
        exitButton.addActionListener(new ActionListener() { // Обработчик событий для кнопки Выйти из игры
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(frame, "Вы хотите выйти из игры?", "Выход из игры", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        playButton.addActionListener(new ActionListener() { // Обработчик событий для кнопки Играть
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) backgroundPanel.getLayout();
                cardLayout.show(backgroundPanel, PLAY_PANEL);
            }
        });
        backButtonPlay.addActionListener(new ActionListener() { // Обработчик событий для кнопки Назад в меню
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) backgroundPanel.getLayout();
                cardLayout.show(backgroundPanel, MENU_PANEL);
            }
        });
        recordsButton.addActionListener(new ActionListener() { // Обработчик событий для кнопки Рекорды
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                try {
                    showRecords("C:\\Users\\SAMIR\\IdeaProjects\\Sudoku\\recordsEasy.txt", "C:\\Users\\SAMIR\\IdeaProjects\\Sudoku\\recordsNormal.txt", "C:\\Users\\SAMIR\\IdeaProjects\\Sudoku\\recordsHard.txt",frame);
                }
                catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Ошибка чтения файла рекордов: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        frame.setVisible(true); // Отображение окна
    }
    private static void showRecords(String recordsEasy, String recordsNormal, String recordsHard,JFrame jf) throws IOException { // метод, который открывает окно Рекорды
        JFrame frame = new JFrame("Рекорды");
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        ImageIcon icon = new ImageIcon("C:\\Users\\SAMIR\\IdeaProjects\\Sudoku\\icon.png");
        frame.setIconImage(icon.getImage());
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        Font titleFont = new Font("Times New Roman", Font.BOLD, 24);
        Font recordFont = new Font("Times New Roman", Font.PLAIN, 14);
        JLabel titleLabel = new JLabel("Уровни сложности");
        titleLabel.setFont(titleFont);
        panel.add(titleLabel, gbc);
        panel.add(Box.createVerticalStrut(10));
        BufferedReader reader1 = new BufferedReader(new FileReader(recordsEasy)); // считывание рекорда с лёгкого уровня сложности
        String line;
        while ((line = reader1.readLine()) != null) {
            JLabel label = new JLabel("Лёгкий: " + line);
            label.setFont(recordFont);
            panel.add(label, gbc);
        }
        reader1.close();
        panel.add(Box.createVerticalStrut(10));
        BufferedReader reader2 = new BufferedReader(new FileReader(recordsNormal)); // считывание рекорда с нормального уровня сложности
        while ((line = reader2.readLine()) != null) {
            JLabel label = new JLabel("Нормальный: " + line);
            label.setFont(recordFont);
            panel.add(label, gbc);
        }
        reader2.close();
        panel.add(Box.createVerticalStrut(10));
        BufferedReader reader3 = new BufferedReader(new FileReader(recordsHard)); // считывание рекорда со сложного уровня сложности
        while ((line = reader3.readLine()) != null) {
            JLabel label = new JLabel("Сложный: " + line);
            label.setFont(recordFont);
            panel.add(label, gbc);
        }
        reader3.close();
        JButton backButtonPlay = new JButton("Назад в меню"); // создание кнопки Назад в меню
        panel.add(backButtonPlay, gbc);
        backButtonPlay.addActionListener(new ActionListener() { // обработчик событий для кнопки Назад в меню
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.setVisible(true);
                frame.dispose(); // закрытие окна с рекордами
            }
        });
        frame.add(panel);
        frame.setVisible(true);
    }
}
class BackgroundPanel extends JPanel { // этот пользовательский класс расширяет функциональность базового класса JPanel
    private Image backgroundImage; // изображение фона
    public BackgroundPanel(String imagePath) { // конструктор класса принимает путь к изображению фона
        loadImage(imagePath); // загрузка изображения
    }
    public BackgroundPanel(String imagePath, LayoutManager layout) { // путь к изображению фона и менеджер компоновки
        super(layout); // менеджер компоновки для панели
        loadImage(imagePath);
    }
    private void loadImage(String imagePath) { // загрузка изображения фона
        try {
            backgroundImage = new ImageIcon(imagePath).getImage();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void paintComponent(Graphics g) { // отрисовка компонента
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}