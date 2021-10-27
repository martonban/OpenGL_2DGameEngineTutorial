package luminance;


import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int width, height;
    private String title;
    private Long glfwWindow;

    private static Window window = null;

    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
    }


    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run(){
        System.out.println("Hello OpenGL!");
        System.out.println(Version.getVersion());
        init();
        loop();
    }

    public void init(){
        // Set Up an error Callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Init GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Nem inicializálódik a GLFW");
        }
        //Beállítjuk a GLFW-t
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //Létrehozzuk az ablakot(window)
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("NEm tudjuk létrehozni a GLFW Abalkot(Window)");
        }

        glfwMakeContextCurrent(glfwWindow);

        // v-sync bekapcsolása
        //glfwSwapBuffers(1);

        //Láthatóvá tesszük az ablakot
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

    }

    public void loop(){
        while(!glfwWindowShouldClose(glfwWindow)){
            //Poll Events
            glfwPollEvents();

            glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            glfwSwapBuffers(glfwWindow);
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
