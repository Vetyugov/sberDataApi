package ru.thebestsolution.dataapi.dataapiservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.thebestsolution.dataapi.dataapiservice.api.GenerateModelDataJsonRequest;
import ru.thebestsolution.dataapi.dataapiservice.llmClient.GigaChatClient;
import ru.thebestsolution.dataapi.dataapiservice.service.JsonConverter;

@RestController
@RequestMapping(value = "v1/generate")
@Slf4j
public class GenerateJsonController {
    private final GigaChatClient gigaChatClient;
    private final JsonConverter jsonConverter;

    public GenerateJsonController(GigaChatClient gigaChatClient, JsonConverter jsonConverter) {
        this.gigaChatClient = gigaChatClient;
        this.jsonConverter = jsonConverter;
    }

//    @PostMapping
//    public void generate(){
//        String s = gigaChatClient.sendMessage("Сгенери пример JSON состоящего из 4 полей");
//        System.out.println("Ответ: " + s);
//    }

    @PostMapping
    public String generate(@RequestBody GenerateModelDataJsonRequest generateModelDataJsonRequest) throws JsonProcessingException {
        String modelData = jsonConverter.convert(
                generateModelDataJsonRequest.getStudentApplication(),
                generateModelDataJsonRequest.getFizPersonInfo(),
                generateModelDataJsonRequest.getSchoolEducationInfo(),
                generateModelDataJsonRequest.getBkiCreditInfo()
        );
        System.out.println("Ответ: " + modelData);
        return modelData;
    }
}
