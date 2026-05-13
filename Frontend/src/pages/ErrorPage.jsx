import { Link } from 'react-router-dom';

export default function ErrorPage({ message }) {
    return (
        <div className="error-page">
            <div className="error-card">
                <span className="material-symbols-outlined error-icon">
                    error
                </span>
                <div className="error-title">
                    Произошла ошибка
                </div>

                {message
                    ? <p className="error-text">{message}</p>
                    : <p className="error-text">Не удалось загрузить данные. Уже работаем над этим.</p>
                }

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