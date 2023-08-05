package renderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.opengl.GL33.glGetShaderInfoLog;

public class Shader {
    public static final Logger logger = LoggerFactory.getLogger(Shader.class);
    private int shaderProgramID;

    private String vertexSource;
    private String fragmentSource;
    private final String filepath;

    public Shader(String filepath) {
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            // Find the first pattern after #type 'pattern'
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            // Find the second pattern after #type 'pattern'
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            switch (firstPattern) {
                case "vertex" -> vertexSource = splitString[1];
                case "fragment" -> fragmentSource = splitString[1];
                default -> throw new IOException("Unexpected token '" + firstPattern + "'");
            }

            switch (secondPattern) {
                case "vertex" -> vertexSource = splitString[2];
                case "fragment" -> fragmentSource = splitString[2];
                default -> throw new IOException("Unexpected token '" + secondPattern + "'");
            }
        } catch (IOException e) {
            logger.error("{}", e);
            assert false : "Error: Could not open file for shader: '" + filepath + "'";
        }
    }

    public void compile() {
        // ============================================================
        // Compile and link shaders
        // ============================================================
        int vertexID, fragmentID;

        // First load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        // Check for errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            logger.error(filepath);
            logger.error("\tVertex shader compilation failed");
            if (logger.isErrorEnabled()) {
                logger.error("{}", glGetShaderInfoLog(vertexID, len));
            }
            assert false : "";
        }

        // First load and compile the vertex shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        // Check for errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            logger.error(filepath);
            logger.error("\tFragment shader compilation failed.");
            if (logger.isErrorEnabled()) {
                logger.error("{}", glGetShaderInfoLog(vertexID, len));
            }
            assert false : "";
        }

        // Link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // Check for linking errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            logger.error(filepath);
            logger.error("\tLinking of shaders failed.");
            if (logger.isErrorEnabled()) {
                logger.error("{}", glGetProgramInfoLog(shaderProgramID, len));
            }
            assert false : "";
        }
    }

    public void use() {
        // Bind shader program
        glUseProgram(shaderProgramID);
    }

    public void detach() {
        glUseProgram(0);
    }
}