<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a id="readme-top"></a>
  <a href="https://github.com/CodecoolGlobal/solar-watch-MVP-java-DelczBalazs">
    <img src="/images/yellowsun.png" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">SolarWatch sunrise/sunset data for a given city</h3>

  <p align="center"></p>
    <br />
    <a href="https://github.com/github_username/repo_name">View Demo</a>



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
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

<p align="center">
A simple demo app showcasing user authentication and integration with external APIs. The Spring Boot backend consumes public sunrise/sunset APIs and exposes secured REST endpoints; the React + Vite + Tailwind frontend provides a minimal UI for registering, logging in, and viewing sunrise/sunset times for a chosen city. Ideal as a small example of authentication, API consumption, and a full-stack Java/React setup.
</p>

<p align="right">(<a href="#readme-top">back to top</a>)</p>



### Built With

* [![React][React.js]][React-url]
* [![Typescript][Typescript.ts]][Typescript-url]
* [![Tailwind][Tailwind.css]][Tailwind-url]
* [![Springboot][Springboot.jar]][Springboot-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

This section explains how to run the project locally. Follow the steps below:

### Prerequisites

* Node.js and npm (for the frontend)

* Java 17+ (for the backend, Spring Boot)

* IntelliJ IDEA or another IDE for the backend

### Installation

Frontend (React + TypeScript)
1. Navigate to the frontend/ folder.
2. Install the required packages:
   ```sh
   npm install
   ```
3. Start the development server:
   ```sh
   npm run dev
   ```
4. Open the given URL in your browser (usually http://localhost:5173/).

Backend (Spring Boot)
1. Open the backend/ folder in IntelliJ IDEA.

2. Run the project using the green arrow (from the Spring Boot main class, e.g. Application.java).
    * Alternatively, you can start the backend from the terminal with:
   ```sh
   mvn spring-boot:run
   ```

3. The backend will be available at http://localhost:8080/ by default.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage

Register a new account and log in. While logged in, open the dashboard to view sunrise and sunset times for a chosen city.

### How to use
1. Register a new account.
2. Log in to the application.
3. On the dashboard, enter a city name or select a city from the list.
4. The dashboard will display the sunrise and sunset times for that city.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->
## Contact

Délczeg Balázs - dbalazsworkemail@gmail.com

Project Link: [github.com/CodecoolGlobal/solar-watch-MVP-java-DelczBalazs](https://github.com/CodecoolGlobal/solar-watch-MVP-java-DelczBalazs)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

* [Ittzés Péter(Codecool mentor)](https://codecool.com/)



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

