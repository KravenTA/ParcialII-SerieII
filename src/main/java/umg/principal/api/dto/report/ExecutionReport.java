package umg.principal.api.dto.report;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "executed_reports")
public class ExecutionReport implements Serializable {

    @EmbeddedId
    private ExecutionReportId id;

    public ExecutionReport() {
    }

    public ExecutionReport(LocalDate executionDate, String countryIso) {
        this.id = new ExecutionReportId(countryIso, executionDate);
    }

    public ExecutionReportId getId() {
        return id;
    }

    public void setId(ExecutionReportId id) {
        this.id = id;
    }

    public LocalDate getExecutionDate() {
        return id.getExecutionDate();
    }

    public String getCountryIso() {
        return id.getCountryIso();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExecutionReport that = (ExecutionReport) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ExecutionReport{" +
                "countryIso='" + getCountryIso() + '\'' +
                ", executionDate=" + getExecutionDate() +
                '}';
    }

    @Embeddable
    public static class ExecutionReportId implements Serializable {

        @Column(name = "country_iso")
        private String countryIso;

        @Column(name = "execution_date")
        private LocalDate executionDate;

        public ExecutionReportId() {
        }

        public ExecutionReportId(String countryIso, LocalDate executionDate) {
            this.countryIso = countryIso;
            this.executionDate = executionDate;
        }

        public String getCountryIso() {
            return countryIso;
        }

        public void setCountryIso(String countryIso) {
            this.countryIso = countryIso;
        }

        public LocalDate getExecutionDate() {
            return executionDate;
        }

        public void setExecutionDate(LocalDate executionDate) {
            this.executionDate = executionDate;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ExecutionReportId that = (ExecutionReportId) o;
            return Objects.equals(countryIso, that.countryIso) &&
                    Objects.equals(executionDate, that.executionDate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(countryIso, executionDate);
        }
    }
}