package com.airhockey.android;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.GL_LINES;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import com.airhockey.android.util.LoggerConfig;
import com.airhockey.android.util.ShaderHelper;
import com.airhockey.android.util.TextResourceReader;


public class AirHockeyRenderer implements Renderer {
	private static final int POSITION_COMPONENT_COUNT = 2;
	private static final int BYTES_PER_FLOAT = 4;
	private final FloatBuffer vertexData;
	private final Context context;
	private int program;
	private static final String U_COLOR = "u_Color";
	private int uColorLocation;
	private static final String A_POSITION = "a_Position";
	private int aPositionLocation;
	
	public AirHockeyRenderer(Context context){
		this.context = context;
//		float[] tableVertices = {
//				0f, 0f,
//				0f, 14f,
//				9f, 14f,
//				9f, 0f,
//		};
//		float[] tableVerticesWithTriangles = {
//				// Triangle 1
//				0f, 0f,
//				9f, 14f,
//				0f, 14f,
//				// Triangle 2
//				0f, 0f,
//				9f, 0f,
//				9f, 14f,
//				//Line 1
//				0f,7f,
//				9f,7f,
//				
//				//Mallets
//				4.5f,2f,
//				4.5f,12f
//		};
		float[] tableVerticesWithTriangles = {
				// Triangle 1
				-0.5f, -0.5f,
				0.5f, 0.5f,
				-0.5f, 0.5f,
				// Triangle 2
				-0.5f, -0.5f,
				0.5f, -0.5f,
				0.5f, 0.5f,
				// Line 1
				-0.5f, 0f,
				0.5f, 0f,
				// Mallets
				0f, -0.25f,
				0f, 0.25f
				};
		vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length*BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertexData.put(tableVerticesWithTriangles);
		
	}
	@Override
	public void onDrawFrame(GL10 arg0) {
		//glClear(GL_COLOR_BUFFER_BIT);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
		glDrawArrays(GL_TRIANGLES, 0, 6);
		glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
		glDrawArrays(GL_LINES, 6, 2);
	}

	@Override
	public void onSurfaceChanged(GL10 glUnsed, int width, int height) {
		// Set the OpenGL viewport to fill the entire surface.
		glViewport(0, 0, width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
		glClearColor(1.0f,0.0f,0.0f,0.0f);
		String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);
		String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader);
		int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
		int fragmentShader = ShaderHelper.compileFramentShader(fragmentShaderSource);
		program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
		if (LoggerConfig.ON) {
			ShaderHelper.validateProgram(program);
		}
		glUseProgram(program);
		uColorLocation = glGetUniformLocation(program, U_COLOR);
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		vertexData.position(0);
		glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
		false, 0, vertexData);
		glEnableVertexAttribArray(aPositionLocation);
	}
	
}
