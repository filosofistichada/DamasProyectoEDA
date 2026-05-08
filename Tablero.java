package Backtracking;

public class Tablero {
	private int dimTablero;
	private int[] posicionDamas;
	
	public Tablero(int dimension, int colInicial) {
		dimTablero = dimension;
		posicionDamas= new int[dimension];
		posicionDamas[0] = colInicial;
		for(int i = 1; i<dimension; i++) {
			posicionDamas[i] = -1;
		}
	}
	
	public void ubicarDamas() {
		int i = 0;
		while (i<dimTablero && posicionDamas[i]!=-1) i++;
		if(i == dimTablero) {
			imprimirTablero();
			System.out.println();
		} else if (!validarDama(0, i)) {
			System.out.println("Esa no es una posición válida");
		} else ubicarDamas();
		
	}
	
	public boolean validarDama(int columna, int fila) {
		if (fila<1) return false;
		if (columna < dimTablero && (!validarColumna(columna, fila) || !validarDiagonal(columna, fila))) {
			posicionDamas[fila] = -1;
			return validarDama(columna+1, fila);
		} else if (columna>=dimTablero) {
			posicionDamas[fila] = -1;
			return validarDama(posicionDamas[fila-1] +1, fila-1);
		} else {
			posicionDamas[fila]=columna;
			return true;
		}
	}
	
	public boolean validarColumna(int columna, int fila) {
		int i = 0;
		while (i<fila && posicionDamas[i]!=columna) {
			i++;
		}
		if(i==fila) return true;
		return false;
	}
	
	public boolean validarDiagonal(int columna, int fila) {
		int j = 0;
		while (j<fila && fila-j!=Math.abs(columna-posicionDamas[j])) {
			j++;
		}
		if(j==fila) return true;
		return false;
	}
	
	public void imprimirTablero() {
		for(int i = 0; i<dimTablero; i++) {
			for (int j = 0; j<dimTablero; j++) {
				if (j==posicionDamas[i]) {
					System.out.print(1 + " ");
				} else System.out.print(0 + " ");
			}
			System.out.println();
		}
	}
	public static void main(String[] args) {
		Tablero tablero = new Tablero(6, -1);
		tablero.ubicarDamas();
	}
}
