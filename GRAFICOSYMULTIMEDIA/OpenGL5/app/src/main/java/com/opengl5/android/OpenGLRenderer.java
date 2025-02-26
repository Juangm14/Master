package com.opengl5.android;

import static android.opengl.GLES20.*;

import static android.opengl.Matrix.*;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.opengl5.android.R;
import com.opengl5.android.util.LoggerConfig;
import com.opengl5.android.util.Resource3DSReader;
import com.opengl5.android.util.ShaderHelper;
import com.opengl5.android.util.TextResourceReader;



public class OpenGLRenderer implements Renderer {
	private static final String TAG = "OpenGLRenderer";
	
	// Para paralela
	private static final float TAM = 1.0f;
	// Para perspectiva
	// private static final float TAM = 1.0f;
	
	private static final int BYTES_PER_FLOAT = 4;
	
	private final Context context;
	private int program;
	private static final String U_COLOR = "u_Color";
	private int uColorLocation;
	private static final String A_POSITION = "a_Position";
	private int aPositionLocation;
	private static final int POSITION_COMPONENT_COUNT = 3;
	
	private static final String A_COLOR = "a_Color";
	private static final int COLOR_COMPONENT_COUNT = 3;
	// Cálculo del tamaño de los datos (5 floats)
	private static final int STRIDE =
			(POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
	private int aColorLocation;
	
	// Nombre del uniform
	private static final String U_MATRIX = "u_Matrix";
	// Vector donde almacenar la matriz
	private final float[] projectionMatrix = new float[16];
	// Entero (ID) para capturar la posición de la matriz
	private int uMatrixLocation;
	Resource3DSReader obj3DS;
	
	float[] tablaVertices = {
		// Abanico de triángulos, x, y, R, G, B
		 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
		-0.5f,-0.8f, 0.7f, 0.7f, 0.7f,
		 0.5f,-0.8f, 0.7f, 0.7f, 0.7f,
		 0.5f, 0.8f, 1.0f, 1.0f, 1.0f,
		-0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
		-0.5f,-0.8f, 0.7f, 0.7f, 0.7f,
		
		// Línea 1, x, y, R, G, B
		-0.5f, 0f, 1.0f, 0.0f, 0.0f,
		 0.5f, 0f, 1.0f, 0.0f, 0.0f
	};

	
	public OpenGLRenderer(Context context) {
		this.context = context;
		
		// Lee un archivo 3DS desde un recurso
		obj3DS = new Resource3DSReader();
		obj3DS.read3DSFromResource(context, R.raw.mono);
		
	}
	
	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
		
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		// Leemos los shaders
		String vertexShaderSource = TextResourceReader
				.readTextFileFromResource(context, R.raw.simple_vertex_shader);
		String fragmentShaderSource = TextResourceReader
				.readTextFileFromResource(context, R.raw.simple_fragment_shader);
		
		// Compilamos los shaders
		int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
		int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
		
		// Enlazamos el programa OpenGL
		program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
		
		// En depuración validamos el programa OpenGL
		if (LoggerConfig.ON) {
			ShaderHelper.validateProgram(program);
		}
		
		// Activamos el programa OpenGL
		glUseProgram(program);
		
		// Capturamos el uniform u_Color
		uColorLocation = glGetUniformLocation(program, U_COLOR);
		//aColorLocation = glGetAttribLocation(program, A_COLOR);
		
		// Capturamos el attribute a_Position
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		
		// Capturamos el uniform u_Matrix
		uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
		
		// Asociando vértices con su attribute
		obj3DS.dataBuffer.position(0);
		//glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
		//false, 0, vertexData);
		glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
				false, 0, obj3DS.dataBuffer);
		
		// Habilitamos el vector de vértices
		glEnableVertexAttribArray(aPositionLocation);
		
		// Asociamos el vector de colores
		//vertexData.position(POSITION_COMPONENT_COUNT);
		//glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT,
		//false, STRIDE, vertexData);
		//glEnableVertexAttribArray(aColorLocation);
		
	}
	
	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		// Establecer el viewport de  OpenGL para ocupar toda la superficie.
		glViewport(0, 0, width, height);
		final float aspectRatio = width > height ?
				(float) width / (float) height :
				(float) height / (float) width;
		if (width > height) {
				// Landscape
				orthoM(projectionMatrix, 0, -aspectRatio*TAM, aspectRatio*TAM, -TAM, TAM, -10.0f, 10.0f);
				//frustumM(projectionMatrix, 0, -aspectRatio*TAM, aspectRatio*TAM, -TAM, TAM, 0.1f, 100.0f);
		} else {
				// Portrait or square
				orthoM(projectionMatrix, 0, -TAM, TAM, -aspectRatio*TAM, aspectRatio*TAM, -10.0f, 10.0f);
				//frustumM(projectionMatrix, 0, -TAM, TAM, -aspectRatio*TAM, aspectRatio*TAM, 0.1f, 100.0f);
		}
		/*
		// Añadimos una translación para ajustar la perspectiva (separarnos)
		final float[] modelMatrix = new float[16];
		setIdentityM(modelMatrix, 0);
		//translateM(modelMatrix, 0, 0f, 0f, -2.0f);
		
		final float[] temp = new float[16];
		multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
		System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
		*/
	}
	
	@Override
	public void onDrawFrame(GL10 glUnused) {
		int i;
		
		// Clear the rendering surface.
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DITHER);
		glLineWidth(2.0f);
		
		
		// Añadimos una rotación
		final float[] modelMatrix = new float[16];
		setIdentityM(modelMatrix, 0);
		// Rotación de 1º alrededor del eje y
		rotateM(modelMatrix, 0, -1.0f, 0f, 1f, 0f); 
		
		final float[] temp = new float[16];
		multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
		System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
		
		// Envía la matriz de proyección al shader
		glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);
		
		// Actualizamos el color (Marrón)
		glUniform4f(uColorLocation, 0.78f, 0.49f, 0.12f, 1.0f);
		
		// Dibujamos el objeto
		glDrawArrays(GL_TRIANGLES, 0, obj3DS.numPol*3);
		
		// Actualizamos el color (Blanco)
		glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f); 
			
		
		glDrawArrays(GL_LINES, 0, obj3DS.numPol*3);
		//for (i=0; i<obj3DS.numPol; i++)
			//glDrawArrays(GL_LINE_LOOP, i*3, 3);
	}
}