# Meme Tagger

A web application for uploading, tagging, and sharing your favorite memes with AI-powered analysis.

## Overview

Meme Tagger is a full-stack application that allows users to upload meme images, automatically analyze them using AI, add custom tags, and browse a collection of memes. The application features user authentication, image uploading, and AI-powered content analysis.

## Features

- **User Authentication**
  - User registration and login
  - JWT token-based authentication
  - Secure password handling

- **Meme Management**
  - Upload meme images
  - AI-powered meme analysis using OpenAI
  - Add and edit tags for memes
  - Browse memes with pagination
  - Copy meme URLs to clipboard

- **User Interface**
  - Responsive design using Element Plus
  - Tab-based navigation
  - User account management
  - OpenAI API key configuration

## Technologies

### Frontend
- Vue.js 3 (Composition API)
- Element Plus UI framework
- TypeScript
- Axios for API requests

### Backend
- RESTful API architecture
- JWT authentication
- File upload handling

### AI Integration
- OpenAI API for meme analysis

## Setup

### Prerequisites
- Node.js and npm installed
- API server running (see backend setup)
- OpenAI API key (optional for AI features)

### Installation

1. Clone the repository
   ```
   git clone https://github.com/yourusername/meme-tagger.git
   cd meme-tagger
   ```

2. Install dependencies
   ```
   cd frontend
   npm install
   ```

3. Start the development server
   ```
   npm run dev
   ```

## Usage

1. **Register/Login**: Create a new account or log in with existing credentials
2. **Upload Meme**: Upload an image file through the Upload tab
3. **AI Analysis**: If OpenAI API key is configured, get automatic analysis of the meme
4. **Add Tags**: Add custom tags to categorize your memes
5. **Browse Memes**: View and filter uploaded memes in the Browse tab

## Project Structure

```
frontend/
├── src/
│   ├── components/         # Vue components
│   │   ├── Auth.vue        # Authentication container
│   │   ├── Login.vue       # Login form
│   │   ├── Register.vue    # Registration form
│   │   ├── MemeUploader.vue # Image upload component
│   │   ├── MemeBrowser.vue # Browse uploaded memes
│   │   └── ...
│   ├── services/
│   │   ├── api.ts          # API service with Axios
│   │   └── aiPrompts.ts    # AI prompts for OpenAI
│   ├── store/
│   │   ├── user.ts         # User state management
│   │   └── settings.ts     # App settings state
│   └── App.vue             # Main application component
```

## API Endpoints

### User Management
- `PUT /user/register` - Register new user
- `POST /user/login` - Login user
- `GET /user` - Get current user details

### Meme Management
- `POST /common/upload` - Upload image file
- `GET /meme` - Get memes with pagination
- `GET /meme/:id` - Get meme by ID

### Tag Management
- `GET /tag/:memeId` - Get tags for a meme
- `POST /tag/:memeId` - Set tags for a meme

<!-- ## License

This project is licensed under the MIT License - see the LICENSE file for details.  -->