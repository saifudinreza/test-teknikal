package com.app.datapribadi.service;

import com.app.datapribadi.dto.KaryawanRequestDTO;
import com.app.datapribadi.dto.KaryawanResponseDTO;
import com.app.datapribadi.entity.Karyawan;
import com.app.datapribadi.repository.KaryawanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

// @Service = "kelas ini berisi LOGIKA BISNIS aplikasi"
// Bayangkan seperti OTAK atau MANAJER di sebuah kantor:
//   - Menerima permintaan dari Controller (resepsionis)
//   - Memproses, memvalidasi, dan menentukan apa yang harus dilakukan
//   - Memanggil Repository (petugas gudang) untuk simpan/ambil data
//   - Mengirim hasil kembali ke Controller
//
// Alur request: Browser → Controller → Service → Repository → Database
//               Browser ← Controller ← Service ← Repository ← Database
@Service
public class KaryawanService {

    // @Autowired = "Spring, tolong buatkan dan berikan objek ini untuk saya"
    // Kita tidak perlu menulis: KaryawanRepository repo = new KaryawanRepository();
    // Spring yang mengurus pembuatan objek. Ini disebut DEPENDENCY INJECTION.
    // Analogi: seperti bayar pajak secara online - kamu tidak perlu datang sendiri,
    // sistem yang mengurus pengiriman data ke tempat yang tepat.
    @Autowired
    private KaryawanRepository karyawanRepository;

    // =========================================================
    // AMBIL SEMUA DATA (dengan filter opsional)
    // =========================================================
    public List<KaryawanResponseDTO> getAllKaryawan(String nik, String nama) {
        List<Karyawan> list;

        // Jika kedua filter kosong, ambil SEMUA data
        if ((nik == null || nik.isEmpty()) && (nama == null || nama.isEmpty())) {
            list = karyawanRepository.findAll();
        } else {
            // Jika ada filter, gunakan query kustom di Repository
            list = karyawanRepository.searchByNikAndNama(nik, nama);
        }

        // .stream() = "buka jalur pemrosesan data satu per satu"
        // .map(this::convertToResponseDTO) = "ubah setiap item Karyawan menjadi ResponseDTO"
        // .collect(Collectors.toList()) = "kumpulkan hasilnya menjadi List"
        //
        // Analogi: seperti ban berjalan di pabrik.
        // Setiap Karyawan masuk → diproses (diubah ke DTO) → keluar sebagai ResponseDTO
        return list.stream()
                   .map(this::convertToResponseDTO)
                   .collect(Collectors.toList());
    }

    // =========================================================
    // AMBIL 1 DATA BERDASARKAN NIK
    // =========================================================
    public KaryawanResponseDTO getKaryawanByNik(String nik) {
        // findById() mengembalikan Optional<Karyawan>
        // Optional = "mungkin ada, mungkin tidak" (seperti kotak yang mungkin berisi hadiah)
        // .orElseThrow() = "jika kosong/tidak ditemukan, lempar exception (error)"
        Karyawan karyawan = karyawanRepository.findById(nik)
                .orElseThrow(() -> new NoSuchElementException("Data tidak ditemukan"));
        return convertToResponseDTO(karyawan);
    }

    // =========================================================
    // TAMBAH DATA BARU
    // =========================================================
    public KaryawanResponseDTO createKaryawan(KaryawanRequestDTO requestDTO) {
        // Cek apakah NIK sudah terdaftar sebelumnya
        // existsById() = cek apakah ada baris dengan NIK ini di database
        if (karyawanRepository.existsById(requestDTO.getNik())) {
            // throw = "lempar error" agar bisa ditangkap oleh GlobalExceptionHandler
            // IllegalArgumentException = jenis error untuk "argumen/input tidak valid"
            throw new IllegalArgumentException("NIK sudah terdaftar");
        }

        // Ubah RequestDTO (formulir dari frontend) menjadi Entity (objek yang bisa disimpan ke DB)
        Karyawan karyawan = convertToEntity(requestDTO);

        // .save() = simpan ke database. Karena ini data BARU, Hibernate akan INSERT
        Karyawan saved = karyawanRepository.save(karyawan);

        // Ubah Entity yang tersimpan menjadi ResponseDTO untuk dikirim ke frontend
        return convertToResponseDTO(saved);
    }

    // =========================================================
    // UPDATE DATA
    // =========================================================
    public KaryawanResponseDTO updateKaryawan(String nik, KaryawanRequestDTO requestDTO) {
        // Pastikan data dengan NIK ini ada terlebih dahulu
        Karyawan karyawan = karyawanRepository.findById(nik)
                .orElseThrow(() -> new NoSuchElementException("Data tidak ditemukan"));

        // Update hanya field yang BOLEH diubah (NIK tidak ikut diupdate karena PRIMARY KEY)
        // Mengubah Primary Key seperti mengganti nomor KTP orang lain — tidak diperbolehkan
        karyawan.setNamaLengkap(requestDTO.getNamaLengkap());
        karyawan.setJenisKelamin(requestDTO.getJenisKelamin());
        karyawan.setTanggalLahir(requestDTO.getTanggalLahir());
        karyawan.setAlamat(requestDTO.getAlamat());
        karyawan.setNegara(requestDTO.getNegara());

        // .save() pada entity yang SUDAH ADA di DB → Hibernate akan UPDATE (bukan INSERT baru)
        Karyawan updated = karyawanRepository.save(karyawan);
        return convertToResponseDTO(updated);
    }

    // =========================================================
    // HAPUS DATA
    // =========================================================
    public void deleteKaryawan(String nik) {
        // Pastikan data ada sebelum dihapus agar bisa memberi pesan error yang tepat
        if (!karyawanRepository.existsById(nik)) {
            throw new NoSuchElementException("Data tidak ditemukan");
        }
        karyawanRepository.deleteById(nik);
    }

    // =========================================================
    // METHOD BANTU (PRIVATE) — hanya dipakai di dalam kelas ini
    // =========================================================

    // Mengubah RequestDTO → Entity (untuk disimpan ke database)
    // Analogi: mengisi formulir resmi (Entity) dari data yang user kirim (DTO)
    private Karyawan convertToEntity(KaryawanRequestDTO dto) {
        Karyawan karyawan = new Karyawan();
        karyawan.setNik(dto.getNik());
        karyawan.setNamaLengkap(dto.getNamaLengkap());
        karyawan.setJenisKelamin(dto.getJenisKelamin());
        karyawan.setTanggalLahir(dto.getTanggalLahir());
        karyawan.setAlamat(dto.getAlamat());
        karyawan.setNegara(dto.getNegara());
        return karyawan;
    }

    // Mengubah Entity → ResponseDTO (untuk dikirim ke frontend sebagai JSON)
    // Di sinilah "umur" dihitung sebelum data dikirim
    private KaryawanResponseDTO convertToResponseDTO(Karyawan karyawan) {
        KaryawanResponseDTO dto = new KaryawanResponseDTO();
        dto.setNik(karyawan.getNik());
        dto.setNamaLengkap(karyawan.getNamaLengkap());
        dto.setJenisKelamin(karyawan.getJenisKelamin());
        dto.setTanggalLahir(karyawan.getTanggalLahir());
        dto.setAlamat(karyawan.getAlamat());
        dto.setNegara(karyawan.getNegara());

        // Hitung umur dari tanggal lahir ke hari ini
        // Period.between() = "hitung jarak waktu antara dua tanggal"
        // .getYears() = ambil bagian "tahun"nya saja
        // Contoh: lahir 1990-01-01, sekarang 2025-06-01 → umur = 35 tahun
        if (karyawan.getTanggalLahir() != null) {
            int umur = Period.between(karyawan.getTanggalLahir(), LocalDate.now()).getYears();
            dto.setUmur(umur);
        }

        return dto;
    }
}
