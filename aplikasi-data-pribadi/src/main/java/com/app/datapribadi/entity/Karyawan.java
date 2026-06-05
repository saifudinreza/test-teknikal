package com.app.datapribadi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

// @Entity artinya "kelas ini MEWAKILI satu tabel di database"
// Bayangkan kelas ini seperti cetakan/blueprint dari satu baris data.
// Setiap objek Karyawan yang kita buat = satu baris di tabel "karyawan".
@Entity
@Table(name = "karyawan") // nama tabel yang akan dibuat di MySQL
public class Karyawan {

    // @Id = ini adalah PRIMARY KEY, penanda unik untuk setiap baris
    // Seperti nomor KTP: tidak boleh ada dua orang dengan NIK yang sama
    // Kita TIDAK pakai @GeneratedValue (tidak auto-increment) karena
    // NIK diisi sendiri oleh pengguna, bukan dibuat otomatis oleh database
    @Id
    @Column(name = "nik", length = 20)
    private String nik;

    // nullable = false artinya kolom ini WAJIB diisi (NOT NULL di SQL)
    @Column(name = "nama_lengkap", nullable = false)
    private String namaLengkap;

    @Column(name = "jenis_kelamin")
    private String jenisKelamin; // "Laki-laki" atau "Perempuan"

    // LocalDate = tipe data tanggal di Java (tanpa jam/menit/detik)
    // Cocok untuk menyimpan tanggal lahir
    @Column(name = "tanggal_lahir")
    private LocalDate tanggalLahir;

    // columnDefinition = "TEXT" agar bisa menampung teks panjang (alamat bisa panjang)
    @Column(name = "alamat", columnDefinition = "TEXT")
    private String alamat;

    @Column(name = "negara")
    private String negara;

    // CATATAN: field "umur" TIDAK ada di sini dan TIDAK disimpan di database.
    // Umur dihitung secara langsung di KaryawanService setiap kali data diminta.
    // Kenapa? Karena umur berubah setiap tahun, tidak efisien menyimpannya permanen.

    // =============================================
    // GETTER & SETTER
    // =============================================
    // Getter = "pintu keluar" untuk membaca nilai field dari luar kelas
    // Setter = "pintu masuk" untuk mengisi/mengubah nilai field dari luar kelas
    // Contoh analogi: seperti jendela (getter) dan pintu (setter) sebuah rumah.
    // Orang luar bisa lihat isi rumah lewat jendela, dan masuk lewat pintu.

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
