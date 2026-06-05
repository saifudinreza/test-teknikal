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
                    <a href="detail.html?nik=${karyawan.nik}" class="btn btn-outline-info btn-action" title="Lihat Detail">
                        <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" fill="currentColor" viewBox="0 0 16 16">
                            <path d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8M1.173 8a13 13 0 0 1 1.66-2.043C4.12 4.668 5.88 3.5 8 3.5s3.879 1.168 5.168 2.457A13 13 0 0 1 14.828 8q-.086.13-.195.288c-.335.48-.83 1.12-1.465 1.755C11.879 11.332 10.119 12.5 8 12.5s-3.879-1.168-5.168-2.457A13 13 0 0 1 1.172 8z"/>
                            <path d="M8 5.5a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5M4.5 8a3.5 3.5 0 1 1 7 0 3.5 3.5 0 0 1-7 0"/>
                        </svg>
                    </a>
                    <a href="edit.html?nik=${karyawan.nik}" class="btn btn-outline-warning btn-action" title="Edit Data">
                        <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" fill="currentColor" viewBox="0 0 16 16">
                            <path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325"/>
                        </svg>
                    </a>
                    <button class="btn btn-outline-danger btn-action btn-delete" title="Hapus Data"
                            data-nik="${karyawan.nik}"
                            data-nama="${karyawan.namaLengkap.replace(/"/g, '&quot;')}">
                        <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" fill="currentColor" viewBox="0 0 16 16">
                            <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0z"/>
                            <path d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4zM2.5 3h11V2h-11z"/>
                        </svg>
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
