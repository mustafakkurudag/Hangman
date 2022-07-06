package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private final String URL = "jdbc:mysql://localhost:3307/";
    private final String USER = "root";
    private final String PASSWORD = "12345";
    private final String DBNAME = "words";

    /**
     * Veri tabanı oluşturma ve bağlantı.
     */
    public void createDatabase() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();

            String sql = "CREATE DATABASE IF NOT EXISTS " + DBNAME;

            System.out.println(statement.execute(sql));
            System.out.println("Veritabanı oluşturuldu");

            createTable();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tablo oluşturur.
     */
    private void createTable() {
        try {
            Connection connection = DriverManager.getConnection(URL+DBNAME, USER, PASSWORD);
            Statement statement = connection.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS turkish" +
                    "(id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL, " +
                    "name VARCHAR(100) NOT NULL)";

            statement.executeUpdate(sql);
            System.out.println("Veritabanında tablo oluşturuldu...");

            addWords();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Oyun ilk çalıştırıldığında WordService classında listeye alınan kelimeler burada tabloya eklenir.
     * Sadece bir kez çalışır.
     */
    private void addWords() {
        WordService service = new WordService();

        try {
            Connection connection = DriverManager.getConnection(URL+DBNAME, USER, PASSWORD);

            String sql = "INSERT INTO " + DBNAME + ".turkish VALUES (?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            List<Word> words = service.fetchWords();
            int i = 1;

            for (Word wrd: words) {
                statement.setInt(1, i);
                statement.setString(2, wrd.getWord());
                i++;
                statement.executeUpdate();
            }

            System.out.println("Kelimeler veritabanına eklendi.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Girilen parametreye göre veritabanında kelime aranır.
     *  Bulunursa true bulunmazsa false döner.
     */
    public boolean searchWord(String word) {
        try {
            Connection connection = DriverManager.getConnection(URL+DBNAME, USER, PASSWORD);
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            String sql = "SELECT * FROM " + DBNAME + ".turkish WHERE name = '" + word + "'";

            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                System.out.println("Kelime bulundu. Oyun devam ediyor.");

                return true;
            } else {
                System.out.println("Kelime bulunamadı...");

                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     *  İnsanın gireceği n sayısı uzunluğunda rastgele bir kelime veritabanından alınır.
     */
    public String searchNLetterRandomWord(int n) {
        String randWord = null;

        try {
            Connection connection = DriverManager.getConnection(URL+DBNAME, USER, PASSWORD);
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            String sql =
                    "SELECT * FROM " + DBNAME + ". turkish WHERE length(name) = "
                    + n + " ORDER BY RAND() LIMIT 1";

            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Kelimeyi belirledim. Oyuna başlayabilirsin.");
                randWord =  resultSet.getString("name");
            } else {
                System.out.println("Kelime bulunamadı...");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return randWord;
    }
}
