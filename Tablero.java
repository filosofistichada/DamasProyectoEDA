package Proyecto;

import java.util.ArrayList;

public class Tablero {
	// --- VARIABLES GLOBALES ---
	private int dimTablero; // Tamaño del tablero (N x N)
	
	// Arreglo unidimensional que representa el tablero. 
	// CLAVE PARA EL EQUIPO: El índice del arreglo representa la FILA.
	// El valor guardado en ese índice representa la COLUMNA donde está la dama.
	// Ejemplo: posicionDamas[0] = 2 significa "En la fila 0, hay una dama en la columna 2".
	private int[] posicionDamas; 
	
	// Bandera para saber si el usuario fijó la primera dama manualmente
	private boolean casillaFija = false; 
	int cont = 0; // Contador general de soluciones (opcional/estadístico)
	
	
	// --- CONSTRUCTOR ---
	public Tablero(int dimension) {
		dimTablero = dimension;
		posicionDamas = new int[dimension];
		
		// Inicializamos todas las filas en -1, lo que significa "fila vacía" (sin damas asignadas)
		for(int i = 0; i < dimension; i++) {
			posicionDamas[i] = -1;
		}
	}
	
	// Permite obligar al algoritmo a iniciar con una dama en la fila 0, columna específica
	public void setCasillaFija(int colInicial) {
		casillaFija = true;
		posicionDamas[0] = colInicial;
	}
	
	// --- MÉTODOS PARA LA PRIMERA SOLUCIÓN ---
	
	// Método "envoltorio" (wrapper) para iniciar la búsqueda de manera limpia
	public int[][] inicializarPrimera() {
		// Si la casilla está fija, empezamos a buscar desde la fila 1. Si no, desde la fila 0.
		// En ambos casos empezamos a evaluar desde la columna 0.
		return primeraSolucionDamas(casillaFija ? 1 : 0, 0);
	}
	
	// Método recursivo/iterativo que encuentra la primera solución válida
	public int[][] primeraSolucionDamas(int fila, int colI) {
		
		// 1. AVANCE: Mientras estemos en una fila válida y la fila actual encuentre una columna segura
		while(fila >= (casillaFija ? 1 : 0) && fila < dimTablero && validarFila(fila, colI)) {
			fila++;     // Pasamos a la siguiente fila
			colI = 0;   // Para la nueva fila, siempre intentamos desde la columna 0
		}
		
		// 2. ÉXITO: Si logramos pasar la última fila, encontramos la solución
		if (fila == dimTablero) {
			return imprimirTablero(); // Transformamos el arreglo a matriz y terminamos
		}
		
		// 3. BACKTRACKING (Retroceso): Si el ciclo while se rompió antes de llegar a la meta,
		// significa que la fila actual no tiene ninguna columna válida. Debemos retroceder a la fila anterior.
		fila--;
		
		// 4. FALLO TOTAL: Si retrocedemos más allá de la fila inicial permitida, el problema no tiene solución.
		if (fila <= (casillaFija ? 0 : -1)) {
			if(casillaFija) {
				System.out.println("Casilla o N no válida");
				return null;
			} else {
				System.out.println("N no válida");
				return null;
			}
		}
		
		// 5. PREPARAR EL SIGUIENTE INTENTO:
		// Guardamos la columna de la dama en la fila a la que retrocedimos
		int temp = posicionDamas[fila]; 
		// "Levantamos" la dama del tablero (reiniciamos la fila)
		posicionDamas[fila] = -1;
		// Llamamos a la recursividad: intentamos en la MISMA fila a la que retrocedimos, 
		// pero a partir de la SIGUIENTE columna (temp + 1)
		return primeraSolucionDamas(fila, temp + 1);
	}

	
	// --- MÉTODOS PARA TODAS LAS SOLUCIONES ---
	
	public ArrayList<int[][]> inicializarTodas() {
		ArrayList<int[][]> soluciones = new ArrayList<int[][]>();
		return todasLasSoluciones(casillaFija ? 1 : 0, 0, soluciones);
	}

	public ArrayList<int[][]> todasLasSoluciones(int fila, int colI, ArrayList<int[][]> soluciones) {
		
		// 1. AVANCE: Mismo funcionamiento que la primera solución
		while(fila >= (casillaFija ? 1 : 0) && fila < dimTablero && validarFila(fila, colI)) {
			fila++;
			colI = 0;
		}
		
		// 2. ÉXITO (DIFERENCIA CLAVE): Encontramos una solución, pero NO nos detenemos.
		if (fila == dimTablero) {
			soluciones.add(imprimirTablero()); // Guardamos una "foto" de la solución actual en la lista
			
			// Forzamos el retroceso (como si hubiéramos fallado) para buscar la siguiente variante
			fila--; 
			int temp = posicionDamas[fila];
			posicionDamas[fila] = -1;
			return todasLasSoluciones(fila, temp + 1, soluciones);
		}
		
		// 3. BACKTRACKING: Callejón sin salida, retrocedemos una fila
		fila--;
		
		// 4. PARADA TOTAL: Ya exploramos todas las ramas posibles del árbol de combinaciones
		if (fila <= (casillaFija ? 0 : -1)) {
			return soluciones; // Retornamos la lista con las soluciones recopiladas
		}
		
		// 5. PREPARAR SIGUIENTE INTENTO
		int temp = posicionDamas[fila];
		posicionDamas[fila] = -1;
		return todasLasSoluciones(fila, temp + 1, soluciones);
	}
	
	
	// --- MÉTODOS DE VALIDACIÓN (REGLAS DEL JUEGO) ---
	
	// Busca dentro de una fila cuál es la primera columna disponible que no ataque a las demás
	public boolean validarFila(int fila, int columna) {
		// Mientras no nos salgamos del tablero, revisamos si la columna actual choca vertical o diagonalmente
		while(columna < dimTablero && (!validarColumna(columna, fila) || !validarDiagonal(columna, fila))) {
			columna++; // Si choca, probamos la siguiente columna
		}
		
		// Si se nos acabaron las columnas, significa que no hay espacio seguro en esta fila
		if (columna == dimTablero) {
			// Limpiamos el estado de la fila actual (sin dama) por seguridad.
			// Esto evita dejar datos residuales ("basura") que puedan afectar 
			// las validaciones futuras al hacer backtracking.
			posicionDamas[fila] = -1; 
			return false;
		}
		
		// Si encontramos una columna segura, colocamos la dama ahí y reportamos éxito
		posicionDamas[fila] = columna;
		return true;
	}
	
	// Verifica que no haya otra dama en la misma columna en las filas anteriores
	public boolean validarColumna(int columna, int fila) {
		int i = 0;
		// Recorremos las filas desde arriba (0) hasta justo antes de la fila actual
		while (i < fila && posicionDamas[i] != columna) {
			i++;
		}	
		// Si el ciclo se detuvo porque encontró una dama en la misma columna, es inválido
		if(posicionDamas[i] == columna) return false;
		return true;
	}
	
	// Verifica que no haya otra dama en la misma diagonal en las filas anteriores
	public boolean validarDiagonal(int columna, int fila) {
		int j = 0;
		// REGLA MATEMÁTICA DE LA DIAGONAL:
		// Dos casillas están en la misma diagonal si la distancia entre sus filas 
		// es exactamente igual a la distancia absoluta entre sus columnas.
		while (j < fila && (fila - j) != Math.abs(columna - posicionDamas[j])) {
			j++;
		}
		// Si la distancia en X es igual a la distancia en Y, hay choque diagonal
		if((fila - j) == Math.abs(columna - posicionDamas[j])) return false;
		return true;
	}

	
	// --- UTILIDADES ---
	
	// Convierte nuestra representación unidimensional de (índice=fila, valor=columna)
	// a una matriz visual 2D tradicional llena de 0s y 1s.
	public int[][] imprimirTablero() {
		int[][] tablero = new int[dimTablero][dimTablero];
		
		for(int i = 0; i < dimTablero; i++) {
			for (int j = 0; j < dimTablero; j++) {
				// Si la columna actual 'j' coincide con la guardada en posicionDamas para la fila 'i'
				if (j == posicionDamas[i]) {
					tablero[i][j] = 1; // Ponemos la dama
				} else {
					tablero[i][j] = 0; // Casilla vacía
				}
			}
		}
		return tablero; 
	}
}
