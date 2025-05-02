package umg.principal.api.dto.province;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "provinces")
public class Province implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String iso;
    private String name;
    private String province;
    private Double lat;
    private Double lon;

    public Province() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Province{" +
                "id=" + id +
                ", iso='" + iso + '\'' +
                ", name='" + name + '\'' +
                ", province='" + province + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}