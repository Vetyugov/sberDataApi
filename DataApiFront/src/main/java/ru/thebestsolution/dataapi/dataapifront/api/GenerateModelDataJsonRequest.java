package ru.thebestsolution.dataapi.dataapifront.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@JsonAutoDetect
@ToString
public class GenerateModelDataJsonRequest implements Serializable {
    private String bkiCreditInfo;
    private String fizPersonInfo;
    private String SchoolEducationInfo;
    private String studentApplication;

    public GenerateModelDataJsonRequest(String bkiCreditInfo, String fizPersonInfo, String schoolEducationInfo, String studentApplication) {
        this.bkiCreditInfo = bkiCreditInfo;
        this.fizPersonInfo = fizPersonInfo;
        SchoolEducationInfo = schoolEducationInfo;
        this.studentApplication = studentApplication;
    }

    public GenerateModelDataJsonRequest() {
    }

    public String getBkiCreditInfo() {
        return bkiCreditInfo;
    }

    public String getFizPersonInfo() {
        return fizPersonInfo;
    }

    public String getSchoolEducationInfo() {
        return SchoolEducationInfo;
    }

    public String getStudentApplication() {
        return studentApplication;
    }

    public void setStudentApplication(String studentApplication) {
        this.studentApplication = studentApplication;
    }

    public void setSchoolEducationInfo(String schoolEducationInfo) {
        SchoolEducationInfo = schoolEducationInfo;
    }

    public void setFizPersonInfo(String fizPersonInfo) {
        this.fizPersonInfo = fizPersonInfo;
    }

    public void setBkiCreditInfo(String bkiCreditInfo) {
        this.bkiCreditInfo = bkiCreditInfo;
    }
}