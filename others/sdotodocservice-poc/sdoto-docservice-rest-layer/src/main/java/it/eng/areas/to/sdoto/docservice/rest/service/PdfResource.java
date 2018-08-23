/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.rest.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.eng.areas.to.sdoto.docservice.delegate.DocumentServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.model.BookingDocumentDTO;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentState;

/**
 * @author Bifulco Luigi
 *
 */
@Component
@Path("repository")
@Api(value = "/documentService", protocols = "http,https", description = "This service provides all operations API to download PDF from repository")
public class PdfResource {

	@Autowired
	private DocumentServiceDelegate delegate;

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/pdf")
	@Path("getPdf/{docId}")
	@ApiOperation(httpMethod = "GET", notes = "You can know id using the DocumentServiceResource", value = "The getPdf returns a PDF document by its reference id", response = InputStream.class)
	public StreamingOutput getPdf(@PathParam(value = "docId") String docId) {
		BookingDocumentDTO dto = delegate.getDocumentById(docId);
		if (dto.getState() == BookingDocumentState.CLOSED) {
			throw new WebApplicationException(403);
		}
		byte[] data = dto.getBynaryData();
		return new PdfResponse(data);
	}
}
