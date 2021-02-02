package ca.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import ca.CASymulation;
import ca.algorithms.CellularAutomata;
import ca.controller.data.fromVizData;
import pngReader.Png;
import pngReader.PngImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RestController
 */
@RestController
@RequestMapping("/api")
public class MainRESTController {
    CellularAutomata ca = new CellularAutomata();

    @RequestMapping("/glparam")
    public int[][][] glParam(@RequestParam("seed") long seed, @RequestParam("N") int n, @RequestParam("iter") int iter,
            @RequestParam("prob") double prob) {
        return ca.gl(prob, n, iter, seed);
    }

    @RequestMapping(value = "gl", consumes = MediaType.APPLICATION_JSON_VALUE)
    
    public int[][][] gl(@RequestBody fromVizData data) {
        return ca.gl(data.getTab(),data.getIter());
    }
    @RequestMapping("/klparam")
    public int[][][] klParam(@RequestParam("seed") long seed, @RequestParam("N") int n, @RequestParam("iter") int iter,
            @RequestParam("prob") double prob) {
        return ca.kl(prob, n, iter, seed);
    }

    @RequestMapping(value = "kl", consumes = MediaType.APPLICATION_JSON_VALUE)
    
    public int[][][] kl(@RequestBody fromVizData data) {

        return ca.kl(data.getTab(), data.getIter());
    }
    
    @PostMapping(value = "png", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean png(@RequestBody String tab){
        ObjectMapper mapper = new ObjectMapper();
        int[][] tmp = null;
        try {
            tmp = mapper.readValue(tab, int[][].class);
            return PngImage.tabToImg(tmp);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}