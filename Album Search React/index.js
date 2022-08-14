//elements
const searchBar = document.querySelector('.search-input');
const introText = document.querySelector('.intro_text');
const resultBody = document.querySelector('.results_albums');
const loader = document.querySelector('.intro_loader');


function checkEnter(e) {
    //perform the search function if the user presses enter in the search bar
    if (e.keyCode === 13) {
        search()
    }
}

class HelloMessage extends React.Component {
    render() {
      return <div>Hello {this.props.name}</div>;
    }
  }
  
  root.render(<HelloMessage name="Taylor" />);


