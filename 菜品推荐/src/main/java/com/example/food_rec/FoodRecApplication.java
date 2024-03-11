package com.example.food_rec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import spark.SparkStreamingApp;
import spark.Spark_Offline_Rec;

@SpringBootApplication
public class FoodRecApplication {

    public static void main(String[] args){

        SpringApplication.run(FoodRecApplication.class, args);
        Spark_Offline_Rec.carry();
        SparkStreamingApp.main(args);


    }


}
