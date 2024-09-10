package ir.webmetric.AdInsight.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.webmetric.AdInsight.model.Click;
import ir.webmetric.AdInsight.model.Impression;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class FileReaderService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Impression> readImpressions(String filePath) throws IOException {
        return objectMapper.readValue(new File(filePath), new TypeReference<List<Impression>>() {
        });
    }

    public List<Click> readClicks(String filePath) throws IOException {
        return objectMapper.readValue(new File(filePath), new TypeReference<List<Click>>() {
        });
    }

    // 1. Generate Summary of Impressions, Clicks, and Revenue
    public List<Map<String, Object>> getImpressionsClicksSummary(String impressionsFilePath, String clicksFilePath) throws IOException {
        List<Impression> impressions = readImpressions(impressionsFilePath);
        List<Click> clicks = readClicks(clicksFilePath);

        // Grouping by appId and countryCode
        Map<String, Map<String, Object>> summary = new HashMap<>();
        Map<String, Integer> impressionCount = new HashMap<>();

        // Initialize the summary structure based on impressions
        for (Impression impression : impressions) {
            String key = impression.getAppId() + "_" + impression.getCountryCode();
            Map<String, Object> record = summary.computeIfAbsent(key, k -> {
                Map<String, Object> map = new HashMap<>();
                map.put("app_id", impression.getAppId());
                map.put("country_code", impression.getCountryCode());
                map.put("impressions", 0);  // initial value
                map.put("clicks", 0);  // initial value
                map.put("revenue", 0.0);  // initial value
                return map;
            });

            // Increment impressions
            record.put("impressions", (int) record.get("impressions") + 1);
            impressionCount.put(impression.getId(), 1);
        }

        // Match clicks and sum revenue
        for (Click click : clicks) {
            for (Impression impression : impressions) {
                if (click.getImpressionId().equals(impression.getId())) {
                    String key = impression.getAppId() + "_" + impression.getCountryCode();
                    Map<String, Object> record = summary.get(key);
                    record.put("clicks", (int) record.get("clicks") + 1);
                    record.put("revenue", (double) record.get("revenue") + click.getRevenue());
                }
            }
        }

        return new ArrayList<>(summary.values());
    }

    // 2. Generate Recommended Advertisers
    public List<Map<String, Object>> getRecommendedAdvertisers(String impressionsFilePath) throws IOException {
        List<Impression> impressions = readImpressions(impressionsFilePath);

        // Grouping by appId and countryCode, and collecting advertiserIds
        Map<String, Set<Integer>> advertisersByAppCountry = new HashMap<>();

        for (Impression impression : impressions) {
            String key = impression.getAppId() + "_" + impression.getCountryCode();
            Set<Integer> advertiserIds = advertisersByAppCountry.computeIfAbsent(key, k -> new HashSet<>());
            advertiserIds.add(impression.getAdvertiserId());
        }

        // Convert the map to a list of maps
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Set<Integer>> entry : advertisersByAppCountry.entrySet()) {
            String[] keyParts = entry.getKey().split("_");
            int appId = Integer.parseInt(keyParts[0]);
            String countryCode = keyParts[1];

            Map<String, Object> record = new HashMap<>();
            record.put("app_id", appId);
            record.put("country_code", countryCode);
            record.put("recommended_advertiser_ids", new ArrayList<>(entry.getValue()));

            result.add(record);
        }

        return result;
    }
}

