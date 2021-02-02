package ca.controller.data;

import java.util.Objects;

public class fromVizData {
    int iter;
    int[][] tab;

    public fromVizData() {
    }

    public fromVizData(int iter, int[][] tab) {
        this.iter = iter;
        this.tab = tab;
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

    public fromVizData iter(int iter) {
        setIter(iter);
        return this;
    }

    public fromVizData tab(int[][] tab) {
        setTab(tab);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof fromVizData)) {
            return false;
        }
        fromVizData glData = (fromVizData) o;
        return iter == glData.iter && Objects.equals(tab, glData.tab);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iter, tab);
    }

    @Override
    public String toString() {
        return "{" +
            " iter='" + getIter() + "'" +
            ", tab='" + getTab() + "'" +
            "}";
    }

}
