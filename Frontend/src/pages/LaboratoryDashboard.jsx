import Header from "../components/general/Header";
import Footer from "../components/general/Footer";
import MetalGrid from "../components/dashboardPages/MetalGrid";
import LastLaboratoryRecordTable from "../components/dashboardPages/LastLaboratoryRecordTable";

export default function LaboratoryDashboard() {
    return (
        <>
            <title>Лаборатория</title>

            <Header />

            <main className="page-container">
                <div className="page-section">
                    <h1>Лаборатория</h1>
                    <div className="divider" />
                </div>

                <div className="page-section">
                    <MetalGrid />
                </div>

                <div className="page-section">
                    <h2>Последние анализы</h2>
                    <LastLaboratoryRecordTable />
                </div>
            </main>

            <Footer />
        </>
    );
}