package com.app.datapribadi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

// DTO = Data Transfer Object
// Bayangkan ini seperti FORMULIR ISIAN yang dikirim dari browser ke server.
// Bedanya dengan Entity (Karyawan.java):
//   - Entity = representasi tabel database (data yang DISIMPAN)
//   - DTO    = representasi data yang DIKIRIM melalui API
//
// RequestDTO = formulir yang MASUK ke server (dari frontend ke backend)
public class KaryawanRequestDTO {

    // @NotBlank = field ini tidak boleh kosong atau hanya spasi
    // @Pattern  = nilai harus sesuai pola regex "\d+" yang artinya "hanya angka"
    // @Size     = maksimal 20 karakter
    // Jika aturan ini dilanggar, Spring akan menolak request dengan pesan error
    @NotBlank(message = "NIK wajib diisi")
    @Pattern(regexp = "\\d+", message = "NIK harus berupa angka")
    @Size(max = 20, message = "NIK maksimal 20 karakter")
    private String nik;

    @NotBlank(message = "Nama Lengkap wajib diisi")
    private String namaLengkap;

    // Field-field berikut tidak ada validasi wajib (opsional)
    private String jenisKelamin;
    private LocalDate tanggalLahir;
    private String alamat;
    private String negara;

    // Getter dan Setter
    public String getNik() { return nik; }
    public void setNik(String nik) { this.nik = nik; }

    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }

    public LocalDate getTanggalLahir() { return tanggalLahir; }
    public void setTanggalLahir(LocalDate tanggalLahir) { this.tanggalLahir = tanggalLahir; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getNegara() { return negara; }
    public void setNegara(String negara) { this.negara = negara; }
}
