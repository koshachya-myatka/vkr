import { Link, useLocation } from 'react-router-dom';

export default function ErrorPage({ message: propMessage }) {
    const location = useLocation();
    const message =
        propMessage ||
        location.state?.message ||
        'Что-то пошло не так. Уже работаем над этим.';

    return (
        <div className="error-page">
            <div className="error-card">
                <span className="material-symbols-outlined error-icon">
                    error
                </span>
                <div className="error-title">
                    Ошибка
                </div>

                <p className="error-text">{message}</p>

                <Link
                    to="/"
                    className="btn btn-primary"
                    style={{
                        textDecoration: 'none',
                        display: 'inline-flex',
                        alignItems: 'center',
                        justifyContent: 'center'
                    }}
                >
                    Вернуться на главную
                </Link>
            </div>
        </div>
    );
}