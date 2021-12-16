package renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {


    private int shaderProgramID;
    private String vertexSource;
    private String fragmentSource;
    private String filePath;

    public Shader(String filePath){
        this.filePath = filePath;
        try{
            String src = new String (Files.readAllBytes(Paths.get(filePath)));
            String[] splitString = src.split("(#type)( )+([a-zA-Z]+)");

            int index = src.indexOf("#type") + 6;
            int endOfTheLine = src.indexOf("\r\n", index);
            String firstPattern = src.substring(index, endOfTheLine).trim();

            index = src.indexOf("#type", endOfTheLine) + 6;
            endOfTheLine = src.indexOf("\r\n", index);
            String secondPattern = src.substring(index, endOfTheLine).trim();

            if(firstPattern.equals("vertex")){
                vertexSource = splitString[1];
            }else if(firstPattern.equals("fragment")){
                fragmentSource = splitString[1];
            }else{
                throw new IOException("Unexpected Token: " + firstPattern);
            }

            if(secondPattern.equals("vertex")){
                vertexSource = splitString[2];
            }else if(secondPattern.equals("fragment")){
                fragmentSource = splitString[2];
            }else{
                throw new IOException("Unexpected Token: " + secondPattern);
            }

        }catch(IOException e){
            e.printStackTrace();
            assert false : "Error can't open file for shader : " + filePath;
        }

        System.out.println(vertexSource);
        System.out.println(fragmentSource);

    }

    public void compile(){
        //=====================================================
        //          Compile and link shaders
        //=====================================================

        int vertexID, fragmentID;

        //First load and complie the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);

        //Pass the shader source code to the GPU
        glShaderSource(vertexID, vertexSource);
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
        glShaderSource(fragmentID, fragmentSource);
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
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        //Check for linking errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if(success == GL_FALSE){
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.printf("ERROR: '"+filePath+"' shader program is failed");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }

    }

    public void use(){
        // Bind Shader Program
        glUseProgram(shaderProgramID);
    }

    public void detach(){
        glUseProgram(0);
    }

}
