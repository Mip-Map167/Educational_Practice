package com.gamecatalog;

import javax.swing.*;

/**
 * Главный класс для запуска приложения
 */
public class Main {
    public static void main(String[] args) {
        // Запуск в потоке событий Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Устанавливаем красивый стиль окон
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Создаем и показываем главное окно
            GameCatalogGUI frame = new GameCatalogGUI();
            frame.setVisible(true);

            // Показываем приветственное сообщение
            JOptionPane.showMessageDialog(frame,
                    "Добро пожаловать в Каталог компьютерных игр!\n\n" +
                            "В каталоге уже загружено 20 примеров игр.\n" +
                            "Вы можете добавлять, редактировать, удалять игры,\n" +
                            "сохранять и загружать каталог, просматривать статистику.",
                    "Добро пожаловать!",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }
}