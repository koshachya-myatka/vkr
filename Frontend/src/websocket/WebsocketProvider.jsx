import { useEffect, useRef } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { connectWebSocket, disconnectWebSocket } from './websocket';

export const WebSocketProvider = ({ children }) => {
    const queryClient = useQueryClient();
    const scadaBufferRef = useRef([]);

    useEffect(() => {
        connectWebSocket((type, msg) => {
            switch (type) {
                case 'notifications':
                    queryClient.invalidateQueries({ queryKey: ['notifications'] });
                    queryClient.setQueryData(['unviewedNotificationsCount'], (old) => { return old + 1; });
                    break;
                case 'mes':
                    queryClient.invalidateQueries({ queryKey: ['dashboard-manag-last-statistics'] });
                    queryClient.invalidateQueries({ queryKey: ['batch-page-batchData', msg.batchId] });
                    queryClient.invalidateQueries({ queryKey: ['dashboard-prod-last-batches'] });
                    queryClient.invalidateQueries({ queryKey: ['batch-page-mes', msg.batchId] });
                    queryClient.invalidateQueries({ queryKey: ['dashboard-metal-grid'] });
                    break;
                case 'lims':
                    queryClient.invalidateQueries({ queryKey: ['batch-page-prod-lims', msg.batchId] });
                    queryClient.invalidateQueries({ queryKey: ['batch-page-lab-lims', msg.batchId] });
                    queryClient.invalidateQueries({ queryKey: ['dashboard-lab-last-lims'] });
                    break;
                case 'scada':
                    scadaBufferRef.current.push(msg.batchId);
                    break;
            }
        });

        const intervalScada = setInterval(async () => {
            if (scadaBufferRef.current.length === 0) {
                return;
            }
            const uniqueBatchIds = [...new Set(scadaBufferRef.current)];
            scadaBufferRef.current = [];
            await Promise.all(
                uniqueBatchIds.map(batchId =>
                    Promise.all([
                        queryClient.invalidateQueries({ queryKey: ['batch-page-prod-scada', batchId] }),
                        queryClient.invalidateQueries({ queryKey: ['batch-page-manag-scada', batchId] }),
                    ])
                )
            )
        }, 1000);

        return () => {
            clearInterval(intervalScada);
            disconnectWebSocket();
        };

    }, [queryClient]);

    return children;
};