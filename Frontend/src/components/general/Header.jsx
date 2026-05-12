import HeaderLinkButton from './HeaderLinkButton';
import LogoutButton from '../security/LogoutButton';
import { useLocation } from 'react-router-dom';

export default function Header() {
    const location = useLocation();

    return (
        <header className="header">
            <div className="header-left">
                <div className="header-logo">
                    <span className="material-symbols-outlined">
                      Assessment
                    </span>
                    Витрина качества металлов
                </div>
            </div>
            <nav className="header-nav">
                <HeaderLinkButton text="Лаборатория" link="/laboratory" active={location.pathname.startsWith('/laboratory')} />
                <HeaderLinkButton text="Производство" link="/production" active={location.pathname.startsWith('/production')} />
                <HeaderLinkButton text="Руководство" link="/management" active={location.pathname.startsWith('/management')} />
                <HeaderLinkButton text="Администрирование" link="/admin" active={location.pathname.startsWith('/admin')} />
                <LogoutButton />
            </nav>
        </header>
    );
}