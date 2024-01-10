package br.com.rafaelvieira.shopbeer.services.report;

import br.com.rafaelvieira.shopbeer.domain.dto.ReportingPeriodDTO;
import java.io.InputStream;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final DataSource dataSource;

    public ReportService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public byte[] generateIssuedSalesReport(ReportingPeriodDTO reportingPeriodDTO) throws Exception {
        Date startDate = Date.from(LocalDateTime.of(reportingPeriodDTO.getStartDate(), LocalTime.of(0, 0, 0))
                .atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDateTime.of(reportingPeriodDTO.getEndDate(), LocalTime.of(23, 59, 59))
                .atZone(ZoneId.systemDefault()).toInstant());

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("format", "pdf");
        parametros.put("start_date", startDate);
        parametros.put("end_date", endDate);

        InputStream inputStream = this.getClass()
                .getResourceAsStream("/reports/relatorio_vendas_emitidas.jasper");

        Connection con = this.dataSource.getConnection();

        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, con);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } finally {
            con.close();
        }
    }
}
