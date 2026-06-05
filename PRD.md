# PRD — Aplikasi Data Pribadi Karyawan

## 1. Overview

Aplikasi web CRUD untuk mengelola data pribadi karyawan. User dapat mencari, menambah, melihat detail, mengedit, dan menghapus data karyawan melalui tampilan yang bersih dan responsif.

---

## 2. Tech Stack

| Layer | Technology |
|---|---|
| Frontend | HTML5, CSS3, JavaScript, jQuery, Bootstrap 5 |
| Backend | Java 17+ dengan Spring Boot 3 + Spring Data JPA (Hibernate) |
| Database | MySQL 8 |
| Build Tool | Maven |
| API Style | REST API (JSON) |

---

## 3. Struktur Project (Maven Spring Boot)

```
aplikasi-data-pribadi/
├── pom.xml
├── src/
│   └── main/
│       ├── java/
│       │   └── com/app/datapribadi/
│       │       ├── DataPribadiApplication.java
│       │       ├── entity/
│       │       │   └── Karyawan.java              ← @Entity Hibernate
│       │       ├── repository/
│       │       │   └── KaryawanRepository.java    ← extends JpaRepository
│       │       ├── service/
│       │       │   └── KaryawanService.java
│       │       ├── controller/
│       │       │   └── KaryawanController.java    ← @RestController
│       │       └── dto/
│       │           ├── KaryawanRequestDTO.java
│       │           └── KaryawanResponseDTO.java
│       └── resources/
│           ├── application.properties             ← DB config
│           ├── static/
│           │   ├── index.html                     ← Monitoring/List
│           │   ├── tambah.html
│           │   ├── edit.html
│           │   ├── detail.html
│           │   ├── css/
│           │   │   └── style.css
│           │   └── js/
│           │       ├── monitoring.js
│           │       ├── form.js
│           │       └── api.js
│           └── data.sql                           ← seed data awal
└── README.md
```

---

## 4. Dependencies (pom.xml)

```xml
<dependencies>
  <!-- Spring Boot Web (REST API) -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>

  <!-- Spring Data JPA + Hibernate -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>

  <!-- MySQL Driver -->
  <dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
  </dependency>

  <!-- Validation -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
  </dependency>

  <!-- Lombok (opsional, untuk getter/setter otomatis) -->
  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
  </dependency>
</dependencies>
```

---

## 5. Konfigurasi Database (application.properties)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_datapribadi?useSSL=false&serverTimezone=Asia/Jakarta
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

server.port=8080
```

---

## 6. Data Model

### Entity: `Karyawan.java`

```java
@Entity
@Table(name = "karyawan")
public class Karyawan {
    @Id
    @Column(name = "nik", length = 20)
    private String nik;           // PRIMARY KEY, mandatory, numeric string

    @Column(name = "nama_lengkap", nullable = false)
    private String namaLengkap;   // mandatory

    @Column(name = "jenis_kelamin")
    private String jenisKelamin;  // "Laki-laki" atau "Perempuan"

    @Column(name = "tanggal_lahir")
    private LocalDate tanggalLahir;

    @Column(name = "alamat", columnDefinition = "TEXT")
    private String alamat;

    @Column(name = "negara")
    private String negara;

    // Umur dihitung di service layer, tidak disimpan di DB
}
```

### Tabel DB (`karyawan`):

| Kolom | Tipe | Constraint |
|---|---|---|
| `nik` | VARCHAR(20) | PRIMARY KEY, NOT NULL |
| `nama_lengkap` | VARCHAR(255) | NOT NULL |
| `jenis_kelamin` | VARCHAR(20) | - |
| `tanggal_lahir` | DATE | - |
| `alamat` | TEXT | - |
| `negara` | VARCHAR(100) | - |

**`umur`** → dihitung di `KaryawanService` menggunakan `Period.between(tanggalLahir, LocalDate.now()).getYears()`, dimasukkan ke `KaryawanResponseDTO`.

---

## 7. Fitur & Halaman

### 7.1 Halaman Monitoring (List / Index)

**URL:** `http://localhost:8080/index.html`

**Komponen:**
- Header: ikon + judul "Aplikasi Data Pribadi"
- Search form:
  - Input **NIK** (opsional)
  - Input **Nama** (opsional)
  - Tombol **Search** → panggil `GET /api/karyawan?nik=...&nama=...`
  - Tombol **Add** → redirect ke `tambah.html`
- Tabel kolom: No, NIK, Nama Lengkap, Umur, Tanggal Lahir, Jenis Kelamin, Alamat, Negara, Action
- Kolom Action: **Detail** | **Edit** | **Delete**

**Behavior Search:**
- NIK diisi → filter exact match
- Nama diisi → filter LIKE %nama%
- Keduanya diisi → AND condition
- Kosong semua → tampilkan semua

---

### 7.2 Halaman Tambah Data

**URL:** `tambah.html`

| Field | Input Type | Validasi |
|---|---|---|
| NIK | `input[type=text]` | Required, hanya angka, max 20 karakter, harus unik |
| Nama Lengkap | `input[type=text]` | Required |
| Jenis Kelamin | Radio: Laki-laki / Perempuan | Default: Laki-laki |
| Tanggal Lahir | `input[type=date]` | - |
| Alamat | `textarea` | - |
| Negara | `select` dropdown | Default: "Pilih Negara" |

Tombol: **Simpan** (POST ke `/api/karyawan`) + **Kembali**

---

### 7.3 Halaman Edit Data

**URL:** `edit.html?nik={nik}`

- Load data via `GET /api/karyawan/{nik}` saat halaman dibuka
- NIK ditampilkan sebagai **readonly** (tidak bisa diubah, karena PK)
- Tombol: **Ubah** (PUT ke `/api/karyawan/{nik}`) + **Kembali**

---

### 7.4 Halaman Detail Data

**URL:** `detail.html?nik={nik}`

- Load data via `GET /api/karyawan/{nik}`
- Semua field **disabled** (read-only view)
- Hanya tombol **Kembali**

---

### 7.5 Hapus Data

- Klik Delete di tabel → muncul **Bootstrap Modal** (bukan `confirm()`)
- Teks modal: `"Anda yakin menghapus data [Nama Lengkap] ?"`
- Klik **OK** → `DELETE /api/karyawan/{nik}` → refresh tabel
- Klik **Batal** → tutup modal

---

## 8. REST API Endpoints

| Method | Endpoint | Deskripsi |
|---|---|---|
| GET | `/api/karyawan` | Ambil semua (support `?nik=&nama=`) |
| GET | `/api/karyawan/{nik}` | Ambil 1 data by NIK |
| POST | `/api/karyawan` | Tambah data baru |
| PUT | `/api/karyawan/{nik}` | Update data |
| DELETE | `/api/karyawan/{nik}` | Hapus data |

**Request Body (POST/PUT):**
```json
{
  "nik": "3370056987450001",
  "namaLengkap": "Lionel Messi",
  "jenisKelamin": "Laki-laki",
  "tanggalLahir": "1987-01-01",
  "alamat": "Jl. Achmad Yani No 89 Jakarta Pusat",
  "negara": "Argentina"
}
```

**Response sukses:**
```json
{
  "status": "success",
  "data": {
    "nik": "3370056987450001",
    "namaLengkap": "Lionel Messi",
    "jenisKelamin": "Laki-laki",
    "tanggalLahir": "1987-01-01",
    "umur": 38,
    "alamat": "Jl. Achmad Yani No 89 Jakarta Pusat",
    "negara": "Argentina"
  }
}
```

**Response error:**
```json
{
  "status": "error",
  "message": "NIK sudah terdaftar"
}
```

---

## 9. Validasi & Error Handling

### Frontend (jQuery):
- NIK kosong → "NIK wajib diisi"
- NIK bukan angka → "NIK harus berupa angka"
- Nama kosong → "Nama Lengkap wajib diisi"
- Tampilkan error di bawah field dengan class Bootstrap `is-invalid`

### Backend (Spring Validation + @ControllerAdvice):
- NIK duplicate → HTTP 400 `"NIK sudah terdaftar"`
- NIK tidak ditemukan → HTTP 404 `"Data tidak ditemukan"`
- Field mandatory kosong → HTTP 422 dari `@Valid` + `@NotBlank`
- Buat `GlobalExceptionHandler.java` dengan `@ControllerAdvice`

---

## 10. UI / Styling Guidelines

- Framework: **Bootstrap 5**
- Header: ikon `👤📋` + teks bold "Aplikasi Data Pribadi"
- Warna tombol:
  - Search, Add, Simpan, Ubah, OK → `btn-primary`
  - Kembali, Batal → `btn-secondary`
  - Detail → `btn-info`
  - Edit → `btn-warning`
  - Delete → `btn-danger`
- Tabel: `table table-bordered table-striped table-hover`
- Form container: max-width ~450px, centered
- Modal delete: Bootstrap Modal component

---

## 11. List Negara (Dropdown)

Indonesia, Malaysia, Singapura, Thailand, Filipina, Vietnam, Jepang, Korea Selatan, China, India, Amerika Serikat, Inggris, Jerman, Prancis, Australia, Belanda, Rusia, Argentina, Portugal, Brazil

---

## 12. Seed Data (data.sql)

```sql
INSERT IGNORE INTO karyawan (nik, nama_lengkap, jenis_kelamin, tanggal_lahir, alamat, negara) VALUES
('3370056987450001', 'Lionel Messi',        'Laki-laki',  '1987-01-01', 'Jl. Achmad Yani No 89 Jakarta Pusat', 'Argentina'),
('3370056983690002', 'Cristiano Ronaldo',   'Laki-laki',  '1985-02-05', 'Jl. Achmad Yani No 78 Jakarta Pusat', 'Portugal'),
('3370056987450003', 'Bambang Pamungkas',   'Laki-laki',  '1984-03-03', 'Jl. Achmad Yani No 67 Jakarta Pusat', 'Indonesia'),
('3370056987450004', 'Natasha Romanov',     'Perempuan',  '1990-07-04', 'Jl. Achmad Yani No 56 Jakarta Pusat', 'Rusia'),
('3370056987450005', 'Entis Siti Jubaidah', 'Laki-laki',  '1992-01-05', 'Jl. Achmad Yani No 45 Jakarta Pusat', 'Malaysia');
```

---

## 13. Prompt untuk Claude Code

Paste ini ke Claude Code setelah PRD.md ada di folder project:

```
Build the "Aplikasi Data Pribadi" web app based on PRD.md in this folder.

Tech stack (STRICTLY follow this, no substitution):
- Backend: Java 17 + Spring Boot 3 + Spring Data JPA (Hibernate) + MySQL 8
- Build tool: Maven
- Frontend: HTML + jQuery + Bootstrap 5 (NO React, NO Vue, NO Thymeleaf)
  - Frontend files go inside src/main/resources/static/
  - Frontend communicates to backend via jQuery AJAX (fetch REST API)

Steps to build:
1. Generate pom.xml with correct dependencies
2. Create application.properties for MySQL connection
3. Create Karyawan entity, repository, service, controller, DTO, GlobalExceptionHandler
4. Create frontend HTML pages: index.html, tambah.html, edit.html, detail.html
5. Create JS files: api.js (all AJAX calls), monitoring.js, form.js
6. Add data.sql seed data
7. Add README.md with how to run

Important rules:
- DELETE confirmation must use Bootstrap Modal, NOT browser confirm()
- Umur is calculated in service layer using Period.between(), not stored in DB
- NIK field is readonly in edit form (Primary Key cannot be changed)
- Enable CORS in Spring if needed for local dev

Read PRD.md fully before writing any code.
```
