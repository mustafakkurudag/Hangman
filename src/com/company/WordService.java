package com.company;

import java.io.BufferedReader;
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
     *  Veritabanından kelimeler bir listeye alınır.
     */
    public List<Word> fetchWords() {
        List<Word> words = new ArrayList<>();

        try {
            newUrl = new URL("https://raw.githubusercontent.com/StarlangSoftware/Dictionary/master/src/main/resources/turkish_dictionary.txt");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(newUrl.openStream())
            );

            String myWord;
            while ((myWord = in.readLine()) != null) {
                word.setWord(myWord.split(" ")[0]);
                words.add(word);
            }

            in.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
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
