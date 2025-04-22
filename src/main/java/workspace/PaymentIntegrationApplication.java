package workspace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"workspace"})
public class PaymentIntegrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentIntegrationApplication.class, args);
        System.out.println("Payment Integration Application is running...");
    }
}