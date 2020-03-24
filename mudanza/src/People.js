import React from 'react';
import { connect } from 'react-redux';

const People = ({ peopleList }) => (
    <section className="row">
        <h2>Lista</h2>
        {
            (peopleList != null && peopleList.length > 0) ?
                (peopleList.map(persona =>
                    <div className="row" key={persona.id}>
                        <div className="col">{persona.cedula}</div>
                        <div className="col">{persona.fecha}</div>
                    </div>)
                )
                :
                (<p className="text-sup">No hay datos registrados.</p>)
        }
    </section>
);

const mapStateProps = state => ({
    peopleList: state.peopleList
});

const mapDispatchToProps = dispatch => ({
    updatePeopleList(people)
    {
        dispatch({
            type: 'UPDATE_LIST',
            people
        });
    }
    
});


export default connect(mapStateProps, mapDispatchToProps)(People);