package Proyecto;

import java.util.ArrayList;

public class Tablero {
	private int dimTablero;
	private int[] posicionDamas;
	private boolean casillaFija = false;
	private boolean nValido = true;
	int cont = 0;
	
	public Tablero(int dimension) {
		dimTablero = dimension;
		posicionDamas= new int[dimension];
		for(int i = 0; i<dimension; i++) {
			posicionDamas[i] = -1;
		}
	}
	
	public void setCasillaFija(int colInicial) {
		casillaFija=true;
		posicionDamas[0]=colInicial;
	}
	
	public int[][] inicializarPrimera() {
		return primeraSolucionDamas(casillaFija?1:0,0);
	}
	
	public int[][] primeraSolucionDamas(int fila, int colI) {
		while(fila>=(casillaFija?1:0) && fila<dimTablero && validarFila(fila,colI)) {
			fila++;
			colI = 0;
		}
		if (fila==dimTablero) {
			return imprimirTablero();
		}
		fila--;
		if (fila <= (casillaFija?0:-1)) {
			if(casillaFija) {
				System.out.println("Casilla o N no válida");
				return null;
			} else {
				System.out.println("N no válida");
				return null;
			}
		}
		int temp = posicionDamas[fila];
		posicionDamas[fila]=-1;
		return primeraSolucionDamas(fila, temp+1);
	}
	
	public ArrayList<int[][]> inicializarTodas() {
		ArrayList<int[][]> soluciones = new ArrayList<int[][]>();
		return encontrarSoluciones(0, soluciones);
	}
	
	private ArrayList<int[][]> encontrarSoluciones(int fila, ArrayList<int[][]> soluciones) {
		if (fila == dimTablero) {
			++cont;
			soluciones.add(imprimirTablero());
		}
		
		for (int columna = 0; columna < dimTablero; columna++) {
				if (validarColumna(columna, fila) && validarDiagonal(columna, fila)) {
				posicionDamas[fila] = columna;
				encontrarSoluciones(fila + 1, soluciones);
				posicionDamas[fila] = -1;
			}
		}
		return soluciones;
	}
	
	public boolean validarFila(int fila, int columna) {
		while(columna<dimTablero && (!validarColumna(columna, fila) || !validarDiagonal(columna, fila))) {
			columna++;
		}
		if (columna==dimTablero) {
			posicionDamas[fila]=-1;
			return false;
		}
		posicionDamas[fila]=columna;
		return true;
	}
	
	public boolean validarColumna(int columna, int fila) {
		int i = 0;
		while (i<fila && posicionDamas[i]!=columna) {
			i++;
		}	
		if(posicionDamas[i]==columna) return false;
		return true;
	}
	
	public boolean validarDiagonal(int columna, int fila) {
		int j = 0;
		while (j<fila && fila-j!=Math.abs(columna-posicionDamas[j])) {
			j++;
		}
		if(fila-j==Math.abs(columna-posicionDamas[j])) return false;
		return true;
	}
	
	public int[][] imprimirTablero() {
		int[][] tablero = new int[dimTablero][dimTablero];
		for(int i = 0; i<dimTablero; i++) {
			for (int j = 0; j<dimTablero; j++) {
				if (j==posicionDamas[i]) {
					tablero[i][j]=1;
				} else tablero[i][j]=0;
			}
		}
		return tablero; 
	}
	public static void main(String[] args) {
		Tablero tablero = new Tablero(4);
		tablero.inicializarPrimera();
	}
}
