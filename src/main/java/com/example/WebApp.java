package com.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;  // <— for CLASSPATH serving
import java.util.Map;

public class WebApp {
    public static void main(String[] args) {
        Javalin app = Javalin.create(cfg -> {
            // serve files from src/main/resources/public
            cfg.staticFiles.add("/public", Location.CLASSPATH);
        }).start(7000);

        app.get("/convert", ctx -> {
            double amt = Double.parseDouble(ctx.queryParam("amount"));
            String from = ctx.queryParam("from").toUpperCase();
            String to   = ctx.queryParam("to").toUpperCase();

            RateResponse resp = new ExchangeRateClient().getRate(from, to);
            double rate = resp.rates.get(to);
            double result = amt * rate;

            ctx.json(Map.of(
                    "amount", amt,
                    "from",   from,
                    "to",     to,
                    "rate",   rate,
                    "result", result
            ));
        });
    }
}
