package com.example.sentimentanalysis;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOperations {
    public static void addFile(Context context, String fileName, String text) {
        try {
            FileOutputStream file = context.openFileOutput(fileName, Context.MODE_APPEND);

            file.write(text.getBytes());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(Context context, String fileName) {
        StringBuilder text = new StringBuilder();

        try {
            FileInputStream file = context.openFileInput(fileName);

            int character;

            while ((character = file.read()) != -1) {
                text.append((char) character);
            }

            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text.toString();
    }
}
