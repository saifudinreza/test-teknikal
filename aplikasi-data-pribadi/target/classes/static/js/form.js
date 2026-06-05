/**
 * form.js - Logika untuk halaman tambah.html, edit.html, dan detail.html
 *
 * Satu file ini melayani tiga halaman sekaligus.
 * Kita deteksi halaman mana yang aktif dari URL, lalu jalankan logika yang sesuai.
 */

// Daftar negara untuk dropdown (sesuai PRD)
const NEGARA_LIST = [
    'Indonesia', 'Malaysia', 'Singapura', 'Thailand', 'Filipina',
    'Vietnam', 'Jepang', 'Korea Selatan', 'China', 'India',
    'Amerika Serikat', 'Inggris', 'Jerman', 'Prancis', 'Australia',
    'Belanda', 'Rusia', 'Argentina', 'Portugal', 'Brazil'
];

/**
 * Ambil nilai parameter dari URL query string.
 * Contoh: URL = "edit.html?nik=3370056987450001"
 *         getNikFromUrl() → "3370056987450001"
 */
function getNikFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get('nik');
}

/**
 * Isi dropdown negara dengan opsi-opsi yang tersedia.
 * @param {string} selectedValue - Nilai yang sudah dipilih (untuk edit/detail)
 */
function populateNegaraDropdown(selectedValue) {
    const select = $('#negara');
    select.empty();
    select.append('<option value="">-- Pilih Negara --</option>');

    NEGARA_LIST.forEach(function(negara) {
        // Jika negara ini adalah nilai yang dipilih, tambahkan atribut "selected"
        const isSelected = (negara === selectedValue) ? 'selected' : '';
        select.append(`<option value="${negara}" ${isSelected}>${negara}</option>`);
    });
}

/**
 * Isi semua field form dengan data dari server.
 * Dipakai saat membuka halaman edit atau detail (data sudah ada).
 * @param {Object} data - Objek karyawan dari response server
 */
function populateForm(data) {
    $('#nik').val(data.nik);
    $('#namaLengkap').val(data.namaLengkap);

    // Set radio button sesuai jenis kelamin
    // $('input[name="jenisKelamin"][value="..."]') = cari radio button dengan nilai tertentu
    // .prop('checked', true) = centang radio button tersebut
    if (data.jenisKelamin) {
        $('input[name="jenisKelamin"][value="' + data.jenisKelamin + '"]').prop('checked', true);
    }

    // Tanggal lahir: server mengembalikan "YYYY-MM-DD", input[type=date] butuh format yang sama
    if (data.tanggalLahir) {
        const tgl = data.tanggalLahir.toString().split('T')[0]; // pastikan tidak ada bagian waktu
        $('#tanggalLahir').val(tgl);
    }

    $('#alamat').val(data.alamat || '');
    populateNegaraDropdown(data.negara);
}

/**
 * Kumpulkan semua nilai dari form menjadi satu objek JavaScript.
 * @returns {Object} Data form siap dikirim ke server
 */
function getFormData() {
    return {
        nik:           $('#nik').val().trim(),
        namaLengkap:   $('#namaLengkap').val().trim(),
        jenisKelamin:  $('input[name="jenisKelamin"]:checked').val(),
        tanggalLahir:  $('#tanggalLahir').val() || null,
        alamat:        $('#alamat').val().trim(),
        negara:        $('#negara').val() || null
    };
}

/**
 * Validasi form di sisi FRONTEND sebelum data dikirim ke server.
 * Ini untuk user experience: feedback cepat tanpa perlu menunggu server.
 * Server TETAP memvalidasi ulang (tidak hanya mengandalkan validasi frontend).
 * @param {Object} data - Data dari form
 * @param {boolean} cekNik - Apakah NIK perlu divalidasi (false untuk halaman edit)
 * @returns {boolean} true jika valid, false jika ada error
 */
function validateForm(data, cekNik) {
    let valid = true;

    // Bersihkan error sebelumnya
    $('.is-invalid').removeClass('is-invalid');
    $('.invalid-feedback').text('');

    // Validasi NIK (hanya untuk halaman tambah)
    if (cekNik) {
        if (!data.nik) {
            tampilkanError('nik', 'NIK wajib diisi');
            valid = false;
        } else if (!/^\d+$/.test(data.nik)) {
            // Regex /^\d+$/ artinya: "dari awal (^) sampai akhir ($), harus semua angka (\d+)"
            tampilkanError('nik', 'NIK harus berupa angka');
            valid = false;
        }
    }

    // Validasi Nama Lengkap
    if (!data.namaLengkap) {
        tampilkanError('namaLengkap', 'Nama Lengkap wajib diisi');
        valid = false;
    }

    return valid;
}

/**
 * Tampilkan pesan error di bawah field yang bermasalah.
 * Bootstrap class "is-invalid" mengubah border field menjadi merah.
 */
function tampilkanError(fieldId, pesan) {
    $('#' + fieldId).addClass('is-invalid');
    $('#' + fieldId + 'Error').text(pesan);
}

// =============================================
// INISIALISASI BERDASARKAN HALAMAN
// =============================================
$(document).ready(function() {

    // Deteksi nama file halaman saat ini dari URL
    // window.location.pathname = path URL, misal "/tambah.html"
    // .split('/').pop() = ambil bagian terakhir setelah "/" → "tambah.html"
    const currentPage = window.location.pathname.split('/').pop() || 'index.html';

    // ==============================
    // HALAMAN: tambah.html
    // ==============================
    if (currentPage === 'tambah.html') {
        populateNegaraDropdown(''); // isi dropdown negara tanpa pilihan awal

        $('#btnSimpan').on('click', function() {
            const data = getFormData();

            // Validasi dulu sebelum kirim ke server
            if (!validateForm(data, true)) return;

            // Kirim data ke server (POST)
            createKaryawan(data)
                .done(function() {
                    alert('Data berhasil disimpan!');
                    window.location.href = 'index.html'; // kembali ke daftar
                })
                .fail(function(xhr) {
                    const msg = xhr.responseJSON ? xhr.responseJSON.message : 'Gagal menyimpan data';
                    alert('Error: ' + msg);
                });
        });
    }

    // ==============================
    // HALAMAN: edit.html
    // ==============================
    if (currentPage === 'edit.html') {
        const nik = getNikFromUrl();

        if (!nik) {
            alert('NIK tidak ditemukan di URL. Kembali ke daftar.');
            window.location.href = 'index.html';
            return;
        }

        // Muat data karyawan dari server untuk mengisi form
        fetchKaryawanByNik(nik)
            .done(function(response) {
                populateForm(response.data);
                // NIK dibuat readonly karena PRIMARY KEY tidak boleh diubah
                // Membayangkan: seperti nomor KTP yang tidak bisa diganti
                $('#nik').prop('readonly', true);
            })
            .fail(function() {
                alert('Data tidak ditemukan.');
                window.location.href = 'index.html';
            });

        // Tombol Ubah: kirim data yang sudah diubah ke server (PUT)
        $('#btnUbah').on('click', function() {
            const data = getFormData();

            // Untuk edit, validasi NIK dimatikan (NIK sudah readonly, tidak bisa diubah)
            if (!validateForm(data, false)) return;

            updateKaryawan(nik, data)
                .done(function() {
                    alert('Data berhasil diubah!');
                    window.location.href = 'index.html';
                })
                .fail(function(xhr) {
                    const msg = xhr.responseJSON ? xhr.responseJSON.message : 'Gagal mengubah data';
                    alert('Error: ' + msg);
                });
        });
    }

    // ==============================
    // HALAMAN: detail.html
    // ==============================
    if (currentPage === 'detail.html') {
        const nik = getNikFromUrl();

        if (!nik) {
            alert('NIK tidak ditemukan di URL. Kembali ke daftar.');
            window.location.href = 'index.html';
            return;
        }

        // Muat data karyawan dari server
        fetchKaryawanByNik(nik)
            .done(function(response) {
                populateForm(response.data);

                // Disable semua input agar tidak bisa diubah (tampilan read-only)
                // $('input, textarea, select') = pilih semua elemen form
                // .prop('disabled', true) = nonaktifkan semua elemen tersebut
                $('input, textarea, select').prop('disabled', true);
            })
            .fail(function() {
                alert('Data tidak ditemukan.');
                window.location.href = 'index.html';
            });
    }
});
