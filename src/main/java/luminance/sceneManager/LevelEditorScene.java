package luminance.sceneManager;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {


    private String vertexShaderSrc = "#version 330 core\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";

    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = fColor;\n" +
            "}";


    private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray = {
        //  vertices            //color
            0.5f,   -0.5f,  0.0f,      1,0f, 0.0f, 0.0f, 1.0f, //Bottom Right  0
            -0.5f,  0.5f,   0.0f,      0,0f, 1.0f, 0.0f, 1.0f, //Top Left      1
            0.5f,   0.5f,   0.0f,      0,0f, 0.0f, 1.0f, 1.0f, //top Right     2
            -0.5f,  -0.5f,  0.0f,      1,0f, 1.0f, 0.0f, 1.0f, //Bottom Left   3
    };
    /*
    private float[] vertexArray = {
            // position               // color
            0.5f, -0.5f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
            -0.5f,  0.5f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
            0.5f,  0.5f, 0.0f ,      0.0f, 0.0f, 1.0f, 1.0f, // Top right    2
            -0.5f, -0.5f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f, // Bottom left  3
    };
    */

    private int[] elementArray = {
            2, 1, 0, //Top Right triangle
            0, 1, 3 //Bottom left triangle
    };


    private int vaoID, vboID, eboID;

    public LevelEditorScene() {

    }

    @Override
    public void init(){
        //=====================================================
        //          Compile and link shaders
        //=====================================================

        //First load and complie the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);

        //Pass the shader source code to the GPU
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);

        // Check for errors in compilation process
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.printf("ERROR: 'default.glsl' vertex shader is failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }



        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        //Pass the shader source code to the GPU
        glShaderSource(fragmentID, fragmentShaderSrc);
        glCompileShader(fragmentID);

        // Check for errors in compilation process
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.printf("ERROR: 'default.glsl' fragment shader is failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }


        //Link Shaders and check for errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        //Check for linking errors
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if(success == GL_FALSE){
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.printf("ERROR: 'default.glsl' shader program is failed");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : "";
        }

        //=====================================================
        //Generate VAO, VBO and EBO buffer object, and set to the GPU
        //=====================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //Create vbo upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);


        //  Add vertex atribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;

        glVertexAttribPointer(0, positionSize, GL_FLOAT,
                false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false,
                vertexSizeBytes, positionSize * floatSizeBytes);
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        glEnableVertexAttribArray(1);

    }


    @Override
    public void update(float deltaTime) {
        // Bind Shader Program
        glUseProgram(shaderProgram);
        //Bind the VAO
        glBindVertexArray(vaoID);

        //Enable vertex atribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length,
                GL_UNSIGNED_INT, 0);

        //Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);
    }

}
