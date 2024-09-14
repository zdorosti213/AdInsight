package ir.webmetric.AdInsight.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecommendedAdvertisers {
    private Integer appId;
    private String countryCode;
    private List<Integer> recommendedAdvertiserIds;
}
