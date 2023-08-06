package components;

import jade.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpriteRenderer extends Component {
    private static final Logger logger = LoggerFactory.getLogger(SpriteRenderer.class);
    private boolean firstTime = false;

    @Override
    public void start() {
        logger.info("I am starting");
    }

    @Override
    public void update(float dt) {
        if (!firstTime) {
            logger.info("I am updating");
            firstTime = true;
        }
    }
}