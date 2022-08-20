import express from "express";
import cors from "cors";
import lyricsFinder from "lyrics-finder"
import SpotifyWebApi from "spotify-web-api-node";
import dotenv from "dotenv";

const app = express()
const PORT = 3001

//dotenv loads environment variables from a .env file into process.env
dotenv.config()

app.use(cors())
app.use(express.json())
app.use(express.urlencoded({ extended: true }))

//Login route. Ensures correct user is logged in
app.post("/login", async (req, res) => {
    //destructure req.body to get unique access code
    const { code } = req.body
    const spotifyApi = new SpotifyWebApi({
        redirectUri: process.env.REDIRECT_URI,
        clientId: process.env.CLIENT_ID,
        clientSecret: process.env.CLIENT_SECRET
    })

    try {
        const {
            body: { access_token, refresh_token, expires_in },
        } = await spotifyApi.authorizationCodeGrant(code);

        res.json({ access_token, refresh_token, expires_in })
    } catch (err) {
        console.log("ERROR ", err)
        res.sendStatus(400)
    }
})

//Refresh route fetches a new expires_in value to mantain the current user's session
app.post("/refresh", async (req, res) => {
    const { refreshToken } = req.body
    const spotifyApi = new SpotifyWebApi({
        redirectUri: process.env.REDIRECT_URI,
        clientId: process.env.CLIENT_ID,
        clientSecret: process.env.CLIENT_SECRET,
        refreshToken
    })

    try {
        const {
            body: { access_token, expires_in }
        } = await spotifyApi.refreshAccessToken();
        res.json({ access_token, expires_in })

    } catch (err) {
        console.log("ERROR ", err)
        res.sendStatus(400)
    }
})

//Lyrics route. Uses the lyrics-finder pkg to fetch lyrics
app.get("/lyrics", async (req, res) => {
    const { artist, track } = req.query;
    const lyrics = (await lyricsFinder(artist, track))
    res.json({ lyrics })
})

app.listen(PORT, err=> {
    if(err){
        console.log("ERROR ", err);
    }
    console.log("Listening on port ", PORT);
})