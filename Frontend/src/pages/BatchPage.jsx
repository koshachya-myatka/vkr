import { useQuery } from '@tanstack/react-query';
import { useParams, useLocation, useNavigate } from "react-router-dom";
import Header from "../components/general/Header";
import Footer from '../components/general/Footer';
import Loader from '../components/general/Loader';
import ReportButton from '../components/batchPage/ReportButton';
import BatchDataPanel from "../components/batchPage/BatchDataPanel";
import BatchLaboratoryPanel from "../components/batchPage/BatchLaboratoryPanel";
import BatchProductionPanel from "../components/batchPage/BatchProductionPanel";
import BatchManagementPanel from "../components/batchPage/BatchManagementPanel";

import { getBatch } from "../api/api";

export default function BatchPage() {
    const navigate = useNavigate();
    const { id: batchId } = useParams(); // batchId
    const location = useLocation();

    const isLaboratory = location.pathname.startsWith("/laboratory");
    const isProduction = location.pathname.startsWith("/production");
    const isManagement = location.pathname.startsWith("/management");

    const { data: batchData, isLoading, isError } = useQuery({
        queryKey: ['batch-page-batchData', batchId],
        queryFn: () => getBatch(batchId).then(res => res.data),
    });

    if (isLoading) {
        return (
            <Loader size="large" />
        );
    }

    if (isError) {
        navigate("/error");
    }

    if (!batchData) {
        <div className="page-section">
            <h4>Нет данных</h4>
        </div>
    }

    return (
        <>
            <title>Партия - {batchId}</title>

            <Header />

            <main className="page-container">
                <div className="page-section flex-between">
                    <h1>Партия #{batchId}</h1>
                    <ReportButton />
                </div>

                <div className="page-section">
                    <BatchDataPanel
                        batchData={batchData}
                    />
                </div>

                {isLaboratory && (
                    <div className="page-section">
                        <BatchLaboratoryPanel
                            batchData={batchData}
                        />
                    </div>
                )}

                {isProduction && (
                    <div className="page-section">
                        <BatchProductionPanel
                            batchData={batchData}
                        />
                    </div>
                )}

                {isManagement && (
                    <div className="page-section">
                        <BatchManagementPanel
                            batchData={batchData}
                        />
                    </div>
                )}
            </main>

            <Footer />
        </>
    )
}