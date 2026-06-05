package com.app.datapribadi.controller;

import com.app.datapribadi.dto.KaryawanRequestDTO;
import com.app.datapribadi.dto.KaryawanResponseDTO;
import com.app.datapribadi.service.KaryawanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @RestController = "kelas ini adalah RESEPSIONIS yang menerima request HTTP dari browser"
// Bayangkan seperti front desk hotel:
//   - Tamu (browser) datang dengan permintaan (HTTP request)
//   - Resepsionis (Controller) menerima, lalu meneruskan ke departemen yang tepat (Service)
//   - Kemudian menyampaikan hasil kembali ke tamu dalam format JSON
//
// @RestController = @Controller + @ResponseBody
//   @Controller    = kelas ini menangani HTTP request
//   @ResponseBody  = setiap nilai yang dikembalikan otomatis diubah menjadi JSON
@RestController

// @RequestMapping = "semua endpoint di kelas ini diawali /api/karyawan"
// Jadi kita tidak perlu tulis "/api/karyawan" berulang di setiap method
@RequestMapping("/api/karyawan")

// @CrossOrigin = izinkan browser dari origin manapun mengakses API ini
// Tanpa ini, browser modern akan MEMBLOKIR request dari frontend ke backend
// karena dianggap "lintas domain" (CORS policy)
@CrossOrigin(origins = "*")
public class KaryawanController {

    @Autowired
    private KaryawanService karyawanService;

    // =========================================================
    // GET /api/karyawan atau GET /api/karyawan?nik=...&nama=...
    // Mengambil semua data / mencari karyawan
    // =========================================================
    // @GetMapping = tangani request GET
    // @RequestParam = ambil nilai dari query string di URL
    //   Contoh URL: /api/karyawan?nik=123&nama=Budi
    //   required=false = parameter ini opsional (boleh tidak ada di URL)
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllKaryawan(
            @RequestParam(required = false) String nik,
            @RequestParam(required = false) String nama) {

        List<KaryawanResponseDTO> list = karyawanService.getAllKaryawan(nik, nama);

        // Bungkus response dalam format standar: {"status": "success", "data": [...]}
        // Map<String, Object> = seperti kamus/dictionary JavaScript
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", list);

        // ResponseEntity.ok() = kirim response dengan HTTP status 200 (OK)
        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET /api/karyawan/{nik}
    // Mengambil 1 data karyawan berdasarkan NIK
    // =========================================================
    // @PathVariable = ambil nilai dari bagian URL path
    //   Contoh URL: /api/karyawan/3370056987450001
    //   → nik = "3370056987450001"
    @GetMapping("/{nik}")
    public ResponseEntity<Map<String, Object>> getKaryawanByNik(@PathVariable String nik) {
        KaryawanResponseDTO dto = karyawanService.getKaryawanByNik(nik);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", dto);
        return ResponseEntity.ok(response);
    }

    // =========================================================
    // POST /api/karyawan
    // Menambah data karyawan baru
    // =========================================================
    // @PostMapping = tangani request POST (biasanya untuk CREATE data baru)
    // @RequestBody = ambil data dari body request (format JSON dari frontend)
    // @Valid = AKTIFKAN validasi yang ditulis di KaryawanRequestDTO
    //          (@NotBlank, @Pattern, dll). Jika gagal → GlobalExceptionHandler menangani
    @PostMapping
    public ResponseEntity<Map<String, Object>> createKaryawan(
            @Valid @RequestBody KaryawanRequestDTO requestDTO) {

        KaryawanResponseDTO dto = karyawanService.createKaryawan(requestDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", dto);

        // HTTP 201 = Created (standar untuk response setelah data berhasil dibuat)
        return ResponseEntity.status(201).body(response);
    }

    // =========================================================
    // PUT /api/karyawan/{nik}
    // Mengubah data karyawan yang sudah ada
    // =========================================================
    @PutMapping("/{nik}")
    public ResponseEntity<Map<String, Object>> updateKaryawan(
            @PathVariable String nik,
            @Valid @RequestBody KaryawanRequestDTO requestDTO) {

        KaryawanResponseDTO dto = karyawanService.updateKaryawan(nik, requestDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", dto);
        return ResponseEntity.ok(response);
    }

    // =========================================================
    // DELETE /api/karyawan/{nik}
    // Menghapus data karyawan
    // =========================================================
    @DeleteMapping("/{nik}")
    public ResponseEntity<Map<String, Object>> deleteKaryawan(@PathVariable String nik) {
        karyawanService.deleteKaryawan(nik);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Data berhasil dihapus");
        return ResponseEntity.ok(response);
    }
}
