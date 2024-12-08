package ru.thebestsolution.dataapi.dataapifront;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HelloController {
    @FXML
    private TextArea chosenFilesTextArea;
    @FXML
    private Button chooseFileButton;
    @FXML
    private Button transferButton;
    @FXML
    private Label statusLabel;
    @FXML
    private Button getResultButton;

    private List<File> selectedFiles = new ArrayList<>();

    private final FilesTransfer transfer = new FilesTransfer();
    private File transferResult = null;


    private void readyForChooseFile(){
        chooseFileButton.setDisable(false);
        transferButton.setDisable(true);
        getResultButton.setDisable(true);
    }
    private void readyForTransfer(){
        chooseFileButton.setDisable(false);
        transferButton.setDisable(false);
        getResultButton.setDisable(true);
    }
    private void readyForGetResult(){
        chooseFileButton.setDisable(false);
        transferButton.setDisable(false);
        getResultButton.setDisable(false);
    }
    @FXML
    protected void initialize() {
        readyForChooseFile();
    }
    /**
     * Нажатие на кнопку Обзор
     */
    @FXML
    protected void onChooseFileButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбор файлов");

        // Установите фильтры для файлов, если необходимо
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Все файлы (*.*)", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);

        // Открытие окна выбора файлов и получение выбранных файлов
        List<File> files = fileChooser.showOpenMultipleDialog(null);

        if (files != null) {
            selectedFiles.clear();
            selectedFiles.addAll(files);
            System.out.println("Выбранные файлы:");
            selectedFiles.forEach(file -> System.out.println(file.getAbsolutePath()));
            chosenFilesTextArea.setText(selectedFiles.stream().map(it -> it.getAbsoluteFile().getPath() + "\n").collect(Collectors.joining()));
            readyForTransfer();
        }else {
            statusLabel.setText("Не выбраны файлы");
            readyForChooseFile();
        }
    }

    /**
     * Нажатие на кнопку Преобразовать
     */
    @FXML
    protected void onTransferButtonClick() throws FileNotMappedException, IOException {
        if(selectedFiles.isEmpty()){
            statusLabel.setText("Не выбраны файл");
            readyForChooseFile();
            return;
        }
        transferResult = transfer.transfer(selectedFiles);
        if (transferResult != null){
            statusLabel.setText("Успешно!");
            readyForGetResult();
        } else {
            statusLabel.setText("Что-то пошло не так...");
            readyForChooseFile();
        }
    }

    /**
     * Нажатие на кнопку Получить результат
     */
    @FXML
    protected void onGetResultButtonClick() {
        if(transferResult == null){
            statusLabel.setText("Результат преобразования не найден");
            readyForChooseFile();
            return;
        }

        // Запрос каталога для сохранения
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выберите каталог для сохранения файла");
        File chosenDirectory = directoryChooser.showDialog(null);

        if (chosenDirectory != null) {
            // Укажите имя файла, который хотите сохранить
            String fileName = "toModelResult.txt"; // Имя файла можно задать любое
            File fileToSave = new File(chosenDirectory, fileName);

            // Вызов метода для сохранения файла
            try {
                // Сохранение файла
                try (FileOutputStream fos = new FileOutputStream(fileToSave);FileInputStream fis = new FileInputStream(transferResult)) {
                    fos.write(fis.readAllBytes());
                    System.out.println("Файл успешно сохранён: " + fileToSave.getAbsolutePath());
                }
            } catch (IOException e) {
                statusLabel.setText("Ошибка при сохранении файла");
                System.out.println("Ошибка при сохранении файла: " + e);
            }
        } else {
            statusLabel.setText("Каталог не выбран");
            System.out.println("Каталог не выбран.");
        }
    }
}