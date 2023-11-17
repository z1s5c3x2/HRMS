import styles from '../../css/error.css';

export default function AccessDenied( props ){
    return(<>
        <div class="errorWrap">
            <div class="error-text">
                <h2>권한이 없습니다.</h2>
            </div>
        </div>
    </>)
}