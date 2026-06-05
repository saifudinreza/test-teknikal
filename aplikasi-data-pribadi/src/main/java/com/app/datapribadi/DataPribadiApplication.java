package com.app.datapribadi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication adalah "pintu masuk" aplikasi Spring Boot.
// Anotasi ini menggabungkan tiga fungsi sekaligus:
//   1. @Configuration      - kelas ini adalah file konfigurasi
//   2. @EnableAutoConfiguration - Spring otomatis mengatur banyak hal (database, server, dll)
//   3. @ComponentScan      - Spring akan "scan" semua kelas di package ini dan turunannya
//
// Bayangkan seperti tombol ON/OFF sebuah mesin besar: satu klik, semuanya hidup.
@SpringBootApplication
public class DataPribadiApplication {

    // main() adalah titik awal eksekusi, sama seperti di C/C++
    // SpringApplication.run() = "nyalakan semua komponen Spring Boot"
    public static void main(String[] args) {
        SpringApplication.run(DataPribadiApplication.class, args);
        System.out.println("========================================");
        System.out.println(" Aplikasi Data Pribadi berhasil berjalan");
        System.out.println(" Buka: http://localhost:8080");
        System.out.println("========================================");
    }
}
