# Real-Time Multi-Room Chat Application

A lightweight, high-performance, responsive web application featuring real-time multi-room messaging, persistent data storage, and containerized deployment. Built using **Spring Boot 4**, **Java 25**, and **PostgreSQL**, this application utilizes persistent WebSockets (STOMP over SockJS) to synchronize live messaging across distinct user sessions instantly without page reloads.

Test it!: https://real-rp-backend.de
<br> Credentials are:
<br> username: test
<br> password: test

---

## Key Features

* **Real-Time Communication:** Persistent bi-directional data pipelines powered by **Spring WebSockets & STOMP** for zero-latency messaging.
* **Multi-Room Architecture:** Support for isolated public and private channels with user membership tables.
* **Secure Authentication:** Integrated **Spring Security 6** architecture enforcing BCrypt password hashing and custom session handling.
* **Mobile-Optimized Interface:** Built with a thumb-friendly responsive design using **W3.CSS**, complete with an auto-collapsing overlay side drawer optimized for iOS/Safari viewports.
* **Production-Ready Deployment:** Zero-configuration orchestration via **Docker Compose**, utilizing managed named data volumes for full database persistence.

---

## Built With

* **Backend:** Java 25, Spring Boot 4.1.0, Spring Data JPA, Spring Security 6
* **Database:** PostgreSQL 15 (Alpine)
* **Real-time Engine:** Spring WebSocket, SockJS, STOMP
* **Frontend UI:** Thymeleaf Templates, W3.CSS, Vanilla JavaScript
* **Build Tool & Containerization:** Docker, Docker Compose

---

## Screenshots
<img width="300" height="600" alt="chat_app_mobile" src="https://github.com/user-attachments/assets/acded727-4cda-4fff-b014-a44ed489316a" />
