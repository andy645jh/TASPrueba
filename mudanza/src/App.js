import React, { Component } from 'react';
import './App.css';
import { Provider } from 'react-redux';
import store from './store/store';
import People from './People';
import Form from './Form';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';

class App extends Component {

  render() {    
    return (
      <Provider store={store}>
        <div className="container">
          <h2 className="text-center">REGISTRO</h2>
          <Form />
          <People />         
        </div>
      </Provider>

    );
  }
}

export default App;
