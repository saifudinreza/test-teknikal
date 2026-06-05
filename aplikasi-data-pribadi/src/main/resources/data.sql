-- data.sql: Seed data awal untuk mengisi database saat aplikasi pertama kali dijalankan
-- INSERT IGNORE = "masukkan data ini, tapi ABAIKAN jika NIK-nya sudah ada"
-- Jadi aman dijalankan berkali-kali tanpa menyebabkan error duplikasi

INSERT IGNORE INTO karyawan (nik, nama_lengkap, jenis_kelamin, tanggal_lahir, alamat, negara) VALUES
('3370056987450001', 'Lionel Messi',        'Laki-laki',  '1987-01-01', 'Jl. Achmad Yani No 89 Jakarta Pusat', 'Argentina'),
('3370056983690002', 'Cristiano Ronaldo',   'Laki-laki',  '1985-02-05', 'Jl. Achmad Yani No 78 Jakarta Pusat', 'Portugal'),
('3370056987450003', 'Bambang Pamungkas',   'Laki-laki',  '1984-03-03', 'Jl. Achmad Yani No 67 Jakarta Pusat', 'Indonesia'),
('3370056987450004', 'Natasha Romanov',     'Perempuan',  '1990-07-04', 'Jl. Achmad Yani No 56 Jakarta Pusat', 'Rusia'),
('3370056987450005', 'Entis Siti Jubaidah', 'Laki-laki',  '1992-01-05', 'Jl. Achmad Yani No 45 Jakarta Pusat', 'Malaysia');
