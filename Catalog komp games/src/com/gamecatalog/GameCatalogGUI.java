package com.gamecatalog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Главный класс приложения с графическим интерфейсом
 */
public class GameCatalogGUI extends JFrame {
    private List<Game> games;
    private JTable gamesTable;
    private DefaultTableModel tableModel;
    private JTextArea statsArea;

    // Компоненты поиска
    private JComboBox<String> searchFieldCombo;
    private JTextField searchField;
    private JButton searchButton;
    private JButton clearSearchButton;

    // Компоненты фильтрации
    private JComboBox<String> genreFilterCombo;
    private JComboBox<String> yearFilterCombo;
    private JSlider ratingSlider;
    private JLabel ratingLabel;

    // Основные кнопки
    private JButton addButton, editButton, deleteButton, statsButton, saveButton, loadButton;

    // Шрифты
    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 22);
    private final Font headerFont = new Font("Segoe UI", Font.BOLD, 14);
    private final Font normalFont = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font buttonFont = new Font("Segoe UI", Font.BOLD, 13);
    private final Font statsFont = new Font("Consolas", Font.PLAIN, 13);
    private final Font tableFont = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font tableHeaderFont = new Font("Segoe UI", Font.BOLD, 13);

    public GameCatalogGUI() {
        games = new ArrayList<>();
        initializeSampleGames();
        initializeUI();
        updateTable();
        updateStatistics();
    }

    /**
     * Инициализация тестовых игр
     */
    private void initializeSampleGames() {
        // 20 примеров игр жанра Action/RPG
        addSampleGame("The Witcher 3: Wild Hunt", Game.Genre.ACTION_RPG, 2015, 9.7, "CD Projekt Red", "PC/PS4/Xbox One");
        addSampleGame("Cyberpunk 2077", Game.Genre.ACTION_RPG, 2020, 7.5, "CD Projekt Red", "PC/PS5/Xbox Series X");
        addSampleGame("Elden Ring", Game.Genre.ACTION_RPG, 2022, 9.5, "FromSoftware", "PC/PS4/PS5/Xbox");
        addSampleGame("Dark Souls III", Game.Genre.ACTION_RPG, 2016, 9.0, "FromSoftware", "PC/PS4/Xbox One");
        addSampleGame("Bloodborne", Game.Genre.ACTION_RPG, 2015, 9.4, "FromSoftware", "PS4");
        addSampleGame("Sekiro: Shadows Die Twice", Game.Genre.ACTION, 2019, 9.2, "FromSoftware", "PC/PS4/Xbox One");
        addSampleGame("God of War (2018)", Game.Genre.ACTION, 2018, 9.8, "Santa Monica Studio", "PC/PS4");
        addSampleGame("God of War: Ragnarök", Game.Genre.ACTION, 2022, 9.6, "Santa Monica Studio", "PS4/PS5");
        addSampleGame("Horizon Zero Dawn", Game.Genre.ACTION_RPG, 2017, 8.9, "Guerrilla Games", "PC/PS4");
        addSampleGame("Horizon Forbidden West", Game.Genre.ACTION_RPG, 2022, 8.8, "Guerrilla Games", "PS4/PS5");
        addSampleGame("Skyrim", Game.Genre.RPG, 2011, 9.3, "Bethesda", "PC/PS3/Xbox 360");
        addSampleGame("Fallout 4", Game.Genre.ACTION_RPG, 2015, 8.5, "Bethesda", "PC/PS4/Xbox One");
        addSampleGame("Mass Effect 2", Game.Genre.RPG, 2010, 9.6, "BioWare", "PC/PS3/Xbox 360");
        addSampleGame("Dragon Age: Inquisition", Game.Genre.RPG, 2014, 8.8, "BioWare", "PC/PS4/Xbox One");
        addSampleGame("Diablo IV", Game.Genre.ACTION_RPG, 2023, 8.2, "Blizzard", "PC/PS5/Xbox Series X");
        addSampleGame("Assassin's Creed Valhalla", Game.Genre.ACTION_RPG, 2020, 8.0, "Ubisoft", "PC/PS4/PS5/Xbox");
        addSampleGame("Nier: Automata", Game.Genre.ACTION_RPG, 2017, 8.9, "PlatinumGames", "PC/PS4/Xbox One");
        addSampleGame("Monster Hunter: World", Game.Genre.ACTION_RPG, 2018, 8.8, "Capcom", "PC/PS4/Xbox One");
        addSampleGame("Red Dead Redemption 2", Game.Genre.ACTION, 2018, 9.7, "Rockstar Games", "PC/PS4/Xbox One");
        addSampleGame("Borderlands 3", Game.Genre.ACTION_RPG, 2019, 7.8, "Gearbox Software", "PC/PS4/Xbox One");
    }

    private void addSampleGame(String title, Game.Genre genre, int year, double rating, String developer, String platform) {
        games.add(new Game(title, genre, year, rating, developer, platform));
    }

    /**
     * Инициализация интерфейса
     */
    private void initializeUI() {
        setTitle("Каталог компьютерных игр жанра Action/RPG");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(1200, 700));

        // Устанавливаем иконку окна
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png")));
        } catch (Exception e) {
            // Иконка не найдена - это нормально
        }

        // Заголовок
        JLabel titleLabel = new JLabel("🎮 КАТАЛОГ КОМПЬЮТЕРНЫХ ИГР ЖАНРА ACTION/RPG 🎮", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(0, 80, 180));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        titleLabel.setBackground(new Color(245, 245, 250));
        titleLabel.setOpaque(true);
        add(titleLabel, BorderLayout.NORTH);

        // Основная панель с разделителем
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(400);
        splitPane.setDividerSize(8);
        splitPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        // Верхняя панель: таблица и поиск
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(createSearchPanel(), BorderLayout.NORTH);
        topPanel.add(createTablePanel(), BorderLayout.CENTER);

        // Нижняя панель: статистика и кнопки
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(createStatsPanel(), BorderLayout.CENTER);
        bottomPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        splitPane.setTopComponent(topPanel);
        splitPane.setBottomComponent(bottomPanel);

        add(splitPane, BorderLayout.CENTER);

        // Центрируем окно
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Панель поиска и фильтрации
     */
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 180, 220), 2),
                "🔍 Поиск и фильтрация",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                headerFont,
                new Color(0, 100, 200)
        ));
        searchPanel.setBackground(new Color(250, 250, 255));
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                searchPanel.getBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Поиск по полю
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel searchByLabel = new JLabel("Искать по:");
        searchByLabel.setFont(normalFont);
        searchByLabel.setForeground(Color.BLACK);
        searchPanel.add(searchByLabel, gbc);

        gbc.gridx = 1;
        searchFieldCombo = new JComboBox<>(new String[]{"Названию", "Разработчику", "Платформе"});
        searchFieldCombo.setFont(normalFont);
        searchFieldCombo.setBackground(Color.WHITE);
        searchFieldCombo.setForeground(Color.BLACK);
        searchPanel.add(searchFieldCombo, gbc);

        gbc.gridx = 2; gbc.weightx = 1.0;
        searchField = new JTextField();
        searchField.setFont(normalFont);
        searchField.setBackground(Color.WHITE);
        searchField.setForeground(Color.BLACK);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filterGames();
            }
        });
        searchPanel.add(searchField, gbc);

        gbc.gridx = 3; gbc.weightx = 0;
        searchButton = createStyledButton("Поиск", new Color(70, 130, 180));
        searchPanel.add(searchButton, gbc);

        gbc.gridx = 4;
        clearSearchButton = createStyledButton("Сброс", new Color(180, 60, 80));
        clearSearchButton.addActionListener(e -> {
            searchField.setText("");
            genreFilterCombo.setSelectedIndex(0);
            yearFilterCombo.setSelectedIndex(0);
            ratingSlider.setValue(0);
            updateTable();
        });
        searchPanel.add(clearSearchButton, gbc);

        // Фильтры
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel genreLabel = new JLabel("Жанр:");
        genreLabel.setFont(normalFont);
        genreLabel.setForeground(Color.BLACK);
        searchPanel.add(genreLabel, gbc);

        gbc.gridx = 1;
        genreFilterCombo = new JComboBox<>(new String[]{"Все", "Action", "RPG", "Action/RPG"});
        genreFilterCombo.setFont(normalFont);
        genreFilterCombo.setBackground(Color.WHITE);
        genreFilterCombo.setForeground(Color.BLACK);
        genreFilterCombo.addActionListener(e -> filterGames());
        searchPanel.add(genreFilterCombo, gbc);

        gbc.gridx = 2;
        JLabel yearLabel = new JLabel("Год:");
        yearLabel.setFont(normalFont);
        yearLabel.setForeground(Color.BLACK);
        searchPanel.add(yearLabel, gbc);

        gbc.gridx = 3;
        yearFilterCombo = new JComboBox<>(getYearOptions());
        yearFilterCombo.setFont(normalFont);
        yearFilterCombo.setBackground(Color.WHITE);
        yearFilterCombo.setForeground(Color.BLACK);
        yearFilterCombo.addActionListener(e -> filterGames());
        searchPanel.add(yearFilterCombo, gbc);

        gbc.gridx = 4;
        JLabel ratingLabelText = new JLabel("Рейтинг от:");
        ratingLabelText.setFont(normalFont);
        ratingLabelText.setForeground(Color.BLACK);
        searchPanel.add(ratingLabelText, gbc);

        gbc.gridx = 5;
        ratingSlider = new JSlider(0, 100, 0); // 0-10.0
        ratingSlider.setMajorTickSpacing(20);
        ratingSlider.setMinorTickSpacing(5);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);
        ratingSlider.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        ratingSlider.setBackground(new Color(250, 250, 255));
        ratingSlider.addChangeListener(e -> {
            ratingLabel.setText(String.format("%.1f+", ratingSlider.getValue() / 10.0));
            filterGames();
        });
        searchPanel.add(ratingSlider, gbc);

        gbc.gridx = 6;
        ratingLabel = new JLabel("0.0+");
        ratingLabel.setFont(normalFont);
        ratingLabel.setForeground(new Color(0, 100, 200));
        ratingLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        searchPanel.add(ratingLabel, gbc);

        return searchPanel;
    }

    /**
     * Получить список годов для фильтра
     */
    private String[] getYearOptions() {
        List<String> years = new ArrayList<>();
        years.add("Все");

        // Получаем уникальные годы из игр
        Set<Integer> uniqueYears = new TreeSet<>(Collections.reverseOrder());
        for (Game game : games) {
            uniqueYears.add(game.getReleaseYear());
        }

        // Добавляем годы в список
        for (int year : uniqueYears) {
            years.add(String.valueOf(year));
        }

        return years.toArray(new String[0]);
    }

    /**
     * Панель с таблицей игр
     */
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Заголовки таблицы
        String[] columns = {"№", "Название", "Жанр", "Год", "Рейтинг", "Разработчик", "Платформа"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        gamesTable = new JTable(tableModel);
        gamesTable.setRowHeight(28);
        gamesTable.setFont(tableFont);
        gamesTable.setForeground(Color.BLACK);
        gamesTable.setBackground(Color.WHITE);
        gamesTable.setSelectionBackground(new Color(220, 235, 255));
        gamesTable.setSelectionForeground(Color.BLACK);
        gamesTable.setGridColor(new Color(230, 230, 240));
        gamesTable.setShowGrid(true);
        gamesTable.setIntercellSpacing(new Dimension(1, 1));

        // Настройка заголовка таблицы
        JTableHeader header = gamesTable.getTableHeader();
        header.setFont(tableHeaderFont);
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        // Панель прокрутки для таблицы
        JScrollPane scrollPane = new JScrollPane(gamesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Инфопанель
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(new Color(245, 248, 255));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 230)),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        JLabel countLabel = new JLabel("Всего игр: 0");
        countLabel.setFont(headerFont);
        countLabel.setForeground(new Color(0, 100, 200));
        infoPanel.add(countLabel);

        // Обновление счетчика при изменении таблицы
        tableModel.addTableModelListener(e -> {
            countLabel.setText("Всего игр: " + tableModel.getRowCount());
        });

        tablePanel.add(infoPanel, BorderLayout.SOUTH);

        return tablePanel;
    }

    /**
     * Панель статистики
     */
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 180, 220), 2),
                "📊 Статистика каталога",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                headerFont,
                new Color(0, 100, 200)
        ));
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
                statsPanel.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        statsArea = new JTextArea();
        statsArea.setFont(statsFont);
        statsArea.setEditable(false);
        statsArea.setForeground(Color.BLACK);
        statsArea.setBackground(new Color(253, 253, 255));
        statsArea.setMargin(new Insets(10, 15, 10, 15));
        statsArea.setLineWrap(true);
        statsArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(statsArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235)));
        scrollPane.getViewport().setBackground(statsArea.getBackground());
        statsPanel.add(scrollPane, BorderLayout.CENTER);

        return statsPanel;
    }

    /**
     * Панель с кнопками управления
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 7, 12, 0));
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 210, 230)),
                BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));
        buttonPanel.setBackground(new Color(245, 248, 255));

        // Кнопка добавления
        addButton = createStyledButton("➕ Добавить игру", new Color(50, 160, 70));
        addButton.addActionListener(e -> showAddGameDialog());

        // Кнопка редактирования
        editButton = createStyledButton("✏️ Редактировать", new Color(240, 150, 30));
        editButton.addActionListener(e -> showEditGameDialog());

        // Кнопка удаления
        deleteButton = createStyledButton("🗑️ Удалить", new Color(220, 60, 80));
        deleteButton.addActionListener(e -> deleteSelectedGame());

        // Кнопка статистики
        statsButton = createStyledButton("📈 Обновить статистику", new Color(140, 80, 210));
        statsButton.addActionListener(e -> updateStatistics());

        // Кнопка сохранения
        saveButton = createStyledButton("💾 Сохранить", new Color(30, 120, 200));
        saveButton.addActionListener(e -> showSaveDialog());

        // Кнопка загрузки
        loadButton = createStyledButton("📂 Загрузить", new Color(60, 170, 120));
        loadButton.addActionListener(e -> showLoadDialog());

        // Кнопка выхода
        JButton exitButton = createStyledButton("🚪 Выход", new Color(120, 120, 140));
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Вы уверены, что хотите выйти?", "Подтверждение выхода",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(statsButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(exitButton);

        return buttonPanel;
    }

    /**
     * Создание стилизованной кнопки
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(buttonFont);
        button.setForeground(Color.BLACK); // ЧЁРНЫЙ текст на кнопках
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Эффект при наведении
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(bgColor.darker().darker(), 2),
                        BorderFactory.createEmptyBorder(10, 5, 10, 5)
                ));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(bgColor.darker(), 1),
                        BorderFactory.createEmptyBorder(10, 5, 10, 5)
                ));
            }
            public void mousePressed(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseReleased(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    /**
     * Обновление таблицы
     */
    private void updateTable() {
        tableModel.setRowCount(0); // Очищаем таблицу

        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            tableModel.addRow(new Object[]{
                    i + 1,
                    game.getTitle(),
                    game.getGenre().toString().replace("_", "/"),
                    game.getReleaseYear(),
                    String.format("%.1f", game.getRating()),
                    game.getDeveloper(),
                    game.getPlatform()
            });
        }
    }

    /**
     * Обновление статистики
     */
    private void updateStatistics() {
        if (games.isEmpty()) {
            statsArea.setText("Каталог пуст.");
            return;
        }

        StringBuilder stats = new StringBuilder();

        int totalGames = games.size();
        double avgRating = 0.0;
        for (Game game : games) {
            avgRating += game.getRating();
        }
        avgRating = totalGames > 0 ? avgRating / totalGames : 0.0;

        Map<Game.Genre, Integer> genreDistribution = new HashMap<>();
        Map<Integer, Integer> yearDistribution = new TreeMap<>(Collections.reverseOrder());

        for (Game game : games) {
            // Распределение по жанрам
            genreDistribution.put(game.getGenre(),
                    genreDistribution.getOrDefault(game.getGenre(), 0) + 1);

            // Распределение по годам
            yearDistribution.put(game.getReleaseYear(),
                    yearDistribution.getOrDefault(game.getReleaseYear(), 0) + 1);
        }

        // Топ-3 игр по рейтингу
        List<Game> topGames = new ArrayList<>(games);
        topGames.sort((g1, g2) -> Double.compare(g2.getRating(), g1.getRating()));

        // Форматирование статистики
        stats.append("══════════════════════════════════════════════════════════\n");
        stats.append("                СТАТИСТИКА КАТАЛОГА ИГР\n");
        stats.append("══════════════════════════════════════════════════════════\n\n");

        stats.append("📊 ОБЩАЯ СТАТИСТИКА:\n");
        stats.append("   Всего игр в каталоге: ").append(totalGames).append("\n");
        stats.append(String.format("   Средний рейтинг: %.2f/10.0\n\n", avgRating));

        stats.append("🎮 РАСПРЕДЕЛЕНИЕ ПО ЖАНРАМ:\n");
        for (Game.Genre genre : Game.Genre.values()) {
            int count = genreDistribution.getOrDefault(genre, 0);
            double percentage = totalGames > 0 ? (count * 100.0 / totalGames) : 0;
            String bar = "■".repeat(Math.max(0, (int)(percentage / 3)));
            stats.append(String.format("   %-12s: %d игр (%.1f%%) %s\n",
                    genre.toString().replace("_", "/"), count, percentage, bar));
        }
        stats.append("\n");

        stats.append("📅 РАСПРЕДЕЛЕНИЕ ПО ГОДАМ:\n");
        for (Map.Entry<Integer, Integer> entry : yearDistribution.entrySet()) {
            stats.append(String.format("   %d: %d игр\n", entry.getKey(), entry.getValue()));
        }
        stats.append("\n");

        stats.append("🏆 ТОП-3 ИГР ПО РЕЙТИНГУ:\n");
        int limit = Math.min(3, topGames.size());
        for (int i = 0; i < limit; i++) {
            Game game = topGames.get(i);
            stats.append(String.format("   %d. %s (рейтинг: %.1f/10.0)\n",
                    i + 1, game.getTitle(), game.getRating()));
        }

        // Самая старая и новая игра
        if (!games.isEmpty()) {
            Game oldest = Collections.min(games, Comparator.comparingInt(Game::getReleaseYear));
            Game newest = Collections.max(games, Comparator.comparingInt(Game::getReleaseYear));
            stats.append("\n");
            stats.append("📈 ДОПОЛНИТЕЛЬНАЯ ИНФОРМАЦИЯ:\n");
            stats.append(String.format("   Самая старая игра: %s (%d год)\n",
                    oldest.getTitle(), oldest.getReleaseYear()));
            stats.append(String.format("   Самая новая игра: %s (%d год)\n",
                    newest.getTitle(), newest.getReleaseYear()));
        }

        statsArea.setText(stats.toString());

        // Автоматическая прокрутка в начало
        statsArea.setCaretPosition(0);
    }

    /**
     * Фильтрация игр
     */
    private void filterGames() {
        String searchText = searchField.getText().toLowerCase().trim();
        String searchFieldType = (String) searchFieldCombo.getSelectedItem();
        String selectedGenre = (String) genreFilterCombo.getSelectedItem();
        String selectedYear = (String) yearFilterCombo.getSelectedItem();
        double minRating = ratingSlider.getValue() / 10.0;

        tableModel.setRowCount(0); // Очищаем таблицу

        int count = 0;
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            boolean matches = true;

            // Поиск по тексту
            if (!searchText.isEmpty()) {
                String searchIn = switch (searchFieldType) {
                    case "Названию" -> game.getTitle().toLowerCase();
                    case "Разработчику" -> game.getDeveloper().toLowerCase();
                    case "Платформе" -> game.getPlatform().toLowerCase();
                    default -> "";
                };

                if (!searchIn.contains(searchText)) {
                    matches = false;
                }
            }

            // Фильтр по жанру
            if (!selectedGenre.equals("Все") && !game.getGenre().toString()
                    .replace("_", "/").equals(selectedGenre)) {
                matches = false;
            }

            // Фильтр по году
            if (!selectedYear.equals("Все") && game.getReleaseYear() != Integer.parseInt(selectedYear)) {
                matches = false;
            }

            // Фильтр по рейтингу
            if (game.getRating() < minRating) {
                matches = false;
            }

            if (matches) {
                tableModel.addRow(new Object[]{
                        count + 1,
                        game.getTitle(),
                        game.getGenre().toString().replace("_", "/"),
                        game.getReleaseYear(),
                        String.format("%.1f", game.getRating()),
                        game.getDeveloper(),
                        game.getPlatform()
                });
                count++;
            }
        }
    }

    /**
     * Диалог добавления игры
     */
    private void showAddGameDialog() {
        JDialog dialog = new JDialog(this, "Добавление новой игры", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(250, 252, 255));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 15, 25));
        formPanel.setBackground(new Color(250, 252, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Поля формы
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel titleLabel = new JLabel("Название игры:");
        titleLabel.setFont(normalFont);
        titleLabel.setForeground(Color.BLACK);
        formPanel.add(titleLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField titleField = new JTextField(25);
        titleField.setFont(normalFont);
        titleField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 195, 220)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        formPanel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel genreLabel = new JLabel("Жанр:");
        genreLabel.setFont(normalFont);
        genreLabel.setForeground(Color.BLACK);
        formPanel.add(genreLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JComboBox<String> genreCombo = new JComboBox<>(new String[]{"Action", "RPG", "Action/RPG"});
        genreCombo.setFont(normalFont);
        genreCombo.setBackground(Color.WHITE);
        genreCombo.setForeground(Color.BLACK);
        formPanel.add(genreCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel yearLabel = new JLabel("Год выпуска:");
        yearLabel.setFont(normalFont);
        yearLabel.setForeground(Color.BLACK);
        formPanel.add(yearLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JSpinner yearSpinner = new JSpinner(new SpinnerNumberModel(2024, 1970, 2030, 1));
        ((JSpinner.DefaultEditor) yearSpinner.getEditor()).getTextField().setFont(normalFont);
        formPanel.add(yearSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel ratingLabel = new JLabel("Рейтинг (0-10):");
        ratingLabel.setFont(normalFont);
        ratingLabel.setForeground(Color.BLACK);
        formPanel.add(ratingLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JSlider ratingSlider = new JSlider(0, 100, 70); // 0-10.0
        ratingSlider.setMajorTickSpacing(20);
        ratingSlider.setMinorTickSpacing(5);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);
        ratingSlider.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        ratingSlider.setBackground(new Color(250, 252, 255));
        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.setBackground(new Color(250, 252, 255));
        sliderPanel.add(ratingSlider, BorderLayout.CENTER);
        JLabel ratingValue = new JLabel("7.0");
        ratingValue.setFont(normalFont);
        ratingValue.setForeground(new Color(0, 100, 200));
        ratingSlider.addChangeListener(e ->
                ratingValue.setText(String.format("%.1f", ratingSlider.getValue() / 10.0)));
        sliderPanel.add(ratingValue, BorderLayout.EAST);
        formPanel.add(sliderPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel devLabel = new JLabel("Разработчик:");
        devLabel.setFont(normalFont);
        devLabel.setForeground(Color.BLACK);
        formPanel.add(devLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField developerField = new JTextField(25);
        developerField.setFont(normalFont);
        developerField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 195, 220)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        formPanel.add(developerField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel platformLabel = new JLabel("Платформа:");
        platformLabel.setFont(normalFont);
        platformLabel.setForeground(Color.BLACK);
        formPanel.add(platformLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField platformField = new JTextField(25);
        platformField.setFont(normalFont);
        platformField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 195, 220)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        formPanel.add(platformField, gbc);

        // Кнопки
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(250, 252, 255));

        JButton saveButton = new JButton("Сохранить");
        saveButton.setFont(buttonFont);
        saveButton.setForeground(Color.BLACK);
        saveButton.setBackground(new Color(50, 160, 70));
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.setFocusPainted(false);

        JButton cancelButton = new JButton("Отмена");
        cancelButton.setFont(buttonFont);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setBackground(new Color(220, 60, 80));
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelButton.setFocusPainted(false);

        saveButton.addActionListener(e -> {
            if (titleField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Введите название игры!", "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Game.Genre genre = Game.Genre.valueOf(genreCombo.getSelectedItem().toString()
                    .replace("/", "_").toUpperCase());

            Game game = new Game(
                    titleField.getText().trim(),
                    genre,
                    (int) yearSpinner.getValue(),
                    ratingSlider.getValue() / 10.0,
                    developerField.getText().trim(),
                    platformField.getText().trim()
            );

            games.add(game);
            updateTable();
            updateStatistics();

            JOptionPane.showMessageDialog(dialog,
                    "Игра \"" + game.getTitle() + "\" успешно добавлена в каталог!",
                    "Успех", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        // Добавляем эффекты при наведении для кнопок диалога
        addButtonHoverEffect(saveButton, new Color(50, 160, 70));
        addButtonHoverEffect(cancelButton, new Color(220, 60, 80));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Эффект при наведении для кнопок
     */
    private void addButtonHoverEffect(JButton button, Color baseColor) {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });
    }

    /**
     * Диалог редактирования игры
     */
    private void showEditGameDialog() {
        int selectedRow = gamesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Выберите игру для редактирования!\n\nНажмите на строку в таблице.",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Game game = games.get(selectedRow);

        JDialog dialog = new JDialog(this, "Редактирование игры: " + game.getTitle(), true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(250, 252, 255));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 15, 25));
        formPanel.setBackground(new Color(250, 252, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Поля формы с текущими значениями
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel titleLabel = new JLabel("Название игры:");
        titleLabel.setFont(normalFont);
        titleLabel.setForeground(Color.BLACK);
        formPanel.add(titleLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField titleField = new JTextField(game.getTitle(), 25);
        titleField.setFont(normalFont);
        titleField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 195, 220)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        formPanel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel genreLabel = new JLabel("Жанр:");
        genreLabel.setFont(normalFont);
        genreLabel.setForeground(Color.BLACK);
        formPanel.add(genreLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JComboBox<String> genreCombo = new JComboBox<>(new String[]{"Action", "RPG", "Action/RPG"});
        genreCombo.setFont(normalFont);
        genreCombo.setBackground(Color.WHITE);
        genreCombo.setForeground(Color.BLACK);
        genreCombo.setSelectedItem(game.getGenre().toString().replace("_", "/"));
        formPanel.add(genreCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel yearLabel = new JLabel("Год выпуска:");
        yearLabel.setFont(normalFont);
        yearLabel.setForeground(Color.BLACK);
        formPanel.add(yearLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JSpinner yearSpinner = new JSpinner(new SpinnerNumberModel(game.getReleaseYear(), 1970, 2030, 1));
        ((JSpinner.DefaultEditor) yearSpinner.getEditor()).getTextField().setFont(normalFont);
        formPanel.add(yearSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel ratingLabel = new JLabel("Рейтинг (0-10):");
        ratingLabel.setFont(normalFont);
        ratingLabel.setForeground(Color.BLACK);
        formPanel.add(ratingLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JSlider ratingSlider = new JSlider(0, 100, (int)(game.getRating() * 10));
        ratingSlider.setMajorTickSpacing(20);
        ratingSlider.setMinorTickSpacing(5);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);
        ratingSlider.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        ratingSlider.setBackground(new Color(250, 252, 255));
        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.setBackground(new Color(250, 252, 255));
        sliderPanel.add(ratingSlider, BorderLayout.CENTER);
        JLabel ratingValue = new JLabel(String.format("%.1f", game.getRating()));
        ratingValue.setFont(normalFont);
        ratingValue.setForeground(new Color(0, 100, 200));
        ratingSlider.addChangeListener(e ->
                ratingValue.setText(String.format("%.1f", ratingSlider.getValue() / 10.0)));
        sliderPanel.add(ratingValue, BorderLayout.EAST);
        formPanel.add(sliderPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel devLabel = new JLabel("Разработчик:");
        devLabel.setFont(normalFont);
        devLabel.setForeground(Color.BLACK);
        formPanel.add(devLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField developerField = new JTextField(game.getDeveloper(), 25);
        developerField.setFont(normalFont);
        developerField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 195, 220)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        formPanel.add(developerField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel platformLabel = new JLabel("Платформа:");
        platformLabel.setFont(normalFont);
        platformLabel.setForeground(Color.BLACK);
        formPanel.add(platformLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField platformField = new JTextField(game.getPlatform(), 25);
        platformField.setFont(normalFont);
        platformField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 195, 220)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        formPanel.add(platformField, gbc);

        // Кнопки
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(250, 252, 255));

        JButton saveButton = new JButton("Сохранить изменения");
        saveButton.setFont(buttonFont);
        saveButton.setForeground(Color.BLACK);
        saveButton.setBackground(new Color(50, 160, 70));
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.setFocusPainted(false);

        JButton cancelButton = new JButton("Отмена");
        cancelButton.setFont(buttonFont);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setBackground(new Color(220, 60, 80));
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelButton.setFocusPainted(false);

        saveButton.addActionListener(e -> {
            if (titleField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Название игры не может быть пустым!",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Game.Genre genre = Game.Genre.valueOf(genreCombo.getSelectedItem().toString()
                    .replace("/", "_").toUpperCase());

            game.setTitle(titleField.getText().trim());
            game.setGenre(genre);
            game.setReleaseYear((int) yearSpinner.getValue());
            game.setRating(ratingSlider.getValue() / 10.0);
            game.setDeveloper(developerField.getText().trim());
            game.setPlatform(platformField.getText().trim());

            updateTable();
            updateStatistics();

            JOptionPane.showMessageDialog(dialog,
                    "Изменения в игре \"" + game.getTitle() + "\" сохранены!",
                    "Успех", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        // Добавляем эффекты при наведении для кнопок диалога
        addButtonHoverEffect(saveButton, new Color(50, 160, 70));
        addButtonHoverEffect(cancelButton, new Color(220, 60, 80));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Удаление выбранной игры
     */
    private void deleteSelectedGame() {
        int selectedRow = gamesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Выберите игру для удаления!\n\nНажмите на строку в таблице.",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Game gameToDelete = games.get(selectedRow);

        // Создаем кастомное диалоговое окно
        JDialog confirmDialog = new JDialog(this, "Подтверждение удаления", true);
        confirmDialog.setLayout(new BorderLayout());
        confirmDialog.setSize(450, 200);
        confirmDialog.setLocationRelativeTo(this);
        confirmDialog.getContentPane().setBackground(new Color(255, 245, 245));

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        messagePanel.setBackground(new Color(255, 245, 245));

        JLabel warningIcon = new JLabel("⚠️");
        warningIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        warningIcon.setHorizontalAlignment(SwingConstants.CENTER);
        warningIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>"
                + "Вы уверены, что хотите удалить игру:<br><br>"
                + "<b>\"" + gameToDelete.getTitle() + "\"</b><br><br>"
                + "Это действие нельзя отменить!</div></html>", SwingConstants.CENTER);
        messageLabel.setFont(normalFont);
        messageLabel.setForeground(Color.BLACK);

        messagePanel.add(warningIcon, BorderLayout.NORTH);
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        // Кнопки подтверждения
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(255, 245, 245));

        JButton yesButton = new JButton("Да, удалить");
        yesButton.setFont(buttonFont);
        yesButton.setForeground(Color.BLACK);
        yesButton.setBackground(new Color(220, 60, 80));
        yesButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        yesButton.setFocusPainted(false);

        JButton noButton = new JButton("Нет, отменить");
        noButton.setFont(buttonFont);
        noButton.setForeground(Color.BLACK);
        noButton.setBackground(new Color(120, 120, 140));
        noButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        noButton.setFocusPainted(false);

        yesButton.addActionListener(e -> {
            games.remove(selectedRow);
            updateTable();
            updateStatistics();

            JOptionPane.showMessageDialog(this,
                    "Игра \"" + gameToDelete.getTitle() + "\" удалена из каталога.",
                    "Успех", JOptionPane.INFORMATION_MESSAGE);
            confirmDialog.dispose();
        });

        noButton.addActionListener(e -> confirmDialog.dispose());

        // Добавляем эффекты при наведении
        addButtonHoverEffect(yesButton, new Color(220, 60, 80));
        addButtonHoverEffect(noButton, new Color(120, 120, 140));

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        confirmDialog.add(messagePanel, BorderLayout.CENTER);
        confirmDialog.add(buttonPanel, BorderLayout.SOUTH);
        confirmDialog.setVisible(true);
    }

    /**
     * Диалог сохранения каталога
     */
    private void showSaveDialog() {
        if (games.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Каталог пуст. Нечего сохранять.",
                    "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Сохранение каталога", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 250);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(250, 252, 255));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 15, 25));
        panel.setBackground(new Color(250, 252, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel formatLabel = new JLabel("Формат файла:");
        formatLabel.setFont(normalFont);
        formatLabel.setForeground(Color.BLACK);
        panel.add(formatLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JComboBox<String> formatCombo = new JComboBox<>(new String[]{"TXT (текстовый)", "CSV (табличный)"});
        formatCombo.setFont(normalFont);
        formatCombo.setBackground(Color.WHITE);
        formatCombo.setForeground(Color.BLACK);
        panel.add(formatCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel nameLabel = new JLabel("Имя файла:");
        nameLabel.setFont(normalFont);
        nameLabel.setForeground(Color.BLACK);
        panel.add(nameLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField fileNameField = new JTextField("game_catalog_" + new Date().getTime(), 20);
        fileNameField.setFont(normalFont);
        fileNameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 195, 220)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        panel.add(fileNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JCheckBox includeHeaderCheck = new JCheckBox("Включить заголовок с датой сохранения", true);
        includeHeaderCheck.setFont(normalFont);
        includeHeaderCheck.setForeground(Color.BLACK);
        includeHeaderCheck.setBackground(new Color(250, 252, 255));
        panel.add(includeHeaderCheck, gbc);

        // Кнопки
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(250, 252, 255));

        JButton saveButton = new JButton("Сохранить");
        saveButton.setFont(buttonFont);
        saveButton.setForeground(Color.BLACK);
        saveButton.setBackground(new Color(30, 120, 200));
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.setFocusPainted(false);

        JButton cancelButton = new JButton("Отмена");
        cancelButton.setFont(buttonFont);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setBackground(new Color(120, 120, 140));
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelButton.setFocusPainted(false);

        saveButton.addActionListener(e -> {
            String fileName = fileNameField.getText().trim();
            if (fileName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Введите имя файла!", "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String format = ((String) formatCombo.getSelectedItem()).contains("TXT") ? "TXT" : "CSV";
            saveToFile(fileName, format, includeHeaderCheck.isSelected());

            JOptionPane.showMessageDialog(dialog,
                    "Каталог успешно сохранен!\n\n" +
                            "Файл: " + fileName + "." + format.toLowerCase() + "\n" +
                            "Сохранено игр: " + games.size(),
                    "Успех", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        // Добавляем эффекты при наведении
        addButtonHoverEffect(saveButton, new Color(30, 120, 200));
        addButtonHoverEffect(cancelButton, new Color(120, 120, 140));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Диалог загрузки каталога
     */
    private void showLoadDialog() {
        if (!games.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Текущий каталог будет заменен. Продолжить?", "Подтверждение",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выберите файл для загрузки");
        fileChooser.setFont(normalFont);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt")
                        || f.getName().toLowerCase().endsWith(".csv");
            }
            public String getDescription() {
                return "Текстовые файлы (*.txt, *.csv)";
            }
        });

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                loadFromFile(file.getAbsolutePath());
                updateTable();
                updateStatistics();

                JOptionPane.showMessageDialog(this,
                        "Каталог успешно загружен!\n\n" +
                                "Файл: " + file.getName() + "\n" +
                                "Загружено игр: " + games.size(),
                        "Успех", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Ошибка загрузки файла:\n\n" + ex.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Сохранение в файл
     */
    private void saveToFile(String fileName, String format, boolean includeHeader) {
        try {
            if (!fileName.toLowerCase().endsWith("." + format.toLowerCase())) {
                fileName += "." + format.toLowerCase();
            }

            PrintWriter writer = new PrintWriter(new FileWriter(fileName, false), true);

            if (includeHeader) {
                writer.println("=".repeat(70));
                writer.println("КАТАЛОГ КОМПЬЮТЕРНЫХ ИГР ЖАНРА ACTION/RPG");
                writer.println("=".repeat(70));
                writer.println("Дата сохранения: " + new Date());
                writer.println("Всего игр: " + games.size());
                writer.println("=".repeat(70));
                writer.println();
            }

            if (format.equals("CSV")) {
                // CSV формат
                writer.println("Title,Genre,Year,Rating,Developer,Platform");
                for (Game game : games) {
                    writer.println(String.format("\"%s\",\"%s\",%d,%.1f,\"%s\",\"%s\"",
                            game.getTitle().replace("\"", "\"\""),
                            game.getGenre().toString().replace("_", "/"),
                            game.getReleaseYear(),
                            game.getRating(),
                            game.getDeveloper().replace("\"", "\"\""),
                            game.getPlatform().replace("\"", "\"\"")));
                }
            } else {
                // TXT формат
                for (int i = 0; i < games.size(); i++) {
                    Game game = games.get(i);
                    writer.printf("%d. %s%n", i + 1, game.getTitle());
                    writer.printf("   Жанр: %s%n", game.getGenre().toString().replace("_", "/"));
                    writer.printf("   Год выпуска: %d%n", game.getReleaseYear());
                    writer.printf("   Рейтинг: %.1f/10.0%n", game.getRating());
                    writer.printf("   Разработчик: %s%n", game.getDeveloper());
                    writer.printf("   Платформа: %s%n", game.getPlatform());
                    writer.println();
                }
            }

            writer.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка сохранения файла:\n\n" + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Загрузка из файла
     */
    private void loadFromFile(String fileName) throws IOException {
        List<Game> loadedGames = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        boolean isCSV = fileName.toLowerCase().endsWith(".csv");

        if (isCSV) {
            // Чтение CSV
            reader.readLine(); // Пропускаем заголовок
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Простой парсинг CSV (без учета кавычек для простоты)
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (parts.length >= 6) {
                    try {
                        // Убираем кавычки если есть
                        for (int i = 0; i < parts.length; i++) {
                            parts[i] = parts[i].trim();
                            if (parts[i].startsWith("\"") && parts[i].endsWith("\"")) {
                                parts[i] = parts[i].substring(1, parts[i].length() - 1);
                            }
                        }

                        Game.Genre genre = Game.Genre.valueOf(parts[1].toUpperCase().replace("/", "_"));
                        Game game = new Game(
                                parts[0],
                                genre,
                                Integer.parseInt(parts[2]),
                                Double.parseDouble(parts[3]),
                                parts[4],
                                parts[5]
                        );
                        loadedGames.add(game);
                    } catch (Exception e) {
                        // Пропускаем некорректные строки
                        System.err.println("Ошибка парсинга строки: " + line);
                    }
                }
            }
        } else {
            // Чтение TXT
            String title = "", developer = "", platform = "";
            Game.Genre genre = Game.Genre.ACTION_RPG;
            int year = 2020;
            double rating = 5.0;
            boolean readingGame = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.matches("\\d+\\.\\s+.+")) {
                    // Новая игра
                    if (readingGame && !title.isEmpty()) {
                        loadedGames.add(new Game(title, genre, year, rating, developer, platform));
                    }

                    title = line.substring(line.indexOf('.') + 1).trim();
                    readingGame = true;
                } else if (line.startsWith("Жанр:") && readingGame) {
                    String genreStr = line.substring(5).trim().toUpperCase().replace("/", "_");
                    genre = Game.Genre.valueOf(genreStr);
                } else if (line.startsWith("Год выпуска:") && readingGame) {
                    try {
                        year = Integer.parseInt(line.substring(12).trim());
                    } catch (NumberFormatException e) {
                        year = 2020;
                    }
                } else if (line.startsWith("Рейтинг:") && readingGame) {
                    try {
                        String ratingStr = line.substring(8).trim().split("/")[0].trim();
                        rating = Double.parseDouble(ratingStr);
                    } catch (Exception e) {
                        rating = 5.0;
                    }
                } else if (line.startsWith("Разработчик:") && readingGame) {
                    developer = line.substring(12).trim();
                } else if (line.startsWith("Платформа:") && readingGame) {
                    platform = line.substring(10).trim();
                }
            }

            // Добавляем последнюю игру
            if (!title.isEmpty()) {
                loadedGames.add(new Game(title, genre, year, rating, developer, platform));
            }
        }

        reader.close();
        games = loadedGames;
    }

    /**
     * Основной метод запуска
     */
    public static void main(String[] args) {
        // Устанавливаем нативный стиль окон
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Кастомизация цветов для лучшей читаемости
            UIManager.put("Panel.background", new Color(245, 248, 255));
            UIManager.put("OptionPane.background", new Color(245, 248, 255));
            UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 13));
            UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 12));

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Запуск приложения
        SwingUtilities.invokeLater(() -> {
            GameCatalogGUI frame = new GameCatalogGUI();
            frame.setVisible(true);

            // Приветственное сообщение
            JOptionPane.showMessageDialog(frame,
                    "<html><div style='text-align: center;'>"
                            + "<h3 style='color: #0064c8;'>Добро пожаловать в Каталог компьютерных игр!</h3>"
                            + "<p style='margin-top: 10px;'>В каталоге уже загружено <b>20 примеров игр</b>.</p>"
                            + "<p>Вы можете:</p>"
                            + "<ul style='text-align: left; margin-left: 20px;'>"
                            + "<li>Добавлять новые игры</li>"
                            + "<li>Редактировать существующие</li>"
                            + "<li>Удалять игры из каталога</li>"
                            + "<li>Искать и фильтровать игры</li>"
                            + "<li>Сохранять каталог в файлы</li>"
                            + "<li>Загружать каталог из файлов</li>"
                            + "<li>Просматривать статистику</li>"
                            + "</ul>"
                            + "<p style='margin-top: 15px; color: #666;'>Приятного использования! 🎮</p>"
                            + "</div></html>",
                    "Добро пожаловать!",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }
}