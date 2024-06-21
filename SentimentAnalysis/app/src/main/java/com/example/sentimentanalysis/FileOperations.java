package com.example.sentimentanalysis;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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

    public static String getOptionValue(Context context, String fileName, String searchStr) {
        String value = "";

        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(searchStr)) {
                    String[] parts = line.split("=");

                    if (parts.length == 2) {
                        value = parts[1];
                    }

                    break;
                }
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return value;
    }

    public static void setOptionValue(Context context, String fileName, String searchStr, String value) {
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(searchStr)) {
                    line = searchStr + "=" + value;
                }

                stringBuilder.append(line).append("\n");
            }

            bufferedReader.close();

            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(stringBuilder.toString().getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
