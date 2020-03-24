import React, { Component, createRef } from 'react';
import './App.css';

import { Provider } from 'react-redux';
import store from './store/store';
import People from './People';
import Form from './Form';

class App extends Component {

  constructor(props) {
    super(props);
    this.state = {
      selectedFile: null,
      people: [],
      cc: 0
    }

    this.cedula = createRef(0);
  }

  
/*
  onChangeHandler = event => {
    console.log(event.target.files[0]);
    this.setState({
      selectedFile: event.target.files[0],
      loaded: 0,
    });
  }

  onClickHandler(e) {
    e.preventDefault();

    if (this.existCedula()) return;

    this.fileUpload(this.state.selectedFile).then(res => {
      console.log("Recibido: ", res);
    });
  }

  existCedula() {
    const cedula = parseInt(this.cedula.current.value);
    const result = this.state.people.find(persona => persona.cedula === cedula);
    return result;
  }*/



  render() {
    const { people } = this.state;

    console.log("People: ", people);
    return (
      <Provider store={store}>
        <div className="container">
          <Form />
          <People />         
        </div>
      </Provider>

    );
  }
}

export default App;
