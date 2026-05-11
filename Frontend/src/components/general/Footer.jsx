export default function Footer() {
    const scrollToTop = () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    };

    return (
        <footer className="footer">
            <div className="footer-content">
                <div className="footer-info">
                    <div className="footer-logo">
                        <span className="material-symbols-outlined">
                            Assessment
                        </span>
                    </div>
                    <p className="footer-text">
                        Система мониторинга производственных,
                        лабораторных и аналитических данных
                        металлургического предприятия.
                    </p>
                </div>
                <button
                    className="btn footer-top-button"
                    onClick={scrollToTop}
                >
                    <span className="material-symbols-outlined">
                        keyboard_arrow_up
                    </span>
                    Наверх
                </button>
            </div>
        </footer>
    );
}