import java.util.Scanner;

public class Main {
    // метод для ввода одной матрицы 
    private static ComplexMatrix inputMatrix(Scanner scanner, String name) {
        System.out.println("\n--- Заполнение матрицы " + name + " ---");
        System.out.print("Введите количество строк: ");
        int r = scanner.nextInt();
        System.out.print("Введите количество столбцов: ");
        int c = scanner.nextInt();

        ComplexMatrix newMatrix = new ComplexMatrix(r, c);

        System.out.println("Вводите элементы (действительную и мнимую части) через пробел, например: 2,5 -9:");
        for (int i = 1; i < r+1; i++) {
            for (int j = 1; j < c+1; j++) {
                System.out.print(name + "[" + i + "][" + j + "]: ");
                double re = scanner.nextDouble();
                double im = scanner.nextDouble();
                newMatrix.setElement(i-1, j-1, new Complex(re, im));
            }
        }
        if (scanner.hasNextLine()) { // для корректного ввода следующей операции
            scanner.nextLine();
        }
        System.out.println("Матрица " + name + " успешно сохранена.");
        return newMatrix;
    }

    // метод контроля, чтобы не вызвать метод у null
    private static void checkMatrixsExist(ComplexMatrix a, ComplexMatrix b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Для выполнения операции должны быть созданы обе матрицы (А и В)");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ComplexMatrix matrixA = null;
        ComplexMatrix matrixB = null;

        while (true) {
            System.out.println("--- Меню для работы с матрицами ---");
            System.out.println("1. Задать/Перезаписать целиком матрицу А");
            System.out.println("2. Задать/Перезаписать целиком матрицу B");
            System.out.println("3. Вывести обе матрицы на экран");
            System.out.println("4. Сложение");
            System.out.println("5. Вычитание");
            System.out.println("6. Умножение");
            System.out.println("7. Деление (А / В = A * B^(-1))");
            System.out.println("8. Посчитать детерминант матрицы");
            System.out.println("9. Транспонировать матрицу");
            System.out.println("10. Редактировать один элемент одной матрицы");
            System.out.println("0. Выйти из программы");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();

            if (choice == 0) {
                System.out.println("Программа успешно завершена");
                break;
            }

            try {
                switch (choice) {
                    case 1:
                        matrixA = inputMatrix(scanner, "A");
                        break;
                    case 2:
                        matrixB = inputMatrix(scanner, "B");
                        break;
                    case 3:
                        System.out.println("Матрица А:");
                        if (matrixA != null) {
                            matrixA.printMatrix();
                            System.out.println(); // перевод строки на следующую
                        }
                        else {
                            System.out.println("Вы не задали матрицу А");
                        }
                        System.out.println("Матрица В:");
                        if (matrixB != null) {
                            matrixB.printMatrix();
                            System.out.println();
                        }
                        else {
                            System.out.println("Вы не задали матрицу В");
                        }
                        break;
                    case 4:
                        checkMatrixsExist(matrixA, matrixB);
                        System.out.println("--- Результат А + В ---");
                        matrixA.plus(matrixB).printMatrix();
                        break;
                    case 5:
                        checkMatrixsExist(matrixA, matrixB);
                        System.out.print("Выберите направление вычитания (1: A - B, 2: B - A): ");
                        int subChoice = scanner.nextInt();
                        if (subChoice == 1) {
                            System.out.println("--- Результат А - В ---");
                            matrixA.minus(matrixB).printMatrix();
                        } else if (subChoice == 2) {
                            System.out.println("--- Результат В - А ---");
                            matrixB.minus(matrixA).printMatrix();
                        } else {
                            System.out.println("Неверный выбор направления");
                        }
                        break;

                    case 6:
                        checkMatrixsExist(matrixA, matrixB);
                        System.out.print("Выберите направление умножения (1: A * B, 2: B * A): ");
                        int subChoice1 = scanner.nextInt();
                        if (subChoice1 == 1) {
                            System.out.println("--- Результат А * В ---");
                            matrixA.multiply(matrixB).printMatrix();
                        } else if (subChoice1 == 2) {
                            System.out.println("--- Результат В * А ---");
                            matrixB.multiply(matrixA).printMatrix();
                        } else {
                            System.out.println("Неверный выбор направления");
                        }
                        break;
                    case 7:
                        checkMatrixsExist(matrixA, matrixB);
                        System.out.print("Выберите направление деления (1: A / B, 2: B / A): ");
                        int subChoice2 = scanner.nextInt();
                        if (subChoice2 == 1) {
                            System.out.println("--- Результат А / В ---");
                            matrixA.divide(matrixB).printMatrix();
                        } else if (subChoice2 == 2) {
                            System.out.println("--- Результат В / А ---");
                            matrixB.divide(matrixA).printMatrix();
                        } else {
                            System.out.println("Неверный выбор направления");
                        }
                        break;
                    case 8:
                        System.out.print("Выберите матрицу, для которой будет посчитан детерминант (1: A, 2: B): ");
                        int subChoice3 = scanner.nextInt();
                        if (subChoice3 == 1) {
                            if (matrixA == null) throw new IllegalArgumentException("Сначала создайте матрицу А");
                            System.out.print("Детерминант матрицы А равен: ");
                            matrixA.determinant().print();
                            System.out.println(); // перевод строки после своего print() для complex чисел
                        } else if (subChoice3 == 2) {
                            if (matrixB == null) throw new IllegalArgumentException("Сначала создайте матрицу B");
                            System.out.print("Детерминант матрицы B равен: ");
                            matrixB.determinant().print();
                            System.out.println();
                        } else {
                            System.out.println("Такого варианта нет");
                        }
                        break;
                    case 9:
                        System.out.print("Выберите матрицу, которая будет транспонирована (1: A, 2: B): ");
                        int subChoice4 = scanner.nextInt();
                        if (subChoice4 == 1) {
                            if (matrixA == null) throw new IllegalArgumentException("Сначала создайте матрицу А");
                            System.out.println("Матрица А успешно транспонирована:");
                            matrixA = matrixA.transposedMatrix(); // перезаписываем матрицу А её новой версией
                            matrixA.printMatrix();
                        } else if (subChoice4 == 2) {
                            if (matrixB == null) throw new IllegalArgumentException("Сначала создайте матрицу В");
                            System.out.println("Матрица В успешно транспонирована:");
                            matrixB = matrixB.transposedMatrix(); // перезаписываем матрицу B её новой версией
                            matrixB.printMatrix();
                        }
                        break;
                    case 10:
                        System.out.print("Выберите матрицу для редактирования (1: А, 2: В): ");
                        int target = scanner.nextInt();
                        ComplexMatrix currentMatrix = (target == 1) ? matrixA : matrixB;
                        if (currentMatrix == null) {
                            System.out.println("Выбранная матрица еще не создана");
                            break;
                        }
                        System.out.println("Текущий вид матрицы:");
                        currentMatrix.printMatrix();

                        System.out.print("Введите номер строки элемента, который хотите поменять: ");
                        int editRow = scanner.nextInt();
                        System.out.print("Введите номер столбца элемента, который хотите поменять: ");
                        int editCol = scanner.nextInt();

                        System.out.println("Введите новые значения:");
                        System.out.print("Действительная часть (Re): ");
                        double newRe = scanner.nextDouble();
                        System.out.print("Мнимая часть (Im): ");
                        double newIm = scanner.nextDouble();

                        currentMatrix.changeElement(editRow, editCol, new Complex(newRe, newIm)); // замена элемента
                        if (scanner.hasNextLine()) { // для корректного ввода следующей операции
                            scanner.nextLine();
                        }
                        System.out.println("Элемент успешно изменен");
                        currentMatrix.printMatrix();
                        break;
                    default:
                        System.out.println("Нет такого пункта меню. Попробуйте снова.");
                }
            } catch (ArithmeticException e) {
                System.out.println("Математическая ошибка: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Некорректные аргументы: " + e.getMessage());
            } catch (java.util.InputMismatchException e) {
                System.out.println("Ошибка ввода: Вы ввели буквы вместо чисел или был использован неверный формат");
                if (scanner.hasNextLine()) { // чтобы не бралось число из буфера и следующий ввод корректно работал
                    scanner.nextLine();
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
            }
        }

        scanner.close();
    }
}
