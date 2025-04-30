package umg.principal.api.dto.report;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "reporte_covid")
public class Report implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String iso;
    private String province;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private LocalDate fecha; // Cambiado a LocalDate

    private int confirmed;
    private int deaths;
    private int recovered;

    // ... (resto del código: getters, setters, toString)
    public Report() {
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", iso='" + iso + '\'' +
                ", province='" + province + '\'' +
                ", fecha=" + fecha +
                ", confirmed=" + confirmed +
                ", deaths=" + deaths +
                ", recovered=" + recovered +
                ", name='" + name + '\'' +
                '}';
    }
}