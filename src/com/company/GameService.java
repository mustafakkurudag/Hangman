package com.company;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameService {
    private Scanner scanner = new Scanner(System.in);
    private Word word = new Word();
    private DatabaseHelper helper = new DatabaseHelper();
    private WordService service = new WordService();
    private int whoStarts;
    private int rightsLeft = 10;

    /**
     *  Oyunu başlatan metod.
     */
    public void startGame() {
        System.out.println("Adam asmaca oyununa hoşgeldiniz.");
        whoStarts = decideWhoStart();

        if (whoStarts == 0) {
            service.inputAndCheckWord();
            guessTheWordComputer();
        } else if (whoStarts == 1) {
            service.inputAndCheckWordComputer();
            guessTheWord();
        }
    }

    /**
     * İnsan kelimeyi tahmin eder.
     */
    public void guessTheWord() {
        this.word.setWord(service.word.getWord());
        ArrayList<Character> guessList = new ArrayList<>();
        boolean isWin = false;
        String tempWord = createFirstTempWord(this.word.getWord().length());

        if (this.word.getWord() != null) {
            while(true) {
                System.out.println(rightsLeft + " adet deneme hakkınız var");
                System.out.print("Harf tahmin edin: ");
                String guess = scanner.next();

                guessList.add(guess.charAt(0));

                String maskedWord = getMaskedWord(this.word.getWord(), guessList);

                if (tempWord.equals(maskedWord)) {
                    rightsLeft--;
                }

                tempWord = maskedWord;
                System.out.println(maskedWord);

                if (maskedWord.equals(this.word.getWord())) {
                    isWin = true;
                    break;
                }

                if (rightsLeft == 0) {
                    break;
                }
            }
        } else {
            System.out.println("Oyun bitti");
        }

        if (!isWin) {
            System.out.println("Malesef oyunu kaybettiniz...");
            System.out.println("Doğru kelime: " + service.word.getWord());
        } else {
            System.out.println("Tebrikler bildiniz...");
        }
    }

    /**
     * Bilgisayar kelimeyi tahmin eder.
     */
    public void guessTheWordComputer()  {
        try {
            this.word.setWord(service.word.getWord());
            ArrayList<Character> guessList = new ArrayList<>();
            boolean isWin = false;
            String tempWord = createFirstTempWord(this.word.getWord().length());

            if (this.word.getWord() != null) {

                while (true) {
                    System.out.println(rightsLeft + " adet deneme hakkım var");
                    String guess = specifyRandChar();

                    while (true) {
                        if (!guessList.contains(guess.charAt(0))) {
                            guessList.add(guess.charAt(0));
                            break;
                        } else {
                            guess = specifyRandChar();
                        }
                    }

                    System.out.print("Harf tahmin ediyorum: ");
                    Thread.sleep(3000);

                    System.out.println(guess);
                    Thread.sleep(1000);

                    String maskedWord = getMaskedWord(this.word.getWord(), guessList);

                    if (tempWord.equals(maskedWord)) {
                        rightsLeft--;
                    }

                    tempWord = maskedWord;
                    System.out.println(maskedWord);

                    if (maskedWord.equals(this.word.getWord())) {
                        isWin = true;
                        break;
                    }

                    if (rightsLeft == 0) {
                        break;
                    }
                }
            } else {
                System.out.println("Oyun bitti");
            }

            if (!isWin) {
                System.out.println("Malesef oyunu kaybettim...");
            } else {
                System.out.println("Yaşasın kazandım...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  Bilgisayar rastgele harf tahmin eder.
     */
    private String specifyRandChar() {
        String letters = "abcçdefgğhıijklmnoöprsştuüvyz";
        Random rnd = new Random();
        char c = letters.charAt(rnd.nextInt(letters.length()));

        return String.valueOf(c);
    }

    /**
     *  Girilen değere göre maskeli bir kelime oluşturulur. Doğru harf girilince bu kelimeye eklenir.
     */
    public String getMaskedWord(String word, ArrayList<Character> guessList) {
        String result = "";

        for (int i = 0; i < word.length(); i++) {
            Character currentChar = word.charAt(i);

            if (guessList.contains(currentChar)) {
                result = result.concat(currentChar.toString());
            } else {
                result = result.concat("_");
            }
        }

        return result;
    }

    /**
     * Kelime yanlışsa maskeli kelime aynı olacak.
     * İlk deneme yanlış olursa maskeli kelime ile
     * karşılaştırılması için geçici bir maskeli kelime oluşturulur.
     */
    public String createFirstTempWord(int length) {
        String firstTemp = "";

        for (int i = 0; i < length; i++) {
            firstTemp = firstTemp.concat("_");
        }

        return firstTemp;
    }

    /**
     *  Oyuna kimin başlayacağına karar verilir.
     */
    public int decideWhoStart() {
        Random random = new Random();
        int num = random.nextInt(10);

        System.out.println("Tek mi çift mi?");
        String guess = scanner.next();

        if (guess.equals("çift")) {
            if (num % 2 == 0) {
                System.out.println("Sayı = " + num
                        +"\nTebrikler. Bilgisayar kelimeyi seçecek. İnsan bulmaya çalışacak.");
                return 1;
            }
            else {
                System.out.println("Sayı = " + num
                        +"\n İnsan kelimeyi seçecek. Bilgisayar bulmaya çalışacak.");
                return 0;
            }
        } else if (guess.equals("tek")) {
            if (num % 2 == 0) {
                System.out.println("Sayı = " + num
                        +"\n İnsan kelimeyi seçecek. Bilgisayar bulmaya çalışacak.");
                return 0;
            }
            else {
                System.out.println("Sayı = " + num
                        +"\nTebrikler. Bilgisayar kelimeyi seçecek. İnsan bulmaya çalışacak.");
                return 1;
            }
        } else {
            System.out.println("Lütfen geçerli bir tahmin yapınız.");
            return -1;
        }
    }

}
