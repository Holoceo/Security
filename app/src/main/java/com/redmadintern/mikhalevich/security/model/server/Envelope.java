package com.redmadintern.mikhalevich.security.model.server;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Envelope<T> {

    @Expose
    private Meta meta;
    @Expose
    private List<T> data = new ArrayList<T>();
    @Expose
    private Pagination pagination;

    /**
     *
     * @return
     * The meta
     */
    public Meta getMeta() {
        return meta;
    }

    /**
     *
     * @return
     * The data
     */
    public List<T> getData() {
        return data;
    }

    /**
     *
     * @return
     * The pagination
     */
    public Pagination getPagination() {
        return pagination;
    }

}