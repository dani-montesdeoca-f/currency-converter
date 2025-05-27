package com.example;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // Amount
        double amount;
        System.out.println("WELCOME TO DANI'S CURRENCY CONVERTER");
        System.out.println("Input data as requested");
        while (true) {
            System.out.print("Enter amount to convert: ");
            String amtStr = in.nextLine().trim();
            try {
                amount = Double.parseDouble(amtStr);
                if (amount < 0) throw new NumberFormatException("negative");
                break;
            } catch (NumberFormatException e) {
                System.err.println("Invalid number. Please enter a positive decimal (e.g. 12.50).");
            }
        }

        // Origin currency
        String from;
        while (true) {
            System.out.print("Enter source currency code (e.g. USD): ");
            from = in.nextLine().trim().toUpperCase();
            if (from.matches("[A-Z]{3}")) break;
            System.err.println("Must be three letters, like USD or eur.");
        }

        // Target Currency
        String to;
        while (true) {
            System.out.print("Enter target currency code (e.g. MXN): ");
            to = in.nextLine().trim().toUpperCase();
            if (to.matches("[A-Z]{3}")) break;
            System.err.println("Must be three letters, like MXN or jpy.");
        }


        // Perform convesion
        ExchangeRateClient client = new ExchangeRateClient();
        try {
            RateResponse resp = client.getRate(from, to);
            Double rate = resp.rates.get(to);    // now non-null
            double result = amount * rate;
            System.out.printf(
                    "%.2f %s = %.2f %s (1 %s = %.4f %s)%n",
                    amount, from, result, to, from, rate, to
            );
        } catch (Exception e) {
            System.err.println("Error fetching rate: " + e.getMessage());
        }
    }
}
