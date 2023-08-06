package renderer;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Costanti;

import java.io.IOException;
import java.nio.FloatBuffer;;
import java.nio.charset.StandardCharsets;
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

    public Shader(@NotNull String filepath) {
        this.filepath = filepath;
        logger.info("loading file {}", filepath);
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)), StandardCharsets.UTF_8);
            String[] splitString = source.split(Costanti.REGEX);

            // Find the first pattern after #type 'pattern'
            int index = source.indexOf(Costanti.HSTYPE) + 6;
            int eol = source.indexOf(Costanti.CRNL, index);
            String firstPattern = source.substring(index, eol).trim();

            // Find the second pattern after #type 'pattern'
            index = source.indexOf(Costanti.HSTYPE, eol) + 6;
            eol = source.indexOf(Costanti.CRNL, index);
            String secondPattern = source.substring(index, eol).trim();

            switch (firstPattern) {
                case Costanti.VERTEX -> vertexSource = splitString[1];
                case Costanti.FRAGMENT -> fragmentSource = splitString[1];
                default -> throw new IOException(String.format("Unexpected token '%s'", firstPattern));
            }

            switch (secondPattern) {
                case Costanti.VERTEX -> vertexSource = splitString[2];
                case Costanti.FRAGMENT -> fragmentSource = splitString[2];
                default -> throw new IOException(String.format("Unexpected token '%s'", secondPattern));
            }
        } catch (IOException e) {
            logger.error("{0}", e);
            assert false : String.format("Error: Could not open file for shader: '%s'", filepath);
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

    public void uploadMat4f(@NotNull String varName, @NotNull Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }
}