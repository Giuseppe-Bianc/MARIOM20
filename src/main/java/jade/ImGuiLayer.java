package jade;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2d;

public class ImGuiLayer {
    private boolean showText = false;

    public void imgui() {
        ImGui.begin(" ", ImGuiWindowFlags.AlwaysAutoResize);

        if (ImGui.button("show pos")) {
            showText = true;
        }

        if (showText) {
            Vector2d pos = new Vector2d(Window.getScene().camera().position);
            ImGui.text("position x=" + pos.x + "; y=" + pos.y + ";");
            if (ImGui.button("hide pos")) {
                showText = false;
            }
        }

        ImGui.end();
    }
}
