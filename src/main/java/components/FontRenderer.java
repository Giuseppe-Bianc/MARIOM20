package components;

import jade.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FontRenderer extends Component {
    private static final Logger logger = LoggerFactory.getLogger(FontRenderer.class);

    @Override
    public void start() {
        if (gameObject.getComponent(SpriteRenderer.class) != null) {
            logger.info("Found Font Renderer!");
        }
    }

    @Override
    public void update(float dt) {

    }
}