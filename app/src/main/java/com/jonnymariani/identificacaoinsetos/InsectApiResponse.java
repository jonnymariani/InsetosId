package com.jonnymariani.identificacaoinsetos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;

public class InsectApiResponse implements Serializable {
    @JsonProperty("access_token")
    public String getAccess_token() {
        return this.access_token; }
    public void setAccess_token(String access_token) {
        this.access_token = access_token; }
    String access_token;
    @JsonProperty("model_version")
    public String getModel_version() {
        return this.model_version; }
    public void setModel_version(String model_version) {
        this.model_version = model_version; }
    String model_version;
    @JsonProperty("custom_id")
    public Object getCustom_id() {
        return this.custom_id; }
    public void setCustom_id(Object custom_id) {
        this.custom_id = custom_id; }
    Object custom_id;
    @JsonProperty("input")
    public Input getInput() {
        return this.input; }
    public void setInput(Input input) {
        this.input = input; }
    Input input;
    @JsonProperty("result")
    public Result getResult() {
        return this.result; }
    public void setResult(Result result) {
        this.result = result; }
    Result result;
    @JsonProperty("status")
    public String getStatus() {
        return this.status; }
    public void setStatus(String status) {
        this.status = status; }
    String status;
    @JsonProperty("sla_compliant_client")
    public boolean getSla_compliant_client() {
        return this.sla_compliant_client; }
    public void setSla_compliant_client(boolean sla_compliant_client) {
        this.sla_compliant_client = sla_compliant_client; }
    boolean sla_compliant_client;
    @JsonProperty("sla_compliant_system")
    public boolean getSla_compliant_system() {
        return this.sla_compliant_system; }
    public void setSla_compliant_system(boolean sla_compliant_system) {
        this.sla_compliant_system = sla_compliant_system; }
    boolean sla_compliant_system;
    @JsonProperty("created")
    public double getCreated() {
        return this.created; }
    public void setCreated(double created) {
        this.created = created; }
    double created;
    @JsonProperty("completed")
    public double getCompleted() {
        return this.completed; }
    public void setCompleted(double completed) {
        this.completed = completed; }
    double completed;
}

class Classification{
    @JsonProperty("suggestions")
    public ArrayList<Suggestion> getSuggestions() {
        return this.suggestions; }
    public void setSuggestions(ArrayList<Suggestion> suggestions) {
        this.suggestions = suggestions; }
    ArrayList<Suggestion> suggestions;
}

class Input{
    @JsonProperty("images")
    public ArrayList<String> getImages() {
        return this.images; }
    public void setImages(ArrayList<String> images) {
        this.images = images; }
    ArrayList<String> images;
    @JsonProperty("similar_images")
    public boolean getSimilar_images() {
        return this.similar_images; }
    public void setSimilar_images(boolean similar_images) {
        this.similar_images = similar_images; }
    boolean similar_images;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @JsonProperty("datetime")
    String datetime;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("longitude")
    String longitude;

    @JsonProperty("latitude")
    String latitude;
}

class Details{
    @JsonProperty("language")
    public String getLanguage() {
        return this.language; }
    public void setLanguage(String language) {
        this.language = language; }
    String language;
    @JsonProperty("entity_id")
    public String getEntity_id() {
        return this.entity_id; }
    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id; }
    String entity_id;
}

class Result{
    @JsonProperty("classification")
    public Classification getClassification() {
        return this.classification; }
    public void setClassification(Classification classification) {
        this.classification = classification; }
    Classification classification;
}



class SimilarImage{
    @JsonProperty("id")
    public String getId() {
        return this.id; }
    public void setId(String id) {
        this.id = id; }
    String id;
    @JsonProperty("url")
    public String getUrl() {
        return this.url; }
    public void setUrl(String url) {
        this.url = url; }
    String url;
    @JsonProperty("license_name")
    public String getLicense_name() {
        return this.license_name; }
    public void setLicense_name(String license_name) {
        this.license_name = license_name; }
    String license_name;
    @JsonProperty("license_url")
    public String getLicense_url() {
        return this.license_url; }
    public void setLicense_url(String license_url) {
        this.license_url = license_url; }
    String license_url;
    @JsonProperty("citation")
    public String getCitation() {
        return this.citation; }
    public void setCitation(String citation) {
        this.citation = citation; }
    String citation;
    @JsonProperty("similarity")
    public double getSimilarity() {
        return this.similarity; }
    public void setSimilarity(double similarity) {
        this.similarity = similarity; }
    double similarity;
    @JsonProperty("url_small")
    public String getUrl_small() {
        return this.url_small; }
    public void setUrl_small(String url_small) {
        this.url_small = url_small; }
    String url_small;
}

class Suggestion{
    @JsonProperty("id")
    public String getId() {
        return this.id; }
    public void setId(String id) {
        this.id = id; }
    String id;
    @JsonProperty("name")
    public String getName() {
        return this.name; }
    public void setName(String name) {
        this.name = name; }
    String name;
    @JsonProperty("probability")
    public double getProbability() {
        return this.probability; }
    public void setProbability(double probability) {
        this.probability = probability; }
    double probability;
    @JsonProperty("similar_images")
    public ArrayList<SimilarImage> getSimilar_images() {
        return this.similar_images; }
    public void setSimilar_images(ArrayList<SimilarImage> similar_images) {
        this.similar_images = similar_images; }
    ArrayList<SimilarImage> similar_images;

    @JsonProperty("details")
    public Details getDetails() {
        return this.details; }
    public void setDetails(Details details) {
        this.details = details; }
    Details details;

}

