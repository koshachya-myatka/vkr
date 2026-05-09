import React from "react";

export default function PaginationButton({ onClick, disabled, children }) {
    return (
        <button
            onClick={onClick}
            disabled={disabled}
            className="btn"
        >
            {children}
        </button>
    );
}