package ru.thebestsolution.dataapi.dataapifront;

import java.io.File;

public class FileNotMappedException extends Exception{
    public FileNotMappedException(String fileName) {
        super("Не удалось распознать файл: " + fileName);
    }
}
