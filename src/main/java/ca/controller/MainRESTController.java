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
import ca.controller.data.FromVizData;
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

    @RequestMapping(value = "glparam", consumes = MediaType.APPLICATION_JSON_VALUE)

    public int[][][] glParam(@RequestBody FromVizData data) {
        int [][][] tmp = ca.gl(data.getProb_a(), data.getN(), data.getIter(), data.getSeed());
        stats.setFileName("glparamStats.txt");
        stats.genereteHaderOfStats(data);
        stats.generateStats(tmp);
        return tmp;
    }

    @RequestMapping(value = "gl", consumes = MediaType.APPLICATION_JSON_VALUE)
    
    public int[][][] gl(@RequestBody FromVizData data) {
        int[][][] tmp = ca.gl(data.getTab(), data.getIter());
        stats.setFileName("glStats.txt");
        stats.genereteHaderOfStats(data);
        stats.generateStats(tmp);
        return tmp;
    }
    
    @RequestMapping(value = "klparam", consumes = MediaType.APPLICATION_JSON_VALUE)
    public int[][][] klParam(@RequestBody FromVizData data) {
        
        int [][][] tmp = ca.kl(data.getProb_a(), data.getN(), data.getIter(), data.getSeed());
        stats.setFileName("klparamStats.txt");
        stats.genereteHaderOfStats(data);
        stats.generateStats(tmp);
        return tmp;
    }

    @RequestMapping(value = "kl", consumes = MediaType.APPLICATION_JSON_VALUE)
    
    public int[][][] kl(@RequestBody FromVizData data) {
        int [][][] tmp = ca.kl(data.getTab(), data.getIter());
        stats.setFileName("klStats.txt");
        stats.genereteHaderOfStats(data);
        stats.generateStats(tmp);
        return tmp;
    }
    @RequestMapping(value = "multirunKl", consumes = MediaType.APPLICATION_JSON_VALUE)
    
    public boolean multirun_kl(@RequestBody FromVizData data) {
        int[][][][] tmp = new int[data.getMultirun_runs()][data.getIter()][data.getN()][data.getN()];
        for (int i = 0; i < data.getMultirun_runs(); i++) {
            tmp[i] = ca.kl(data.getProb_a(), data.getN(), data.getIter(), data.getSeed());
        }

        // stats.setFileName("klStats.txt");
        // stats.genereteHaderOfStats(data);
        // stats.generateStats(tmp);
        return true;
    }
    
    @RequestMapping(value = "multirunGl", consumes = MediaType.APPLICATION_JSON_VALUE)
    
    public boolean multirun_gl(@RequestBody FromVizData data) {
        int[][][][] tmp = new int[data.getMultirun_runs()][data.getIter()][data.getN()][data.getN()];
        for (int i = 0; i < data.getMultirun_runs(); i++) {
            tmp[i] = ca.gl(data.getProb_a(), data.getN(), data.getIter(), data.getSeed());
        }

        // stats.setFileName("klStats.txt");
        // stats.genereteHaderOfStats(data);
        // stats.generateStats(tmp);
        return true;
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