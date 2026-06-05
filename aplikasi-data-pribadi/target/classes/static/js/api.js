/**
 * api.js - Semua fungsi komunikasi antara frontend dan backend (REST API)
 *
 * File ini seperti "telepon" yang digunakan halaman HTML untuk bicara dengan server.
 * Setiap fungsi di sini mengirim satu jenis permintaan ke server menggunakan jQuery AJAX.
 *
 * AJAX = Asynchronous JavaScript And XML
 * "Asynchronous" artinya: browser tidak perlu BEKU menunggu jawaban server.
 * Saat menunggu, browser tetap bisa dipakai. Saat jawaban tiba, baru diproses.
 * Analogi: seperti pesan WhatsApp (kirim, lanjut aktivitas lain, baca saat balasan tiba)
 * vs telepon biasa (harus diam menunggu diangkat).
 */

// Alamat dasar API backend (semua endpoint diawali dengan ini)
const API_BASE_URL = '/api/karyawan';

/**
 * Ambil semua karyawan, atau cari berdasarkan NIK dan/atau nama.
 * @param {string} nik  - NIK untuk filter (boleh kosong)
 * @param {string} nama - Nama untuk filter (boleh kosong)
 * @returns jQuery AJAX promise (objek yang mewakili operasi async)
 */
function fetchKaryawan(nik, nama) {
    // URLSearchParams = cara mudah membuat query string di URL
    // Contoh: nik=123&nama=Budi
    const params = new URLSearchParams();
    if (nik)  params.append('nik', nik);
    if (nama) params.append('nama', nama);

    const queryString = params.toString();
    const url = queryString ? `${API_BASE_URL}?${queryString}` : API_BASE_URL;

    return $.ajax({
        url: url,
        method: 'GET',
        contentType: 'application/json'
    });
}

/**
 * Ambil 1 data karyawan berdasarkan NIK.
 * @param {string} nik
 */
function fetchKaryawanByNik(nik) {
    return $.ajax({
        url: `${API_BASE_URL}/${nik}`,
        method: 'GET',
        contentType: 'application/json'
    });
}

/**
 * Tambah karyawan baru ke database.
 * @param {Object} data - Objek berisi field karyawan
 */
function createKaryawan(data) {
    return $.ajax({
        url: API_BASE_URL,
        method: 'POST',
        contentType: 'application/json',
        // JSON.stringify = ubah objek JavaScript menjadi teks JSON untuk dikirim
        // Contoh: {nik: "123", nama: "Budi"} → '{"nik":"123","nama":"Budi"}'
        data: JSON.stringify(data)
    });
}

/**
 * Update data karyawan yang sudah ada.
 * @param {string} nik  - NIK karyawan yang akan diupdate
 * @param {Object} data - Data baru
 */
function updateKaryawan(nik, data) {
    return $.ajax({
        url: `${API_BASE_URL}/${nik}`,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(data)
    });
}

/**
 * Hapus data karyawan dari database.
 * @param {string} nik - NIK karyawan yang akan dihapus
 */
function deleteKaryawan(nik) {
    return $.ajax({
        url: `${API_BASE_URL}/${nik}`,
        method: 'DELETE',
        contentType: 'application/json'
    });
}
