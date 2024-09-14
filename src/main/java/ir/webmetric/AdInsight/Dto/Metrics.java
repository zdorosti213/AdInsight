package ir.webmetric.AdInsight.Dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Metrics {
    //@EqualsAndHashCode.Include
    private int appId;
    private String countryCode;

    private int impressions;
    private long clicks;
    private double revenue;
}
