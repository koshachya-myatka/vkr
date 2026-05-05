import HeaderLinkButton from './HeaderLinkButton';
import { useLocation } from 'react-router-dom';

export default function Header() {
    const location = useLocation();

    return (
        <div style={{ display: 'flex', gap: '10px' }}>
            <HeaderLinkButton text="Лаборатория" link="/laboratory" active={location.pathname.startsWith('/laboratory')} />
            <HeaderLinkButton text="Производство" link="/production" active={location.pathname.startsWith('/production')} />
            <HeaderLinkButton text="Руководство" link="/management" active={location.pathname.startsWith('/management')} />
        </div>
    );
}