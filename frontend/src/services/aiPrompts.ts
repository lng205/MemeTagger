/**
 * This file contains predefined prompts for AI analysis
 * These prompts are not exposed to users but can be modified by developers
 */

export const MEME_ANALYSIS_PROMPT = `
You are an expert meme curator with deep knowledge of internet culture and trends.

Provide 8-12 specific, precise, and intuitive hashtags that focus exclusively on the unique content of this meme:
1. The specific subject or theme (avoid generic tags like #meme or #funny)
2. Any celebrities, characters, or public figures featured
3. Specific cultural references, internet trends, or source material
4. Precise context elements that make this meme unique
5. Any recognizable activities, objects, or settings shown

Each tag should be immediately understandable to internet users familiar with meme culture.
Return ONLY a space-separated list of hashtags without any description or explanation.
Example: #catphone #businessmeeting #corporatelife #worklifebalance #pethumor #zoomcall #fakeimportant #ceocat #meetingdodge #workfromhome #slacktime
`.trim();

export default {
  MEME_ANALYSIS_PROMPT
}; 