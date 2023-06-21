package univ.tuit.telegramclone.util;

import java.util.Random;

public abstract class StringHelper {
    public static int generateRandomNumber(int length){
        int max = Integer.parseInt("999999999".substring(0, length));
        int min = Integer.parseInt("100000000".substring(0, length));

        Random random = new Random();

        return random.nextInt((max - min)+1) +min;
    }
}
