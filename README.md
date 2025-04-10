# Meme Tagger

A web application for uploading, tagging, and sharing your favorite memes with AI-powered analysis.

## Overview

Meme Tagger is a full-stack application that allows users to upload meme images, automatically analyze them using AI, add custom tags, and browse a collection of memes. The application features user authentication, image uploading, and AI-powered content analysis.

## Frontend

The frontend is a Vue application that uses Element Plus for styling.

### Deployment

Frontend use [github actions](.github/workflows/deploy-frontend.yml) for CI/CD.

- The building process requires a VITE_API_URL environment variable for the backend URL. Set it in the repository settings(Settings -> Secrets and variables -> Actions -> New repository secret).

- You may also run `npm run build` to build the frontend locally.

## Backend

The backend is a Java application that uses Spring Boot and Spring Security.

### Credentials

[certs](backend/certs) contains the credentials for the Google Cloud Storage bucket and the RSA private and public keys.

To run the docker image, place the `googleCloud.json`, `private.pem`, and `public.pem` files in the `backend/certs` folder.

- `googleCloud.json` is the service account key for the Google Cloud Storage bucket.
- `private.pem` and `public.pem` are the RSA private and public keys for encryption and decryption.
    - certs/script.ps1 is a script to generate the keys.

To develop locally or build the docker image, set the absolute path to the certs in the `application-dev.yml` file.

### Deployment

Backend use docker compose for deploy the java application and mysql database.

Running `docker compose up` will start the backend server on localhost:8080.

- Run `docker compose down` to stop the backend server. Run `docker compose down -v` to stop the backend server and remove the sql volume.

#### nginx

```bash
sudo cp api.memetagger.site.conf /etc/nginx/sites-available/
sudo ln -s /etc/nginx/sites-available/api.memetagger.site.conf /etc/nginx/sites-enabled/
```

- Sets up https to avoid mixed content issues using [certbot](https://certbot.eff.org/).
