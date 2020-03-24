import { createStore } from 'redux';

const initialState = {
    peopleList: [],
    file:null
};

const reducerApp = (state = initialState, action) => {

    switch (action.type) {
        case 'UPDATE_LIST':
            return {
                ...state,
                peopleList: action.people
            };

        case 'SET_FILE':
            return {
                ...state,
                file: action.file
            };
            
        default:
            return state;
    }
};

export default createStore(reducerApp, window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__());