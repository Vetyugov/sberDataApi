package ru.thebestsolution.dataapi.dataapiservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.thebestsolution.dataapi.dataapiservice.data.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JsonConverter {

    public String convert(String studentApplicationJson, String fizPersonInfoJson, String schoolEducationInfoJson, String bkiCreditInfoJson) throws JsonProcessingException {

        // Преобразование входных JSON-объектов в соответствующие объекты Java
        ObjectMapper mapper = new ObjectMapper();
        StudentApplication studentApplication = mapper.readValue(studentApplicationJson, StudentApplication.class);
        FizPersonInfo fizPersonInfo = mapper.readValue(fizPersonInfoJson, FizPersonInfo.class);
        SchoolEducationInfo schoolEducationInfo = mapper.readValue(schoolEducationInfoJson, SchoolEducationInfo.class);
        BkiCreditInfo bkiCreditInfo = mapper.readValue(bkiCreditInfoJson, BkiCreditInfo.class);

        // Создание объекта Данные для модели
        ModelData modelData = new ModelData();
        modelData.setStudentData(createStudentData(studentApplication, fizPersonInfo, schoolEducationInfo, bkiCreditInfo));
        modelData.setParentsData(createParentsData(fizPersonInfo));

        // Преобразование объекта Модель в JSON
        return mapper.writeValueAsString(modelData);
    }

    private StudentData createStudentData(StudentApplication studentApplication, FizPersonInfo fizPersonInfo, SchoolEducationInfo schoolEducationInfo,BkiCreditInfo bkiCreditInfo) {
        StudentData studentData = new StudentData();
        studentData.setAge(calculateAge(fizPersonInfo.getResult().getBirthDate()));
        studentData.setResidenceRegion(regionCodeFromAddress(studentApplication.getPersonInfo().getAddresses().getRegistrationAddress()));
        studentData.setAverageScore(averageScoreFromSchoolGrades(schoolEducationInfo.getPersonData().getSchoolInfo().getSubjects()));
        studentData.setProfession(schoolEducationInfo.getPersonData().getUniversityInfo()!=null ?schoolEducationInfo.getPersonData().getUniversityInfo().getEducationSpecialty():null);
        studentData.setAdministrativeOffense(fizPersonInfo.getResult().getOffesnseHistory() != null ? fizPersonInfo.getResult().getOffesnseHistory().size() : 0);
        studentData.setOverdueLoanStudent(calculateOverdueLoanPercentage(bkiCreditInfo));
        return studentData;
    }

    private ParentsData createParentsData(FizPersonInfo fizPersonInfo) {
        ParentsData parentsData = new ParentsData();
        parentsData.setOverdueLoanParents(calculateOverdueLoanPercentageForParents(List.of()));
        parentsData.setParentsBankruptcy(fizPersonInfo.getResult().getBankruptcy()!= null);
        return parentsData;
    }

    private int calculateAge(String birthDate) {
        // Получаем текущую дату и время
        LocalDate now = LocalDate.now();

        // Разбираем строку с датой рождения
        LocalDate birthDateLocal = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        // Вычисляем возраст
        return Period.between(birthDateLocal, now).getYears();
    }

    private String regionCodeFromAddress(String address) {
        // Пример списка регионов и их кодов
        Map<String, String> regionsMap = new HashMap<>();
        regionsMap.put("Москва", "77");
        regionsMap.put("Санкт-Петербург", "78");
        regionsMap.put("Краснодарский край", "23");
        // Добавьте другие регионы по необходимости

        // Предположим, что адрес приходит в формате "город, улица, дом"
        String cityPart = address.split(",")[0]; // Извлекаем городскую часть адреса

        // Поиск кода региона по городу
        return regionsMap.get(cityPart);
    }

    private double averageScoreFromSchoolGrades(List<Subject> subjects) {
        if (subjects == null || subjects.isEmpty()) {
            return 0; // Возвращаем 0, если предметы отсутствуют
        }

        // Суммируем все оценки
        double sumOfGrades = subjects.stream()
                .mapToDouble(subject -> Double.parseDouble(subject.getGrade()))
                .sum();

        // Считаем количество предметов
        int countOfSubjects = subjects.size();

        // Рассчитываем средний балл
        return sumOfGrades / countOfSubjects;
    }

    private double calculateOverdueLoanPercentage(BkiCreditInfo bkiCreditInfo) {
        int totalAccounts = bkiCreditInfo.getLoaner().getCreditHistory().getTotalCreditAccounts();
        long pastDueAccounts = bkiCreditInfo.getLoaner().getCreditHistory().getAccounts().stream()
                .filter(account -> account.getBalance().getPastDue() > 0)
                .count();

        if (totalAccounts == 0) {
            return 0; // Нет кредитов вообще, значит и просрочек нет
        }

        return ((double) pastDueAccounts / totalAccounts) * 100;
    }

    private double calculateOverdueLoanPercentageForParents(List<BkiCreditInfo> parents) {
        if (parents.isEmpty()) {
            return 0; // Нет информации о родителях или их кредитах
        }

        int pastDueAccounts = 0;
        int totalAccounts = 0;

        for (Loaner parent : parents.stream().map(BkiCreditInfo::getLoaner).toList()) {
            if (parent.getPhones() != null && !parent.getPhones().getMobilePhone().isEmpty()) {
                totalAccounts++;
                if (parent.getCreditHistory() != null && !parent.getCreditHistory().getAccounts().isEmpty()) {
                    for (Account account : parent.getCreditHistory().getAccounts()) {
                        if (account.getBalance() != null && account.getBalance().getPastDue() > 0) {
                            pastDueAccounts++;
                            break; // Предполагается, что если есть хотя бы одна просрочка, она учитывается
                        }
                    }
                }
            }
        }

        if (totalAccounts == 0) {
            return 0; // Нет кредитов у родителей
        }

        return ((double) pastDueAccounts / totalAccounts) * 100;
    }
}