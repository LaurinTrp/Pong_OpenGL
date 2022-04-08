package lwjgl_new.rendering;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

public class Texture {

	private int texID;

	public void loadFromResource(String path) throws Exception {

		texID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texID);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);

		ByteBuffer image = stbi_load(path, width, height, channels, 0);

		if (channels.get(0) == 3 && image != null) {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
		} else if (channels.get(0) == 4 && image != null) {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
		} else {
			throw new Exception("Texture was not loaded: " + path);
		}

		stbi_image_free(image);
	}

	public void readBufferedImage(BufferedImage image) {
		float[] data = new float[image.getWidth() * image.getHeight() * 4];
		
		byte[] imageData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

		for (int w = 0; w < (image.getWidth() * 4); w += 4) {
			for (int h = 0; h < image.getHeight(); h++) {

				int indexInBufferedImage = h * (image.getWidth() * 4) + w;

				data[indexInBufferedImage + 0] = Byte.toUnsignedInt(imageData[indexInBufferedImage + 3]) / 255.0f; // red
				data[indexInBufferedImage + 1] = Byte.toUnsignedInt(imageData[indexInBufferedImage + 2]) / 255.0f; // green
				data[indexInBufferedImage + 2] = Byte.toUnsignedInt(imageData[indexInBufferedImage + 1]) / 255.0f; // blue
				data[indexInBufferedImage + 3] = Byte.toUnsignedInt(imageData[indexInBufferedImage + 0]) / 255.0f; // alpha
			}
		}
		texID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texID);
		{
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_FLOAT, data);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		}
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, texID);
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);

	}

}
