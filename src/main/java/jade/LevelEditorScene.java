package jade;

import org.lwjgl.BufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;

public class LevelEditorScene extends Scene {

    public static final Logger logger = LoggerFactory.getLogger(LevelEditorScene.class);
    private static final String vertexShaderSrc = """
            #version 330 core
            layout (location=0) in vec3 aPos;
            layout (location=1) in vec4 aColor;

            out vec4 fColor;

            void main() {
                fColor = aColor;
                gl_Position = vec4(aPos, 1.0);
            }
            """;


    private static final String fragmentShaderSrc = """
            #version 330 core

            in vec4 fColor;

            out vec4 color;

            void main() {
                color = fColor;
            }
            """;


    private int vertexID, fragmentID, shaderProgram;

    private static final float[] vertexArray = {
            // position               // color
            0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
            -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
            0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, // Top right    2
            -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, // Bottom left  3
    };
    // IMPORTANT: Must be in counter-clockwise order
    private static final int[] elementArray = {
            /*
                    x        x
                    x        x
             */
            2, 1, 0, // Top right triangle
            0, 1, 3 // bottom left triangle
    };
    private int vaoID, vboID, eboID;

    public LevelEditorScene() {
        logger.info("Inside level editor scene");
    }

    @Override
    public void init() {
        // ============================================================
        // Compile and link shaders
        // ============================================================

        // First load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);

        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            logger.error("defaultShader.glsl");
            logger.error("\tVertex shader compilation failed");
            if (logger.isErrorEnabled()) {
                logger.error("{}", glGetShaderInfoLog(vertexID, len));
            }
            assert false : "";
        }

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(fragmentID, fragmentShaderSrc);
        glCompileShader(fragmentID);

        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            logger.error("defaultShader.glsl");
            logger.error("\tFragment shader compilation failed");
            if (logger.isErrorEnabled()) {
                logger.error("{}", glGetShaderInfoLog(fragmentID, len));
            }
            assert false : "";
        }

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        // Check for linking errors
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            logger.error("defaultShader.gls");
            logger.error("\tLinking of shaders failed.");
            if (logger.isErrorEnabled()) {
                logger.error("{}", glGetProgramInfoLog(shaderProgram, len));
            }
            assert false : "";
        }
        // ============================================================
        // Generate VAO, VBO, and EBO buffer objects, and send to GPU
        // ============================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        // Bind shader program
        glUseProgram(shaderProgram);
        // Bind the VAO that we're using
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        for (int i = 0; i < 2; i++) {
            glEnableVertexAttribArray(i);
        }

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        for (int i = 0; i < 2; i++) {
            glDisableVertexAttribArray(i);
        }

        glBindVertexArray(0);

        glUseProgram(0);
    }
}