package ca.controller;

import java.io.File;
import java.io.FileInputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.controller.data.FromVizData;



@RestController
@RequestMapping("/api")
public class SaveController {
    
    /**
     * Obsługuje żądanie zapisania danych
     * @param data
     * @return
     */
    @RequestMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String saveRequest(@RequestBody FromVizData data) {
        
        return save(data);
    }

    /**
     * Obsluguje żądanie wczytania danych
     * @param fileName
     * @return
     */
    @PostMapping(value = "read", consumes = MediaType.APPLICATION_JSON_VALUE)
    public FromVizData readRequest(@RequestBody String fileName){
        return read(fileName);
    }

    public static String save(FromVizData data) {
        if (data != null) {
            String name = data.getFileName();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
                File saveFile = new File("src/main/resources/static/saves/" + name + ".json");
                saveFile.getParentFile().mkdirs();
                saveFile.createNewFile();
                objectMapper.writeValue(saveFile, data);
                return "{\"path\": \"" + saveFile.getAbsolutePath()+"\"}";
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
    public static FromVizData read(String fileName) {
        
        ObjectMapper obj = new ObjectMapper();
        try{
            FromVizData tmp = obj.readValue(new FileInputStream(new File("src/main/resources/static/saves/" + fileName)),FromVizData.class);
            return tmp;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    // public static void main(String[] args) {
    //     int [][] tab = {{1,2,3},{2,3,1},{3,1,2}};
    //     FromVizData tmp = new FromVizData(0,tab,1,12,0.2d,0.3d,0.1d,0,false);
    //     save(tmp);
    //     FromVizData tmp2 = read("tmp");
    //     System.out.println(tmp.equals(tmp2));
    // }
}
