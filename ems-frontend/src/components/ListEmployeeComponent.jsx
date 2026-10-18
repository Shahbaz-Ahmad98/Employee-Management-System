import {useEffect, useState} from 'react'
import { deleteEmployee, listEmployees } from '../services/EmployeeService'
import { useNavigate } from 'react-router-dom'

const ListEmployeeComponent = () => {

    const [employees, setEmployees] = useState([])
    const [deletingIds, setDeletingIds] = useState([])
    const [errorMessage, setErrorMessage] = useState('')

    const navigator = useNavigate();

    useEffect(() => {
        let active = true;
        listEmployees().then((response) => {
            if (!active) return;
            setEmployees(response.data);
        }).catch(error => {
            if (!active) return;
            console.error(error);
            setErrorMessage('Unable to load employees. Please refresh and try again.');
        })
        return () => { active = false; };
    }, [])

    function addNewEmployee() {
        navigator('/add-employee')
    }
    
    function updateEmployee(id) {
        navigator(`/edit-employee/${id}`)
    }

    async function removeEmployee(id){
        if (deletingIds.includes(id)) return;
        setErrorMessage('');
        setDeletingIds(current => [...current, id]);

        try {
            await deleteEmployee(id);
            setEmployees(current => current.filter(employee => employee.id !== id));
        } catch (error) {
            console.error(error);
            setErrorMessage('Unable to delete the employee. Please try again.');
        } finally {
            setDeletingIds(current => current.filter(employeeId => employeeId !== id));
        }
    }

  return (
    <div className='container'>
        <h2 className='text-center'>List of Employees</h2>
        <button className='btn btn-primary mb-2' onClick = {addNewEmployee} > Add Employee </button>
        {errorMessage && <p role='alert' className='text-danger'>{errorMessage}</p>}
        <table className='table table-striped table-bordered'>
            <thead>
                <tr>
                    <th>Employee Id</th>
                    <th>Employee First Name</th>
                    <th>Employee Last Name</th>
                    <th>Employee Email Id</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                {
                    employees.map(employee => 
                        <tr key = {employee.id}>
                        <td>{employee.id}</td>
                        <td>{employee.firstName}</td>
                        <td>{employee.lastName}</td>
                        <td>{employee.email}</td>
                        <td>
                            <button className = 'btn btn-info' onClick={() => updateEmployee(employee.id)}>Update</button>
                            <button type='button' className='btn btn-danger ms-3' disabled={deletingIds.includes(employee.id)} onClick={() => removeEmployee(employee.id)}>
                                {deletingIds.includes(employee.id) ? 'Deleting...' : 'Delete'}
                            </button>
                        </td>
                    </tr>)
                }
            </tbody>
        </table>
    </div>
  )
}

export default ListEmployeeComponent
