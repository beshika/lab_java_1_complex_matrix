class Complex {
    private double re;
    private double im;
    public Complex (double x, double y) {
        this.re = x;
        this.im = y;
    }
    public Complex(double x) {
        this(x,0);
    }
    public Complex() {
        this(0,0);
    }
    public double getRe() {return re;}
    public double getIm() {return im;}
    public void setIm(double im) {
        this.im = im;
    }
    public void setRe(double re) {
        this.re = re;
    }
    public Complex plus(Complex other) {
        return new Complex(this.re+other.re, this.im+other.im);
    }
    public Complex minus(Complex other) {
        return new Complex(this.re-other.re, this.im-other.im);
    }
    // z1*z2 = (a + bi)*(c + di) = ac + bci + adi - bd = ac - bd + (bc - ad)i
    public Complex multiply(Complex other) {
        double newRe = this.re*other.re - this.im*other.im;
        double newIm = this.im*other.re + this.re*other.im;
        return new Complex(newRe, newIm);
    }
    /* z1 = a + bi; z2 = x + yi
    * z1/z2 = [домножить числитель и знаменатель на сопряженное к z2 = x - yi]
    * = (a + bi)(x - yi)/(x^2+y^2) = [где (x^2+y^2) - действительное число] =
    * = ((ax + by) + (bx - ay)i)/(x^2+y^2)*/
    public Complex divide(Complex other) {
        double denominator = other.re*other.re + other.im*other.im;
        if (denominator == 0) {
            throw new ArithmeticException("Деление на ноль запрещено");
        }
        double newRe = (this.re*other.re + this.im*other.im) / denominator;
        double newIm = (this.im*other.re - this.re*other.im) / denominator;
        return new Complex(newRe,newIm);
    }
}

class MatrixSizeMismatchException extends RuntimeException {
    public MatrixSizeMismatchException(String message) {
        super(message);
    }
}

class ComplexMatrix {
    final private int rows;
    final private int cols;
    final private Complex[][] matrix;

    public ComplexMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.matrix = new Complex[rows][cols];
    }
    public void setElement(int row, int col, Complex val) {
        this.matrix[row][col] = val;
    }

    public ComplexMatrix getMinor(int minor_row, int minor_col) {
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
            throw new MatrixSizeMismatchException("Нельзя посчитать детерминант, т.к матрица не квадратная");
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
            ComplexMatrix minor = getMinor(0,j); // матрица без 1ой строки и jого столбца
            Complex minorDet = minor.determinant(); // определитель матрицы без 1ой строки и jого столбца
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
            throw new MatrixSizeMismatchException("Нельзя сложить матрицы разного размера");
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
            throw new MatrixSizeMismatchException("Нельзя вычитать матрицы разного размера");
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
            throw new MatrixSizeMismatchException("Нельзя умножать матрицу с "+this.cols+" столбцами на матрицу с "+other.rows+" строчками");
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


}
