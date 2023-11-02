import { Link } from 'react-router-dom';
export default function Header(props){
    return(<>
        <h3>헤더</h3>
        <Link to='/salary/list'>급여리스트</Link>
    </>)
}