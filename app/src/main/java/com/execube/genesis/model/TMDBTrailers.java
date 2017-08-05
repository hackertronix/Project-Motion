package com.execube.genesis.model;

import java.util.List;

/**
 * Created by hackertronix on 05/08/17.
 */

public class TMDBTrailers {



    private int id;
    private List<Trailer> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }


}
