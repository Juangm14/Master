package com.japg.mastermoviles.opengl10;

import android.opengl.Matrix;

public class Tanque {
    private float[] cuerpoMatriz = new float[16];  // Matriz de transformación para el cuerpo
    private float[] cabezaMatriz = new float[16];  // Matriz de transformación para la cabeza
    private float[] cañonMatriz = new float[16];   // Matriz de transformación para el cañón

    private float anguloCuerpo = 0f;  // Ángulo de rotación del cuerpo
    private float anguloCabeza = 0f;  // Ángulo de rotación de la cabeza

    public Tanque() {
        // Inicializar matrices a la identidad
        Matrix.setIdentityM(cuerpoMatriz, 0);
        Matrix.setIdentityM(cabezaMatriz, 0);
        Matrix.setIdentityM(cañonMatriz, 0);
    }

    // Método para mover el cuerpo (avanzar)
    public void moverCuerpo(float distancia) {
        Matrix.translateM(cuerpoMatriz, 0, 0, distancia, 0);
    }

    // Método para rotar el cuerpo
    public void rotarCuerpo(float angulo) {
        anguloCuerpo += angulo;
        Matrix.setIdentityM(cuerpoMatriz, 0);
        Matrix.rotateM(cuerpoMatriz, 0, anguloCuerpo, 0, 1, 0);
    }

    // Método para rotar la cabeza (y el cañón) independientemente del cuerpo
    public void rotarCabeza(float angulo) {
        anguloCabeza += angulo;
        Matrix.setIdentityM(cabezaMatriz, 0);
        Matrix.rotateM(cabezaMatriz, 0, anguloCabeza, 0, 1, 0);

        // El cañón se moverá junto con la cabeza
        Matrix.setIdentityM(cañonMatriz, 0);
        Matrix.rotateM(cañonMatriz, 0, anguloCabeza, 0, 1, 0);
    }

    // Método para dibujar el tanque
    public void dibujar() {
        // Primero, dibujar el cuerpo
        // Usar la matriz de transformación del cuerpo
        dibujarCuerpo(cuerpoMatriz);

        // Luego, dibujar la cabeza, que está sobre el cuerpo
        // Usar la matriz del cuerpo y la matriz de la cabeza
        float[] matrizFinalCabeza = new float[16];
        System.arraycopy(cuerpoMatriz, 0, matrizFinalCabeza, 0, 16);
        Matrix.multiplyMM(matrizFinalCabeza, 0, matrizFinalCabeza, 0, cabezaMatriz, 0);
        dibujarCabeza(matrizFinalCabeza);

        // Finalmente, dibujar el cañón, que se mueve con la cabeza
        float[] matrizFinalCañon = new float[16];
        System.arraycopy(matrizFinalCabeza, 0, matrizFinalCañon, 0, 16);
        Matrix.multiplyMM(matrizFinalCañon, 0, matrizFinalCañon, 0, cañonMatriz, 0);
        dibujarCañon(matrizFinalCañon);
    }

    // Métodos de dibujo (simulados aquí, pero en la práctica usarías OpenGL para dibujar los objetos)
    private void dibujarCuerpo(float[] matriz) {
        // Aquí dibujarías el cuerpo del tanque usando la matriz de transformación
    }

    private void dibujarCabeza(float[] matriz) {
        // Aquí dibujarías la cabeza del tanque usando la matriz de transformación
    }

    private void dibujarCañon(float[] matriz) {
        // Aquí dibujarías el cañón del tanque usando la matriz de transformación
    }
}
