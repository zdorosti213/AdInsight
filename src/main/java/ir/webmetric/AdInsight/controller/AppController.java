package ir.webmetric.AdInsight.controller;

import ir.webmetric.AdInsight.Dto.Metrics;
import ir.webmetric.AdInsight.Dto.RecommendedAdvertisers;
import ir.webmetric.AdInsight.model.Impression;
import ir.webmetric.AdInsight.service.AdInsightService;
import ir.webmetric.AdInsight.service.FileReaderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AppController {

    private FileReaderService fileReaderService;
    private AdInsightService adInsightService;

    @PostMapping("/impressions")
    public List<Impression> getImpressions(
            @RequestParam(name = "impression_file_name", required = false,
                    defaultValue = "src/main/resources/impressions.json") String impressionFileName,
            @RequestParam(name = "click_file_name", required = false,
                    defaultValue = "src/main/resources/clicks.json") String clickFileName) {
        return fileReaderService.readEventFiles(impressionFileName, clickFileName);
    }

    @GetMapping("/advertisers")
    public List<RecommendedAdvertisers> getRecommendedAdvertisers() {
        return adInsightService.getRecommendedAdvertisers();
    }

    @GetMapping("/metrics")
    public List<Metrics> aggregateMetrics() {
        return adInsightService.aggregateMetrics();
    }
}

