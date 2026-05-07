import { useQuery } from '@tanstack/react-query';
import { useParams, useLocation } from "react-router-dom";
import Header from "../components/general/Header";
import BatchDataPanel from "../components/batchPage/BatchDataPanel";
import BatchLaboratoryPanel from "../components/batchPage/BatchLaboratoryPanel";
import BatchProductionPanel from "../components/batchPage/BatchProductionPanel";
import BatchManagementPanel from "../components/batchPage/BatchManagementPanel";

import { getBatch } from "../api/api";

export default function BatchPage() {
    const { id } = useParams(); // batchId
    const location = useLocation();

    const isLaboratory = location.pathname.startsWith("/laboratory");
    const isProduction = location.pathname.startsWith("/production");
    const isManagement = location.pathname.startsWith("/management");

    const { data: batchData, isLoading, isError } = useQuery({
        queryKey: ['batch-page-batchData', id],
        queryFn: () => getBatch(id).then(res => res.data),
    });

    if (isLoading) {
        return (
            <div>
                Загрузка...
            </div>
        )
    }

    if (isError || !batchData) {
        return (
            <div>
                Произошла какая-то ошибка или данные партии не были найдены
            </div>
        )
    }

    return (
        <div>
            <title>Партия - {id}</title>

            <Header />

            <BatchDataPanel batchData={batchData} />

            {isLaboratory && <BatchLaboratoryPanel batchData={batchData} />}

            {isProduction && <BatchProductionPanel batchData={batchData} />}

            {isManagement && <BatchManagementPanel batchData={batchData} />}
        </div>
    )
}