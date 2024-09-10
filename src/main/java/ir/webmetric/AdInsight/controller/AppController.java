package ir.webmetric.AdInsight.controller;


import ir.webmetric.AdInsight.model.Click;
import ir.webmetric.AdInsight.model.Impression;
import ir.webmetric.AdInsight.service.FileReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/data")
public class AppController {

    @Autowired
    private FileReaderService fileReaderService;

    @GetMapping("/impressions")
    public List<Impression> getImpressions() throws IOException {
        return fileReaderService.readImpressions("src/main/resources/impressions.json");
    }

    @GetMapping("/clicks")
    public List<Click> getClicks() throws IOException {
        return fileReaderService.readClicks("src/main/resources/clicks.json");
    }

    @GetMapping("/impressions")
    public List<Map<String, Object>> getImpressionsClicksSummary() throws IOException {
        return fileReaderService.getImpressionsClicksSummary("src/main/resources/impressions.json", "src/main/resources/clicks.json");
    }

    @GetMapping("/advertisers")
    public List<Map<String, Object>> getRecommendedAdvertisers() throws IOException {
        return fileReaderService.getRecommendedAdvertisers("src/main/resources/impressions.json");
    }
}

