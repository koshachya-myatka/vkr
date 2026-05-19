import { useState } from "react";

const statuses = ["ARRIVAL", "PROCESSING", "ANALYSIS", "ACCEPTED", "DEFECTIVE"];
const nameStatuses = new Map([["ARRIVAL", "Поступление"], ["PROCESSING", "Обработка"],
["ANALYSIS", "Анализ"], ["ACCEPTED", "Одобрено"], ["DEFECTIVE", "Брак"]]);

export default function MetalSearchPanel({ onSearch }) {
    const [batchId, setBatchId] = useState("");
    const [equipmentId, setEquipmentId] = useState("");
    const [startTime, setStartTime] = useState("");
    const [endTime, setEndTime] = useState("");
    const [processStatus, setProcessStatus] = useState("");

    const handleSubmit = () => {
        onSearch({
            batchId: batchId || null,
            equipmentId: equipmentId || null,
            startTime: startTime || null,
            endTime: endTime || null,
            processStatus: processStatus || null,
        });
    };

    const resetFilter = () => {
        setBatchId("");
        setEquipmentId("");
        setStartTime("");
        setEndTime("");
        setProcessStatus("");
        onSearch({
            batchId: null,
            equipmentId: null,
            startTime: null,
            endTime: null,
            processStatus: null,
        });
    };

    const handleKeyDown = (e) => {
        if (e.key === "Enter") {
            handleSubmit();
        }
    };

    return (
        <div className="card search-panel-wrapper">
            <div className="search-panel-actions">
                <button
                    className="btn"
                    onClick={resetFilter}
                >
                    Сброс
                </button>

                <button
                    className="btn btn-primary"
                    onClick={handleSubmit}
                >
                    Найти
                </button>
            </div>

            <div className="search-panel">
                <input
                    className="input"
                    placeholder="ID партии"
                    value={batchId}
                    onChange={(e) =>
                        setBatchId(e.target.value)
                    }
                    onKeyDown={handleKeyDown}
                />

                <input
                    className="input"
                    placeholder="ID оборудования"
                    value={equipmentId}
                    onChange={(e) =>
                        setEquipmentId(e.target.value)
                    }
                    onKeyDown={handleKeyDown}
                />

                <input
                    className="input search-date"
                    type="datetime-local"
                    value={startTime}
                    onChange={(e) =>
                        setStartTime(e.target.value)
                    }
                    onKeyDown={handleKeyDown}
                />

                <input
                    className="input search-date"
                    type="datetime-local"
                    value={endTime}
                    onChange={(e) =>
                        setEndTime(e.target.value)
                    }
                    onKeyDown={handleKeyDown}
                />

                <select
                    className="select search-select"
                    value={processStatus}
                    onChange={(e) =>
                        setProcessStatus(e.target.value)
                    }
                    onKeyDown={handleKeyDown}
                >
                    <option value="">
                        Все статусы
                    </option>

                    {statuses.map((s) => (
                        <option key={s} value={s}>
                            {nameStatuses.get(s)}
                        </option>
                    ))}
                </select>
            </div>
        </div>
    );
}