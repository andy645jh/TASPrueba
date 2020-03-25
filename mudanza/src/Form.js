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

        if (this.existCedula() || !file) {
            this.cedula.current.value = 0;
            return;
        }


        this.fileUpload(file).then(res => {
            console.log("Recibido: ", res);
            this.props.fileSelected(null);
            this.cedula.current.value = '';
            this.getAllPeople();
        });
    }

    fileUpload(file) {
        const url = 'http://localhost:8080/upload';
        
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
            
            this.props.updatePeopleList(res.data);
            console.log("Personas: ", res);
        });
    }

    existCedula() {
        const ced = parseInt(this.cedula.current.value);

        //validar q sea un numero positivo
        if (ced < 0) return true;

        const result = this.props.peopleList.find(persona => persona.cedula === ced);
        return result;
    }

    render() {
        return (
            <section className="row">
                <div className="col">
                    <form className="form-group">
                        <div className="form-row">
                            <input className="form-control mb-3" type="number" ref={this.cedula} placeholder="Ingrese su Cedula" />
                        </div>
                        <div className="form-row">
                            <input className="form-control mb-3" type="file" name="file" onChange={(e) => this.onChangeHandler(e)} />
                        </div>
                        <div className="form-row">
                            <button type="button" className="form-control mb-3 btn btn-success btn-block" onClick={(e) => this.onClickHandler(e)}>Subir</button>
                        </div>
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
    updatePeopleList(people) {
        dispatch({
            type: 'UPDATE_LIST',
            people
        });
    },

    fileSelected(file) {
        dispatch({
            type: 'SET_FILE',
            file
        });
    }
});

export default connect(mapStateProps, mapDispatchToProps)(Form);