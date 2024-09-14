package ir.webmetric.AdInsight.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import ir.webmetric.AdInsight.model.Click;
import ir.webmetric.AdInsight.model.Impression;
import ir.webmetric.AdInsight.repository.ImpressionDao;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FileReaderService {

    public ImpressionDao impressionDao;

    public List<Impression> readEventFiles(String impressionFile, String clickFile) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        try {
            List<Impression> impressions = mapper.readValue(new File(impressionFile), new TypeReference<>() {
            });
            List<Click> clicks = mapper.readValue(new File(clickFile), new TypeReference<>() {
            });
            Map<String, List<Click>> groupedByImpressionId = clicks.stream()
                    .collect(Collectors.groupingBy(Click::getImpressionId));

            for (Impression impression : impressions) {
                List<Click> clickList = groupedByImpressionId.get(impression.getId().toString());
                impression.setClicks(clickList == null ? new ArrayList<>() : clickList);
            }
            return impressionDao.saveAll(impressions);
        } catch (IOException e) {
            log.error("Reading files cause exception: {}", e.getMessage());
            return null;
        }
    }
}

