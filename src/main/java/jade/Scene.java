package jade;

public abstract class Scene {

    protected Camera camera;
    protected Scene() {

    }

    public void init() {

    }

    public abstract void update(float dt);
}