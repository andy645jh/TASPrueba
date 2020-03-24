import React, { createRef, Component } from 'react';
import { connect } from 'react-redux';
import axios, { post } from 'axios';
import store from './store/store';

class Form extends Component {

    constructor() {
        super();
        this.cedula = createRef(0);
    }

    componentDidMount() {
        this.getAllPeople();
    }

    onChangeHandler(event) {
        console.log(event.target.files[0]);   
        this.props.fileSelected(event.target.files[0]);     
    }

    onClickHandler(e) {
        e.preventDefault();
        const file = store.getState().file;

        if (this.existCedula() || !file) return;

        
        this.fileUpload(file).then(res => {
            console.log("Recibido: ", res);
            this.getAllPeople();
        });
    }

    fileUpload(file) {
        const url = 'http://localhost:8080/mudanza/up';
        const formData = new FormData();
        formData.append("cedula", this.cedula.current.value);
        formData.append('file', file);
        const config = {
            headers: {
                'content-type': 'multipart/form-data'
            }
        }
        return post(url, formData, config);
    }

    getAllPeople() {
        axios.get('http://localhost:8080/mudanza/').then(res => {
            //this.setState({ people: res.data });
            this.props.updatePeopleList(res.data);
            console.log("Personas: ", res);
        });
    }   

    existCedula() {
        const ced = parseInt(this.cedula.current.value);
        const result = this.props.peopleList.find(persona => persona.cedula === ced);
        return result;
    }

    render() {
        return (
            <section className="row">
                <div className="row">
                    <form>
                        <input type="number" ref={this.cedula} placeholder="Ingrese su Cedula" />
                        <input type="file" name="file" onChange={(e) => this.onChangeHandler(e)} />
                        <button type="button" className="btn btn-success btn-block" onClick={(e) => this.onClickHandler(e)}>Subir</button>
                    </form>
                </div>
            </section>
        )
    }

}

const mapStateProps = state => ({
    peopleList: state.peopleList
});

const mapDispatchToProps = dispatch => ({
    updatePeopleList (people){
        dispatch({
            type: 'UPDATE_LIST',
            people
        });
    },

    fileSelected (file){
        dispatch({
            type: 'SET_FILE',
            file
        });
    }
});

export default connect(mapStateProps, mapDispatchToProps)(Form);