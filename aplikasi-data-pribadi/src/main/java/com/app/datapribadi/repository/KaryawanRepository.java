package com.app.datapribadi.repository;

import com.app.datapribadi.entity.Karyawan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repository = "lapisan yang langsung bersentuhan dengan database"
// Bayangkan seperti petugas gudang: tugasnya HANYA simpan dan ambil barang (data).
// Dia tidak peduli logika bisnis, hanya tahu cara kerja gudang (database).
//
// JpaRepository sudah menyediakan fungsi dasar secara OTOMATIS tanpa kita tulis:
//   - save(entity)         → INSERT atau UPDATE
//   - findById(id)         → SELECT WHERE id = ?
//   - findAll()            → SELECT * FROM tabel
//   - deleteById(id)       → DELETE WHERE id = ?
//   - existsById(id)       → SELECT COUNT(*) WHERE id = ?
//
// Parameter generik JpaRepository<Karyawan, String>:
//   - Karyawan = tipe entity yang dikelola
//   - String   = tipe data PRIMARY KEY (NIK adalah String)
@Repository
public interface KaryawanRepository extends JpaRepository<Karyawan, String> {

    // Method pencarian kustom menggunakan JPQL (Java Persistence Query Language)
    // JPQL mirip SQL tapi menggunakan nama CLASS dan FIELD Java, bukan nama tabel/kolom DB
    //
    // Logika query ini:
    //   - Jika nik tidak diisi (null atau kosong) → abaikan filter NIK
    //   - Jika nama tidak diisi → abaikan filter nama
    //   - Jika keduanya diisi → AND condition (harus cocok keduanya)
    //   - LIKE '%nama%' → mencari nama yang MENGANDUNG kata yang dicari (tidak harus persis)
    //   - LOWER() → ubah ke huruf kecil semua agar pencarian tidak case-sensitive
    @Query("SELECT k FROM Karyawan k WHERE " +
           "(:nik IS NULL OR :nik = '' OR k.nik = :nik) AND " +
           "(:nama IS NULL OR :nama = '' OR LOWER(k.namaLengkap) LIKE LOWER(CONCAT('%', :nama, '%')))")
    List<Karyawan> searchByNikAndNama(@Param("nik") String nik, @Param("nama") String nama);
}
