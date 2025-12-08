<!-- PROJECT LOGO -->
<br />
<div align="center">
<a id="readme-top"></a>
  <a href="https://github.com/CodecoolGlobal/solar-watch-MVP-java-DelczBalazs">
    <img src="images/yellowsun.png" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">SolarWatch</h3>

  <p align="center">
SolarWatch dashboard with sunrise/sunset, a compact weather summary, a city map embed, and an optional AI chat assistant.
    <br />
    <br />
    <br />
    &middot;
    <a href="https://github.com/CodecoolGlobal/solar-watch-MVP-java-DelczBalazs">View Demo</a>
    &middot;
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started-with-docker">Getting started with Docker</a>
      <ul>
        <li><a href="#prerequisites-using-docker">Prerequisites</a></li>
        <li><a href="#quick-start">Quick start</a></li>
        <li><a href="#managing-the-stack">Managing the stack</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contact">Contact</a></li>
    
    
   
  </ol>
</details>

<!-- ABOUT THE PROJECT -->

## About The Project

A full‑stack demo showing authentication and multiple integrations on a single dashboard:

- Sunrise and sunset times for a selected city
- Weather summary cards (temperature, pressure, humidity, wind)
- Google Maps city embed (Google Cloud Maps Embed API)
- AI chat assistant (separate Spring Boot microservice) to ask city‑related questions

The backend (Spring Boot) serves the core API. The AI capability is split into a dedicated microservice so secrets (OpenAI key) never reach the browser. The frontend (React + Vite + Tailwind) provides an accordion chat panel, loading states, and a dark, glassy UI.

  <p align="center">
External APIs used:
    <br />
    &middot;
    <a href="https://sunrise-sunset.org/api">Sunset and sunrise times API</a>
    &middot;
    <br>
    &middot;
    <a href="https://openweathermap.org/api/geocoding-api">Geocoding API (OpenWeather)</a>
    &middot;
    <br>
    &middot;
    <a href="https://developers.google.com/maps/documentation/embed">Google Maps Embed API (Google Cloud)</a>
    &middot;
  </p>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Built With

- [![React][React.js]][React-url]
- [![Typescript][Typescript.ts]][Typescript-url]
- [![Tailwind][Tailwind.css]][Tailwind-url]
- [![Springboot][Springboot.jar]][Springboot-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Architecture at a glance

- solarwatch-backend (Spring Boot, :8080): authentication and solar endpoints
- solarwatch-ai-service (Spring Boot, :8082): AI chat microservice calling OpenAI
- solarwatch-frontend (Vite React, :5173): UI with Vite proxy to backend and AI service

Dev proxy (Vite):
- /api → http://localhost:8080 (backend)
- /api/ai → http://localhost:8082 (AI microservice)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED WITH DOCKER -->

## Getting started with Docker

Run the full stack (frontend + backend + AI microservice + database + dev Nginx reverse proxy) using Docker.

### Prerequisites using Docker

- Docker Desktop (Windows/macOS) or Docker Engine (Linux)
- Docker Compose v2 (bundled with Docker Desktop)

### Quick start (single command)

1. Open a terminal and **navigate to the project root** (the folder containing `docker-compose.yml`).
2. Create a `.env` file with at least:
   ```env
   DATABASE_USERNAME=postgres
   DATABASE_PASSWORD=postgres
   OPENWEATHER_API_KEY=your_openweather_key
   AI_OPENAI_API_KEY=sk-...your_openai_key...
   # Optional for the map widget
   VITE_GOOGLE_MAPS_EMBED_KEY=your_google_maps_embed_key
   ```
3. Build and start the stack in the background:
   ```sh
   docker compose up -d --build
   ```
4. (Optional) Tail logs during the first run:
   ```sh
   docker compose logs -f
   ```
5. Open the app via the dev reverse proxy (Nginx):
   - App (proxied): http://localhost/  
     - Nginx forwards `/` → Vite dev server, `/api` → backend, `/api/ai` → AI microservice
   - Direct services (useful for debugging):
     - Frontend Vite: http://localhost:5173/
     - Backend API: http://localhost:8080/
     - AI health: http://localhost:8082/actuator/health

### Managing the stack

- See running services:
  ```sh
  docker compose ps
  ```
- Stop and remove containers (keep data volumes):
  ```sh
  docker compose down
  ```
- Stop only (keep containers for quick restart):
  ```sh
  docker compose stop
  ```
- Rebuild images without cache:
  ```sh
  docker compose build --no-cache
  ```
- **Reset everything** (containers **and** volumes, e.g., wipe the DB):
  ```sh
  docker compose down -v
  ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->

## Getting Started

This section explains how to run the project locally. Follow the steps below:

### Prerequisites

- Node.js and npm (frontend)
- Java 21+ (Spring Boot services)
- IntelliJ IDEA or another IDE

### Installation & Local development

Frontend (React + TypeScript)

1. Navigate to `solarwatch-frontend/`
2. Install packages:
   ```sh
   npm install
   ```
3. Start the development server:
   ```sh
   npm run dev
   ```
4. Open http://localhost:5173/

Backend (Spring Boot)

1. Open the backend/ folder in IntelliJ IDEA.

2. Run the project using the green arrow (from the Spring Boot main class, e.g. Application.java).

   - Alternatively, you can start the backend from the terminal with:

   ```sh
   mvn spring-boot:run
   ```

3. The backend will be available at http://localhost:8080/ by default.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- USAGE EXAMPLES -->

## Usage

1. Register and log in.
2. Open the SolarWatch dashboard.
3. Enter a city to see:
   - Sunrise and sunset times
   - Weather summary cards (temperature, pressure, humidity, wind)
   - Google Maps city embed (if `VITE_GOOGLE_MAPS_EMBED_KEY` is set)
   - AI Assistant accordion → ask questions about the selected city

Notes
- The AI service uses a separate API key on the server. If OpenAI returns 429 (insufficient quota), you’ll see: “The AI service is currently unavailable (quota exceeded). Demo only.”
- In development, Vite proxies `/api` to :8080 and `/api/ai` to :8082.

### Environment variables

AI microservice (server):
- `AI_OPENAI_API_KEY` – required
- `AI_OPENAI_MODEL` – optional (default: gpt-4o-mini)

Frontend:
- `VITE_GOOGLE_MAPS_EMBED_KEY` – Google Maps Embed API key (Google Cloud) to show the map
- `VITE_AI_BASE_URL` – optional for production if the AI service is on a different origin; in dev the proxy is used

  <p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->

## Contact

Délczeg Balázs - dbalazsworkemail@gmail.com

Project Link - [GitHub repository](https://github.com/CodecoolGlobal/solar-watch-MVP-java-DelczBalazs)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[contributors-shield]: https://img.shields.io/github/contributors/github_username/repo_name.svg?style=for-the-badge
[contributors-url]: https://github.com/github_username/repo_name/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/github_username/repo_name.svg?style=for-the-badge
[forks-url]: https://github.com/github_username/repo_name/network/members
[stars-shield]: https://img.shields.io/github/stars/github_username/repo_name.svg?style=for-the-badge
[stars-url]: https://github.com/github_username/repo_name/stargazers
[issues-shield]: https://img.shields.io/github/issues/github_username/repo_name.svg?style=for-the-badge
[issues-url]: https://github.com/github_username/repo_name/issues
[license-shield]: https://img.shields.io/github/license/github_username/repo_name.svg?style=for-the-badge
[license-url]: https://github.com/github_username/repo_name/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/linkedin_username
[product-screenshot]: images/screenshot.png
[React.js]: https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB
[React-url]: https://reactjs.org/
[Typescript.ts]: https://shields.io/badge/TypeScript-3178C6?logo=TypeScript&logoColor=FFF&style=flat-square
[Typescript-url]: https://www.typescriptlang.org/
[Tailwind.css]: https://img.shields.io/badge/Tailwind_CSS-grey?style=for-the-badge&logo=tailwind-css&logoColor=38B2AC
[Tailwind-url]: https://tailwindcss.com/
[Springboot.jar]: https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=Spring&logoColor=white
[Springboot-url]: https://spring.io/projects/spring-boot
