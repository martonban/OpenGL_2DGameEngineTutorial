package luminance.sceneManager;

import luminance.KeyListener;
import luminance.Window;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {

    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;

    public LevelEditorScene() {
        System.out.println("Inside level Editor Scene");

    }

    @Override
    public void update(float deltaTime) {
        System.out.println("" + (1.1f / deltaTime ) + "FPS");
        if(!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)){
            changingScene = true;
        }
        if(changingScene && timeToChangeScene > 0){
            timeToChangeScene -= deltaTime;
            Window.get().r -=  deltaTime * 5.0f;
            Window.get().g -=  deltaTime * 5.0f;
            Window.get().b -=  deltaTime * 5.0f;

        }else if(changingScene){
            Window.changeScene(1);
        }
    }

}
