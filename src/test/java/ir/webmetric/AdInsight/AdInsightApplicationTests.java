package ir.webmetric.AdInsight;

import ir.webmetric.AdInsight.Dto.Metrics;
import ir.webmetric.AdInsight.Dto.RecommendedAdvertisers;
import ir.webmetric.AdInsight.model.Click;
import ir.webmetric.AdInsight.model.Impression;
import ir.webmetric.AdInsight.repository.ImpressionDao;
import ir.webmetric.AdInsight.service.AdInsightService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AdInsightApplicationTests {

    @Autowired
    AdInsightService adInsightService;

    @MockBean(name = "impressionDao")
    ImpressionDao impressionDao;

    @Test
    void aggregateMetricsTest() {
        List<Metrics> result = new ArrayList<>();
        Metrics metrics = new Metrics();
        metrics.setAppId(1);
        metrics.setCountryCode("IR");
        metrics.setImpressions(1);
        metrics.setClicks(2);
        metrics.setRevenue(5.9);
        result.add(metrics);

        UUID uuid1 = UUID.randomUUID();
        Impression impression = new Impression();
        impression.setId(uuid1);
        impression.setAppId(1);
        impression.setCountryCode("IR");
        impression.setAdvertiserId(100);
        impression.getClicks().add(new Click(2.5, uuid1.toString()));
        impression.getClicks().add(new Click(3.4, uuid1.toString()));

        Mockito.when(impressionDao.findAll()).thenReturn(List.of(impression));

        assertEquals(result, adInsightService.aggregateMetrics());
    }

    @Test
    void getRecommendedAdvertisersTest() {
        List<RecommendedAdvertisers> result = List.of(
                new RecommendedAdvertisers(1, "IR", List.of(100, 101, 102, 103, 104)));

        List<Impression> data = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            UUID uuid1 = UUID.randomUUID();
            Impression impression = new Impression();
            impression.setId(uuid1);
            impression.setAppId(1);
            impression.setCountryCode("IR");
            impression.setAdvertiserId(100 + i);
            impression.getClicks().add(new Click(2.5 - (0.1 * i), uuid1.toString()));
            data.add(impression);
        }

        Mockito.when(impressionDao.findAll()).thenReturn(data);

        assertEquals(result, adInsightService.getRecommendedAdvertisers());
    }
}
