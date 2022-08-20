import styled from "styled-components"

export const DashBoardContainer = styled.div`
  padding: 2rem;
  min-height: 100vh;
  width: 100%;
`

export const MenuContainer = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  padding: 10px;
  background-color: #191414;
`

export const ButtonContainer = styled.div`
  display: flex;
  justify-content: space-around;
  margin-bottom: 1rem;

`

export const SearchInput = styled.input`
  width: 100%;
  height: 55px;
  padding: 10px;
  border-radius: 5px;
  font-size: 1.5rem;

  &::placeholder {
    font-size: 1.5rem;
  }
`

export const MenuButton = styled.button`
  width: 10rem;
  height: 55px;
  background-color: #1db954;
  padding: 1rem;
  border-radius: 8px;
  color: white;
  font-size: .75rem;
  font-weight: bold;
  cursor: pointer;
`

export const ResultsContainer = styled.div`
  flex-grow: 1;
  margin: 3rem 0;
  overflow-y: auto;
  overflow-x: auto;
`

export const LyricsContainer = styled.div`
  height: 65vh;
  text-align: center;
  color: #fff;
  white-space: pre;
`

export const PlayerContainer = styled.div`
  width: 100%;
  position: fixed;
  bottom: 0;
  left: 0;
`