package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Donghun Shin
 * Created on 2023. 01. 03
 */
public class FileUtil {

    public static void write(final File file, final String text) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
