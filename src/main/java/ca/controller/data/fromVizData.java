package ca.controller.data;

import java.util.Objects;

public class FromVizData {
    int iter;
    int[][] tab;
    long seed;
    int n;
    double prob_a;
    double prob_a_gl;
    double prob_a_kl;
    int multirun_runs;
    boolean debug;

    public FromVizData() {
    }

    public FromVizData(int iter, int[][] tab, long seed, int n, double prob_a, double prob_a_gl, double prob_a_kl, int multirun_runs, boolean debug) {
        this.iter = iter;
        this.tab = tab;
        this.seed = seed;
        this.n = n;
        this.prob_a = prob_a;
        this.prob_a_gl = prob_a_gl;
        this.prob_a_kl = prob_a_kl;
        this.multirun_runs = multirun_runs;
        this.debug = debug;
    }

    public int getIter() {
        return this.iter;
    }

    public void setIter(int iter) {
        this.iter = iter;
    }

    public int[][] getTab() {
        return this.tab;
    }

    public void setTab(int[][] tab) {
        this.tab = tab;
    }

    public long getSeed() {
        return this.seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public int getN() {
        return this.n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public double getProb_a() {
        return this.prob_a;
    }

    public void setProb_a(double prob_a) {
        this.prob_a = prob_a;
    }

    public double getProb_a_gl() {
        return this.prob_a_gl;
    }

    public void setProb_a_gl(double prob_a_gl) {
        this.prob_a_gl = prob_a_gl;
    }

    public double getProb_a_kl() {
        return this.prob_a_kl;
    }

    public void setProb_a_kl(double prob_a_kl) {
        this.prob_a_kl = prob_a_kl;
    }

    public int getMultirun_runs() {
        return this.multirun_runs;
    }

    public void setMultirun_runs(int multirun_runs) {
        this.multirun_runs = multirun_runs;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public boolean getDebug() {
        return this.debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public FromVizData iter(int iter) {
        setIter(iter);
        return this;
    }

    public FromVizData tab(int[][] tab) {
        setTab(tab);
        return this;
    }

    public FromVizData seed(long seed) {
        setSeed(seed);
        return this;
    }

    public FromVizData n(int n) {
        setN(n);
        return this;
    }

    public FromVizData prob_a(double prob_a) {
        setProb_a(prob_a);
        return this;
    }

    public FromVizData prob_a_gl(double prob_a_gl) {
        setProb_a_gl(prob_a_gl);
        return this;
    }

    public FromVizData prob_a_kl(double prob_a_kl) {
        setProb_a_kl(prob_a_kl);
        return this;
    }

    public FromVizData multirun_runs(int multirun_runs) {
        setMultirun_runs(multirun_runs);
        return this;
    }

    public FromVizData debug(boolean debug) {
        setDebug(debug);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof FromVizData)) {
            return false;
        }
        FromVizData fromVizData = (FromVizData) o;
        return iter == fromVizData.iter && Objects.equals(tab, fromVizData.tab) && seed == fromVizData.seed && n == fromVizData.n && prob_a == fromVizData.prob_a && prob_a_gl == fromVizData.prob_a_gl && prob_a_kl == fromVizData.prob_a_kl && multirun_runs == fromVizData.multirun_runs && debug == fromVizData.debug;
    }

    @Override
    public int hashCode() {
        return Objects.hash(iter, tab, seed, n, prob_a, prob_a_gl, prob_a_kl, multirun_runs, debug);
    }

    @Override
    public String toString() {
        return "{" +
            " iter='" + getIter() + "'" +
            ", tab='" + getTab() + "'" +
            ", seed='" + getSeed() + "'" +
            ", n='" + getN() + "'" +
            ", prob_a='" + getProb_a() + "'" +
            ", prob_a_gl='" + getProb_a_gl() + "'" +
            ", prob_a_kl='" + getProb_a_kl() + "'" +
            ", multirun_runs='" + getMultirun_runs() + "'" +
            ", debug='" + isDebug() + "'" +
            "}";
    }

}
