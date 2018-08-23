package it.eng.areas.ems.sdodaeservices.rest.service.portal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.swagger.annotations.ApiParam;

@Component
@Path("/portal/excelService")
public class ExcelServiceREST {

	private Logger logger = LoggerFactory.getLogger(ExcelServiceREST.class);

	@Context
	private SecurityContext secContext;

	private Response generateExcel(List<List<String>> values, String name) throws IOException {
		// Create blank workbook
		try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			// Create a blank sheet
			XSSFSheet spreadsheet = workbook.createSheet(name);
			// Create row object
			XSSFRow row;
			// Iterate over data and write to sheet
			int rowid = 0;
			int columns = 0;

			for (List<String> jsonRow : values) {
				row = spreadsheet.createRow(rowid++);
				int cellid = 0;
				for (String obj : jsonRow) {
					Cell cell = row.createCell(cellid++);

					if (StringUtils.isNumeric(obj)) {
						cell.setCellType(CellType.NUMERIC);
					}

					cell.setCellValue(obj);
				}
				columns = cellid;
			}
			// effettuo l'autosize delle colonne
			for (int i = 0; i < columns; i++) {
				spreadsheet.autoSizeColumn(i);
			}
			// Write the workbook in file system
			workbook.write(out);

			return Response.ok(out.toByteArray()).build();
		}
	}

	@POST
	@Path("/json2Excel")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/vnd.ms-excel")
	public Response json2Excel(@Context HttpServletRequest httpServletRequest,
			@Context HttpServletResponse httpServletResponse,
			@ApiParam(value = "Filtro di ricerca") List<List<String>> json) {
		try {
			// logger.info(json.toString());
			return generateExcel(json, "DAE");
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING json2Excel", e);
			return Response.serverError().build();
		}
	}

}
