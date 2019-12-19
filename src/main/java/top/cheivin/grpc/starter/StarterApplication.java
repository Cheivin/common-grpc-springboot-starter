package top.cheivin.grpc.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.cheivin.grpc.starter.annotation.EnableScanGrpcService;

@SpringBootApplication
@EnableScanGrpcService
public class StarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarterApplication.class, args);
    }
}
