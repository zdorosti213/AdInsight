package ir.webmetric.AdInsight.model;

import lombok.Data;

@Data
public class Impression {
    private String id;
    private int appId;
    private String countryCode;
    private int advertiserId;
}