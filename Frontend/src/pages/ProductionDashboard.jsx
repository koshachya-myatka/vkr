import Header from "../components/general/Header";
import Footer from "../components/general/Footer";
import MetalGrid from "../components/dashboardPages/MetalGrid";
import LastProductionRecordTable from "../components/dashboardPages/LastProductionRecordTable";

export default function ProductionDashboard() {
  return (
    <>
      <title>Производство</title>

      <Header />

      <main className="page-container">
        <div className="page-section">
          <h1>Производство</h1>
          <div className="divider" />
        </div>

        <div className="page-section">
          <MetalGrid />
        </div>

        <div className="page-section">
          <h2>Последние партии</h2>
          <LastProductionRecordTable />
        </div>
      </main>

      <Footer />
    </>
  );
}