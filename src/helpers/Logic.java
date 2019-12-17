package helpers;

public class Logic {

    public static double a1, b1;
    public static double a2, b2, c2;
    public static double lnA3, b3;

    //Линейная регрессия
    public static void mnk(double[] x, double[] y) {
        int n = x.length;

        double sumX = 0;
        double sumXSquare = 0;
        double sumY = 0;
        double sumXY = 0;
        for (int i = 0; i < x.length; i++) {
            sumX += x[i];
            sumY += y[i];
            sumXSquare += Math.pow(x[i], 2);
            sumXY += x[i] * y[i];
        }
        a1 = (sumXY - sumX * sumY / n) / (sumXSquare - sumX * sumX / n);
        b1 = sumY / n - a1 * sumX / n;

    }

    //Подсчёт значений
    public static double linearFun(double x) {
        return a1 * x + b1;
    }

    //Нелинейная регрессия. полином 2й степени
    public static void polynom2nd(double[] x, double[] y) {
        int n = x.length;
        double det;
        double sumX4 = 0, sumX3 = 0, sumX2 = 0, sumX = 0, sumX2Y = 0, sumXY = 0, sumY = 0;

        for (int i = 0; i < n; i++) {
            sumX4 += Math.pow(x[i], 4);
            sumX3 += Math.pow(x[i], 3);
            sumX2 += Math.pow(x[i], 2);
            sumX += x[i];
            sumY += y[i];
            sumX2Y += Math.pow(x[i], 2) * y[i];
            sumXY += x[i] * y[i];
        }
        //Определитель
        det = sumX4 * sumX2 * n + 2 * sumX3 * sumX * sumX2 -
                3 * sumX2 - sumX4 * 2 * sumX - 2 * sumX3 * n;

        //Формулы Крамера
        a2 = (sumX2Y * sumX2 * n + sumXY * sumX * sumX2 + sumX3 * sumX * sumY -
                2 * sumX2 * sumY - 2 * sumX * sumX2Y - n * sumX3 * sumXY) / det;
        b2 = (sumX4 * sumXY * n + sumX2Y * sumX * sumX2 + sumX3 * sumX2 * sumY -
                sumXY * 2 * sumX2 - sumX4 * sumY * sumX - sumX2Y * sumX3 * n) / det;
        c2 = (sumX4 * sumX2 * sumY + sumX3 * sumXY * sumX2 + sumX3 * sumX * sumX2Y -
                sumX2Y * 2 * sumX2 - sumX4 * sumXY * sumX - 2 * sumX3 * sumY) / det;
    }

    //Подсчёт значений
    public static double nonlinearFun(double x) {
        return a2 * x * x + b2 * x + c2;
    }

    //Нелинейная регрессия. Экспоненциальная аппрокимация
    public static void exponential(double[] x, double[] y) {
        int n = x.length;
        double sumLnY = 0, sumX = 0, sumLnYX = 0, sumX2 = 0;

        for (int i = 0; i < n; i++) {
            sumLnY += Math.log(y[i]);
            sumX += x[i];
            sumX2 += Math.pow(x[i], 2);
            sumLnYX += Math.log(y[i]) * x[i];
        }

        lnA3 = (sumLnY * sumX2 - sumLnYX * sumX) / (n * sumX2 - sumX * sumX);
        b3 = (sumLnYX * n - sumLnY * sumX) / (n * sumX2 - sumX * sumX);
    }

    //Подсчёт значений
    public static double expFun(double x) {
        return Math.exp(lnA3) * Math.exp(b3 * x);
    }
}
