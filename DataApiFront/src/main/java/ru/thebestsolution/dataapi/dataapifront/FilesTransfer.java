package ru.thebestsolution.dataapi.dataapifront;

import ru.thebestsolution.dataapi.dataapifront.api.GenerateModelDataJsonRequest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FilesTransfer {

    private final WebClientHelper webClientHelper = new WebClientHelper();

    public File transfer(List<File> files) throws FileNotMappedException, IOException {
        GenerateModelDataJsonRequest request = new GenerateModelDataJsonRequest();
        files.forEach(file -> {
            try {
                String result = Files.readString(Path.of(file.getAbsolutePath()));
                if (result.contains("personInfo")) {
                    request.setStudentApplication(result);
                    System.out.println("setStudentApplication - OK");
                } else if (result.contains("loaner")) {
                    request.setBkiCreditInfo(result);
                    System.out.println("setBkiCreditInfo - OK");
                } else if (result.contains("personData")) {
                    request.setSchoolEducationInfo(result);
                    System.out.println("setSchoolEducationInfo - OK");
                } else if (result.contains("result")) {
                    request.setFizPersonInfo(result);
                    System.out.println("setFizPersonInfo - OK");
                } else {
                    throw new FileNotMappedException(file.getAbsoluteFile().getAbsolutePath());
                }
            } catch (IOException | FileNotMappedException e) {
                throw new RuntimeException(e);
            }
        });

        if (request.getBkiCreditInfo() != null &&
                request.getFizPersonInfo() != null &&
                request.getStudentApplication() != null &&
                request.getSchoolEducationInfo() != null) {
            String s = webClientHelper.sendGetRequest(request);
            File f = new File("C:\\Users\\Admin\\Downloads\\result.txt");
            try (FileWriter fos = new FileWriter(f)) {
                fos.write(s);
                fos.flush();
                return f;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println(request);
            throw new FileNotMappedException("request Не заполнен");
        }
    }
}
