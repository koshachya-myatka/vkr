import { useState } from "react";

const STATUSES_NAMES = {
    CREATED: "СОЗДАНО",
    IN_PROGRESS: "НА ПРОВЕРКЕ",
    FALSE_POSITIVE: "ЛОЖНОЕ СРАБАТЫВАНИЕ",
    RESOLVED: "ПРИЧИНА УСТРАНЕНА"
};
const SIGNAL_SOURCES = ["MES", "SCADA", "LIMS"];

export default function NotificationsSearchPanel({ onSearch }) {
    const [equipmentId, setEquipmentId] = useState("");
    const [signalSource, setSignalSource] = useState("");
    const [status, setStatus] = useState("");
    const [dateFrom, setDateFrom] = useState("");
    const [dateTo, setDateTo] = useState("");

    return (
        <div className="card search-panel-wrapper">
            <div className="search-panel-actions">
                <button
                    className="btn"
                    onClick={() => {
                        setEquipmentId("");
                        setSignalSource("");
                        setStatus("");
                        setDateFrom("");
                        setDateTo("");
                        onSearch({
                            equipmentId: null,
                            signalSource: null,
                            status: null,
                            dateFrom: null,
                            dateTo: null
                        });
                    }}
                >
                    Сброс
                </button>

                <button
                    className="btn btn-primary"
                    onClick={() =>
                        onSearch({
                            equipmentId: equipmentId || null,
                            signalSource: signalSource || null,
                            status: status || null,
                            dateFrom: dateFrom || null,
                            dateTo: dateTo || null
                        })
                    }
                >
                    Найти
                </button>
            </div>

            <div className="search-panel">
                <input
                    className="input"
                    placeholder="ID оборудования"
                    value={equipmentId}
                    onChange={(e) =>
                        setEquipmentId(
                            e.target.value
                        )
                    }
                />

                <select
                    className="select search-select"
                    value={signalSource}
                    onChange={(e) =>
                        setSignalSource(
                            e.target.value
                        )
                    }
                >
                    <option value="">
                        Все источники
                    </option>

                    {
                        SIGNAL_SOURCES.map(source => (
                            <option
                                key={source}
                                value={source}
                            >
                                {source}
                            </option>
                        ))
                    }
                </select>

                <select
                    className="select search-select"
                    value={status}
                    onChange={(e) =>
                        setStatus(
                            e.target.value
                        )
                    }
                >
                    <option value="">
                        Все статусы
                    </option>

                    {
                        Object.entries(
                            STATUSES_NAMES
                        ).map(([key, value]) => (
                            <option
                                key={key}
                                value={key}
                            >
                                {value}
                            </option>
                        ))
                    }
                </select>

                <input
                    className="input"
                    type="datetime-local"
                    value={dateFrom}
                    onChange={(e) =>
                        setDateFrom(
                            e.target.value
                        )
                    }
                />

                <input
                    className="input"
                    type="datetime-local"
                    value={dateTo}
                    onChange={(e) =>
                        setDateTo(
                            e.target.value
                        )
                    }
                />
            </div>
        </div>
    );
}