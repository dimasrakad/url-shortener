# URL Shortener REST API

RESTful API sederhana untuk memendekkan URL menggunakan Spring Boot, Java 17, JWT Authentication, dan MySQL.

---

## ⚙️ Teknologi

| Teknologi | Keterangan |
| --------- | ---------- |
| Spring Boot | Framework utama |
| Java 17 | Bahasa pemrograman |
| Maven | Build tool |
| MySQL | Database |
| JWT | Stateless Authentication |
| Docker | Containerization |
| Swagger UI | API Documentation |

---

## 🧩 Fitur Utama

- Buat short URL dari input user
- Redirect otomatis ke URL asli
- Hitungan akses
- Token JWT untuk autentikasi user
- Blacklist token saat logout
- Validasi custom path dan expiry date
- Penghapusan otomatis token yang kadaluarsa

---

## 🔗 Endpoint API

### Auth

| Method | Endpoint | Keterangan |
| ------ | -------- | ---------- |
| POST | `/api/auth/login` | Login user |
| POST | `/api/auth/register` | Register user |
| POST | `/api/auth/refresh` | Refresh access token |
| GET | `/api/auth/logout` | Logout user |

### URL

| Method | Endpoint | Keterangan |
| ------ | -------- | ---------- |
| POST | `/api/url` | Membuat short URL |
| GET | `/api/url` | Get semua data short URL |
| GET | `/api/url/{shortCode}` | Get data short URL |
| DELETE | `/api/url/{shortCode}` | Menghapus data short URL |

### Redirect ke URL Asli

| Method | Endpoint | Keterangan |
| ------ | -------- | ---------- |
| GET | `/{shortCode}` | Redirect ke URL asli |