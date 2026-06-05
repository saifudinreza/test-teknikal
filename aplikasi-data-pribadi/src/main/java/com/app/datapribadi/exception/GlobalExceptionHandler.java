package com.app.datapribadi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

// @RestControllerAdvice = "kelas ini adalah PUSAT PENANGANAN ERROR dari semua Controller"
// Bayangkan seperti bagian CUSTOMER SERVICE terpusat di sebuah perusahaan besar:
//   - Semua keluhan (error) dari seluruh aplikasi diarahkan ke sini
//   - Di sini error "dibungkus" menjadi response JSON yang rapi
//   - Sehingga setiap Controller tidak perlu membuat handler error sendiri-sendiri
//
// Tanpa kelas ini, jika ada error, Spring akan menampilkan halaman error putih default
// yang tidak mudah dipahami oleh frontend
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler = "tangani jika terjadi error jenis ini"
    // IllegalArgumentException dilempar dari Service jika NIK sudah terdaftar
    // HTTP 400 = Bad Request (data yang dikirim tidak valid)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", ex.getMessage()); // ex.getMessage() = pesan error aslinya
        return ResponseEntity.badRequest().body(response); // HTTP 400
    }

    // NoSuchElementException dilempar dari Service jika NIK tidak ditemukan di database
    // HTTP 404 = Not Found
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoSuchElementException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(404).body(response);
    }

    // MethodArgumentNotValidException dilempar otomatis oleh Spring ketika validasi @Valid gagal
    // Contoh: NIK kosong, NIK bukan angka, Nama kosong
    // HTTP 422 = Unprocessable Entity (data bisa dibaca tapi isinya tidak valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationError(
            MethodArgumentNotValidException ex) {

        // Ambil pesan error PERTAMA dari semua field yang gagal validasi
        // .getBindingResult() = daftar semua hasil validasi
        // .getFieldErrors()   = daftar field yang gagal
        // .stream().map(...)  = ambil pesan dari setiap field
        // .findFirst()        = ambil yang pertama saja
        String errorMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Data tidak valid");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", errorMessage);
        return ResponseEntity.status(422).body(response);
    }
}
