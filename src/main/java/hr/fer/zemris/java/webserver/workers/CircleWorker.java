package hr.fer.zemris.java.webserver.workers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class CircleWorker creates png black image with size 200x200 and yellow
 * circle in center with radius of 80 pixels. Sends that image to client in HTTP
 * response with mime-type 'image/png'
 * 
 * @author Nikola Sekulić
 * 
 */
public class CircleWorker implements IWebWorker {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processRequest(RequestContext context) {

		final BufferedImage bim = new BufferedImage(200, 200,
				BufferedImage.TYPE_3BYTE_BGR);
		final Graphics2D g2d = bim.createGraphics();

		g2d.setColor(Color.yellow);
		g2d.fillOval(20, 20, 160, 160);

		g2d.dispose();

		context.setMimeType("image/png");

		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bim, "png", bos);
			final byte[] bytes = bos.toByteArray();
			context.setContentLength(bytes.length);
			context.write(bytes);
		} catch (final IOException e) {
		}

	}

}
