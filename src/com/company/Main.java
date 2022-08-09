package com.company;

public class Main {

    public static void main(String[] args) {
        /**
         *  Veritabanı ve tablo oluşturup kelimeleri URL'den alıp tabloya yazar.
         *  bir kez çalıştırılıp daha sonra yorum satırı haline getirilmelidir.
         *  Aksi halde her çalışmada zaman ve performans kaybına sebep olacaktır.
         *
         *   DatabaseHelper helper = new DatabaseHelper();
         *   helper.createDatabase();
         */

        DatabaseHelper helper = new DatabaseHelper();
        if (helper.isFull()) {
            helper.createDatabase();
        }

        /**
         *  Oyunu başlatır
         */
        try {
            GameService gameService = new GameService();
            gameService.startGame();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
