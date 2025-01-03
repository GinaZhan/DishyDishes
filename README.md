# DishyDishes
A Recipe-Social website project, this repository includes both the frontend and backend components. It was transferred from local storage after we lost access to the original GitLab repository, which was tied to a school account. The contributors to this project are Jie Zhang, Kaicheng Xu, Shuren Xu, and Zirui He.

## Getting started
Visit [DishyDishyes.top](https://dishydishes.top)(Avaliable on Expo day)  
### OR deploy locally
**Frontend(Deploy)**  
Download the newest release file: frontend.zip from the [Release Page](https://gitlab.cas.mcmaster.ca/2023-2024-capstone/case-capstone/-/releases) and unpack.  
Use your favrourite web server(Nginx,Apache,Caddy...) to host the unpacked directory  
**Backend(Deploy)**  
Install and configure Java 17(any distribution) to PATH    
Install and run MongoDB    
Download the newest release file: backend.zip from the [Release Page](https://gitlab.cas.mcmaster.ca/2023-2024-capstone/case-capstone/-/releases) and unpack  
Run `java -jar unpackedfile.jar`.  
### OR run as dev mode locally
**Frontend(Dev)**  
Install and configure Node.js to PATH  
Clone the repo  
Navigate to src/dishydishes  
Run `npm install`  
Run `npm run serve`

**Backend(Dev)**  
Install and configure Java 17(any distribution) to PATH    
Install and configure Maven to PATH  
Install and run MongoDB    
Clone the repo  
Navigate to src/backend  
Run `mvn install`  
Run `mvn run:spring-boot`

## Build
**Frontend**  
Install and configure Node.js to PATH  
Clone the repo  
Navigate to src/dishydishes  
Run `npm install`  
Run `npm run build`

**Backend**  
Install and configure Java 17(any distribution) to PATH.    
Install and configure Maven to PATH.  
Clone the repo  
Navigate to src/backend  
Run `mvn install`  
Run `mvn build`

## API References
[API References](src/backend/doc.md)

## Dependencies
**Frontend**  
vue: 3.2.13  
vue-cookies: 1.8.3  
vue-router: 4.0.3  
axios: 1.6.8  
bulma: 0.9.4  
core-js: 3.8.3  
quasar: 2.14.1  
@auth0/auth0-spa-js: 2.1.2  
@auth0/auth0-vue: 2.3.2  
@quasar/extras: 1.16.9  
vue3-carousel: 0.3.1  
yarn: 1.22.21

**Backend**  
org.mongodb:mongodb-driver-sync: 5.0.0  
org.mongodb:mongodb-driver-core: 5.0.0  
org.springframework.data:spring-data-mongodb  
org.springframework.boot:spring-boot-starter-data-mongodb  
org.springframework.boot:spring-boot-starter-web  
org.springframework.boot:spring-boot-starter-test  
org.springframework.boot:spring-boot-starter-security  
org.modelmapper:modelmapper: 3.0.0  
org.mindrot:jbcrypt: 0.4  
com.auth0:java-jwt: 4.4.0  
commons-codec:commons-codec: 1.15  
org.projectlombok:lombok

## License
[Mozilla Public License Version 2.0](https://gitlab.cas.mcmaster.ca/2023-2024-capstone/case-capstone/-/blob/main/LICENSE)  