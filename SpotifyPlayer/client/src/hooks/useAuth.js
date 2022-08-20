import { useState, useEffect } from "react"
import axios from "axios"

const useAuth = code => {
    //State variables
    const [accessToken, setAccessToken] = useState()
    const [refreshToken, setRefreshToken] = useState()
    const [expiresIn, setExpiresIn] = useState()


    //TODO: Figure this out

    /**
     * First useEffect makes api call to login route, sends code value as request body and receives
     * access_token, refresh_token, and expires_in which we assign to state vars
     */
    useEffect(() => {
        ; (async () => {
            try {
                const {
                    data: { access_token, refresh_token, expires_in },
                } = await axios.post(`${process.env.REACT_APP_BASE_URL}/login`, {
                    code,
                })
                setAccessToken(access_token)
                setRefreshToken(refresh_token)
                setExpiresIn(expires_in)
                window.history.pushState({}, null, "/")
            } catch {
                window.location = "/"
            }
        })()
    }, [code])

    /**
     * Second useEffect hook makes api call to refresh route 
     */
    useEffect(() => {
        if (!refreshToken || !expiresIn) return
        const interval = setInterval(async () => {
            try {
                const {
                    data: { access_token, expires_in },
                } = await axios.post(`${process.env.REACT_APP_BASE_URL}/refresh`, {
                    refreshToken,
                })
                setAccessToken(access_token)
                setExpiresIn(expires_in)
            } catch {
                window.location = "/"
            }
        }, (expiresIn - 60) * 1000)

        return () => clearInterval(interval)
    }, [refreshToken, expiresIn])

    return accessToken;
}

export default useAuth;


