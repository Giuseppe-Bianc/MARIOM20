package components;

import jade.Component;
import org.joml.Vector4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpriteRenderer extends Component {
    private static final Logger logger = LoggerFactory.getLogger(SpriteRenderer.class);
    private Vector4f color;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
    }

    @Override
    public void start() {
    }

    @Override
    public void update(float dt) {

    }

    public Vector4f getColor() {
        return this.color;
    }
}