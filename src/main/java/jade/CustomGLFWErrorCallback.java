package jade;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomGLFWErrorCallback extends GLFWErrorCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomGLFWErrorCallback.class);

    @Override
    public void invoke(int error, long description) {
        // Convert the GLFW error code to a human-readable string
        String errorDescription = GLFWErrorCallback.getDescription(description);

        // Log the error using SLF4J and logback
        LOGGER.error("GLFW Error ({}): {}", error, errorDescription);
    }
}