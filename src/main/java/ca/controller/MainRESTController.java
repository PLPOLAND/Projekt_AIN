package ca.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import ca.CASymulation;
import ca.algorithms.CellularAutomata;

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
    public int[][][] glParam(@RequestParam("seed") long seed, @RequestParam("N") int n, @RequestParam("iter") int iter, @RequestParam("prob") double prob) {
        return ca.gl(prob, n, iter, seed);
    }
    @RequestMapping("/gl")
    public int[][][] gl(@RequestParam("iter") int iter, @RequestParam("tab") int[][] tab) {
        return ca.gl(tab,iter);
    }

}