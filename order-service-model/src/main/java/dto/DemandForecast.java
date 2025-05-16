package dto;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "DemandForecasts")
public class DemandForecast {

    @Id
    @Column(name = "id")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(name = "productId")
    private UUID productId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "predictedDemand")
    private double predictedDemand;

    public DemandForecast() {
    }

    public DemandForecast(UUID id, UUID productId, LocalDate date, double predictedDemand) {
        this.id = id;
        this.productId = productId;
        this.date = date;
        this.predictedDemand = predictedDemand;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getPredictedDemand() {
        return predictedDemand;
    }

    public void setPredictedDemand(double predictedDemand) {
        this.predictedDemand = predictedDemand;
    }
}
