package com.n3;

import com.Constants;
import com.n2.util.FileReader;

public class TestState2 {
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            FileReader.read(Constants.MP4_FULL_PATH);
            FileReader.read(Constants.MP4_FULL_PATH);
            FileReader.read(Constants.MP4_FULL_PATH);
        }, "t1").start();

        Thread.sleep(1000);
        System.out.println("ok");
    }
}
