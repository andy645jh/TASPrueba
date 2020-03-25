import React from 'react';
import { connect } from 'react-redux';
import dateformat from 'dateformat';

const People = ({ peopleList }) => (
    <section className="row mt-3">
        <div className="col">            
            <div className="row">
                <table className="table text-center">
                    <thead>
                        <tr>
                            <th>Cedula</th>
                            <th>Fecha</th>
                            <th>Archivo Resultado</th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            (peopleList != null && peopleList.length > 0) ?
                                (peopleList.map(persona =>
                                    <tr className="border-bottom mt-2 mb-2" key={persona.id}>
                                        <td>{persona.cedula}</td>
                                        <td>{dateformat(persona.fecha, 'dd/mm/yyyy')}</td>
                                        <td><a href={"http://localhost:8080/download/" + persona.id} target="_blank" rel="download">Descargar</a></td>
                                    </tr>)
                                )
                                :
                                (<tr><td colSpan="3" className="text-sup">No hay datos registrados.</td></tr>)
                        }
                    </tbody>
                </table>
            </div>
        </div>
    </section>
);

const mapStateProps = state => ({
    peopleList: state.peopleList
});

/*const mapDispatchToProps = dispatch => ({
    updatePeopleList(people) {
        dispatch({
            type: 'UPDATE_LIST',
            people
        });
    }

});*/


export default connect(mapStateProps, {})(People);