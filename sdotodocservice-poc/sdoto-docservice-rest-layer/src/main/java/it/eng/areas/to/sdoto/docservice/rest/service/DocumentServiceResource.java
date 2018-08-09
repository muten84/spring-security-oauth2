/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.rest.service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.internal.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.eng.area118.sdocommon.dao.DataAccessException;
import it.eng.area118.sdocommon.print.excpetions.PrintReportException;
import it.eng.areas.to.sdoto.docservice.delegate.DocumentServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.model.BookingDocumentDTO;
import it.eng.areas.to.sdoto.docservice.delegate.model.BookingDocumentFilter;
import it.eng.areas.to.sdoto.docservice.delegate.model.User;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentState;
import it.eng.areas.to.sdoto.docservice.rest.authentication.Secured;
import it.esel.parsley.lang.StringUtils;

/**
 * The DocumentServiceResource offers all operations for creating and updating documents
 * 
 * @author Bifulco Luigi
 *
 */
@Component
@Path("/documentService")
@Api(value = "/documentService", protocols = "http,https")
@Service
public class DocumentServiceResource {

	@Autowired
	private DocumentServiceDelegate delegate;

	MessageDigest digest = null;

	@PUT
	@Path("createDocument")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Insert a document in the repository", notes = "The repository depends on the data layer type, it should be an RDBMS")
	public BookingDocumentDTO insertDocument(//
			@ApiParam(value = "The insert document request data", required = true) InsertDocumentRequest request) throws PrintReportException, FileNotFoundException {
		BookingDocumentDTO dto = null;
		try {
			dto = delegate.createDocument(request.getParking(), request.getDocReference());
		} catch (IllegalArgumentException e) {
			throw new WebApplicationException(400);
		} catch (DataAccessException e) {
			throw new WebApplicationException(500);
		}
		return dto;
	}

	@PUT
	@Path("createAndSendDocument")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Insert a document in the repository and automatically send it to associated receivers", notes = "Insert a document in the repository and automatically send it to associated receivers")
	public BookingDocumentDTO createAndSend(@ApiParam(value = "The insert document request data", required = true) InsertDocumentRequest request) throws FileNotFoundException, PrintReportException {
		BookingDocumentDTO dto = null;
		try {
			dto = delegate.createDocument(request.getParking(), request.getDocReference());
		} catch (IllegalArgumentException e) {
			throw new WebApplicationException(400);
		} catch (DataAccessException e) {
			throw new WebApplicationException(500);
		}
		dto = delegate.sendDocumentByAddressee(request.getParking(), request.getDocReference());
		return dto;

	}

	@POST
	@Path("changeDocumentStatus")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "With the change document status operation you can change the state of document.", notes = "Possible states are CREATED, SENT, OPENED, CLOSED")
	public BookingDocumentDTO changeDocumentStatus(//
			@ApiParam(value = "The change status document request data", required = true) //
			ChangeDocumentStatusRequest request) {
		return delegate.changeDocumentStatus(request.getDocumentId(), BookingDocumentState.valueOf(request.getNewState()), null);
	}

	@POST
	@Path("listDocuments")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Using list documents you can retrieve all documents in the repository.", notes = "If you need to filter the list you can post a BookingDocumentFilter json data")
	public List<BookingDocumentDTO> listDocuments(//
			@ApiParam(value = "The search document filter request data", required = true) //
			BookingDocumentFilter filter) {
		return delegate.listDocumentByFilter(filter);
	}

	@GET
	@Path("getDocument/{docId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Useful when you need to get document data when you know the document id", notes = "The document id will never change")
	public BookingDocumentDTO getDocumentById(@PathParam(value = "docId") String docId) {
		return delegate.getDocumentById(docId);
	}

	@POST
	@Path("notifyDocumentByReference")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "When you have to notify that a document was create you can call this operation.", notes = "The addressee are derived from reference data")
	public BookingDocumentDTO notifyDocumentByReference(//
			@ApiParam(value = "The notify document request data", required = true) //
			NotifyDocumentRequest request) {
		return delegate.sendDocumentByAddressee(request.getReceiverReference(), request.getDocReference());
	}

	@GET
	@Path("notifyDocumentByMail/{docId}/{email}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Notify a just created document to receiver knowing its email", notes = "")
	public BookingDocumentDTO notifyDocumentByMail(@PathParam(value = "docId") String docId, @PathParam(value = "email") String email) {
		return delegate.sendDocumentByEmail(email, docId);
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/pdf")
	@Path("openDocument/{docId}")
	@ApiOperation(httpMethod = "GET", notes = "You can know id using the DocumentServiceResource", value = "The getPdf returns a PDF document by its reference id", response = InputStream.class)
	@Secured
	public PdfResponse openDocument(@PathParam(value = "docId") String docId, @Context SecurityContext sc) {
		String username = sc.getUserPrincipal().getName();
		BookingDocumentDTO dto = delegate.getDocumentById(docId);
		if (dto.getState() == BookingDocumentState.CLOSED) {
			throw new WebApplicationException(403);
		}
		dto = delegate.changeDocumentStatus(docId, BookingDocumentState.OPENED, username);
		byte[] data = dto.getBynaryData();
		return new PdfResponse(data);
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("test")
	@ApiOperation(value = "Test operation for check purpose")
	@Secured
	public String test(@Context SecurityContext sc) {
		String username = sc.getUserPrincipal().getName();
		return "Got it: user is " + username;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAuthenticated")
	@ApiOperation(value = "Get current user authenticated")
	@Secured
	public AuthResponse getAuthenticated(@Context SecurityContext sc) {
		String username = sc.getUserPrincipal().getName();
		User user = delegate.getAuthenticatedUser(username);
		// check user session if not present set authenticated to false
		if (user == null || StringUtils.isEmpty(user.getToken())) {
			throw new WebApplicationException(401);
		}
		AuthResponse response = new AuthResponse();
		response.setAuthenticated(true);
		response.setUsername(user.getUsername());
		response.setToken(user.getToken());
		response.setDetails(user.getDetails());
		return response;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("logout")
	@ApiOperation(value = "Logout current authenticated user if user not logged do nothing")
	@Secured
	public Boolean logout(@Context SecurityContext sc) {
		String username = sc.getUserPrincipal().getName();
		return delegate.logout(username);

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("needToRenew/{days}")
	@ApiOperation(value = "Test if current user need to renew password based on days passed in input")
	@Secured
	public Boolean needToRenew(@Context SecurityContext sc, @PathParam(value = "days") int days) {
		String username = sc.getUserPrincipal().getName();
		return delegate.needToRenew(username, days) != null;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Enable a user to change its password and perform a logout")
	@Secured
	@Path("changePassword")
	public Boolean changePassword(@Context SecurityContext sc, @ApiParam(value = "Credentials to use for changing password", required = true)Credentials credentials) {
		String username = sc.getUserPrincipal().getName();
		boolean changed = delegate.changePassword(username, credentials.getPassword()) != null;
		if (changed) {
			return logout(sc);
		}
		return changed;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("authenticateUser")
	@ApiOperation(value = "Authenticate user operation for check purpose")
	public AuthResponse authenticateUser(@ApiParam(value = "The search document filter request data", required = true) Credentials credentials) {
		String username = credentials.getUsername();
		String password = credentials.getPassword();
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			throw new WebApplicationException(401);
		}
		User user = delegate.login(username, password);
		if (user == null) {
			throw new WebApplicationException(401);
		}
		AuthResponse resp = new AuthResponse();
		resp.setAuthenticated(true);
		resp.setToken(Base64.encodeAsString(user.getToken()));
		resp.setDetails(user.getDetails());
		resp.setUsername(user.getUsername());
		return resp;
	}
}
