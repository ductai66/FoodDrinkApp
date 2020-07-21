package com.tai06.dothetai.fooddrink.Contacts;

import java.util.Random;

public class RandomCode {
    public int randomCode(int min, int max) {
        Random random = new Random();
        int result = 0;
        do {
            result = random.nextInt((max - min + 1) + min);
        } while (result < min);
        return result;
    }
}
