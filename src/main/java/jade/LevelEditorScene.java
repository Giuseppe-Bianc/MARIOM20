package jade;

import components.*;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.AssetPool;
import util.Costanti;

import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorScene extends Scene {

    public static final Logger logger = LoggerFactory.getLogger(LevelEditorScene.class);

    GameObject testObj;


    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));

        int xOffset = 10;
        int yOffset = 10;

        final float totalWidth = (600 - xOffset * 2);
        final float totalHeight = (300 - yOffset * 2);
        final float sizeX = totalWidth / 100.0f;
        final float sizeY = totalHeight / 100.0f;
        final float padding = 0;


        float xPos = 0;
        float yPos = 0;
        StringBuilder ss = new StringBuilder();
        for (int x = 0; x < 100; x++) {
            xPos = xOffset + (x * sizeX) + (padding * x);
            for (int y = 0; y < 100; y++) {
                yPos = yOffset + (y * sizeY) + (padding * y);
                ss.setLength(0);
                ss.append("Obj").append(x).append(y);
                GameObject go = new GameObject(ss.toString(), new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                go.addComponent(new SpriteRenderer(new Vector4f(xPos / totalWidth, yPos / totalHeight, 1, 1)));
                this.addGameObjectToScene(go);
            }
        }
        loadResources();
    }

    private void loadResources() {
        AssetPool.getShader(Costanti.FILEPATHSD);
    }

    @Override
    public void update(float dt) {
        //logger.info("FPS: {}", (1.0 / dt));
        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
            camera.position.x += 100f * dt;
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
            camera.position.x -= 100f * dt;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
            camera.position.y += 100f * dt;
        } else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
            camera.position.y -= 100f * dt;
        }

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
}