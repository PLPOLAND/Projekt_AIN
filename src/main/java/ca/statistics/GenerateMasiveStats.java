package ca.statistics;

import java.util.Random;

import ca.algorithms.CellularAutomata;
import ca.controller.data.FromVizData;

public class GenerateMasiveStats {
    final static int multiruns = 4;
    final static int iterations = 1000;
    final static int N = 100;

    public static void main(String[] args) {
        Stats stats = new Stats();
        Random rand = new Random(System.currentTimeMillis());
        CellularAutomata ca = new CellularAutomata();

        double prob_a = 0.1;
        int seed = rand.nextInt();
        double prob_a_kl = 0.3;
        double prob_GL_tol = 0.3;
        double prob_KL_tol = 0.3;
        double expansion = 0.3;

        double[] prob_a_vals = { 0.1, 0.2, 0.3 };
        double[] prob_a_kl_vals = {  0.4, 0.50,  0.60 };
        double[] prob_GL_tol_vals = { 0.4, 0.50, 0.60 };
        double[] prob_KL_tol_vals = { 0.4, 0.50, 0.60 };
        double[] expansion_vals = { 0.4, 0.50, 0.60 };
        for (int z = 0; z < prob_a_vals.length; z++) {
            prob_a = prob_a_vals[z];
            for (int j = 0; j < prob_a_kl_vals.length; j++) {
                prob_a_kl = prob_a_kl_vals[j];
                for (int j3 = 0; j3 < expansion_vals.length; j3++) {
                    expansion = expansion_vals[j3];
                    for (int j2 = 0; j2 < prob_GL_tol_vals.length; j2++) {
                        prob_GL_tol = prob_GL_tol_vals[j2];
                        for (int k = 0; k < prob_KL_tol_vals.length; k++) {
                            prob_KL_tol = prob_KL_tol_vals[k];
                                System.out.println("prob_a" + prob_a + " kl" + prob_a_kl + " gltol" + prob_GL_tol
                                        + " kltol" + prob_KL_tol + " exp" + expansion);
                                int[][][][] tmp = new int[multiruns][iterations][N][N];
                                long[] seeds = new long[multiruns];
                                tmp[0] = ca.klAndGlv2(prob_a, N, iterations, seed, prob_a_kl, prob_GL_tol, prob_KL_tol,
                                        false, expansion);
                                seeds[0] = seed;
                                System.out.println("multirun 1: done");

                                for (int i = 1; i < multiruns; i++) {
                                    seeds[i] = rand.nextLong();
                                    System.out.println("multirun " + (i + 1) + ": inProgress");
                                    tmp[i] = ca.klAndGlv2(prob_a, N, iterations, seed, prob_a_kl, prob_GL_tol, prob_KL_tol, false, expansion);
                                    System.out.println("multirun " + (i + 1) + ": done");
                                }
                                FromVizData fData = new FromVizData(iterations, null, seed, N, prob_a, 1 - prob_a_kl, prob_a_kl, expansion, prob_GL_tol, prob_KL_tol, multiruns, false, null);
                                stats.setFileName("results.txt");
                                stats.generateStats(tmp, false, seeds, fData, "prob_a" + prob_a + "/kl" + prob_a_kl+ "/gltol" + prob_GL_tol + "/kltol" + prob_KL_tol + "/exp" + expansion + "/stdV.txt");
                        }
                    }
                }
                
            }
        }
        
    }
}
