package com.app.datapribadi.dto;

import java.time.LocalDate;

// ResponseDTO = formulir yang KELUAR dari server (dari backend ke frontend)
// Berbeda dengan Entity (Karyawan.java) yang hanya punya field database,
// ResponseDTO ini punya field tambahan yaitu "umur".
//
// Alur data:
//   Database → Entity (Karyawan) → [diproses di Service] → ResponseDTO → Frontend (JSON)
//
// Umur tidak disimpan di database, tapi DIHITUNG di Service dan dimasukkan ke sini
// sebelum dikirim ke frontend.
public class KaryawanResponseDTO {

    private String nik;
    private String namaLengkap;
    private String jenisKelamin;
    private LocalDate tanggalLahir;
    private Integer umur; // field TAMBAHAN: hasil hitungan dari tanggal lahir, tidak ada di DB
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

    public Integer getUmur() { return umur; }
    public void setUmur(Integer umur) { this.umur = umur; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getNegara() { return negara; }
    public void setNegara(String negara) { this.negara = negara; }
}
