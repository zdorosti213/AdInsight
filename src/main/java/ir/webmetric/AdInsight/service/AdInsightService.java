package ir.webmetric.AdInsight.service;

import ir.webmetric.AdInsight.Dto.Metrics;
import ir.webmetric.AdInsight.Dto.RecommendedAdvertisers;
import ir.webmetric.AdInsight.model.Click;
import ir.webmetric.AdInsight.model.Impression;
import ir.webmetric.AdInsight.repository.ImpressionDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdInsightService {

    public ImpressionDao impressionDao;

    public List<Metrics> aggregateMetrics() {

        List<Metrics> result = new ArrayList<>();
        List<Impression> impressions = impressionDao.findAll();

        Map<String, List<Impression>> groupedByAppAndCountry = impressions.stream()
                .collect(Collectors.groupingBy(
                        imp -> imp.getAppId() + "-" + imp.getCountryCode()
                ));

        groupedByAppAndCountry.forEach((appAndCountryKey, groupedImpressions) -> {
            String[] keyItems = appAndCountryKey.split("-");
            Metrics metrics = new Metrics();
            metrics.setAppId(Integer.valueOf(keyItems[0]));
            metrics.setCountryCode(keyItems[1]);
            metrics.setImpressions(groupedImpressions.size());
            metrics.setClicks(groupedImpressions.stream()
                    .flatMap(impression -> impression.getClicks().stream())
                    .count());

            metrics.setRevenue(groupedImpressions.stream()
                    .flatMap(impression -> impression.getClicks().stream())
                    .mapToDouble(Click::getRevenue)
                    .sum());

            result.add(metrics);
        });
        return result;
    }

    public List<RecommendedAdvertisers> getRecommendedAdvertisers() {
        List<Impression> impressions = impressionDao.findAll();

        Map<String, List<Impression>> groupedByAppAndCountry = impressions.stream()
                .collect(Collectors.groupingBy(
                        imp -> imp.getAppId() + "-" + imp.getCountryCode()
                ));

        Map<String, List<Integer>> topAdvertisersByAppAndCountry = new HashMap<>();

        groupedByAppAndCountry.forEach((appAndCountryKey, groupedImpressions) -> {
            List<Integer> topAdvertisers = groupedImpressions.stream()
                    .collect(Collectors.groupingBy(
                            Impression::getAdvertiserId,
                            Collectors.summingDouble(imp -> imp.getClicks().stream()
                                    .mapToDouble(Click::getRevenue)
                                    .sum())
                    ))
                    .entrySet().stream()
                    .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                    .limit(5)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            topAdvertisersByAppAndCountry.put(appAndCountryKey, topAdvertisers);
        });

        List<RecommendedAdvertisers> result = new ArrayList<>();
        topAdvertisersByAppAndCountry.forEach((appAndCountryKey, advertiserIds) -> {
            String[] appCountry = appAndCountryKey.split("-");
            result.add(new RecommendedAdvertisers(Integer.valueOf(appCountry[0]), appCountry[1], advertiserIds));
        });

        return result;
    }
}

