# MemeTagger Frontend

The frontend for the MemeTagger application, built with Vue 3, TypeScript, and Element Plus.

## Features

- Upload and share meme images
- Analyze meme images using OpenAI's GPT-4o vision model
- User registration and authentication

## OpenAI API Integration

This application includes integration with OpenAI's API to analyze uploaded images.

### Setting Your OpenAI API Key

You need an OpenAI API key to use the image analysis feature. There are two ways to set your API key:

#### Option 1: Environment Variable (for development)

1. Create or edit the `.env` file in the root of the frontend directory
2. Add your OpenAI API key as follows:
   ```
   VITE_OPENAI_API_KEY=your_openai_api_key_here
   ```
3. Restart the application

#### Option 2: Global API Key Settings (recommended)

1. Click the "Set API Key" button in the top-right corner of the application header
2. Enter your OpenAI API key in the dialog that appears
3. Click "Save"

Your API key will be securely stored in your browser's local storage and will be used for all image analysis requests. You can change or clear your API key at any time using the same dialog.

## Security Note

When using Option 2 (Global API Key Settings), your API key is:
- Only stored in your browser's local storage
- Never sent to our servers
- Only used for direct requests to OpenAI
- Not shared with any third parties

## Setup and Development

1. Install dependencies:
   ```
   npm install
   ```

2. Start the development server:
   ```
   npm run dev
   ```

3. Build for production:
   ```
   npm run build
   ```

## Environment Variables

- `VITE_API_URL`: Backend API URL (default: http://localhost:8080)
- `VITE_OPENAI_API_KEY`: Your OpenAI API key for image analysis (optional) 