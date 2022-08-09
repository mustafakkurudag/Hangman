package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WordService {

    private URL newUrl;
    public Word word = new Word();
    private int letterNumber;
    private Scanner scanner = new Scanner(System.in);
    private DatabaseHelper helper = new DatabaseHelper();

    /**
     * Kelimeler dosyadan listeye alınır.
     */
    public List<String> fetchWordsFromFile() {
        List<String> words = new ArrayList<>();

        try {
            URL path = WordService.class.getResource("words.txt");
            File file = new File(path.getFile());
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String myWord = reader.nextLine();
                word.setWord(myWord.split(" ")[0]);
                words.add(word.getWord());
            }

            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return words;
    }

    /**
     * İnsan bilgisayarın belirlediği bir sayı uzunluğunda kelimeyi ekrana girer.
     */
    public String inputAndCheckWord() {
        int number = decideLetterNumberComputer();

        System.out.println("Oyuna başlamak için tahmin etmem gereken "
                + number + " harfli bir kelime belirleyiniz");
        String wrd = scanner.next();
        word.setWord(wrd);

        if (compare(number, word.getWord())) {
            System.out.println("Kelime aranıyor...");

            if(helper.searchWord(this.word.getWord().trim())) {
                return this.word.getWord().trim();
            } else {
                word.setWord(null);

                return null;
            }
        } else {
            System.out.println("Lütfen belirlediğim sayı uzunluğunda bir kelime giriniz.");

            return null;
        }
    }

    /**
      *  Bilgisayar insanın girdiği sayı uzunluğunda rastgele bir kelimeyi veritabanından çeker.
     */
    public String inputAndCheckWordComputer() {
        int number = inputLetterNumber();
        System.out.println("Kelimeyi belirliyorum...");

        word.setWord(helper.searchNLetterRandomWord(number));

        return word.getWord();
    }

    /**
     *  İnsan bilmesi gereken kelime için uzunluk belirler
     */
    private int inputLetterNumber() {
        do {
            System.out.print("Harf sayısını giriniz(3 ile 10 arasında): ");
            this.letterNumber = scanner.nextInt();

        } while (this.letterNumber < 3 || this.letterNumber > 11);

        return this.letterNumber;
    }

    /**
     *  Bilgisayar insanın söyleyeceği kelimenin uzunluğunu belirler
     */
    private int decideLetterNumberComputer() {
        try {
            Random random = new Random();

            System.out.println("Harf sayısını belirliyorum...");

            Thread.sleep(3000);

            do {
                this.letterNumber = random.nextInt(3, 12);

            } while (this.letterNumber < 3);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this.letterNumber;
    }

    /**
     *  Girilen kelime ile uzunluk eşit mi kontrol eder
     */
    public boolean compare(int number, String w) {
        if (w.length() == number) {
            return true;
        } else {
            return false;
        }
    }

}
