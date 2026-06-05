# Aplikasi Data Pribadi Karyawan

Aplikasi web untuk mengelola data karyawan (tambah, lihat, edit, hapus) yang dibangun dengan **Spring Boot** di backend dan **HTML/CSS/JavaScript** di frontend.

---

## Daftar Isi

1. [Gambaran Besar](#1-gambaran-besar)
2. [Teknologi yang Digunakan](#2-teknologi-yang-digunakan)
3. [Cara Menjalankan](#3-cara-menjalankan)
4. [Struktur Folder](#4-struktur-folder)
5. [Penjelasan Setiap File](#5-penjelasan-setiap-file)
6. [Alur Kerja Aplikasi](#6-alur-kerja-aplikasi)
7. [API Endpoints](#7-api-endpoints)
8. [Fitur Aplikasi](#8-fitur-aplikasi)

---

## 1. Gambaran Besar

### Apa yang dilakukan aplikasi ini?

Aplikasi ini adalah sistem **CRUD** (Create, Read, Update, Delete) untuk data karyawan. Pengguna bisa:
- Melihat daftar semua karyawan
- Mencari karyawan berdasarkan NIK atau nama
- Menambah karyawan baru
- Mengedit data karyawan yang sudah ada
- Menghapus data karyawan

### Analogi Sederhana

Bayangkan aplikasi ini seperti **buku catatan karyawan di kantor HR**, tapi versi digital:
- **Frontend (HTML)** = tampilan buku yang dilihat dan diisi pengguna
- **Backend (Spring Boot)** = otak yang memproses setiap permintaan
- **Database (MySQL)** = laci tempat menyimpan semua data secara permanen

### Arsitektur: Client-Server

```
[ Browser / Pengguna ]
        ↕  HTTP Request/Response (format JSON)
[ Spring Boot Backend — berjalan di port 8080 ]
        ↕  SQL Query
[ MySQL Database ]
```

---

## 2. Teknologi yang Digunakan

| Teknologi | Versi | Fungsi |
|-----------|-------|--------|
| Java | 17 | Bahasa pemrograman utama backend |
| Spring Boot | 3.2.3 | Framework Java untuk membuat REST API dengan cepat |
| Spring Data JPA | - | Menghubungkan Java ke database tanpa SQL manual |
| Hibernate | 6.4 | ORM yang dipakai JPA di balik layar |
| MySQL | 8+ | Database untuk menyimpan data karyawan secara permanen |
| Maven | 3.9+ | Tools untuk download library dan build project |
| Bootstrap | 5.3.0 | Framework CSS untuk tampilan yang rapi dan responsif |
| jQuery | 3.7.0 | Library JavaScript untuk AJAX dan manipulasi halaman |

### Penjelasan Teknologi dengan Analogi

- **Spring Boot** = seperti rumah yang sudah jadi. Listrik, air, tembok sudah ada. Kamu tinggal masuk dan isi perabotannya, tidak perlu bangun dari pondasi.
- **JPA / Hibernate** = penerjemah antara Java dan database. Kamu tulis kode Java, dia yang otomatis ubah jadi perintah SQL untuk MySQL.
- **Maven** = seperti tukang belanja. Kamu tulis daftar kebutuhan (`pom.xml`), dia yang pergi mengambil semua library dari internet.
- **Bootstrap** = template desain siap pakai. Tidak perlu mendesain dari nol, tinggal pakai class yang sudah ada.
- **jQuery + AJAX** = cara JavaScript berbicara dengan server secara diam-diam di balik layar, tanpa harus reload halaman.

---

## 3. Cara Menjalankan

### Prasyarat
- Java 17 sudah terinstall (`java -version`)
- Maven sudah terinstall (`mvn -version`)
- MySQL sudah berjalan

### Langkah-Langkah

**1. Buat database di MySQL**
```sql
CREATE DATABASE db_datapribadi;
```

**2. Sesuaikan konfigurasi di `src/main/resources/application.properties`**
```properties
spring.datasource.username=root
spring.datasource.password=password_kamu
```

**3. Jalankan aplikasi**
```bash
cd aplikasi-data-pribadi
mvn spring-boot:run
```

**4. Buka di browser**
```
http://localhost:8080
```

> Tabel `karyawan` akan dibuat otomatis oleh Hibernate. Data contoh (5 karyawan) langsung tersedia dari `data.sql`.

---

## 4. Struktur Folder

```
aplikasi-data-pribadi/
│
├── pom.xml                                   ← Daftar library yang dibutuhkan (seperti daftar belanja)
│
└── src/main/
    ├── java/com/app/datapribadi/
    │   │
    │   ├── DataPribadiApplication.java        ← FILE PERTAMA: Pintu masuk / tombol ON aplikasi
    │   │
    │   ├── entity/
    │   │   └── Karyawan.java                  ← Blueprint/cetakan tabel database
    │   │
    │   ├── dto/
    │   │   ├── KaryawanRequestDTO.java         ← Format data MASUK dari frontend ke backend
    │   │   └── KaryawanResponseDTO.java        ← Format data KELUAR dari backend ke frontend
    │   │
    │   ├── repository/
    │   │   └── KaryawanRepository.java         ← Lapisan yang langsung menyentuh database
    │   │
    │   ├── service/
    │   │   └── KaryawanService.java            ← Logika bisnis (otak/manajer aplikasi)
    │   │
    │   ├── controller/
    │   │   └── KaryawanController.java         ← Penerima request HTTP dari browser (resepsionis)
    │   │
    │   └── exception/
    │       └── GlobalExceptionHandler.java     ← Penanganan semua error secara terpusat
    │
    └── resources/
        ├── application.properties              ← Konfigurasi database & server
        ├── data.sql                            ← Data awal (5 karyawan contoh)
        └── static/                             ← File frontend (HTML, CSS, JS)
            ├── index.html                      ← Halaman daftar + pencarian karyawan
            ├── tambah.html                     ← Halaman form tambah karyawan baru
            ├── edit.html                       ← Halaman form edit karyawan
            ├── detail.html                     ← Halaman lihat detail karyawan (read-only)
            ├── css/style.css                   ← Tampilan kustom tambahan
            └── js/
                ├── api.js                      ← Semua fungsi AJAX (komunikasi ke backend)
                ├── form.js                     ← Logika form tambah/edit/detail
                └── monitoring.js               ← Logika halaman daftar karyawan
```

---

## 5. Penjelasan Setiap File

### `pom.xml` — Daftar Kebutuhan Library

File **pertama** yang dibuat sebelum menulis kode Java apapun. Berisi daftar semua library yang dibutuhkan.

```xml
<dependencies>
    spring-boot-starter-web        <!-- Untuk membuat REST API dan server web -->
    spring-boot-starter-data-jpa   <!-- Untuk koneksi dan operasi database -->
    mysql-connector-j              <!-- Driver agar Java bisa bicara dengan MySQL -->
    spring-boot-starter-validation <!-- Untuk validasi input (wajib diisi, hanya angka, dll) -->
</dependencies>
```

**Analogi:** `pom.xml` adalah daftar belanja yang kamu berikan ke Maven. Maven pergi ke internet (Maven Central), mengunduh semua library yang diperlukan, dan menyiapkannya untuk proyek.

---

### `DataPribadiApplication.java` — Pintu Masuk Aplikasi

File ini adalah **titik awal eksekusi**. Hanya punya satu fungsi penting:

```java
@SpringBootApplication
public class DataPribadiApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataPribadiApplication.class, args);
        // Setelah ini: server web hidup di port 8080, koneksi DB terbuka, semua siap
    }
}
```

**Analogi:** seperti menekan tombol **ON** di mesin besar. Satu klik, seluruh mesin langsung hidup — server web, koneksi database, semua konfigurasi otomatis aktif.

**`@SpringBootApplication`** = anotasi (label) yang menggabungkan 3 fungsi sekaligus:
1. Kelas ini adalah file konfigurasi
2. Spring otomatis mengatur database, server, dll
3. Spring akan mencari dan mendaftarkan semua kelas `@Controller`, `@Service`, `@Repository`

---

### `Karyawan.java` (Entity) — Blueprint Tabel Database

File ini mendefinisikan **struktur tabel `karyawan`** di MySQL. Setiap field = satu kolom di tabel.

```java
@Entity
@Table(name = "karyawan")
public class Karyawan {

    @Id
    @Column(name = "nik", length = 20)
    private String nik;           // PRIMARY KEY — unik, tidak boleh duplikat

    @Column(nullable = false)
    private String namaLengkap;   // NOT NULL — wajib diisi

    private String jenisKelamin;
    private LocalDate tanggalLahir;
    private String alamat;        // tipe TEXT, bisa panjang
    private String negara;

    // TIDAK ADA field "umur" — umur dihitung otomatis setiap saat, tidak disimpan
}
```

**Kenapa `umur` tidak disimpan di database?**
Karena umur berubah setiap tahun. Kalau disimpan, kita harus update semua data karyawan setiap tahun. Lebih efisien dihitung langsung dari tanggal lahir setiap kali data diminta.

**Anotasi penting:**
- `@Entity` = "kelas ini mewakili tabel di database"
- `@Table(name = "karyawan")` = nama tabelnya "karyawan" di MySQL
- `@Id` = kolom ini adalah Primary Key (penanda unik, seperti nomor KTP)
- `@Column(nullable = false)` = kolom ini tidak boleh NULL/kosong

**Analogi:** kelas `Karyawan` adalah **cetakan kue**. Setiap objek `Karyawan` yang dibuat = satu kue (satu baris data di tabel).

---

### `KaryawanRequestDTO.java` dan `KaryawanResponseDTO.java` — Amplop Data

**DTO = Data Transfer Object** — objek khusus untuk membawa data antar lapisan.

Kenapa perlu DTO? Kenapa tidak pakai Entity (`Karyawan.java`) langsung?

| | Entity (Karyawan.java) | RequestDTO | ResponseDTO |
|---|---|---|---|
| Fungsi | Representasi tabel database | Data yang MASUK dari frontend | Data yang KELUAR ke frontend |
| Punya validasi? | Tidak | Ya (@NotBlank, @Pattern) | Tidak |
| Punya field `umur`? | Tidak | Tidak | **Ya** (dihitung di Service) |

**RequestDTO** — data yang dikirim pengguna dari browser:
```java
public class KaryawanRequestDTO {
    @NotBlank(message = "NIK wajib diisi")
    @Pattern(regexp = "\\d+", message = "NIK harus berupa angka")
    @Size(max = 20, message = "NIK maksimal 20 karakter")
    private String nik;

    @NotBlank(message = "Nama Lengkap wajib diisi")
    private String namaLengkap;

    // Field lain bersifat opsional
    private String jenisKelamin;
    private LocalDate tanggalLahir;
    private String alamat;
    private String negara;
}
```

**ResponseDTO** — data yang dikirim ke browser (ada tambahan field `umur`):
```java
public class KaryawanResponseDTO {
    private String nik;
    private String namaLengkap;
    private String jenisKelamin;
    private LocalDate tanggalLahir;
    private Integer umur;   // ← TAMBAHAN: hasil kalkulasi, tidak ada di database
    private String alamat;
    private String negara;
}
```

**Analogi:** bayangkan membeli tiket bioskop online.
- Form yang kamu isi (nama, nomor HP) = **RequestDTO**
- Tiket yang kamu terima (berisi nama + nomor kursi + info tambahan) = **ResponseDTO**
- Data tersimpan di sistem bioskop = **Entity**

---

### `KaryawanRepository.java` — Lapisan Database

Repository adalah lapisan yang **langsung bersentuhan dengan database**. Tugasnya hanya satu: simpan dan ambil data.

```java
@Repository
public interface KaryawanRepository extends JpaRepository<Karyawan, String> {

    // Fungsi-fungsi ini GRATIS dari JpaRepository, tidak perlu ditulis sendiri:
    // save(karyawan)       → INSERT atau UPDATE ke tabel
    // findById(nik)        → SELECT * WHERE nik = ?
    // findAll()            → SELECT * FROM karyawan
    // deleteById(nik)      → DELETE WHERE nik = ?
    // existsById(nik)      → cek apakah baris dengan NIK ini ada

    // Fungsi pencarian KUSTOM yang ditulis sendiri:
    @Query("SELECT k FROM Karyawan k WHERE " +
           "(:nik IS NULL OR :nik = '' OR k.nik = :nik) AND " +
           "(:nama IS NULL OR :nama = '' OR LOWER(k.namaLengkap) LIKE LOWER(CONCAT('%', :nama, '%')))")
    List<Karyawan> searchByNikAndNama(@Param("nik") String nik, @Param("nama") String nama);
}
```

**Penjelasan query kustom:**
- Jika NIK diisi → cari yang NIK-nya sama persis
- Jika nama diisi → cari yang namanya **mengandung** kata tersebut (tidak harus sama persis)
- `LOWER()` → huruf besar/kecil dianggap sama (tidak case-sensitive)
- Contoh: cari "bam" → menemukan "Bambang Pamungkas"

**Analogi:** Repository adalah **petugas gudang**. Dia tidak peduli logika bisnis. Tugasnya hanya: simpan barang, ambil barang, hapus barang.

---

### `KaryawanService.java` — Logika Bisnis (Otak Aplikasi)

Service berisi semua **aturan dan proses bisnis** aplikasi. Dia yang memutuskan apa yang boleh dan tidak boleh dilakukan.

**Fungsi-fungsi di dalam Service:**

| Fungsi | Yang Dilakukan |
|--------|---------------|
| `getAllKaryawan(nik, nama)` | Ambil semua data. Jika ada parameter, gunakan query pencarian |
| `getKaryawanByNik(nik)` | Ambil 1 data. Jika tidak ada → lempar error 404 |
| `createKaryawan(dto)` | Cek duplikasi NIK dulu. Jika sudah ada → tolak dengan error 400 |
| `updateKaryawan(nik, dto)` | Pastikan data ada. Update semua field kecuali NIK (Primary Key tidak boleh diubah) |
| `deleteKaryawan(nik)` | Pastikan data ada dulu, baru hapus |

**Cara umur dihitung:**
```java
// Period.between = hitung selisih antara dua tanggal
// .getYears() = ambil bagian "tahun" saja
int umur = Period.between(karyawan.getTanggalLahir(), LocalDate.now()).getYears();
// Contoh: lahir 1990-01-01, sekarang 2026-06-05 → umur = 36 tahun
```

**Konsep penting — Dependency Injection:**
```java
@Autowired
private KaryawanRepository karyawanRepository;
// Spring yang buat objeknya, kita tinggal pakai
```
Kita tidak perlu tulis `new KaryawanRepository()`. Spring secara otomatis membuat dan menyuntikkan objek tersebut. Ini disebut **Dependency Injection (DI)**.

**Analogi:** seperti karyawan baru yang tidak perlu beli laptop sendiri — IT kantor yang menyediakannya. Kita tinggal pakai.

**Analogi Service secara keseluruhan:** Service adalah **manajer** di kantor. Menerima laporan dari resepsionis (Controller), memverifikasi dan memproses, lalu meminta petugas gudang (Repository) untuk mengakses data.

---

### `KaryawanController.java` — Penerima Request HTTP

Controller adalah **pintu masuk** semua request dari browser. Dia menentukan URL mana ditangani oleh fungsi mana.

```java
@RestController
@RequestMapping("/api/karyawan")  // semua URL di kelas ini diawali /api/karyawan
@CrossOrigin(origins = "*")       // izinkan request dari browser manapun (CORS)
public class KaryawanController {

    @GetMapping              // GET /api/karyawan          → ambil semua
    @GetMapping("/{nik}")    // GET /api/karyawan/123      → ambil 1 data
    @PostMapping             // POST /api/karyawan         → tambah baru
    @PutMapping("/{nik}")    // PUT /api/karyawan/123      → update
    @DeleteMapping("/{nik}") // DELETE /api/karyawan/123   → hapus
}
```

**Format response selalu konsisten:**
```json
{
  "status": "success",
  "data": { ... }
}
```

**Anotasi penting:**
| Anotasi | Fungsi |
|---------|--------|
| `@RestController` | Kelas ini menangani HTTP dan mengembalikan JSON |
| `@RequestMapping` | Prefix URL untuk semua endpoint di kelas ini |
| `@GetMapping` | Tangani request HTTP GET (mengambil data) |
| `@PostMapping` | Tangani request HTTP POST (membuat data baru) |
| `@PutMapping` | Tangani request HTTP PUT (mengubah data) |
| `@DeleteMapping` | Tangani request HTTP DELETE (menghapus data) |
| `@PathVariable` | Ambil nilai dari URL path — contoh: `/api/karyawan/123` → nik = "123" |
| `@RequestBody` | Ambil data JSON dari body request |
| `@Valid` | Aktifkan validasi dari RequestDTO |

**Analogi:** Controller adalah **resepsionis hotel**. Tamu (browser) datang dengan permintaan. Resepsionis meneruskan ke departemen yang tepat (Service), lalu menyampaikan hasilnya kembali ke tamu.

---

### `GlobalExceptionHandler.java` — Penanganan Error Terpusat

Kelas ini menangkap **semua error** dari seluruh aplikasi dan mengubahnya menjadi response JSON yang rapi dan konsisten.

**Tiga jenis error yang ditangani:**

| Jenis Error | Penyebab | HTTP Status |
|-------------|----------|-------------|
| `IllegalArgumentException` | NIK sudah terdaftar (duplikat) | 400 Bad Request |
| `NoSuchElementException` | NIK tidak ditemukan di database | 404 Not Found |
| `MethodArgumentNotValidException` | Validasi gagal (NIK kosong, bukan angka, dll) | 422 Unprocessable Entity |

**Tanpa kelas ini:** jika ada error, browser mendapat halaman error Java yang membingungkan.

**Dengan kelas ini:** browser mendapat JSON yang jelas:
```json
{
  "status": "error",
  "message": "NIK sudah terdaftar"
}
```

**Analogi:** seperti bagian **customer service terpusat** di perusahaan besar. Semua keluhan dari seluruh divisi diarahkan ke sini, lalu direspons dengan format yang seragam dan mudah dipahami.

---

### File Frontend

#### `index.html` — Halaman Utama (Daftar Karyawan)
Menampilkan tabel semua karyawan dengan form pencarian di atas. Setiap baris punya tombol Detail, Edit, dan Hapus.

#### `tambah.html` — Form Tambah Karyawan
Form kosong untuk mengisi data karyawan baru. Saat submit → AJAX POST ke `/api/karyawan`.

#### `edit.html` — Form Edit Karyawan
Form yang sudah terisi otomatis dari database. NIK tidak bisa diubah (readonly). Saat submit → AJAX PUT ke `/api/karyawan/{nik}`.

#### `detail.html` — Halaman Detail (Read-Only)
Sama seperti form edit, tapi semua field dalam kondisi disabled — hanya bisa dilihat, tidak bisa diubah.

#### `js/api.js` — Semua Fungsi AJAX
Satu tempat untuk semua komunikasi dengan backend:
```javascript
fetchKaryawan(nik, nama)     // GET semua data (dengan filter opsional)
fetchKaryawanByNik(nik)      // GET 1 data berdasarkan NIK
createKaryawan(data)         // POST tambah data baru
updateKaryawan(nik, data)    // PUT update data
deleteKaryawan(nik)          // DELETE hapus data
```

#### `js/monitoring.js` — Logika Halaman Daftar
Mengatur render tabel, fungsi search, dan modal konfirmasi hapus. Semua tombol di tabel dikelola di sini.

#### `js/form.js` — Logika Form
Mendeteksi halaman aktif dari URL, mengisi form dengan data dari server, memvalidasi input, dan mengirim data saat submit.

---

## 6. Alur Kerja Aplikasi

### Contoh 1: Membuka Halaman Daftar Karyawan

```
1. Browser buka http://localhost:8080/index.html
2. monitoring.js otomatis memanggil fetchKaryawan() di api.js
3. api.js mengirim request: GET /api/karyawan
4. KaryawanController menerima request
5. Controller memanggil karyawanService.getAllKaryawan()
6. Service memanggil karyawanRepository.findAll()
7. Repository menjalankan: SELECT * FROM karyawan (ke MySQL)
8. MySQL mengembalikan semua baris data
9. Data kembali ke Service → Service konversi setiap baris jadi ResponseDTO + hitung umur
10. Controller membungkus dalam {"status":"success","data":[...]}
11. JSON dikirim kembali ke browser
12. monitoring.js menampilkan data dalam bentuk tabel HTML
```

### Contoh 2: Menambah Karyawan Baru

```
1. Pengguna isi form di tambah.html dan klik "Simpan"
2. form.js ambil semua nilai dari input form
3. api.js mengirim: POST /api/karyawan (body: JSON data karyawan)
4. @Valid di Controller memvalidasi data
   → NIK kosong / bukan angka → GlobalExceptionHandler → error 422
5. Controller memanggil karyawanService.createKaryawan(dto)
6. Service cek: apakah NIK sudah ada? (existsById)
   → NIK duplikat → throw IllegalArgumentException → error 400
7. Service konversi RequestDTO → Entity
8. Service memanggil karyawanRepository.save(karyawan)
9. Repository menjalankan: INSERT INTO karyawan (...) VALUES (...)
10. Data tersimpan di MySQL
11. Response sukses: {"status":"success","data":{...}} dikirim ke browser
12. form.js redirect pengguna ke index.html
```

### Diagram Lapisan Arsitektur

```
┌──────────────────────────────────────────────────┐
│  FRONTEND (Browser)                              │
│  index.html │ tambah.html │ edit.html │ detail.html │
│  api.js │ monitoring.js │ form.js                │
└───────────────────┬──────────────────────────────┘
                    │  HTTP Request/Response (JSON)
                    ▼
┌──────────────────────────────────────────────────┐
│  CONTROLLER LAYER  (KaryawanController.java)     │
│  Terima request, validasi input, kirim response  │
└───────────────────┬──────────────────────────────┘
                    │
                    ▼
┌──────────────────────────────────────────────────┐
│  SERVICE LAYER  (KaryawanService.java)           │
│  Logika bisnis: cek duplikasi NIK, hitung umur   │
│  konversi antara Entity dan DTO                  │
└───────────────────┬──────────────────────────────┘
                    │
                    ▼
┌──────────────────────────────────────────────────┐
│  REPOSITORY LAYER  (KaryawanRepository.java)     │
│  Eksekusi query ke database, tidak ada logika    │
└───────────────────┬──────────────────────────────┘
                    │  SQL Query (otomatis oleh JPA)
                    ▼
┌──────────────────────────────────────────────────┐
│  DATABASE  (MySQL — tabel karyawan)              │
│  Menyimpan data secara permanen                  │
└──────────────────────────────────────────────────┘
```

---

## 7. API Endpoints

Base URL: `http://localhost:8080`

### `GET /api/karyawan`
Ambil semua data karyawan. Support filter via query parameter:
- `?nik=123` → filter NIK (exact match)
- `?nama=budi` → filter nama (mengandung kata, tidak case-sensitive)
- `?nik=123&nama=budi` → kombinasi keduanya

**Response:**
```json
{
  "status": "success",
  "data": [
    {
      "nik": "337005698745001",
      "namaLengkap": "Bambang Pamungkas",
      "jenisKelamin": "Laki-laki",
      "tanggalLahir": "1980-06-10",
      "umur": 45,
      "alamat": "Jl. Kebayoran Baru No.5, Jakarta",
      "negara": "Indonesia"
    }
  ]
}
```

### `GET /api/karyawan/{nik}`
Ambil 1 data berdasarkan NIK. Response: objek karyawan tunggal (bukan array).

### `POST /api/karyawan`
Tambah karyawan baru. Request body (JSON):
```json
{
  "nik": "1234567890",
  "namaLengkap": "Budi Santoso",
  "jenisKelamin": "Laki-laki",
  "tanggalLahir": "1995-03-15",
  "alamat": "Jl. Merdeka No.1, Jakarta",
  "negara": "Indonesia"
}
```
Response HTTP 201 (Created) jika berhasil.

### `PUT /api/karyawan/{nik}`
Update data karyawan. NIK di URL yang menentukan data mana yang diubah.

### `DELETE /api/karyawan/{nik}`
Hapus karyawan berdasarkan NIK. Response:
```json
{ "status": "success", "message": "Data berhasil dihapus" }
```

---

## 8. Fitur Aplikasi

### Validasi Data
- NIK wajib diisi, hanya boleh angka, maksimal 20 digit
- Nama Lengkap wajib diisi
- NIK tidak boleh duplikat — sistem menolak jika NIK sudah terdaftar
- NIK tidak bisa diubah setelah disimpan (karena berfungsi sebagai Primary Key)

### Kalkulasi Umur Otomatis
Umur tidak disimpan di database. Setiap kali data diminta, umur dihitung otomatis dari tanggal lahir ke tanggal hari ini menggunakan `Period.between()` di Java.

### Pencarian Fleksibel
- Cari berdasarkan NIK (harus tepat/exact)
- Cari berdasarkan nama (sebagian kata cukup, tidak case-sensitive)
- Bisa kombinasi keduanya

### Penanganan Error Terstandar
Semua error dikembalikan dalam format JSON yang konsisten `{"status":"error","message":"..."}` sehingga mudah ditangani oleh frontend.

### Data Awal (Seed Data)
Saat aplikasi pertama kali dijalankan, 5 data karyawan contoh langsung tersedia dari file `data.sql`:
1. Lionel Messi — Argentina
2. Cristiano Ronaldo — Portugal
3. Bambang Pamungkas — Indonesia
4. Natasha Romanov — Rusia
5. Entis Siti Jubaidah — Malaysia

---

*Technical Test — Aplikasi CRUD Data Pribadi Karyawan menggunakan Spring Boot 3 + MySQL + Bootstrap 5*
