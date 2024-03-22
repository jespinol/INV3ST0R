package com.jmel.inv3st0r.service;

import org.springframework.stereotype.Service;
import com.jmel.inv3st0r.model.Account;

import java.awt.*;
import java.util.ArrayList;

@Service
public class PieChartService {
    private ArrayList<String> labels;
    private ArrayList<Double> balances;
    private ArrayList<String> colorsPrimary;
    private ArrayList<String> colorsSecondary;

    public PieChartService(ArrayList<Account> accounts) {
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Double> balances = new ArrayList<>();
        double cashBalance = 0;

        for (Account account : accounts) {
            labels.add(account.getAccountName());
            balances.add(account.getInvestedBalance());
            cashBalance += account.getCashBalance();
        }

        if (cashBalance > 0) {
            labels.add("Cash");
            balances.add(cashBalance);
        }

        setLabels(labels);
        setBalances(balances);
        setColorsPrimary(labels.size());
        setColorsSecondary(getColorsPrimary());
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }

    public ArrayList<Double> getBalances() {
        return balances;
    }

    public void setBalances(ArrayList<Double> balances) {
        this.balances = balances;
    }

    public ArrayList<String> getColorsPrimary() {
        return colorsPrimary;
    }

    public void setColorsPrimary(int size) {
        ArrayList<String> colorsPrimary = new ArrayList<>();
        String[] baseColors = {
                "#5672D8", // Blue
                "#4CAF50", // Green
                "#FFEB3B", // Yellow
                "#FF9800", // Orange
                "#FF6655", // Red
                "#9C27B0", // Purple
        };

        for (int i = 0; i < size; i++) {
            colorsPrimary.add(baseColors[i % baseColors.length]);
        }

        if (size > 0) {
            colorsPrimary.set(size - 1, "#5A5C66"); // dark gray
        }

        this.colorsPrimary = colorsPrimary;
    }

    public ArrayList<String> getColorsSecondary() {
        return colorsSecondary;
    }

    public void setColorsSecondary(ArrayList<String> colorsPrimary) {
        ArrayList<String> colorsSecondary = new ArrayList<>();

        for (String color : colorsPrimary) {
            Color original = Color.decode(color);
            int r = (int) Math.min(255, original.getRed() + (255 - original.getRed()) * 0.30);
            int g = (int) Math.min(255, original.getGreen() + (255 - original.getGreen()) * 0.30);
            int b = (int) Math.min(255, original.getBlue() + (255 - original.getBlue()) * 0.30);
            Color lighter = new Color(r, g, b);
            String lighterHex = "#" + Integer.toHexString(lighter.getRGB()).substring(2).toUpperCase();
            colorsSecondary.add(lighterHex);
        }

        this.colorsSecondary = colorsSecondary;
    }
}
