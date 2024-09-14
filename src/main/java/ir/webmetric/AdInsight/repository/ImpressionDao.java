package ir.webmetric.AdInsight.repository;

import ir.webmetric.AdInsight.model.Impression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImpressionDao extends JpaRepository<Impression, Integer> {

}
