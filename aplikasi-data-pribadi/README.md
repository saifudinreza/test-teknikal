# Aplikasi Data Pribadi Karyawan

Aplikasi web CRUD untuk mengelola data pribadi karyawan menggunakan Spring Boot + MySQL + jQuery Bootstrap.

## Prasyarat

Pastikan sudah terinstal:
- **Java 17+** — cek dengan: `java -version`
- **Maven 3.6+** — cek dengan: `mvn -version`
- **MySQL 8** — pastikan service MySQL berjalan

## Langkah Setup

### 1. Buat Database MySQL

Buka MySQL (via terminal atau MySQL Workbench), lalu jalankan:

```sql
CREATE DATABASE db_datapribadi CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Konfigurasi Password Database

Buka file `src/main/resources/application.properties`, ubah baris ini:

```properties
spring.datasource.password=your_password
```

Ganti `your_password` dengan password MySQL Anda (default MySQL biasanya kosong atau `root`).

### 3. Jalankan Aplikasi

```bash
cd aplikasi-data-pribadi
mvn spring-boot:run
```

Tunggu hingga muncul pesan:
```
 Aplikasi Data Pribadi berhasil berjalan
 Buka: http://localhost:8080
```

### 4. Buka di Browser

Buka: **http://localhost:8080**

Data awal (seed data) akan otomatis terisi saat aplikasi pertama kali jalan.

---

## Struktur Halaman

| URL | Fungsi |
|-----|--------|
| `http://localhost:8080` atau `/index.html` | Daftar & Pencarian Karyawan |
| `/tambah.html` | Tambah Data Baru |
| `/edit.html?nik={nik}` | Edit Data |
| `/detail.html?nik={nik}` | Lihat Detail Data |

## REST API

| Method | Endpoint | Keterangan |
|--------|----------|------------|
| GET | `/api/karyawan` | Ambil semua (support `?nik=&nama=`) |
| GET | `/api/karyawan/{nik}` | Ambil 1 data |
| POST | `/api/karyawan` | Tambah data baru |
| PUT | `/api/karyawan/{nik}` | Update data |
| DELETE | `/api/karyawan/{nik}` | Hapus data |

## Build JAR (opsional)

Untuk membuat file JAR yang bisa dijalankan standalone:

```bash
mvn clean package
java -jar target/datapribadi-1.0.0.jar
```
