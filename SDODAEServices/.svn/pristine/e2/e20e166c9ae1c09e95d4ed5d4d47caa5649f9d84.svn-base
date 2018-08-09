package it.eng.areas.ems.sdodaeservices.rest.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.areas.ems.sdodaeservices.delegate.model.Image;
import it.esel.parsley.misc.Base64;

public class ImageUtils {

	public static enum Size {
		SMALL(45), MEDIUM(250), LARGE(2000);

		private int maxWidth;

		private Size(int maxWidth) {
			this.maxWidth = maxWidth;
		}

		public int getMaxWidth() {
			return maxWidth;
		}
	}

	private static Logger logger = LoggerFactory.getLogger(ImageUtils.class);

	public static BufferedImage processImage(HttpServletRequest request, Image img, String imgName, Size size) {
		try {

			ByteArrayOutputStream out = new ByteArrayOutputStream();

			byte dearr[] = Base64.decode(img.getData());

			BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(dearr));
			int width = originalImage.getWidth();
			int height = originalImage.getHeight();

			BufferedImage toRet = originalImage;
			if (width > size.getMaxWidth()) {
				// se la size dell'immagine è maggiore della dimensione voluta
				int newHeight = height * size.getMaxWidth() / width;
				resize(originalImage, out, "jpg", size.getMaxWidth(), newHeight, size == Size.SMALL);

				toRet = ImageIO.read(new ByteArrayInputStream(out.toByteArray()));
			}

			return toRet;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING processImage", e);
			return null;
		}
	}

	public static void resize(String inputImagePath, String outputImagePath, int scaledWidth, int scaledHeight)
			throws IOException {
		// reads input image
		File inputFile = new File(inputImagePath);
		BufferedImage inputImage = ImageIO.read(inputFile);

		// creates output image
		BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

		// scales the input image to the output image
		Graphics2D g2d = outputImage.createGraphics();
		g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
		g2d.dispose();

		// extracts extension of output file
		String formatName = outputImagePath.substring(outputImagePath.lastIndexOf(".") + 1);

		// writes to output file
		ImageIO.write(outputImage, formatName, new File(outputImagePath));
	}

	public static void resize(BufferedImage inputImage, OutputStream output, String formatName, int scaledWidth,
			int scaledHeight, boolean crop) throws IOException {

		int x = 0;
		int y = 0;

		int w = inputImage.getWidth();
		int h = inputImage.getHeight();

		if (crop) {
			if (inputImage.getWidth() > inputImage.getHeight()) {
				x = (inputImage.getWidth() - inputImage.getHeight()) / 2;
				y = 0;
				w = h = inputImage.getHeight();
				scaledWidth = scaledHeight;
			} else {
				x = 0;
				y = (inputImage.getHeight() - inputImage.getWidth()) / 2;
				w = h = inputImage.getWidth();
				scaledHeight = scaledWidth;
			}
		}

		BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());
		// scales the input image to the output image
		Graphics2D g2d = outputImage.createGraphics();
		g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, x, y, w, h, null);
		g2d.dispose();

		// writes to output file
		ImageIO.write(outputImage, formatName, output);
	}

	public static void cleanImage(HttpServletRequest request, String id) {
		String realPath = request.getServletContext().getRealPath("/") + "/daeimg/";
		String fileName = realPath + "/" + id + ".jpg";
		String fileNameMin = realPath + "/" + id + "_min.jpg";
		File imgFile = new File(fileName);
		File imgFileMin = new File(fileNameMin);
		if (imgFile.exists()) {
			imgFile.delete();
		}
		if (imgFileMin.exists()) {
			imgFileMin.delete();
		}
	}

	public static String resizeAndEncode(InputStream fileContent, int maxWidth, int maxHeight) throws IOException {
		File tempFile = File.createTempFile("tmp", ".jpg");
		FileOutputStream outputStream = new FileOutputStream(tempFile);

		BufferedImage originalImage = ImageIO.read(fileContent);
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		int newWidth = width;
		int newHeight = height;

		if (width > height) {
			// immagine orizzontale
			if (width > maxWidth) {
				newWidth = maxWidth;
				newHeight = height * 1000 / width;
			}
		} else {
			// immagine scattata in altezza
			if (height > maxHeight) {
				newHeight = maxHeight;
				newWidth = width * 1000 / height;
			}
		}

		// faccio il resize dell'immagine
		ImageUtils.resize(originalImage, outputStream, "jpg", newWidth, newHeight, false);
		outputStream.flush();
		outputStream.close();

		byte[] fileByte = Files.readAllBytes(tempFile.toPath());

		String encode = Base64.encodeBytes(fileByte);
		// cancello i file temporanei
		tempFile.delete();

		return encode;
	}

	public static String extractAndEncode(HttpServletRequest request, int maxWidth, int maxHeight)
			throws FileUploadException {

		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(15000000);
		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum file size to be uploaded.
		upload.setSizeMax(15000000);

		// Parse the request to get file items.
		List<FileItem> fileItems = upload.parseRequest(request);

		for (FileItem fi : fileItems) {
			try {
				if (!fi.isFormField()) {
					logger.info("Received image " + fi.getName());
					InputStream fileContent = fi.getInputStream();

					return ImageUtils.resizeAndEncode(fileContent, maxWidth, maxHeight);

				}
			} catch (Exception e) {
				logger.error("ERROR WHILE EXECUTING uploadImmagine", e);
			}
		}
		return null;
	}

}
