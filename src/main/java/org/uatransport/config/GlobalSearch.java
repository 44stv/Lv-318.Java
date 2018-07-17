package org.uatransport.config;

public class GlobalSearch {
    private String globalSearch = "";
    private String city = "";

    public GlobalSearch(String globalSearch, String city) {
        this.globalSearch = globalSearch;
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGlobalSearch() {
        return globalSearch;
    }

    public void setGlobalSearch(String globalSearch) {
        this.globalSearch = globalSearch;
    }
}
