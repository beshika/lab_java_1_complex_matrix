public class Complex {
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

    public void print() {
        // округление числа до 1 знака после запятой
        String formattedRe = String.format("%.1f", this.re);
        String formattedIm = String.format("%.1f", Math.abs(this.im)); // здесь модуль для красивого вывода знака "-" у комплексного числа

        if (this.im >= 0) {
            System.out.print(formattedRe + " + " + formattedIm + "i");
        } else {
            System.out.print(formattedRe + " - " + formattedIm + "i");
        }
    }
}
