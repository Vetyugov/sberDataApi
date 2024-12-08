//package ru.thebestsolution.dataapi.dataapiservice.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.stereotype.Component;
//import ru.thebestsolution.dataapi.dataapiservice.data.*;
//
//import java.time.LocalDate;
//import java.time.Period;
//import java.time.format.DateTimeFormatter;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//public class JsonConverterGiga {
//    public String convert(String studentApplicationJson, String fizPersonInfoJson, String schoolEducationInfoJson, String bkiCreditInfoJson) throws JsonProcessingException {
//
//        // Преобразование входных JSON-объектов в соответствующие объекты Java
//        ObjectMapper mapper = new ObjectMapper();
//        StudentApplication studentApplication = mapper.readValue(studentApplicationJson, StudentApplication.class);
//        FizPersonInfo fizPersonInfo = mapper.readValue(fizPersonInfoJson, FizPersonInfo.class);
//        SchoolEducationInfo schoolEducationInfo = mapper.readValue(schoolEducationInfoJson, SchoolEducationInfo.class);
//        BkiCreditInfo bkiCreditInfo = mapper.readValue(bkiCreditInfoJson, BkiCreditInfo.class);
//
//        // Создание объекта Данные для модели
//        ModelData modelData = new ModelData();
//        modelData.setStudentData(createStudentData(studentApplication, fizPersonInfo, schoolEducationInfo));
//        modelData.setParentsData(createParentsData(fizPersonInfo));
//
//        // Преобразование объекта Модель в JSON
//        return mapper.writeValueAsString(modelData);
//    }
//
//    private StudentData createStudentData(StudentApplication studentApplication, FizPersonInfo fizPersonInfo, SchoolEducationInfo schoolEducationInfo) {
//        StudentData studentData = new StudentData();
//        studentData.setAge(calculateAge(fizPersonInfo.getBirthDate()));
//        studentData.setResidenceRegion(regionCodeFromAddress(studentApplication.getPersonInfo().getAddresses().getRegistrationAddress()));
//        studentData.setAverageScore(averageScoreFromSchoolGrades(schoolEducationInfo.getSubjects()));
//        studentData.setProfession(schoolEducationInfo.getUniversityInfo().getEducationSpecialty());
//        studentData.setAdministrativeOffense(fizPersonInfo.getOffenses() != null ? fizPersonInfo.getOffenses().size() : 0);
//        studentData.setOverdueLoanStudent(calculateOverdueLoanPercentage(bkiCreditInfo));
//        return studentData;
//    }
//
//    private ParentsData createParentsData(FizPersonInfo fizPersonInfo) {
//        ParentsData parentsData = new ParentsData();
//        parentsData.setOverdueLoanParents(calculateOverdueLoanPercentageForParents(fizPersonInfo));
//        parentsData.setParentsBankruptcy(fizPersonInfo.isBankruptcyCurrent());
//        return parentsData;
//    }
//
//    private int calculateAge(String birthDate) {
//        // Получаем текущую дату и время
//        LocalDate now = LocalDate.now();
//
//        // Разбираем строку с датой рождения
//        LocalDate birthDateLocal = LocalDate.parse(birthDate);
//
//        // Вычисляем возраст
//        return Period.between(birthDateLocal, now).getYears();
//    }
//
//    private String regionCodeFromAddress(String address) {
//        // Пример списка регионов и их кодов
//        Map<String, String> regionsMap = new HashMap<>();
//        regionsMap.put("Москва", "77");
//        regionsMap.put("Санкт-Петербург", "78");
//        regionsMap.put("Краснодарский край", "23");
//        // Добавьте другие регионы по необходимости
//
//        // Предположим, что адрес приходит в формате "город, улица, дом"
//        String cityPart = address.split(",")[0]; // Извлекаем городскую часть адреса
//
//        // Поиск кода региона по городу
//        return regionsMap.get(cityPart);
//    }
//
//    private double averageScoreFromSchoolGrades(List<Subject> subjects) {
//        if (subjects == null || subjects.isEmpty()) {
//            return 0; // Возвращаем 0, если предметы отсутствуют
//        }
//
//        // Суммируем все оценки
//        double sumOfGrades = subjects.stream()
//                .mapToDouble(Subject::getGrade)
//                .sum();
//
//        // Считаем количество предметов
//        int countOfSubjects = subjects.size();
//
//        // Рассчитываем средний балл
//        return sumOfGrades / countOfSubjects;
//    }
//
//    private double calculateOverdueLoanPercentage(BkiCreditInfo bkiCreditInfo) {
//        int totalAccounts = bkiCreditInfo.getCreditHistory().getTotalCreditAccounts();
//        int pastDueAccounts = bkiCreditInfo.getCreditHistory().getAccounts().stream()
//                .filter(account -> account.getBalance().getPastDue() > 0)
//                .count();
//
//        if (totalAccounts == 0) {
//            return 0; // Нет кредитов вообще, значит и просрочек нет
//        }
//
//        return ((double) pastDueAccounts / totalAccounts) * 100;
//    }
//
//    private double calculateOverdueLoanPercentageForParents(FizPersonInfo fizPersonInfo) {
//        if (fizPersonInfo.getParentsInfo() == null || fizPersonInfo.getParentsInfo().getParents() == null || fizPersonInfo.getParentsInfo().getParents().isEmpty()) {
//            return 0; // Нет информации о родителях или их кредитах
//        }
//
//        int pastDueAccounts = 0;
//        int totalAccounts = 0;
//
//        for (Parent parent : fizPersonInfo.getParentsInfo().getParents()) {
//            if (parent.getPhones() != null && !parent.getPhones().getMobilePhone().isEmpty()) {
//                totalAccounts++;
//                if (parent.getCreditHistory() != null && !parent.getCreditHistory().getAccounts().isEmpty()) {
//                    for (Account account : parent.getCreditHistory().getAccounts()) {
//                        if (account.getBalance() != null && account.getBalance().getPastDue() > 0) {
//                            pastDueAccounts++;
//                            break; // Предполагается, что если есть хотя бы одна просрочка, она учитывается
//                        }
//                    }
//                }
//            }
//        }
//
//        if (totalAccounts == 0) {
//            return 0; // Нет кредитов у родителей
//        }
//
//        return ((double) pastDueAccounts / totalAccounts) * 100;
//    }
//}
