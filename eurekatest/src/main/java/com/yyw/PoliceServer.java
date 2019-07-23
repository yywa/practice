package com.yyw;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import java.util.Scanner;

/**
 * @author yyw
 * @date 2019/7/23
 **/
@SpringBootApplication
@EnableEurekaClient
public class PoliceServer {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String port = scan.nextLine();
        new SpringApplicationBuilder(PoliceServer.class).properties("server.port=" + port).run(args);
    }
}
