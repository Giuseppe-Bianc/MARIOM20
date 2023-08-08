package jade;

import components.*;
import org.joml.Vector2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.AssetPool;
import util.Costanti;


public class LevelEditorScene extends Scene {

    public static final Logger logger = LoggerFactory.getLogger(LevelEditorScene.class);
    private GameObject obj1;
    private Spritesheet sprites;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f(-250, 0));

        sprites = AssetPool.getSpritesheet(Costanti.FILEPAHTSPS);

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100),
                new Vector2f(256, 256)), 2);
        obj1.addComponent(new SpriteRenderer(new Sprite(
                AssetPool.getTexture(Costanti.FILEPAHTBIMAGE)
        )));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 2",
                new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 3);
        obj2.addComponent(new SpriteRenderer(new Sprite(
                AssetPool.getTexture(Costanti.FILEPAHTBIMAGE2)
        )));
        this.addGameObjectToScene(obj2);
    }

    private void loadResources() {
        AssetPool.getShader(Costanti.FILEPATHSD);

        AssetPool.addSpritesheet(Costanti.FILEPAHTSPS,
                new Spritesheet(AssetPool.getTexture(Costanti.FILEPAHTSPS),
                        16, 16, 26, 0));
    }


    @Override
    public void update(float dt) {
        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
}