import React from "react";
import { useState, useEffect } from "react";

import useAuth from "./hooks/useAuth";
import Player from "./Player";
import TrackSearchResult from "./TrackSearchResults";
import SpotifyWebApi from "spotify-web-api-node";
import axios from "axios";
import { DashBoardContainer, SearchInput, ResultsContainer, LyricsContainer, PlayerContainer, MenuButton, MenuContainer, ButtonContainer } from "./styles/Dashboard.styles";

const spotifyApi = new SpotifyWebApi({ clientId: process.env.REACT_APP_CLIENT_ID, })

const Dashboard = ({ code }) => {
    const accessToken = useAuth(code)   //Get accesstoken from custom hook
    const [search, setSearch] = useState("")
    const [searchResults, setSearchResults] = useState([])
    const [playingTrack, setPlayingTrack] = useState()
    const [lyrics, setLyrics] = useState("")

    function chooseTrack(track) {
        setPlayingTrack(track);
        setSearch("");
        setLyrics("");
    }

    function getRandomInt(max) {
        return Math.floor(Math.random() * max);
    }

    function playTop10(artistId) {
        spotifyApi.getArtistTopTracks(artistId, "US").then((res) => {
            let randomTrack = res.body.tracks[getRandomInt(res.body.tracks.length)]
            let trackInfo = {
                artist: randomTrack.artists[0].name,
                artistId: randomTrack.artists[0].id,
                title: randomTrack.name,
                uri: randomTrack.uri,
            }
            chooseTrack(trackInfo);
        })
    }

    function shuffleLiked() {
        spotifyApi.getMySavedTracks({ limit: 50 }).then((res) => {
            let randomTrack = res.body.items[getRandomInt(res.body.items.length)].track
            let trackInfo = {
                artist: randomTrack.artists[0].name,
                artistId: randomTrack.artists[0].id,
                title: randomTrack.name,
                uri: randomTrack.uri,
            }
            chooseTrack(trackInfo);
        })

    }

    useEffect(() => {
        if (!playingTrack) return
            ; (async () => {
                const {
                    data: { lyrics },
                } = await axios.get(`${process.env.REACT_APP_BASE_URL}/lyrics`, {
                    params: {
                        track: playingTrack.title,
                        artist: playingTrack.artist,
                    },
                })
                setLyrics(lyrics)
            })()
    }, [playingTrack])

    useEffect(() => {
        if (!accessToken) return
        spotifyApi.setAccessToken(accessToken)
    }, [accessToken])

    useEffect(() => {
        if (!search) return setSearchResults([])
        if (!accessToken) return

        let cancel = false
            ; (async () => {
                const { body } = await spotifyApi.searchTracks(search)
                if (cancel) return
                setSearchResults(
                    body.tracks.items.map(track => {
                        const smallestAlbumImage = track.album.images.reduce(
                            (smallest, image) => {
                                if (image.height < smallest.height) return image
                                return smallest
                            },
                            track.album.images[0]
                        )

                        return {
                            artist: track.artists[0].name,
                            artistId: track.artists[0].id,
                            title: track.name,
                            uri: track.uri,
                            albumUrl: smallestAlbumImage.url,
                        }
                    })
                )
            })()

        return () => (cancel = true)
    }, [search, accessToken])


    return (
        <DashBoardContainer>

            <MenuContainer>
                <ButtonContainer>
                    <MenuButton onClick={e => shuffleLiked()}>Shuffle Liked Songs</MenuButton>
                    <MenuButton onClick={e => playTop10("3eqjTLE0HfPfh78zjh6TqT")}>Shuffle Bruce Springsteen</MenuButton>
                    <MenuButton onClick={e => playTop10("5K4W6rqBFWDnAN6FQUkS6x")}>Shuffle Kanye West</MenuButton>
                    <MenuButton onClick={e => playTop10("5ictveRyhWRs8Gt8Dvt1hS")}>Shuffle The Front Bottoms</MenuButton>
                </ButtonContainer>

                <SearchInput
                    type="search"
                    placeholder="Search for Songs or Artists"
                    value={search}
                    onChange={e => setSearch(e.target.value)}
                />
            </MenuContainer>


            <ResultsContainer>
                {searchResults.map(track => (
                    <TrackSearchResult
                        track={track}
                        key={track.uri}
                        chooseTrack={chooseTrack}
                    />
                ))}
                {searchResults.length === 0 && (<LyricsContainer>{lyrics}</LyricsContainer>)}
            </ResultsContainer>

            <PlayerContainer>
                <Player
                    accessToken={accessToken}
                    trackUri={playingTrack?.uri}
                />
            </PlayerContainer>
        </DashBoardContainer>
    )
}

export default Dashboard;