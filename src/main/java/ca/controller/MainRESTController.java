package ca.controller;

import java.io.IOException;
import java.util.Random;

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
    Random random = new Random();

    @Autowired
    Stats stats;//do obsługi statystyk


    @RequestMapping(value = "glparam", consumes = MediaType.APPLICATION_JSON_VALUE)

    public int[][][] glParam(@RequestBody FromVizData data) {
        int [][][] tmp = ca.gl(data.getProb_a(), data.getN(), data.getIter(), data.getSeed());
        stats.setFileName("results.txt");
        stats.generateStats(tmp,true,data);
        return tmp;
    }

    @RequestMapping(value = "gl", consumes = MediaType.APPLICATION_JSON_VALUE)
    
    public int[][][] gl(@RequestBody FromVizData data) {
        int[][][] tmp = ca.gl(data.getTab(), data.getIter());
        stats.setFileName("results.txt");
        stats.generateStats(tmp,true, data);
        return tmp;
    }
    
    @RequestMapping(value = "klparam", consumes = MediaType.APPLICATION_JSON_VALUE)
    public int[][][] klParam(@RequestBody FromVizData data) {
        
        int [][][] tmp = ca.kl(data.getProb_a(), data.getN(), data.getIter(), data.getSeed());
        stats.setFileName("results.txt");
        stats.generateStats(tmp, true, data);
        return tmp;
    }

    @RequestMapping(value = "kl", consumes = MediaType.APPLICATION_JSON_VALUE)
    
    public int[][][] kl(@RequestBody FromVizData data) {
        int [][][] tmp = ca.kl(data.getTab(), data.getIter());
        stats.setFileName("results.txt");
        stats.generateStats(tmp, true, data);
        return tmp;
    }
    @RequestMapping(value = "multirunKl", consumes = MediaType.APPLICATION_JSON_VALUE)
    
    public boolean multirun_kl(@RequestBody FromVizData dane) {
        int[][][][] tmp = new int[dane.getMultirun_runs()][dane.getIter()][dane.getN()][dane.getN()];
        long[] seeds= new long[dane.getMultirun_runs()];
        tmp[0] = ca.kl(dane.getProb_a(), dane.getN(), dane.getIter(), dane.getSeed());
        seeds[0] = dane.getSeed();
        for (int i = 1; i < dane.getMultirun_runs(); i++) {
            seeds[i] = random.nextLong();
            tmp[i] = ca.kl(dane.getProb_a(), dane.getN(), dane.getIter(), seeds[i]);
        }

        stats.setFileName("results.txt");
        stats.generateStats(tmp,true, seeds, dane);
        return true;
    }
    
    @RequestMapping(value = "multirunGl", consumes = MediaType.APPLICATION_JSON_VALUE)
    
    public boolean multirun_gl(@RequestBody FromVizData dane) {
        int[][][][] tmp = new int[dane.getMultirun_runs()][dane.getIter()][dane.getN()][dane.getN()];
        long[] seeds = new long[dane.getMultirun_runs()];
        tmp[0] = ca.gl(dane.getProb_a(), dane.getN(), dane.getIter(), dane.getSeed());
        seeds[0] = dane.getSeed();
        for (int i = 1; i < dane.getMultirun_runs(); i++) {
            seeds[i] = random.nextLong();
            tmp[i] = ca.gl(dane.getProb_a(), dane.getN(), dane.getIter(), seeds[i]);
        }

        stats.setFileName("results.txt");
        stats.generateStats(tmp, true, seeds, dane);
        return true;
    }
    

    @RequestMapping(value = "klglparam", consumes = MediaType.APPLICATION_JSON_VALUE)
    public int[][][] klglParam(@RequestBody FromVizData data) {

        int[][][] tmp = ca.klAndGl(data.getProb_a(), data.getN(), data.getIter(), data.getSeed(), data.getProb_a_kl(), data.getDebug());
        stats.setFileName("results.txt");
        stats.generateStats(tmp, false, data);
        return tmp;
    }

    @RequestMapping(value = "klgl", consumes = MediaType.APPLICATION_JSON_VALUE)

    public int[][][] klgl(@RequestBody FromVizData data) {
        int[][][] tmp = ca.klAndGl(data.getTab(), data.getIter(), data.getDebug(), data.getProb_a_kl());
        stats.setFileName("results.txt");
        stats.generateStats(tmp, false, data);
        return tmp;
    }

    @RequestMapping(value = "multirunklgl", consumes = MediaType.APPLICATION_JSON_VALUE)

    public boolean multirun_klgl(@RequestBody FromVizData dane) {
        int[][][][] tmp = new int[dane.getMultirun_runs()][dane.getIter()][dane.getN()][dane.getN()];
        long[] seeds = new long[dane.getMultirun_runs()];
        tmp[0] = ca.klAndGl(dane.getProb_a(), dane.getN(), dane.getIter(), dane.getSeed(), dane.getProb_a_kl(), dane.getDebug());
        seeds[0] = dane.getSeed();
        for (int i = 1; i < dane.getMultirun_runs(); i++) {
            seeds[i] = random.nextLong();
            tmp[i] = ca.klAndGl(dane.getProb_a(), dane.getN(), dane.getIter(), seeds[i], dane.getProb_a_kl(), dane.getDebug());
        }

        stats.setFileName("results.txt");
        stats.generateStats(tmp, false, seeds, dane);
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