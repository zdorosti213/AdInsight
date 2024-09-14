package ir.webmetric.AdInsight.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Impression {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private Integer appId;
    private String countryCode;
    private Integer advertiserId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Click> clicks = new ArrayList<>();
}