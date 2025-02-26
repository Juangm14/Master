package com.opengl5.android.util;

/* Copyright (C) 2014 Juan Antonio Puchol García
 * Depto. Ciencia de la Computación e Inteligencia Artificial
 * Universidad de Alicante
 * 
 * Máster Universitario en Desarrollo de Software para Dispositivos Móviles
 */

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Vector;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;


//Versión 1.0 de Resource3DSReader
public class Resource3DSReader {
	private static final String TAG = "Resource3DSReader";
	
	private static final int BYTES_PER_FLOAT = 4;
	private static final int BYTES_PER_INT   = 4;
	
	
	// Identificadores de los trozos (chunks) del archivo que vamos a leer
	// Descripción del formato en: http://es.wikipedia.org/wiki/.3ds
	private static final int CHUNK_MAIN		= 0x4d4d;
	private static final int CHUNK_OBJMESH	= 0x3d3d;
	private static final int CHUNK_OBJBLOCK	= 0x4000;
	private static final int CHUNK_TRIMESH	= 0x4100;
	private static final int CHUNK_VERTLIST	= 0x4110;
	private static final int CHUNK_FACELIST	= 0x4120;
	private static final int CHUNK_MAPLIST	= 0x4140;
	private static final int CHUNK_SMOOLIST	= 0x4150;
	
	// Número de vértices
	public int numVer;
	
	// Número de polígonos
	public int numPol;
	
	// Vectores temporales
	float[]				vertexBuffer;
	int[]				polBuffer;
	float[]				uvBuffer;
	
	// vector con la malla de triángulos resultante
	public FloatBuffer		dataBuffer;
	
	// Nombre de cada malla 3D
	String 					name="vacío";
	
	public void Resource3DSReader() {
		numVer = 0;
		numPol = 0;
	}
	
	public int readUnsignedShort(InputStream is) {
		
		int a, b; 
		
		try {
			a = is.read();
			b = is.read();
		
		} catch (IOException e) {
			throw new RuntimeException("No se pudo abrir el recurso", e);
		} catch (Resources.NotFoundException nfe) {
			throw new RuntimeException("Recurso no encontrado", nfe);
		}
		return a + (b << 8);
	}

	public int readUnsignedInt(InputStream is) {
		
		int a, b, c, d; 
		
		try {
			a = is.read();
			b = is.read();
			c = is.read();
			d = is.read();
			
		} catch (IOException e) {
			throw new RuntimeException("No se pudo abrir el recurso", e);
		} catch (Resources.NotFoundException nfe) {
			throw new RuntimeException("Recurso no encontrado", nfe);
		}
		return a + (b << 8) + (c << 16) + (d <<24);
	}
	
	public float readFloat(InputStream is) {
		int a, b, c, d;
			
		try {
			a = is.read();
			b = is.read();
			c = is.read();
			d = is.read();
			
		} catch (IOException e) {
			throw new RuntimeException("No se pudo abrir el recurso", e);
		} catch (Resources.NotFoundException nfe) {
			throw new RuntimeException("Recurso no encontrado", nfe);
		}
		return Float.intBitsToFloat(a + (b << 8) + (c << 16) + (d <<24));
	}
	
	public int read3DSFromResource(Context context, int resourceId) {
		int 	l_chunk_id;
		int 	l_chunk_length;
		int 	l_byte;
		int 	l_qty;
		int 	l_face_flags;
		int		i, j, pos;
		
		try {
			InputStream inputStream = context.getResources().openRawResource(resourceId);
	
			// Bucle para leer los trozos (chunks) de archivo mientras queden bytes por leer
			while (inputStream.available() > 0) {
				
				// Lee la cabecera, id y longitud								
				l_chunk_id = readUnsignedShort(inputStream);
				l_chunk_length = readUnsignedInt(inputStream);
				
				if (LoggerConfig.ON) {
					//Log.w(TAG, "Leyendo 3DS id: " + l_chunk_id);
					//Log.w(TAG, "Leyendo 3DS length: " + l_chunk_length);
				}
				switch (l_chunk_id)
			    {
			    	case CHUNK_MAIN: 
			    		break;
			        
			    	case CHUNK_OBJMESH:
			        	break;
			         
			        case CHUNK_OBJBLOCK: 
			        	
			        	name="";
			        	i=0;
			            do {
			            	l_byte = inputStream.read();
			            	if (l_byte>0) name=name+(char)l_byte;
			            	i++;
			            } while(l_byte != 0 && i<20);
			            if (LoggerConfig.ON) {
			        		Log.w(TAG, "CHUNK_OBJBLOCK: " + name);
						}
			            break;
			        
			        case CHUNK_TRIMESH:
			        	break;  
			        	
			        case CHUNK_VERTLIST: 
			        	numVer = readUnsignedShort(inputStream);
			            
			        	vertexBuffer = new float[numVer*3];
			        	
			            if (LoggerConfig.ON) {
							Log.w(TAG, "Número de Vértices: " + numVer);
						}
			            for (i=0; i<numVer; i++)
			            {
			            	vertexBuffer[i*3]   = readFloat(inputStream);
			            	vertexBuffer[i*3+1] = readFloat(inputStream);
			            	vertexBuffer[i*3+2] = readFloat(inputStream);
			            	
			            	if (LoggerConfig.ON) {
								//Log.w(TAG, "Vértice: " + i + ": ["+ vertexBuffer[i*3]+", "+vertexBuffer[i*3+1]+", "+vertexBuffer[i*3+2]+"]");
							}
			            }
			            break;
			            
			        case CHUNK_FACELIST:
			        	numPol = readUnsignedShort(inputStream);
			            	
			        	polBuffer = new int[numPol*3];
			        	
			        	if (LoggerConfig.ON) {
							Log.w(TAG, "Número de Polígonos: " + numPol);
						}
			        			        		            
			        	for (i=0; i<numPol; i++)
			            {
			        		polBuffer[i*3]   = readUnsignedShort(inputStream);
			        		polBuffer[i*3+1] = readUnsignedShort(inputStream);
			        		polBuffer[i*3+2] = readUnsignedShort(inputStream);
			        		l_face_flags = readUnsignedShort(inputStream);
			            	
			        		if (LoggerConfig.ON) {
								//Log.w(TAG, "Polígono: " + i + ": [" + polBuffer[i*3] + ", " + polBuffer[i*3+1] + ", " + polBuffer.[i*3+2] + "]");
							}
			            }
			            break;
			            
			        case CHUNK_MAPLIST:
			        	l_qty = readUnsignedShort(inputStream);
			        
			        	if (LoggerConfig.ON) {
							Log.w(TAG, "Número de uv's: " + l_qty);
						}
			        				        	
			        	uvBuffer = new float[l_qty*2];
			        	
			        	for (i=0; i<l_qty; i++)
			            {
			            	uvBuffer[i*2]   = readFloat(inputStream);
			            	uvBuffer[i*2+1] = readFloat(inputStream);
			            }
			            break;
			            
			        default:
			           	if (LoggerConfig.ON) {
							//Log.w(TAG, "3DS saltando: " + (l_chunk_length-6) + " bytes");
						}
			        	inputStream.skip(l_chunk_length-6);
			        	break;
			    }
			}
		} catch (EOFException e) {
			throw new RuntimeException("Fin de fichero EOF: " + resourceId, e);
		} catch (IOException e) {
			throw new RuntimeException("No se pudo abrir el recurso: " + resourceId, e);
		} catch (Resources.NotFoundException nfe) {
			throw new RuntimeException("Recurso no encontrado: " + resourceId, nfe);
		}
		
		if (LoggerConfig.ON) {
			Log.w(TAG, "Recurso 3DS leído correctamente, con " + numVer + " vértices y "+numPol+" polígonos.");
		}
		// Crea un buffer en JNI (Java Native Interface)
		dataBuffer = ByteBuffer
				.allocateDirect(numPol * 9 * BYTES_PER_FLOAT)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		
		// Crea la malla de triángulos
		for (i=0; i<numPol; i++) {
			
			for(j=0;j<3;j++) {
				pos = polBuffer[i*3+j];
				dataBuffer.put(9*i+j*3, vertexBuffer[pos*3]);
				dataBuffer.put(9*i+j*3+1, vertexBuffer[pos*3+1]);
				dataBuffer.put(9*i+j*3+2, vertexBuffer[pos*3+2]);
			}
		}
		// Liberamos la memoria (buffers) temporal
		vertexBuffer=null;
		polBuffer=null;
		uvBuffer=null;
			
		if (LoggerConfig.ON) {
			//Log.w(TAG, "dataBuffer creado correctamente.");
		}
		return numPol*9;
	}
}
