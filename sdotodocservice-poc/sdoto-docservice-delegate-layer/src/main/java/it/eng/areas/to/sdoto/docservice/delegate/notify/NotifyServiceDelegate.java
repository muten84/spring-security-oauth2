/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.delegate.notify;

/**
 * @author Bifulco Luigi
 *
 */
public interface NotifyServiceDelegate {
	/**
	 * 
	 * @param to
	 * @param note
	 * @param payload
	 * @return
	 */
	public boolean sendData(String to, String note, byte[] payload);

	/**
	 * @param dest
	 * @param data
	 * @return
	 */
	boolean sendData(String dest, byte[] data);

	/**
	 * @param dest
	 * @param text
	 * @param data
	 * @return
	 */
	boolean sendData(String[] dest, String text, byte[] data);

	/**
	 * @param dest
	 * @param data
	 * @return
	 */
	boolean sendData(String[] dest, byte[] data);

	/**
	 * @param dest
	 * @param text
	 * @return
	 */
	boolean sendSimpleData(String dest, String text);

	/**
	 * @param dest
	 * @param text
	 * @return
	 */
	boolean sendSimpleData(String[] dest, String text);
}
