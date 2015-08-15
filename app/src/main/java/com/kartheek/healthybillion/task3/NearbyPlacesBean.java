package com.kartheek.healthybillion.task3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jitendra on 21/4/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NearbyPlacesBean {
    private String[] html_attributions;
    private List<PlaceBean> results;
    private String status;

    public String[] getHtml_attributions() {
        return html_attributions;
    }

    public void setHtml_attributions(String[] html_attributions) {
        this.html_attributions = html_attributions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PlaceBean> getResults() {
        return results;
    }

    public void setResults(List<PlaceBean> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "SearchedPlacesBean{" +
                "html_attributions=" + Arrays.toString(html_attributions) +
                ", status='" + status + '\'' +
                ", results=" + results +
                '}';
    }
}
