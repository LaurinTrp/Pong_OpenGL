package lwjgl_new.main;

import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL11C.GL_NEAREST;
import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11C.glBindTexture;
import static org.lwjgl.opengl.GL11C.glGenTextures;
import static org.lwjgl.opengl.GL11C.glTexImage2D;
import static org.lwjgl.opengl.GL11C.glTexParameteri;
import static org.lwjgl.opengl.GL12C.GL_CLAMP_TO_EDGE;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {

	public static int loadTexture(BufferedImage image) {
		float[] data = new float[image.getWidth() * image.getHeight() * 4];
		byte[] imageData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

		for (int w = 0; w < (image.getWidth() * 4); w += 4) {
			for (int h = 0; h < image.getHeight(); h++) {

				int indexInBufferedImage = h * (image.getWidth() * 4) + w;
				int indexInTexture = (image.getHeight() - 1 - h) * (image.getWidth() * 4) + w;

				data[indexInTexture + 0] = Byte.toUnsignedInt(imageData[indexInBufferedImage + 3]) / 255.0f; // red
				data[indexInTexture + 1] = Byte.toUnsignedInt(imageData[indexInBufferedImage + 2]) / 255.0f; // green
				data[indexInTexture + 2] = Byte.toUnsignedInt(imageData[indexInBufferedImage + 1]) / 255.0f; // blue
				data[indexInTexture + 3] = Byte.toUnsignedInt(imageData[indexInBufferedImage + 0]) / 255.0f; // alpha
			}
		}

		int tex = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, tex);
		{
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 192, 128, 0, GL_RGBA, GL_FLOAT, data);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		}
		glBindTexture(GL_TEXTURE_2D, 0);

		return tex;
	}

	public static int loadTextureFromResource(String path) {

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return loadTexture(image);
	}
}
