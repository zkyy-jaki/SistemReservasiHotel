# Hotel Reservation System with Dynamic Pricing

Proyek ini merupakan aplikasi desktop berbasis Java Swing untuk sistem reservasi hotel dengan konsep Object Oriented Programming (OOP), MVC, JDBC MySQL, dan Dynamic Pricing.

## Tujuan Proyek

Membuat sistem reservasi hotel yang dapat:
- Mengelola data kamar hotel
- Melakukan reservasi kamar
- Menghitung harga kamar secara dinamis
- Menggunakan konsep OOP dan MVC
- Terhubung dengan database MySQL menggunakan JDBC

---

# Teknologi yang Digunakan

- Java
- Java Swing
- MySQL
- JDBC
- NetBeans IDE

---

# Konsep yang Digunakan

## Object Oriented Programming (OOP)

### Encapsulation
Menggunakan private attribute dan getter/setter pada setiap model class.

### Inheritance
Class turunan:
- StandardRoom extends Room
- DeluxeRoom extends Room
- SuiteRoom extends Room

### Polymorphism
Method `calculatePrice()` dioverride pada masing-masing tipe kamar.

### Abstraction
Room dirancang sebagai superclass untuk seluruh tipe kamar.

---

# Arsitektur MVC

## Model
Berisi data dan business logic.

Contoh:
- Room
- StandardRoom
- DeluxeRoom
- SuiteRoom
- Reservation
- Customer
- DynamicPricing

## View
Berisi tampilan GUI Java Swing.

Rencana:
- LoginView
- DashboardView
- ReservationView
- RoomView

## Controller
Penghubung antara View dan Model.

Rencana:
- ReservationController
- RoomController
- LoginController

---

# Dynamic Pricing

Harga kamar akan berubah berdasarkan kondisi tertentu, misalnya:
- Weekend
- High season
- Lama menginap
- Jenis kamar

Contoh:
- Deluxe Room memiliki tambahan harga layanan
- Suite Room memiliki tambahan layanan VIP

---

# Struktur Project (Planned)

```text
src/
│
├── model/
│   ├── Room.java 
│   ├── StandardRoom.java
│   ├── DeluxeRoom.java
│   ├── SuiteRoom.java
│   ├── Customer.java
│   ├── Reservation.java
│   ├── DynamicPricing.java
│   └── DatabaseConnection.java
│
├── view/
│   ├── LoginView.java
│   ├── DashboardView.java
│   ├── ReservationView.java
│   └── RoomView.java
│
├── controller/
│   ├── ReservationController.java
│   ├── RoomController.java
│   └── LoginController.java
│
└── main/
    └── Main.java

    # Progress Project

## Model
- ✅ Room.java
- ✅ StandardRoom.java
- ✅ DeluxeRoom.java
- ✅ SuiteRoom.java
- ⬜ Customer.java
- ⬜ Reservation.java
- ⬜ DynamicPricing.java
- ⬜ DatabaseConnection.java

## View
- ⬜ LoginView.java
- ⬜ DashboardView.java
- ⬜ ReservationView.java
- ⬜ RoomView.java

## Controller
- ⬜ ReservationController.java
- ⬜ RoomController.java
- ⬜ LoginController.java

## Database
- ⬜ Membuat database MySQL
- ⬜ Membuat tabel rooms
- ⬜ Membuat tabel reservations
- ⬜ Koneksi JDBC

## Fitur
- ⬜ CRUD Reservasi
- ⬜ CRUD Data Kamar
- ⬜ Dynamic Pricing
- ⬜ Login Admin
- ⬜ JTable untuk menampilkan data
- ⬜ Perhitungan total harga otomatis
