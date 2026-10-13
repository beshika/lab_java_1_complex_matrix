public class ComplexMatrix {
    final private int rows;
    final private int cols;
    final private Complex[][] matrix;

    public ComplexMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.matrix = new Complex[rows][cols];
    }
    public Complex getElement(int row, int col) {
        return this.matrix[row][col];
    }
    public void setElement(int row, int col, Complex val) {
        this.matrix[row][col] = val;
    }
    public void changeElement(int userRow, int userCol, Complex newValue) {
        // перевод из пользовательских координат (с 1) в программные (с 0)
        int r = userRow - 1;
        int c = userCol - 1;
        // проверка, входят ли индексы в границы матрицы
        if (r < 0 || r >= this.rows || c < 0 || c >= this.cols) {
            throw new IllegalArgumentException("Ячейки с координатами [" + userRow + "][" + userCol + "] не существует!");
        }
        this.setElement(r, c, newValue);
    }

    public ComplexMatrix getMinorMatrix(int minor_row, int minor_col) { // метод для получения матрицы без iой строки и jого столбца
        ComplexMatrix minorMatrix = new ComplexMatrix(this.rows-1, this.cols-1);
        for (int i = 0; i < this.rows; i++) {
            if (i == minor_row) {continue;}
            for (int j = 0; j < this.cols; j++) {
                if (j == minor_col) {continue;}
                // учитываем строку и столбец, которые вычеркнули, чтобы правильно сместить индексы
                if ((j > minor_col) && (i < minor_row)) {
                    minorMatrix.setElement(i,j-1,this.matrix[i][j]);
                }
                if ((j > minor_col) && (i > minor_row)) {
                    minorMatrix.setElement(i-1,j-1,this.matrix[i][j]);
                }
                if ((j < minor_col) && (i < minor_row)) {
                    minorMatrix.setElement(i,j,this.matrix[i][j]);
                }
                if ((j < minor_col) && (i > minor_row)) {
                    minorMatrix.setElement(i-1,j,this.matrix[i][j]);
                }
            }
        }
        return minorMatrix;
    }
    public Complex determinant() {
        if (this.rows != this.cols){
            throw new IllegalArgumentException("Нельзя посчитать детерминант, т.к матрица не квадратная");
        }
        if (this.rows == 1){
            return this.matrix[0][0];
        }
        /* A = (a b)
         *      (c d)   det A = ad - bc  */
        if (this.rows == 2) {
            Complex ad = this.matrix[0][0].multiply(this.matrix[1][1]);
            Complex bc = this.matrix[0][1].multiply(this.matrix[1][0]);
            return (ad.minus(bc));
        }
        Complex det = new Complex();
        for (int j = 0; j < this.cols; j++){ // получаем миноры по первой строке
            ComplexMatrix minorMatrix = getMinorMatrix(0,j); // матрица без 1ой строки и jого столбца
            Complex minorDet = minorMatrix.determinant(); // определитель матрицы без 1ой строки и jого столбца
            Complex t = this.matrix[0][j].multiply(minorDet);
            if (j%2==0) {
                det = det.plus(t); // для j = 0 (1 эл-т 1ой строки) должен быть знак +
            }
            else {
                det = det.minus(t);
            }
        }
        return det;
    }
    public ComplexMatrix transposedMatrix() {
        /* Элементу аij в исходной матрице А соответствует
        элемент aji в транспонированной матрице Ат */
        ComplexMatrix transposedMatrix = new ComplexMatrix(this.cols,this.rows);
        for (int i = 0; i < this.cols; i++) {
            for (int j = 0; j < this.rows; j++) {
                transposedMatrix.setElement(i, j, this.matrix[j][i]);
            }
        }
        return transposedMatrix;
    }
    public ComplexMatrix plus(ComplexMatrix other){
        if ((this.rows != other.rows) || (this.cols != other.cols)){
            throw new IllegalArgumentException("Нельзя сложить матрицы разного размера");
        }
        ComplexMatrix sumMatrix = new ComplexMatrix(this.rows,this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                sumMatrix.setElement(i, j, (this.matrix[i][j].plus(other.matrix[i][j])));
            }
        }
        return sumMatrix;
    }
    public ComplexMatrix minus(ComplexMatrix other){
        if ((this.rows != other.rows) || (this.cols != other.cols)){
            throw new IllegalArgumentException("Нельзя вычитать матрицы разного размера");
        }
        ComplexMatrix minusMatrix = new ComplexMatrix(this.rows,this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                minusMatrix.setElement(i, j, (this.matrix[i][j].minus(other.matrix[i][j])));
            }
        }
        return minusMatrix;
    }
    public ComplexMatrix multiply(ComplexMatrix other) {
        if (this.cols != other.rows){
            throw new IllegalArgumentException("Нельзя умножать матрицу с "+this.cols+" столбцами на матрицу с "+other.rows+" строчками");
        }
        ComplexMatrix multiplyMatrix = new ComplexMatrix(this.rows,other.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                Complex value = new Complex();
                for (int l = 0; l < this.cols; l++) {
                    Complex t_value = this.matrix[i][l].multiply(other.matrix[l][j]);
                    value = value.plus(t_value);
                }
                multiplyMatrix.setElement(i,j,value);
            }
        }
        return multiplyMatrix;
    }

    public ComplexMatrix inverseMatrix() {
        if (this.rows != this.cols){
            throw new IllegalArgumentException("Нельзя посчитать обратную матрицу, т.к матрица не квадратная");
        }
        Complex det = this.determinant();
        if (det.getRe() == 0 && det.getIm() == 0) {
            throw new ArithmeticException("Матрица вырождена - детерминант равен 0");
        }
        ComplexMatrix adjugateMatrix = new ComplexMatrix(this.rows,this.cols); // матрица дополнений
        /* В ней каждому элементу aij исходной матрицы соответствует его алгебраическое дополнение (минор умноженный на (-1)^(i+j))*/
        for (int i = 0; i < this.rows; i++){
            for (int j = 0; j < this.cols; j++){
                ComplexMatrix minorMatrix = getMinorMatrix(i,j);
                Complex minorDet = minorMatrix.determinant();
                if ((i+j) % 2 == 1) {
                    minorDet = minorDet.multiply(new Complex(-1));
                }
                adjugateMatrix.setElement(i,j,minorDet);
            }
        }
        ComplexMatrix transposedAdjugateMatrix = adjugateMatrix.transposedMatrix(); // транспонируем матрицу дополнений, получаем союзную матрицу
        ComplexMatrix inverseMatrix = new ComplexMatrix(this.rows,this.cols);
        for (int i = 0; i < this.rows; i++){
            for (int j = 0; j < this.cols; j++){
                Complex newElement = transposedAdjugateMatrix.matrix[i][j].divide(det); // каждый элемент союзной матрицы делим на детерминант исходной матрицы
                inverseMatrix.setElement(i,j,newElement);
            }
        }
        return inverseMatrix;
    }

    public ComplexMatrix divide(ComplexMatrix other){ // деление это умножение на обратную матрицу, а возможность умножения проверяется в методе multiply
        ComplexMatrix inverseOtherMatrix = other.inverseMatrix();
        return this.multiply(inverseOtherMatrix);
    }
    public void printMatrix() {
        for (int i = 0; i < this.rows; i++){
            System.out.print("( ");
            for (int j = 0; j < this.cols; j++) {
                this.matrix[i][j].print();
                System.out.print("   ");
            }
            System.out.print(" )");
            System.out.println();
        }
    }
}
