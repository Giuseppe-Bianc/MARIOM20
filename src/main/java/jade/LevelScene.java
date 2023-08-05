package jade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LevelScene extends Scene {
    public static final Logger logger = LoggerFactory.getLogger(LevelScene.class);

    public LevelScene() {
        logger.info("Inside level scene");
        Window.get().r = 1;
        Window.get().g = 1;
        Window.get().b = 1;
    }

    @Override
    public void update(float dt) {

    }
}