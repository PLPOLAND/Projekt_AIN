package ca.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ca.algorithms.CellularAutomata;
import ca.controller.data.fromVizData;
import ca.statistics.Stats;
import png.PngImage;

/**
 * RestController
 */
@RestController
@RequestMapping("/api")
public class MainRESTController {
    CellularAutomata ca = new CellularAutomata();

    @Autowired
    Stats stats;//do obsługi statystyk

    @RequestMapping("/glparam")
    public int[][][] glParam(@RequestParam("seed") long seed, @RequestParam("N") int n, @RequestParam("iter") int iter,
            @RequestParam("prob") double prob) {
        int [][][] tmp = ca.gl(prob, n, iter, seed);
        stats.generateStats(tmp);
        return tmp;
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