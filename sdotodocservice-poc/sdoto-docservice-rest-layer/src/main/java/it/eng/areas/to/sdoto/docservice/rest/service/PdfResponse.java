/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.rest.service;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

/**
 * @author Bifulco Luigi
 *
 */
public class PdfResponse implements StreamingOutput {

	byte[] data;

	public PdfResponse(byte[] data) {
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ws.rs.core.StreamingOutput#write(java.io.OutputStream)
	 */
	@Override
	public void write(OutputStream output) throws IOException, WebApplicationException {
		output.write(this.data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		this.data = null;
		super.finalize();
	}

}