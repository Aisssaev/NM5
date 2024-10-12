package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final File file = new File("output.txt");

    public static double f(double x) {
        return Math.sin(x);
    }

    public static double fp(double x) {
        return Math.cos(x);
    }

    public static double fn(double x, double h) {
        return (f(x + h) - f(x - h)) / (2.0 * h);
    }

    public static double eps1(double x, double h) {
        return Math.abs(fn(x, h) - fp(x));
    }

    public static double eps2(double x, double h) {
        return Math.abs(fp(x) - (fn(x, h) + (fn(x, h) - fn(x, 2.0 * h)) / 3.0));
    }

    public static double eps3(double x, double h) {
        return Math.abs(fp(x) - (fn(x, 2.0 * h) * fn(x, 2.0 * h) - fn(x, h) * fn(x, 4 * h)) / (2.0 * fn(x, 2.0 * h) - (fn(x, h) + fn(x, 4.0 * h))));
    }

    public static void main(String[] args) throws IOException {
        double x0 = 0.56, yp0 = 0.0, h = 1e-4, h0 = 0, h1, R0, R1, R2, R3;
        StringBuilder outputText = new StringBuilder();

        double min = 10;

        for (int i = 20; i >= -3; i--) {
            h = Math.pow(10, -i);
            outputText.append(String.format("%e\t%f%n", h, eps1(x0, h)));
            if (eps1(x0, h) < min) {
                min = eps1(x0, h);
                h0 = h;
            }
        }

        int por = (int) Math.log10(h0);
        double hh = 1;

        for (int i = 1; i <= Math.abs(por - 1); i++) {
            hh = hh / 10.0;
        }

        for (h = hh; h <= 100.0 * hh; h = h + hh) {
            outputText.append(h).append("\t").append(eps1(x0, h)).append(System.lineSeparator());
            if (eps1(x0, h) < min){
                min = eps1(x0, h);
                h0 = h;
            }
        }

        yp0 = fp(x0);
        h1 = 1e-3;
        R0 = eps1(x0, h0);
        R1 = eps1(x0, h1);
        R2 = eps2(x0, h1);
        R3 = eps3(x0, h1);
        double pmet = p(x0, h1);

        outputText.append("y_pohidna0=").append(yp0).append(System.lineSeparator());
        outputText.append("h0=").append(h0).append(System.lineSeparator());
        outputText.append("h1=").append(h1).append(System.lineSeparator());
        outputText.append("R_for_h0=").append(R0).append(System.lineSeparator());
        outputText.append("R_for_h1=").append(R1).append(System.lineSeparator());
        outputText.append("R_Runge_Romberg=").append(R2).append(System.lineSeparator());
        outputText.append("R_Aitken=").append(R3).append(System.lineSeparator());
        outputText.append("p=").append(pmet).append(System.lineSeparator());

        Files.write(Paths.get("output.txt"), outputText.toString().getBytes());
        System.out.println(outputText);
    }

    static double p(double x, double h) {
        return 1 / Math.log(2) * Math.log((fn(x, 4.0 * h) - fn(x, 2.0 * h)) / (fn(x, 2.0 * h) - fn(x, h)));
    }

}