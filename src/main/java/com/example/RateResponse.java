package com.example;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class RateResponse {
    @SerializedName("base_code")
    public String base;

    @SerializedName("conversion_rates")
    public Map<String, Double> rates;
}
