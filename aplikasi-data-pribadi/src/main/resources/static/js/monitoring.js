/**
 * monitoring.js - Logika untuk halaman index.html (daftar/monitoring karyawan)
 *
 * File ini mengatur:
 *   1. Menampilkan data karyawan dalam bentuk tabel
 *   2. Fungsi pencarian (Search)
 *   3. Konfirmasi dan eksekusi hapus data (Delete)
 */

// Variabel global untuk menyimpan NIK yang akan dihapus
// Disimpan di sini agar tombol "OK" di modal tahu data mana yang harus dihapus
let nikToDelete = null;
let namaToDelete = null;

/**
 * Format tanggal dari format ISO "YYYY-MM-DD" ke "DD/MM/YYYY"
 * Contoh: "1987-01-01" → "01/01/1987"
 * Agar tampilan tanggal lebih familiar bagi pengguna Indonesia
 */
function formatTanggal(tanggal) {
    if (!tanggal) return '-';
    const parts = tanggal.toString().split('T')[0]; // hilangkan bagian waktu jika ada
    const [year, month, day] = parts.split('-');
    return `${day}/${month}/${year}`;
}

/**
 * Render / tampilkan array data karyawan ke dalam tabel HTML.
 * Setiap kali data berubah (search/delete), fungsi ini dipanggil ulang.
 * @param {Array} data - Array objek karyawan dari server
 */
function renderTable(data) {
    const tbody = $('#tableBody');
    tbody.empty(); // kosongkan isi tabel dulu sebelum diisi ulang

    if (data.length === 0) {
        tbody.append('<tr><td colspan="9" class="text-center text-muted">Tidak ada data ditemukan</td></tr>');
        return;
    }

    // Looping setiap item di array data
    // forEach((item, index) => {...}) = "untuk setiap karyawan, lakukan ini"
    data.forEach(function(karyawan, index) {
        // Template literal (backtick ``) = cara membuat string HTML dengan variabel di dalamnya
        // ${...} = tempat memasukkan nilai variabel ke dalam string
        const row = `
            <tr>
                <td>${index + 1}</td>
                <td>${karyawan.nik}</td>
                <td>${karyawan.namaLengkap}</td>
                <td>${karyawan.umur != null ? karyawan.umur : '-'}</td>
                <td>${formatTanggal(karyawan.tanggalLahir)}</td>
                <td>${karyawan.jenisKelamin || '-'}</td>
                <td class="hide-mobile">${karyawan.alamat || '-'}</td>
                <td>${karyawan.negara || '-'}</td>
                <td>
                    <a href="detail.html?nik=${karyawan.nik}" class="btn btn-info btn-sm">Detail</a>
                    <a href="edit.html?nik=${karyawan.nik}" class="btn btn-warning btn-sm">Edit</a>
                    <button class="btn btn-danger btn-sm btn-delete"
                            data-nik="${karyawan.nik}"
                            data-nama="${karyawan.namaLengkap.replace(/"/g, '&quot;')}">
                        Delete
                    </button>
                </td>
            </tr>
        `;
        tbody.append(row);
    });
}

/**
 * Muat data dari server lalu tampilkan ke tabel.
 * Dipanggil saat halaman pertama dibuka dan setelah search/delete.
 */
function loadData(nik, nama) {
    // Tampilkan loading state
    $('#tableBody').html('<tr><td colspan="9" class="text-center">Memuat data...</td></tr>');

    fetchKaryawan(nik, nama)
        .done(function(response) {
            renderTable(response.data);
        })
        .fail(function() {
            $('#tableBody').html(
                '<tr><td colspan="9" class="text-center text-danger">' +
                'Gagal memuat data. Pastikan server berjalan di port 8080.' +
                '</td></tr>'
            );
        });
}

// =============================================
// INISIALISASI: jalankan saat halaman selesai dimuat
// $(document).ready() = "jalankan kode ini SETELAH semua elemen HTML siap"
// Analogi: seperti menunggu ruangan siap sebelum memulai rapat
// =============================================
$(document).ready(function() {

    // Muat semua data pertama kali saat halaman dibuka
    loadData('', '');

    // Event: klik tombol Search
    $('#btnSearch').on('click', function() {
        const nik  = $('#searchNik').val().trim();
        const nama = $('#searchNama').val().trim();
        loadData(nik, nama);
    });

    // Event: tekan Enter di input pencarian (agar lebih praktis)
    $('#searchNik, #searchNama').on('keypress', function(e) {
        if (e.which === 13) { // 13 = kode tombol Enter
            $('#btnSearch').click();
        }
    });

    // Event: klik tombol Delete di baris tabel
    // Kita gunakan EVENT DELEGATION: listen di #tableBody karena tombol delete
    // dibuat secara dinamis (tidak ada sejak awal di HTML)
    // Analogi: daripada pasang pendengar di setiap kursi, pasang satu pendengar di ruangan
    $('#tableBody').on('click', '.btn-delete', function() {
        // data-nik dan data-nama adalah atribut HTML yang kita pasang di tombol
        nikToDelete  = $(this).data('nik');
        namaToDelete = $(this).data('nama');

        // Isi teks modal dengan nama karyawan yang akan dihapus
        $('#deleteModalText').text('Anda yakin menghapus data ' + namaToDelete + ' ?');

        // Tampilkan Bootstrap Modal (bukan confirm() bawaan browser)
        const modal = new bootstrap.Modal(document.getElementById('deleteModal'));
        modal.show();
    });

    // Event: klik tombol OK di dalam modal konfirmasi hapus
    $('#btnConfirmDelete').on('click', function() {
        if (!nikToDelete) return;

        deleteKaryawan(nikToDelete)
            .done(function() {
                // Tutup modal
                bootstrap.Modal.getInstance(document.getElementById('deleteModal')).hide();
                // Reload tabel agar data terhapus tidak muncul lagi
                loadData('', '');
                // Reset variabel global
                nikToDelete  = null;
                namaToDelete = null;
            })
            .fail(function(xhr) {
                const msg = xhr.responseJSON ? xhr.responseJSON.message : 'Gagal menghapus data';
                alert('Error: ' + msg);
            });
    });
});
