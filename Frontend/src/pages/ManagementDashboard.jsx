import Header from "../components/general/Header";
import NotificationPanel from "../components/general/NotificationPanel";
import MetalGrid from "../components/dashboardPages/MetalGrid";
import LastMetalStatisticsGrid from "../components/dashboardPages/LastMetalStatisticsGrid";

export default function ManagementDashboard() {
    return (
        <>
            <title>Главная - Руководство</title>

            <Header />

            <main className="page-container">
                <div className="page-section">
                    <NotificationPanel />
                </div>

                <div className="page-section">
                    <h1>Руководство</h1>
                    <p>
                        Сводная аналитика производства,
                        эффективности и качества продукции.
                    </p>
                </div>

                <div className="page-section">
                    <MetalGrid />
                </div>

                <div className="page-section">
                    <h2>Статистика по металлам</h2>
                    <LastMetalStatisticsGrid />
                </div>

            </main>
        </>
    )
}